/**
 * 
 */
package com.shandagames.android.service;

import com.shandagames.android.R;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.shandagames.android.provider.AppWidgetProviderHelper;
import com.shandagames.android.util.DateHelper;

/**
 * @file UpdateService.java
 * @create 2012-8-24 上午11:34:37
 * @author lilong
 * @description 更新当前时间
 */
public class UpdateService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		//AppWidget运行的进程和应用app不再一个进程中，访问View只能采用RemoteViews
		RemoteViews updateViews = new RemoteViews(getPackageName(), R.layout.appwidget); 
		updateViews.setTextViewText(R.id.time, DateHelper.formatString(System.currentTimeMillis(), "HH:mm:ss"));  
		
		ComponentName thisWidget = new ComponentName(this, AppWidgetProviderHelper.class);  
		AppWidgetManager manager = AppWidgetManager.getInstance(this);  
		manager.updateAppWidget(thisWidget, updateViews); 
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
