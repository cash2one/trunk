package com.shandagames.android.oauth;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.NameValuePair;
import android.util.Log;
import com.shandagames.android.network.QHttpClient;
import com.shandagames.android.support.StrOperate;
import com.shandagames.android.util.Base64Encoder;

/**
 * 工具类 OAuth version 1 认证授权以及签名相关<br>
 * 如需自定制http管理器请使用 <pre>OAuthV1Client.setQHttpClient(QHttpClient qHttpClient)</pre> <br>
 * 为本工具类指定http管理器
 */
public class OAuthV1Client {
    private static final String hashAlgorithmName = "HmacSHA1";
    private static String TAG="OAuthV1Client.class";
    
    private static QHttpClient httpClient = QHttpClient.getInstance();;

    private OAuthV1Client(){
    }
    
    /**
     * 获取未授权的Request Token
     * 
     * @param oAuth
     * @return
     * @throws Exception
     */
    public static OAuthV1 requestToken(OAuthV1 oAuth, String requestTokenEndpoint) throws Exception {
        String queryString = getOauthParams(requestTokenEndpoint, "GET", oAuth.getOauthConsumerSecret(), "",
                oAuth.getParamsList());
        oAuth.log("requestToken queryString = " + queryString);
        
        String responseData = httpClient.doHttpGet(requestTokenEndpoint, queryString);
        oAuth.log("requestToken responseData = " + responseData);
        
        if (!parseToken(responseData, oAuth)) {// Request Token 授权不通过
            oAuth.log("requestToken past !");
        }
        return oAuth;
    }
    
    /**
     * 合成转向授权页面的url
     * 
     * @param oAuth
     * @return
     */
    public  static String generateAuthorizationURL(OAuthV1 oAuth, String authorizationUrl){
        return authorizationUrl+"?oauth_token="+ oAuth.getOauthToken();
    }
    
    /**
     * 请求用户授权后，解析开放平台返回的参数是否包含授权码等信息
     * 
     * @param responseData 格式：oauth_token=OAUTH_TOKEN&vcode=VERIFY_CODE&openid=OPENID&openkey=OPENKEY
     * @param oAuth
     * @return
     * @throws Exception
     */
     public  static boolean parseAuthorization(String responseData, OAuthV1 oAuth) throws Exception {
         if (!StrOperate.hasValue(responseData)) {
            return false;
        }

        oAuth.setMsg(responseData);
        String[] tokenArray = responseData.split("&");
        
        Log.i(TAG, "parseToken response=>> tokenArray.length = "+tokenArray.length);
        
        if (tokenArray.length < 4) {
            return false;
        }

        String strOAuthToken = tokenArray[0];
        String strOauthVerifier = tokenArray[1];
        String  strOpenid = tokenArray[2];
        String  strOpenkey = tokenArray[3];

        String[] oAuthToken = strOAuthToken.split("=");
        if (oAuthToken.length < 2) {
            return false;
        }
        oAuth.setOauthToken(oAuthToken[1]);

        String[] oauthVerifier = strOauthVerifier.split("=");
        if (oauthVerifier.length < 2) {
            return false;
        }
        oAuth.setOauthVerifier(oauthVerifier[1]);
         
        String[] openid = strOpenid.split("=");
        if (openid.length < 2) {
            return false;
        }
//        oAuth.setOpenid(openid[1]);
//        
//        String[] openkey = strOpenkey.split("=");
//        if (openkey.length < 2) {
//            return false;
//        }
//        oAuth.setOpenkey(openkey[1]);
//
//        oAuth.setStatus(0);
        return true;
    }

    /**
     * 使用授权后的Request Token换取Access Token
     * 
     * @param oAuth
     * @return
     * @throws Exception
     */
    public  static OAuthV1 accessToken(OAuthV1 oAuth, String accessTokenEndpoint) throws Exception {
        oAuth.log("Getting Access Token ...... \n RequestToken = " + oAuth.getOauthToken() + "\nOauthVerifier = "
                + oAuth.getOauthVerifier());

        String queryString = getOauthParams(accessTokenEndpoint, "GET", oAuth.getOauthConsumerSecret(),
                oAuth.getOauthTokenSecret(), oAuth.getAccessParams());

        oAuth.log("accessToken queryString = " + queryString);
        oAuth.log("accessToken url = " + accessTokenEndpoint);

        String responseData = httpClient.doHttpGet(accessTokenEndpoint, queryString);
        oAuth.log("accessToken responseData = " + responseData);

        if (!parseToken(responseData, oAuth)) {// Access Token 授权不通过
        }
        return oAuth;
    }

