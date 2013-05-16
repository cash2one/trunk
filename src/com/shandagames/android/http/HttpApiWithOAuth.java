package com.shandagames.android.http;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;
import com.shandagames.android.parser.Parser;
import com.shandagames.android.parser.Result;
import com.shandagames.android.parser.ResultType;

/**
 * @file HttpApiWithOAuth.java
 * @create 2012-10-11 下午3:20:18
 * @author lilong
 * @description TODO
 */
public class HttpApiWithOAuth extends AbstractHttpApi {
    protected static final String TAG = "HttpApiWithOAuth";

    private OAuthConsumer mConsumer;

    public HttpApiWithOAuth(DefaultHttpClient httpClient, String clientVersion) {
        super(httpClient, clientVersion);
    }

    public Result doHttpRequest(HttpRequestBase httpRequest,
            Parser<? extends ResultType> parser) {
            try {
                if (DEBUG) Log.d(TAG, "Signing request: " + httpRequest.getURI());
                if (DEBUG) Log.d(TAG, "Consumer: " + mConsumer.getConsumerKey() + ", "
                        + mConsumer.getConsumerSecret());
                if (DEBUG) Log.d(TAG, "Token: " + mConsumer.getToken() + ", "
                        + mConsumer.getTokenSecret());
                mConsumer.sign(httpRequest);
            } catch (OAuthCommunicationException ex) {
            	if (DEBUG) Log.d(TAG, "OAuthCommunicationException", ex);
                throw new RuntimeException(ex);
            } catch (OAuthMessageSignerException ex) {
                if (DEBUG) Log.d(TAG, "OAuthMessageSignerException", ex);
                throw new RuntimeException(ex);
            } catch (OAuthExpectationFailedException ex) {
                if (DEBUG) Log.d(TAG, "OAuthExpectationFailedException", ex);
                throw new RuntimeException(ex);
            }
        return executeHttpRequest(httpRequest, parser);
    }

    public void setOAuthConsumerCredentials(String key, String secret) {
        mConsumer = new CommonsHttpOAuthConsumer(key, secret);
    }

    public void setOAuthTokenWithSecret(String token, String tokenSecret) {
        verifyConsumer();
        if (token == null && tokenSecret == null) {
            if (DEBUG) Log.d(TAG, "Resetting consumer due to null token/secret.");
            String consumerKey = mConsumer.getConsumerKey();
            String consumerSecret = mConsumer.getConsumerSecret();
            mConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        } else {
            mConsumer.setTokenWithSecret(token, tokenSecret);
        }
    }

    public boolean hasOAuthTokenWithSecret() {
        verifyConsumer();
        return (mConsumer.getToken() != null) && (mConsumer.getTokenSecret() != null);
    }

    private void verifyConsumer() {
        if (mConsumer == null) {
            throw new IllegalStateException("Cannot call method without setting consumer credentials.");
        }
    }

}
