/**
 * Copyright 2010 Mark Wyszomierski
 * Portions Copyright (c) 2008-2010 CommonsWare, LLC
 */
package com.shandagames.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * 
 * @file WakefulIntentService.java
 * @create 2012-9-20 下午1:12:52
 * @author lilong
 * @description This is based off the chapter 13 sample in Advanced Android Development, by Mark Murphy
 * 				http://www.cnblogs.com/keyindex/articles/1819504.html
 */
public abstract class WakefulIntentService extends IntentService { 
    public static final String TAG = "WakefulIntentService";
    public static final String LOCK_NAME_STATIC = "com.shandagames.android.WakefulintentService.Static"; 
    
    private static PowerManager.WakeLock lockStatic = null; 
    
    abstract void doWakefulWork(Intent intent); 
    
    /** 获取了 SCREEN_DIM_WAKE_LOCK锁，该锁使 CPU 保持运转，屏幕保持亮度 */
    public static void acquireStaticLock(Context context) { 
        getLock(context).acquire(); 
    }
  
    private synchronized static PowerManager.WakeLock getLock(Context context) { 
        if (lockStatic == null) { 
            PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE); 
            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true); 
        }
    
        return(lockStatic); 
    } 
  
    public WakefulIntentService(String name) { 
        super(name); 
    } 
  
    @Override 
    final protected void onHandleIntent(Intent intent) { 
        doWakefulWork(intent); 
        getLock(this).release(); 
    } 
}
