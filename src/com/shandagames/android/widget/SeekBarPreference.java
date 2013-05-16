/**
 * 
 */
package com.shandagames.android.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * @file SeekBarPreference.java
 * @create 2012-9-13 下午3:19:36
 * @author lilong
 * @description TODO
 */
public class SeekBarPreference extends DialogPreference {
	static private final String SENSITIVITY_LEVEL_PREF = "sensitivity";
	
    private Context context; 
    private SeekBar sensitivityLevel = null;
    private LinearLayout layout = null;
    public SeekBarPreference(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        this.context = context;
        persistInt(10);
    } 
    
    protected void onPrepareDialogBuilder(Builder builder) {  
        layout = new LinearLayout(context); 
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); 
        layout.setMinimumWidth(400); 
        layout.setPadding(20, 20, 20, 20); 
        sensitivityLevel = new SeekBar(context); 
        if (this.getKey().equalsIgnoreCase(SENSITIVITY_LEVEL_PREF)) {
        	sensitivityLevel.setMax(100);
        }
        else {
        	sensitivityLevel.setMax(10); 
        }
        sensitivityLevel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); 
    	sensitivityLevel.setProgress(getPersistedInt(10));
        layout.addView(sensitivityLevel); 
        builder.setView(layout); 
        //super.onPrepareDialogBuilder(builder); 
    } 
    
    protected void onDialogClosed(boolean positiveResult) { 
        if(positiveResult){ 
            persistInt(sensitivityLevel.getProgress()); 
        } 
        super.onDialogClosed(positiveResult);
    } 
} 
// see http://androidsnippets.com/seekbarpreference
