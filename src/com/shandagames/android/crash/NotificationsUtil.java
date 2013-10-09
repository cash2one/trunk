package com.shandagames.android.crash;

import com.shandagames.android.constant.Config;
import com.shandagames.android.location.LocationException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @file NotificationsUtil.java
 * @create 2013-5-22 下午05:38:22
 * @author lilong
 * @description TODO dreamxsky@gmail.com
 */
public class NotificationsUtil {
    private static final String TAG = "NotificationsUtil";
    private static final boolean DEBUG = Config.DEBUG;

    public static void ToastReasonForFailure(Context context, Exception e) {
        if (DEBUG) Log.d(TAG, "Toasting for exception: ", e);

        if (e == null) {
            Toast.makeText(context, "A surprising new problem has occured. Try again!",
                    Toast.LENGTH_SHORT).show();
        } else if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "Foursquare over capacity, server request timed out!", Toast.LENGTH_SHORT).show();
            
        } else if (e instanceof SocketException) {
            Toast.makeText(context, "Foursquare server not responding", Toast.LENGTH_SHORT).show();

        } else if (e instanceof IOException) {
            Toast.makeText(context, "Network unavailable", Toast.LENGTH_SHORT).show();

        } else if (e instanceof LocationException) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        } else if (e instanceof IllegalArgumentException) {
            String message;
            int toastLength = Toast.LENGTH_SHORT;
            if (e.getMessage() == null) {
                message = "Invalid Request";
            } else {
                message = e.getMessage();
                toastLength = Toast.LENGTH_LONG;
            }
            Toast.makeText(context, message, toastLength).show();

        } else {
            Toast.makeText(context, "A surprising new problem has occured. Try again!",  Toast.LENGTH_SHORT).show();
        }
    }
}
