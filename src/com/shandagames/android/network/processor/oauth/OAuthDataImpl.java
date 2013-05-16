
package com.shandagames.android.network.processor.oauth;

public class OAuthDataImpl implements OAuthData {

    private String consumerKey;

    private String consumerSecret;

    private String token;

    private String secret;

    @Override
    public String getTokenKey() {
        return token;
    }

    @Override
    public String getTokenSecret() {
        return secret;
    }

    @Override
    public String getConsumerKey() {
        return consumerKey;
    }

    @Override
    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return String.format(
                "consumerKey: %1$s, consumerSecret: %2$s, token: %3$s, tokenSecret: %4$s",
                consumerKey, consumerSecret, token, secret);
    }

}
