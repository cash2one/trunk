package com.shandagames.android.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.util.Log;
import com.shandagames.android.network.CountingOutputStream.Progress;
import com.shandagames.android.support.StringSupport;

/**
 * 自定义参数的Httpclient <br>
 * 提供httpGet，httpPost两种传送消息的方式<br>
 * 提供httpPost上传文件的方式
 */
public class QHttpClient {
	private static final boolean DEBUG = true;
	 //日志输出
    private static final String TAG = "QHttpClient";
    
    // SDK默认参数设置 
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int CON_TIME_OUT_MS= 5000;
    public static final int SO_TIME_OUT_MS= 5000;
    public static final int MAX_CONNECTIONS_PER_HOST = 2;
    public static final int MAX_TOTAL_CONNECTIONS = 5;
    
    private HttpClient httpClient;
    
    private static QHttpClient instance;
    
    /**
     * 单例模式创建QHttpClient实例
     * @return QHttpClient对象
     */
    public synchronized static QHttpClient getInstance() {
    	if (instance == null) {
    		instance = new QHttpClient();
    	}
    	return instance;
    }
    
    
    private QHttpClient(){
        this(MAX_CONNECTIONS_PER_HOST,MAX_TOTAL_CONNECTIONS,CON_TIME_OUT_MS,SO_TIME_OUT_MS);
    }
    
    
    private QHttpClient(int maxConnectionPerHost, int maxTotalConnections, int conTimeOutMs, int soTimeOutMs){
        // Register the "http" & "https" protocol scheme, They are required
        // by the default operator to look up socket factories.        
        SchemeRegistry supportedSchemes = new SchemeRegistry();         
        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        //-----------------------------------SSL Scheme------------------------------------------
        try {
            SSLSocketFactory sslSocketFactory=SSLSocketFactory.getSocketFactory();
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            supportedSchemes.register(new Scheme("https",new EasySSLSocketFactory(), 443));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------SSL Scheme end---------------------------------------
        
        // Prepare parameters.
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, maxTotalConnections);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(maxConnectionPerHost); 
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute); 
        
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        
        HttpConnectionParams.setConnectionTimeout(httpParams, conTimeOutMs);
        HttpConnectionParams.setSoTimeout(httpParams, soTimeOutMs);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        
        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(httpParams,supportedSchemes);
        HttpClientParams.setCookiePolicy(httpParams, CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient=createHttpClient(connectionManager, httpParams);
    }

    /**
     * 拦截QHttpClient请求,添加Gzip,Deflate处理
     * @param conman ClientConnectionManager管理器
     * @param params HttpParams参数
     * @return
     */
    private DefaultHttpClient createHttpClient(ClientConnectionManager conman, HttpParams params) {
		return new DefaultHttpClient(conman, params) {
			@Override
			protected BasicHttpProcessor createHttpProcessor() {
				// Add interceptor to prevent making requests from main thread.
				BasicHttpProcessor processor = super.createHttpProcessor();
				processor.addRequestInterceptor(new RequestAcceptEncoding());
				processor.addResponseInterceptor(new ResponseContentEncoding());
				return processor;
			}
		};
	}
    
    private String executeHttpRequest(HttpRequestBase httpRequest)  {
    	String responseData = null;
        if (DEBUG) Log.d(TAG, "doHttpRequest [1] url = " + httpRequest.getURI()); 
        
        try {
        	httpClient.getConnectionManager().closeExpiredConnections();
        	
        	long start=System.currentTimeMillis();
	        HttpResponse response = httpClient.execute(httpRequest);
	        long end=System.currentTimeMillis();
	        if (DEBUG) Log.d(TAG, "doHttpRequest [2] time = " + (end - start));
	
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (DEBUG) Log.d(TAG, "doHttpRequest [3] statusLine = " + statusCode);
	        if (statusCode == HttpStatus.SC_OK) {
	        	responseData = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                if (DEBUG) Log.d(TAG, "doHttpRequest [4] responseData = " + responseData);
	        }
        } catch (IOException ex) {
        	ex.printStackTrace();
        } finally {
        	httpRequest.abort();
        }
        
        return responseData;
    }
    
