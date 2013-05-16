package com.shandagames.android.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.util.Log;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.network.CountingOutputStream;
import com.shandagames.android.network.CountingOutputStream.Progress;
import com.shandagames.android.network.FlushedInputStream;
import com.shandagames.android.parser.JSONUtils;
import com.shandagames.android.parser.Parser;
import com.shandagames.android.parser.Result;
import com.shandagames.android.parser.ResultType;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @file AbstractHttpApi.java
 * @create 2012-10-11 下午1:04:54
 * @author Jacky.Lee
 * @description TODO 封装常用的网络请求操作
 */
abstract public class AbstractHttpApi implements HttpApi {
    private static final String TAG = "AbstractHttpApi";

    private static final String DEFAULT_CLIENT_VERSION = "";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private static final int TIMEOUT = 60;

    private final String mClientVersion;
    private final DefaultHttpClient mHttpClient;
   
    protected static final boolean DEBUG = Constants.DEVELOPER_MODE;

    public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
        mHttpClient = httpClient;
        if (clientVersion != null) {
            mClientVersion = clientVersion;
        } else {
            mClientVersion = DEFAULT_CLIENT_VERSION;
        }
    }

    
    /** execute() an httpRequest catching exceptions and returning null instead. */
    public Result executeHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends ResultType> parser)  {
        if (DEBUG) Log.d(TAG, "doHttpRequest [1] url = " + httpRequest.getURI()); 
        
        try {
        	mHttpClient.getConnectionManager().closeExpiredConnections();
        	httpRequest.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        	
        	long start=System.currentTimeMillis();
	        HttpResponse response = mHttpClient.execute(httpRequest);
	        long end=System.currentTimeMillis();
	        if (DEBUG) Log.d(TAG, "doHttpRequest [2] time = " + (end - start));
	
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (DEBUG) Log.d(TAG, "doHttpRequest [3] statusLine = " + statusCode);
	        switch (statusCode) {
	            case 200:
	                String content = EntityUtils.toString(response.getEntity());
	                if (DEBUG) Log.d(TAG, "doHttpRequest [4] responseData = " + content);
	                return JSONUtils.consume(parser, content, DEBUG);
	            default:
	            	String message = "Default case for status code reached: " + response.getStatusLine().toString();
	                if (DEBUG) Log.d(TAG, "doHttpRequest [4] exception = " + message);
	                response.getEntity().consumeContent();
	                Result result=new Result();
	                result.setException(new Exception(message));
	                return result;
	        }
        } catch (IOException ex) {
        	if (DEBUG) ex.printStackTrace();
        	Result result=new Result();
			result.setException(ex);
			return result;
        } finally {
        	httpRequest.abort();
        }
    }

    
    public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
    	if (nameValuePairs.length > 0) {
    		//注意URLEncodedUtils会对参数中的特殊字符加密，包含空格。
    		url = url + "?" + URLEncodedUtils.format(Arrays.asList(nameValuePairs), HTTP.UTF_8);
    	}
    	HttpGet httpGet = new HttpGet(url);
        return httpGet;
    }

    
    public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(nameValuePairs), HTTP.UTF_8));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
        return httpPost;
    }
    
    
    public HttpPost createHttpPostWithFile(String url, String queryString, NameValuePair... attachments) {
        StringBody stringBody;
        FileBody fileBody;
        File targetFile;
        String filePath;
        FormBodyPart fbp;
        
        MultipartEntity mpEntity = new MultipartEntity();
        HttpPost httpPost = new HttpPost(url);
        
        try {
        	List<NameValuePair> nameValuePairs = splitString(queryString);
	        for(NameValuePair queryParam : nameValuePairs){
				stringBody=new StringBody(queryParam.getValue(),Charset.forName("UTF-8"));
				fbp= new FormBodyPart(queryParam.getName(), stringBody);
		        mpEntity.addPart(fbp);
	        }
        } catch (UnsupportedEncodingException e) { }
        
        for (NameValuePair param : attachments) {
            filePath = param.getValue();
            targetFile= new File(filePath);
            fileBody = new FileBody(targetFile,"application/octet-stream");
            fbp= new FormBodyPart(param.getName(), fileBody);
            mpEntity.addPart(fbp);
        }
        httpPost.setEntity(mpEntity);
        
        return httpPost;
    }
    
    
    public byte[] downloadFileToBytes(String url) {
    	HttpGet httpGet = null;
	    try {
	    	httpGet = new HttpGet(url);
            HttpResponse response=mHttpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
            	HttpEntity entity = response.getEntity();
            	BufferedInputStream bis = new BufferedInputStream(new FlushedInputStream(entity.getContent()));
         		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
         		
         		byte[] buffer = new byte[1024];
				int readsize = 0;
				while ((readsize = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, readsize);
				}
				bos.flush();
				bos.close();
				bis.close();
				entity.consumeContent();
				return bos.toByteArray();
            }
        } catch (Exception ex) {
        	if (DEBUG) ex.printStackTrace();
        } finally {
        	if (httpGet != null) {
        		httpGet.abort();
        	}
        }
	    
    	return null;
    }
    
    
    public File downloadFileToDisk(String url, String filePath, Progress progress) {
    	File desFile = new File(filePath); // 目标文件
    	File parentFile = desFile.getParentFile();
    	File tmpFile = new File(filePath.concat(".tmp")); //临时文件
    	
    	if (desFile.isDirectory()) 
    		return null;
    	if (desFile.exists())
    		return desFile;
    	if (!parentFile.exists()&&!parentFile.mkdirs())
    		return null;
    	
    	HttpGet httpGet = null;
    	try {
 	    	 httpGet = new HttpGet(url);
             HttpResponse response=mHttpClient.execute(httpGet);
             int statusCode = response.getStatusLine().getStatusCode();
             if (statusCode == HttpStatus.SC_OK) {
            	tmpFile.createNewFile();
            	HttpEntity entity = response.getEntity();
            	if (entity == null) return null;
            	long length = entity.getContentLength();
            	
            	BufferedInputStream bis = new BufferedInputStream(new FlushedInputStream(entity.getContent()));
            	OutputStream out = new CountingOutputStream(new FileOutputStream(tmpFile), length, progress);

            	byte[] buffer = new byte[2048];
				int readsize = 0;
				while ((readsize = bis.read(buffer)) > 0) {
					out.write(buffer, 0, readsize);
				}
				out.flush();
				out.close();
				bis.close();
				entity.consumeContent();
				if (tmpFile.length() == length) {
					tmpFile.renameTo(desFile);
					return desFile;
				}
             }
         } catch (Exception ex) {
         	if (DEBUG) ex.printStackTrace();
         } finally {
        	 if (httpGet != null) {
        		 httpGet.abort();
        	 }
         }
         return null;
    }
    
    
    public HttpURLConnection createHttpURLConnection(String actionUrl, String method) 
		throws IOException {
		 URL url = new URL(actionUrl);
		 HttpURLConnection httpConnection = 
			 (HttpURLConnection) url.openConnection();
		 httpConnection.setDoInput(true);        
		 httpConnection.setDoOutput(true); 
		 httpConnection.setUseCaches(false); 
		 httpConnection.setReadTimeout(TIMEOUT * 1000);
		 httpConnection.setConnectTimeout(TIMEOUT * 1000);
		 httpConnection.setRequestMethod(method);
		 httpConnection.setRequestProperty("Charsert", "UTF-8");
		 httpConnection.setRequestProperty("connection", "keep-alive");
		 httpConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");  
		 httpConnection.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion);
	     return httpConnection;
	}
    
    
    /** 分割queryString，取得List<NameValuePair>格式存储的参数队列. */
    private List<NameValuePair> splitString(String queryString) {
        if (queryString.startsWith("?")) {
            queryString = queryString.substring(1);
        }

        List<NameValuePair> result = new ArrayList<NameValuePair>();

        if (queryString != null && !queryString.equals("")) {
            String[] p = queryString.split("&");
            for (String s : p) {
                if (s != null && !s.equals("")) {
                    if (s.indexOf('=') > -1) {
                        String[] temp = s.split("=");
                        if (temp.length > 1) {
                            result.add(new BasicNameValuePair(temp[0], temp[1]));
                        }
                    }
                }
            }
        }
        return result;
    }  
    

}
