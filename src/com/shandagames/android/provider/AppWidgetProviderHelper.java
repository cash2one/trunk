package com.shandagames.android.provider;

import com.shandagames.android.log.LogUtils;
import com.shandagames.android.service.UpdateService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * @file AppWidgetProviderHelper.java
 * @create 2012-8-24 上午10:42:16
 * @author lilong
 * @description TODO
 */
public class AppWidgetProviderHelper extends AppWidgetProvider {

	private static final String TAG = "AppWidgetProviderHelper";
	
	private static final String UPDATE_ACTION = "com.app.widget.provider.UPDATE_APP_WIDGET";
	
	@Override
	//接收广播事件,每接收一次广播消息就调用一次
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "onReceive");
		super.onReceive(context, intent);
		
		if (intent.getAction().equals(UPDATE_ACTION)) {
			LogUtils.d(TAG, UPDATE_ACTION);
		}
	}

	@Override
	//到达指定更新时间或用户向桌面添加了App Widget时调用此方法
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "onUpdate");
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent(context ,UpdateService.class);
		intent.setAction(UPDATE_ACTION);
		PendingIntent refreshIntent=PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, 0, 1000, refreshIntent);//每秒1次
	}

	@Override
	//删除App Widget是调用此方法
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	//App WIdget第一次被创建是调用此方法
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "onEnabled");
		super.onEnabled(context);
		
	}

	@Override
	//最后一个App Widget被删除后调用此方法
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		LogUtils.d(TAG, "onDisabled");
		super.onDisabled(context);
	}

}
