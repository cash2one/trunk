package com.shandagames.android.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import android.util.Log;
import com.shandagames.android.network.CountingOutputStream.Progress;

/**
 * 自定义参数的BetterHttp <br>
 * 提供httpGet，httpPost两种传送消息的方式<br>
 * 提供httpPost上传文件的方式
 */
public class BetterHttp {
    public static final boolean DEBUG = true; 
    private static final String TAG = "BetterHttp"; //日志输出
    
	private static BetterHttp instance;
	
	/**
     * 单例模式创建ConnectionClient实例
     * @return BetterHttp对象
     */
	public synchronized static BetterHttp init() {
		if (instance == null) {
			instance = new BetterHttp();
		}
		return instance;
	}
	
	private BetterHttp() {
	}

	/** 创建HttpURLConnection连接 */
	public HttpURLConnection createHttpURLConnection(String actionUrl, String method) 
			throws IOException {
		 HttpURLConnection httpConnection = null;
		 // 请求实例对象Url
		 URL url = new URL(actionUrl);
		 // 判断验证数据类型
		 if (url.getProtocol().toLowerCase().equals("https")) {
			trustAllHttpsCertificates();
			httpConnection = (HttpsURLConnection) url.openConnection();
		 } else {
			httpConnection = (HttpURLConnection) url.openConnection();
		 }
		 httpConnection.setDoInput(true);  // 允许输入      
		 httpConnection.setDoOutput(true); // 允许输出
		 httpConnection.setUseCaches(false); 
		 httpConnection.setReadTimeout(5 * 1000); // 读取数据超时时间
		 httpConnection.setConnectTimeout(5 * 1000); // 连接的超时时间
		 httpConnection.setRequestMethod(method);
		 
		 httpConnection.setRequestProperty("Charsert", "UTF-8"); // 请求编码类型
		 httpConnection.setRequestProperty("connection", "keep-alive"); // 维持连接
		 httpConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");  // 响应Gzip,Deflate压缩数据
	     
	     return httpConnection;
	}
	
