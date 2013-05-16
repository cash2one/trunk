
package com.shandagames.android.network.processor.oauth;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;

/**
 * Processor responsible implement oauth. IMPORTANT! sign of the
 * CommonsHttpOAuthConsumer is not thread safe and as such there are two
 * options. The first is the following implementation where the
 * CommonsHttpOAuthConsumer is created on each process call. The second is to
 * wrap the sign with a synchronized block. <br>
 * By not doing this the CommonsHttpOAuthConsumer can sign different request
 * with the same oauth_nonce parameter (that must be unique x request)
 * 
 * @author luigi@novoda.com, carl@novoda.com
 */
public class OAuthProcessor implements HttpProcessor, OnAccountsUpdateListener {

	private static final boolean DEBUG = true;
	
	private static final String TAG = "OAuthProcessor";
	
    // Within the Account we keep track of consumer key and tokens
    public static final String CONSUMER_KEY = "consumerKey";

    public static final String CONSUMER_SECRET_KEY = "consumerSecretKey";

    public static final String ACCESS_TOKEN_KEY = "tokenKey";

    public static final String ACCESS_TOKEN_SECRET_KEY = "tokenSecretKey";

    private final Handler oauthHandler = new Handler();

    private String consumerKey;

    private String consumerSecret;

    private String token = "";

    private String secret = "";

    private String accountType;

    private Context context;

    public OAuthProcessor(Context context, String accountType) {
        AccountManager.get(context).addOnAccountsUpdatedListener(this, oauthHandler, true);
        this.accountType = accountType;
        this.context = context;
        Account[] account = AccountManager.get(context).getAccountsByType(accountType);
        if (account != null && account.length > 0) {
            init(account[0]);
        }
    }

    @Deprecated
    public OAuthProcessor(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public boolean shouldSignWithoutTokens() {
        return true;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        if (DEBUG) {
            Log.v(TAG, "Executing OAuthProcessor : " + request.getRequestLine().getUri());
        }
        /*if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }*/
        if (request instanceof HttpUriRequest && isInitialized()) {
            try {
                CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
                //consumer.setSendEmptyTokens(false);
                consumer.setTokenWithSecret(token, secret);
                consumer.sign(request);
            } catch (final OAuthMessageSignerException e) {
                Log.e(TAG, "OAuthMessageSignerException", e);
            } catch (final OAuthExpectationFailedException e) {
            	Log.e(TAG, "OAuthExpectationFailedException", e);
            } catch (final OAuthCommunicationException e) {
            	Log.e(TAG, "OAuthCommunicationException", e);
            }
        }
    }

    private boolean isInitialized() {
        return (consumerKey != null && consumerSecret != null);
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
        // Do nothing
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        for (Account account : accounts) {
            if (account.type.equalsIgnoreCase(accountType)) {
                if (DEBUG) {
                    Log.i(TAG, "Getting account information for: " + account.type);
                }
                init(account);
            }
        }
    }

    private void init(Account account) {
    	if (DEBUG) {
            Log.v(TAG, "Inint account");
        }
        AccountManager manager = AccountManager.get(context);
        consumerKey = manager.getUserData(account, CONSUMER_KEY);
        consumerSecret = manager.getUserData(account, CONSUMER_SECRET_KEY);
        token = manager.getUserData(account, ACCESS_TOKEN_KEY);
        secret = manager.getUserData(account, ACCESS_TOKEN_SECRET_KEY);

        OAuthDataImpl oauthdata = new OAuthDataImpl();
        oauthdata.setConsumerKey(consumerKey);
        oauthdata.setConsumerSecret(consumerSecret);
        oauthdata.setToken(token);
        oauthdata.setSecret(secret);

        if (DEBUG) {
            Log.v(TAG, "New Token: " + oauthdata.toString());
        }
    }
}