    /**
     * 处理请求参数 和 生成签名
     * 
     * @param url
     * @param httpMethod
     * @param consumerSecret
     * @param tokenSecrect
     * @param queryParamsList
     * @return
     */
    public static String getOauthParams(String url, String httpMethod, String consumerSecret, String tokenSecrect,
            List<NameValuePair> queryParamsList) {

        // 对参数进行排序
        Comparator<NameValuePair> comparator = new Comparator<NameValuePair>() {
            public int compare(NameValuePair p1, NameValuePair p2) {
                int result = p1.getName().compareTo(p2.getName());
                if (0 == result)
                    result = p1.getValue().compareTo(p2.getValue());
                return result;
            }
        };
        Collections.sort(queryParamsList, comparator);

        //生成请求URL
        String urlWithParameter = url;

        String queryString = StrOperate.getQueryString(queryParamsList);
        if (StrOperate.hasValue(queryString)) {
            urlWithParameter += "?" + queryString;
        }

        URL aUrl = null;
        try {
            aUrl = new URL(urlWithParameter);
        } catch (MalformedURLException e) {
            System.err.println("URL parse error:" + e.getLocalizedMessage());
        }

        String signature = generateSignature(aUrl, consumerSecret, tokenSecrect, httpMethod, queryParamsList);

        queryString += "&oauth_signature=";
        queryString += StrOperate.paramEncode(signature);
        return queryString;
    }

    /**
     * 验证Token返回结果
     * 
     * @param response
     * @param oAuth
     * @return
     * @throws Exception
     */
    public  static boolean parseToken(String response, OAuthV1 oAuth) throws Exception {
        if (response == null || response.equals("")) {
            return false;
        }

        oAuth.setMsg(response);
        String[] tokenArray = response.split("&");
        Log.i(TAG,"parseToken response=>> tokenArray.length = " + tokenArray.length);
        
        if (tokenArray.length < 2) {
            return false;
        }

        String strTokenKey = tokenArray[0];
        String strTokenSecrect = tokenArray[1];

        String[] token = strTokenKey.split("=");
        if (token.length < 2) {
            return false;
        }
        oAuth.setOauthToken(token[1]);

        String[] tokenSecrect = strTokenSecrect.split("=");
        if (tokenSecrect.length < 2) {
            return false;
        }
        oAuth.setOauthTokenSecret(tokenSecrect[1]);
        
        return true;
    }

    /**
     * 生成签名值
     * 
     * @param url
     * @param consumerSecret
     * @param accessTokenSecret
     * @param httpMethod
     * @param queryParamsList
     * @return
     */
    private  static String generateSignature(URL url, String consumerSecret, String accessTokenSecret, String httpMethod,
            List<NameValuePair> queryParamsList) {
        String base = generateSignatureBase(url, httpMethod, queryParamsList);
        return generateSignature(base, consumerSecret, accessTokenSecret);
    }

    /**
     * 生成签名值
     * 
     * @param base
     * @param consumerSecret
     * @param accessTokenSecret
     * @return
     */
    private static String generateSignature(String base, String consumerSecret, String accessTokenSecret) {
        try {
            Mac mac = Mac.getInstance(hashAlgorithmName);
            String oAuthSignature = StrOperate.paramEncode(consumerSecret) + "&"
                    + ((accessTokenSecret == null) ? "" : StrOperate.paramEncode(accessTokenSecret));

            SecretKeySpec spec = new SecretKeySpec(oAuthSignature.getBytes(), hashAlgorithmName);
            mac.init(spec);
            byte[] bytes = mac.doFinal(base.getBytes());
            return new String(Base64Encoder.encode(bytes));
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 处理签名请求和参数
     * 
     * @param url
     * @param httpMethod
     * @param queryParamsList
     * @return
     */
    private  static String generateSignatureBase(URL url, String httpMethod, List<NameValuePair> queryParamsList) {
        StringBuilder base = new StringBuilder();
        base.append(httpMethod.toUpperCase(Locale.getDefault()));
        base.append("&");
        base.append(StrOperate.paramEncode(getNormalizedUrl(url)));
        base.append("&");
        base.append(StrOperate.paramEncode(StrOperate.getQueryString(queryParamsList)));

        return base.toString();
    }

    /**
     * 处理请求URL
     * 
     * @param url
     * @return
     */
    private static String getNormalizedUrl(URL url) {
        try {
            StringBuilder buf = new StringBuilder();
            buf.append(url.getProtocol());
            buf.append("://");
            buf.append(url.getHost());
            if ((url.getProtocol().equals("http") || url.getProtocol().equals("https")) && url.getPort() != -1) {
                buf.append(":");
                buf.append(url.getPort());
            }
            buf.append(url.getPath());
            return buf.toString();
        } catch (Exception e) {
        }
        return null;
    }

    public static QHttpClient getQHttpClient() {
        return httpClient;
    }

    public static void setQHttpClient(QHttpClient qHttpClient) {
    	httpClient = qHttpClient;
    }

}