	/**
	 * 信任所有请求站点
	 */
	private void trustAllHttpsCertificates() {
		try {
			javax.net.ssl.X509TrustManager tm = new javax.net.ssl.X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {return null;}
				@Override
				public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
				@Override
				public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
			};
			// Create a trust manager that does not validate certificate chains:
			javax.net.ssl.TrustManager[] trustAllCerts = { tm };
			javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("TLS"); 
			sc.init(null, trustAllCerts, null);
			javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			javax.net.ssl.HostnameVerifier hv = new javax.net.ssl.HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {return true;}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
     * GET方法传送消息
     * 
     * @param url  连接的URL
     * @param queryString  请求参数串
     * @return 服务器返回的信息
     * @throws Exception
     */
	public String doHttpGet(String url, String queryString) {
		String responseData = null;
		HttpURLConnection connection = null;
        
		try {
			if (queryString != null && !queryString.equals("")) {
	            url += "?" + queryString;
	        }
			connection = createHttpURLConnection(url, "GET");
			connection.connect();
			// 读取响应数据
			responseData = doHttpRequest(connection);
		} catch (IOException ex) {
			if (DEBUG) {
				ex.printStackTrace();
			}
		} finally {
			// 断开连接
			if (connection!=null) {
				connection.disconnect();
				connection=null;
			}
		}
		return responseData;
	}
	
	/**
     * POST方法传送消息
     * 
     * @param url  连接的URL
     * @param queryString 请求参数串
     * @return 服务器返回的信息
     */
	public String doHttpPost(String url, String queryString) {
		String responseData = null;
		HttpURLConnection connection = null;
		
		try {
			connection = createHttpURLConnection(url, "POST");
			connection.connect();
			byte[] data = queryString.toString().getBytes();
			// POST请求不允许缓存
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", String.valueOf(data.length));
			connection.connect();
			// 向服务器端发送数据
			DataOutputStream dataStream = new DataOutputStream(connection.getOutputStream());
			BufferedOutputStream outStream = new BufferedOutputStream(dataStream);
			outStream.write(data);
			outStream.flush();
			outStream.close();
			// 读取响应数据
			responseData = doHttpRequest(connection);
		} catch (IOException ex) {
			if (DEBUG) {
				ex.printStackTrace();
			}
		} finally {
			// 断开连接
			if(connection!=null) {
				connection.disconnect();
				connection=null;
			}
		}
		return responseData;
	}
	
	/**
     * POST方法传送文件和消息 ,通过拼接的方式构造请求内容，实现参数传输以及文件传输
     * 
     * @param url  连接的URL
     * @param queryString 请求参数串
     * @param files 上传的文件列表
     * @return 服务器返回的信息
     */
	public String doHttpPostWithFile(String url, Map<String, String> params, List<File> attachments) {
		String responseData = null;
		HttpURLConnection connection = null;
		
		String BOUNDARY = java.util.UUID.randomUUID().toString(); // 分隔符
		String PREFIX = "--", LINEND = "\r\n"; // 分隔符
		String MULTIPART_FROM_DATA = "multipart/form-data"; // form提交
		String CHARSET = "UTF-8"; // 编码
		if (DEBUG) Log.d(TAG, "BetterHttp httpPostWithFile [1] url = "+url);
		
		try {
			connection = createHttpURLConnection(url, "POST");
			connection.setUseCaches(false);// 不允许缓存
			// 设置请求类型和分隔符
			connection.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			connection.connect();// 打开连接
			// 向服务器端发送数据
			DataOutputStream dataStream = new DataOutputStream(connection.getOutputStream());
			BufferedOutputStream outStream = new BufferedOutputStream(dataStream);
			// 发送参数数据
			if (params != null && params.size() > 0) {
				// 首先组拼文本类型的参数
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET
							+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}
				outStream.write(sb.toString().getBytes());
			}
			// 发送文件数据
			if (attachments !=null && attachments.size() > 0) {
				int i = 0;
				for (File file : attachments) {
					StringBuilder sb = new StringBuilder();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\"file"
							+ (i++) + "\"; filename=\"" + file.getName() + "\""
							+ LINEND);
					sb.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb.append(LINEND);
					outStream.write(sb.toString().getBytes());

					InputStream is = new FileInputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
					outStream.write(LINEND.getBytes());
				}
			}
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			outStream.close();
			// 读取响应数据
			responseData = doHttpRequest(connection);
		} catch (IOException ex) {
			if (DEBUG) {
				ex.printStackTrace();
			}
		} finally {
			// 断开连接
			if (connection != null) {
				connection.disconnect();
				connection=null;
			}
		}
		return responseData;
	}
	
	
	// 读取响应的数据
	private String doHttpRequest(HttpURLConnection connection) {
		if (DEBUG) Log.d(TAG, "BetterHttp httpRequest [1] url = " + connection.getURL());
		try {
			// 读取响应码
			int responseCode = connection.getResponseCode();
			if (DEBUG) Log.d(TAG, "BetterHttp httpRequest [2] StatusLine : "+ responseCode);
			// 200返回码表示成功，其余的表示失败
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = getUngzippedContent(connection);
				// 解析响应数据
		        StringBuilder sb = new StringBuilder();
				BufferedReader reader = new BufferedReader(new UnicodeReader(inputStream, "UTF-8"));
				for (String s = reader.readLine(); s != null; s = reader.readLine()) {
					sb.append(s);
				}
				reader.close();
				String responseData = sb.toString();
				if (DEBUG) Log.d(TAG, "BetterHttp httpRequest [3] Response = "+ responseData);
				return responseData;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
     * 下载网络资源
     * @param pathName 资源存储路径
     * @param url 连接的URL
     * @param progress 下载进度监听
     * @return 资源文件
     */
	 public File downloadFileWithProgress(String pathName, String url, Progress progress) {
		if (DEBUG) Log.d(TAG, "BetterHttp downloadFile [1] url = "+url);
		File desFile = new File(pathName); // 目标文件
    	File tmpFile = new File(pathName.concat(".tmp")); //临时文件

    	if (desFile.exists()) 
    		return desFile;
    	if (desFile.isDirectory()) 
    		return null;
    	if (!desFile.getParentFile().exists()) 
    		desFile.getParentFile().mkdirs();
    	
		InputStream input = null;
		OutputStream out = null;
		HttpURLConnection connection = null;
		
		try {
			connection = createHttpURLConnection(url, "GET");
			connection.connect();
			// 读取响应状态码
            int statusCode = connection.getResponseCode();
            if (DEBUG) Log.d(TAG, "BetterHttp downloadFile [2] StatusLine : "+ statusCode);
            // 200返回码表示成功，其余的表示失败
            if (statusCode != HttpURLConnection.HTTP_OK) {
            	if (DEBUG) Log.e(TAG, "Error " + statusCode + " while retrieving bitmap from " + url); 
            	return null;
            }
            if (connection.getInputStream() != null) {
            	tmpFile.createNewFile(); // 创建新的资源文件
    			long length = connection.getContentLength(); // 网络资源字节长度
    			input = new BufferedInputStream(new FlushedInputStream(connection.getInputStream()));
     			out = new CountingOutputStream(new FileOutputStream(tmpFile), length, progress);
     			
     			byte buffer[] = new byte[2048];
     			int readsize = 0;
     			while ((readsize = input.read(buffer)) > 0) {
     				out.write(buffer, 0, readsize);
     			}
     			input.close();
     			out.flush();
     			out.close();
     			// 更改文件名称
     			if (tmpFile.length() == length) {
					tmpFile.renameTo(desFile);
					return desFile;
				}
            }
		} catch (IOException ex) {
			if (DEBUG) {
				ex.printStackTrace();
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
				connection=null;
			}
		}
		
		return null;
	}

	 /**
      * 下载网络资源
      * @param url 连接的URL
      * @return 资源文件字节码
      */
     public byte[] downloadFileToByte(String url) {
    	Log.d(TAG, "BetterHttp downloadFile [1] url = "+url);
    	
    	BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		HttpURLConnection connection = null;
		
		try {
			connection = createHttpURLConnection(url, "GET");
			connection.connect();
			// 读取响应状态码
            int statusCode = connection.getResponseCode();
            Log.d(TAG, "BetterHttp downloadFile [2] StatusLine : "+ statusCode);
            // 200返回码表示成功，其余的表示失败
            if (statusCode != HttpURLConnection.HTTP_OK) {
            	Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url); 
            	return null;
            }
            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
            	bis = new BufferedInputStream(new FlushedInputStream(inputStream));
     			bos = new ByteArrayOutputStream(2048);
     			byte buffer[] = new byte[2048];
     			int readsize = 0;
     			while ((readsize = bis.read(buffer)) > 0) {
     				bos.write(buffer, 0, readsize);
     			}
     			bis.close();
     			bos.flush();
     			bos.close();
     			return bos.toByteArray();
            }
		} catch (IOException ex) {
			if (DEBUG) {
				ex.printStackTrace();
			}
		} finally {
			if (connection != null) {
				connection.disconnect();
				connection=null;
			}
		}
	    
	    return null;
    }
	 
	/**
	 * 解压缩Gizp/Deflate数据
	 * 
	 * @param connection HttpURLConnection连接
	 * @return 响应数据流
	 */
	private InputStream getUngzippedContent(HttpURLConnection connection)
			throws IOException {
		InputStream inputStream = connection.getInputStream();
		if (inputStream == null)  
			return inputStream;
		String contentEncoding = connection.getHeaderField("Content-Encoding");
		if (contentEncoding == null) 
			return inputStream; 
		if (contentEncoding.contains("gzip") || contentEncoding.contains("zip")) 
			inputStream = new GZIPInputStream(inputStream);
		if (contentEncoding.contains("deflate")) 
			inputStream = new InflaterInputStream(inputStream);
		
		return inputStream;
	}
	
}
