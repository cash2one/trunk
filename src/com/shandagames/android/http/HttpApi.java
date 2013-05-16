package com.shandagames.android.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import com.shandagames.android.network.CountingOutputStream.Progress;
import com.shandagames.android.parser.Parser;
import com.shandagames.android.parser.Result;
import com.shandagames.android.parser.ResultType;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @file HttpApi.java
 * @create 2012-9-24 下午4:32:07
 * @author Jacky.Lee
 * @description TODO
 */
public interface HttpApi {

	/** 请求/响应数据逻辑处理 */
	abstract public Result doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends ResultType> parser);

	/** Get方式请求数据 */
	abstract public HttpGet createHttpGet(String url,
			NameValuePair... nameValuePairs);

	/** Post方式请求数据 */
	abstract public HttpPost createHttpPost(String url,
			NameValuePair... nameValuePairs);

	/** 文件上传(参数说明：请求url，参数，上传文件列表) */
	abstract public HttpPost createHttpPostWithFile(String url,
			String queryString, NameValuePair... attachments);

	/** 文件转化成字节流 */
	abstract public byte[] downloadFileToBytes(String url);

	/** 下载文件(参数说明：下载路径，存储路径，下载进度) */
	abstract public File downloadFileToDisk(String url, String filePath,
			Progress progress);

	/** 初始化Http请求参数 */
	abstract public HttpURLConnection createHttpURLConnection(String url,
			String method) throws IOException;

}
