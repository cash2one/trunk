/**
 * 
 */
package com.shandagames.android;

import android.os.Bundle;
import com.phonegap.DroidGap;

/**
 * @file PhotoGapActivity.java
 * @create 2012-9-19 上午11:22:09
 * @author lilong
 * @description TODO
 */
public class PhoneGapActivity extends DroidGap {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}
