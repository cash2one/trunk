package com.shandagames.android.util;

import com.shandagames.android.base.AndroidApplication;

import android.content.Context;

/**
 * @author  lilong
 * @version 2012-7-19 上午11:11:29
 *
 */
public class AppUtils {

	private AppUtils() {
	}

	/**
     * Return the current {@link GDApplication}
     * 
     * @param context The calling context
     * @return The {@link GDApplication} the given context is linked to.
     */
    public static AndroidApplication getBaseApplication(Context context) {
        return (AndroidApplication) context.getApplicationContext();
    }
    
}