    public String doHttpGet(String url, NameValuePair... nameValuePairs) {
    	if (nameValuePairs.length > 0) {
    		//注意URLEncodedUtils会对参数中的特殊字符加密，包含空格。
    		url = url + "?" + URLEncodedUtils.format(Arrays.asList(nameValuePairs), HTTP.UTF_8);
    	}
    	HttpGet httpGet = new HttpGet(url);
    	return executeHttpRequest(httpGet);
    }
    
    public String doHttpGet(String url, String queryString) {
    	if (queryString != null && !queryString.equals("")) {
    		url += "?" + queryString;
    	}
    	HttpGet httpGet = new HttpGet(url);
    	return executeHttpRequest(httpGet);
    }

    public String doHttpPost(String url, NameValuePair... nameValuePairs) {
    	 HttpPost httpPost = new HttpPost(url);
         try {
             httpPost.setEntity(new UrlEncodedFormEntity(Arrays.asList(nameValuePairs), HTTP.UTF_8));
         } catch (UnsupportedEncodingException ex) {
             throw new IllegalArgumentException("Unable to encode http parameters.");
         }
         return executeHttpRequest(httpPost);
    }

    public String doHttpPost(String url, String queryString) { 
    	try {
	    	URI tmpUri=new URI(url);
	    	URI uri = URIUtils.createURI(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(), tmpUri.getPath(), queryString, null);
	    	HttpPost httpPost = new HttpPost(uri);
	    	if (queryString != null && queryString.length()==0) {
	    		StringEntity reqEntity = new StringEntity(queryString);   
	    		reqEntity.setContentType("application/x-www-form-urlencoded");  // 设置类型    
	         	httpPost.setEntity(reqEntity); // 设置请求的数据   
	    	}
	    	return executeHttpRequest(httpPost);
    	} catch (URISyntaxException e) {
    		e.printStackTrace();
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * Post方法传送文件和消息
     * 
     * @param url  连接的URL
     * @param queryString 请求参数串
     * @param files 上传的文件列表
     * @return 服务器返回的信息
     * @throws Exception
     */
    public String doHttpPostWithFile(String url, String queryString, List<File> files) {
        String responseData = null;
        HttpPost httpPost = null;

        try {
        	 URI tmpUri=new URI(url);
             URI uri = URIUtils.createURI(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(), tmpUri.getPath(), 
                     queryString, null);
             if (DEBUG) Log.d(TAG, "HttpClient httpPostWithFile [1]  uri = "+uri.toURL());
             
             MultipartEntity mpEntity = new MultipartEntity();
             httpPost = new HttpPost(uri);
             StringBody stringBody;
             FileBody fileBody;
             File targetFile;
             FormBodyPart fbp;
             
             List<NameValuePair> queryParamList=StringSupport.getQueryParamsList(queryString);
             for(NameValuePair queryParam:queryParamList){
                 stringBody=new StringBody(queryParam.getValue(),Charset.forName("UTF-8"));
                 fbp= new FormBodyPart(queryParam.getName(), stringBody);
                 mpEntity.addPart(fbp);
             }
             
             for (File file : files) {
             	 targetFile= file.getAbsoluteFile();
                 fileBody = new FileBody(targetFile,"application/octet-stream");
                 fbp= new FormBodyPart(file.getName(), fileBody);
                 mpEntity.addPart(fbp);
             }
             httpPost.setEntity(mpEntity);
             
             //关闭所有过期的QHttpClient连接
             httpClient.getConnectionManager().closeExpiredConnections();
             //执行POST请求操作
             HttpResponse response=httpClient.execute(httpPost);
             // 读取响应状态码
             int statusCode = response.getStatusLine().getStatusCode();
             if (DEBUG) Log.d(TAG, "HttpClient httpPostWithFile [2] StatusLine = "+ statusCode);
             // 200返回码表示成功，其余的表示失败
             if (statusCode == HttpStatus.SC_OK) {
            	 responseData =EntityUtils.toString(response.getEntity());
            	 if (DEBUG) Log.d(TAG, "HttpClient httpPostWithFile [3] Response = "+ responseData);
            }
        } catch (Exception ex) {
        	if (DEBUG) {
        		ex.printStackTrace();
        	}
        } finally{ 
        	if (httpPost != null) {
        		httpPost.abort();
        	}
        }
        
        return responseData;
    }

    /**
     * 下载网络资源到文件
     * @param pathName 资源存储路径
     * @param url 连接的URL
     * @return 资源文件
     */
    public File downloadFileWithProgress(String pathName, String url, Progress progress) {
    	if (DEBUG) Log.d(TAG, "HttpClient downloadFile [1] url = "+url);
    	File desFile = new File(pathName); // 目标文件
    	File parentFile = desFile.getParentFile();
    	File tmpFile = new File(pathName.concat(".tmp")); //临时文件
    		
    	if (desFile.isDirectory()) 
    		return null;
    	if (desFile.exists())
    		return desFile;
    	if (!parentFile.exists()&&!parentFile.mkdirs())
    		return null;
    	
    	HttpGet httpGet = null;
    	InputStream is = null;
		OutputStream out = null;
	    try {
	    	httpGet = new HttpGet(url);
	    	// 执行下载文件请求
            HttpResponse response=httpClient.execute(httpGet);
            // 读取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (DEBUG) Log.d(TAG, "HttpClient downloadFile [2] StatusLine : "+ statusCode);
            // 200返回码表示成功，其余的表示失败
            if (statusCode != HttpStatus.SC_OK) {
            	if (DEBUG) Log.e(TAG, "Error " + statusCode + " while retrieving bitmap from " + url); 
            	return null;
            }
            HttpEntity entity = response.getEntity();
        	if (entity != null) {
        		tmpFile.createNewFile(); // 创建新的资源文件
    			long length = entity.getContentLength(); // 网络资源字节长度
    			is = new BufferedInputStream(new FlushedInputStream(entity.getContent()));
    			out = new CountingOutputStream(new FileOutputStream(tmpFile), length, progress);
				
				byte[] buffer = new byte[2048];
				int readsize = 0;
				while ((readsize = is.read(buffer)) > 0) {
					out.write(buffer, 0, readsize);
				}
				is.close();
				out.close();
				entity.consumeContent();
				// 更改文件名称
				if (tmpFile.length() == length) {
					tmpFile.renameTo(desFile);
					return desFile;
				}
        	}
        } catch (IOException ex) {
        	Log.e(TAG, "Error while retrieving bitmap from " + url + ">>" + ex.toString());
        	if (DEBUG) {
        		ex.printStackTrace();
        	}
        } finally {
        	if (httpGet != null) {
        		httpGet.abort();
        	}
        }

	    return null;
    }
    
    /**
     * 下载网络资源到内存中
     * @param url 连接的URL
     * @return 资源文件字节码
     */
    public byte[] downloadFileToBytes(String url) {
    	if (DEBUG) Log.d(TAG, "HttpClient downloadFile [1] url = "+url);
		HttpGet httpGet = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
	    try {
	    	httpGet = new HttpGet(url);
	    	// 执行下载数据请求
            HttpResponse response=httpClient.execute(httpGet);
            // 读取响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (DEBUG) Log.d(TAG, "HttpClient downloadFile [2] StatusLine : "+ statusCode);
            // 200返回码表示成功，其余的表示失败
            if (statusCode != HttpStatus.SC_OK) {
            	if (DEBUG) Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url); 
            	return null;
            }
            HttpEntity entity = response.getEntity();
        	if (entity != null) {
        		bis = new BufferedInputStream(new FlushedInputStream(entity.getContent()));
    			bos = new ByteArrayOutputStream(2048);
    			byte[] buffer = new byte[2048];
				int readsize = 0;
				while ((readsize = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, readsize);
				}
				bis.close();
				bos.close();
				entity.consumeContent();
				return bos.toByteArray();
        	}
        } catch (IOException ex) {
        	Log.w(TAG, "Error while retrieving bitmap from " + url + ex.toString());
        	if (DEBUG) { 
        		ex.printStackTrace();
        	} 
        } finally {
        	if (httpGet != null) {
        		httpGet.abort();
        	}
        }
	    
	    return null;
    }
    
    /**
     * HttpEntity请求类型
     * @param entity 数据流
     * @param defaultCharset 默认请求编码
     * @return 编码类型
     */
    public String getContentCharSet(HttpEntity entity, String defaultCharset) {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		String charset = defaultCharset;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}
		return charset;
	}
    
    
    /**
     * 断开QHttpClient的连接
     */
    public void shutdownConnection(){
        try { 
        	if(httpClient!=null && httpClient.getConnectionManager()!=null) {
        		httpClient.getConnectionManager().shutdown(); 
        		httpClient=null;
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
