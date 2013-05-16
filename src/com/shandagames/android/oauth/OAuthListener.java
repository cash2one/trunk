package com.shandagames.android.oauth;

import android.os.Bundle;


public interface OAuthListener {

    /**
     * Called when the webview completes.
     * 
     * Executed by the thread that initiated the webview.
     * 
     * @param values
     *            Key-value string pairs extracted from the response.
     */
    public void onComplete(Bundle values);


    /**
     * Called when the webview has an error.
     * 
     * Executed by the thread that initiated the webview.
     * 
     */
    public void onError(OAuthError e);

    /**
     * Called when the webview is canceled by the user.
     * 
     * Executed by the thread that initiated the webview.
     * 
     */
    public void onPageFinished();
}
