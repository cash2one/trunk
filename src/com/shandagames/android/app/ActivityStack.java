package com.shandagames.android.app;

import java.util.Stack;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import java.io.ObjectStreamException;

/**
 * @file ActivityStack.java
 * @create 2012-9-20 下午6:04:05
 * @author Jacky.Lee
 * @description 单例模式如果反序列化会生成两个实例：
 * 		实现Cloneable接口，调用clone()方法，复制一个对象副本，即反序列化会生成副本；
 * 		解决：实现序列化的类里添加方法：readResolve()
 */
public class ActivityStack {

	private static final long serialVersionUID = 1L;
	
	 //定义一个私有的静态全局变量来保存该类的唯一实例
	private static ActivityStack instance;
	
	private Stack<Activity> activityStack = new Stack<Activity>();

	/**
	 * 构造函数必须是私有的 
	 * 这样在外部便无法使用 new 来创建该类的实例 
	 */
	private ActivityStack() {
	}

	public synchronized static ActivityStack getInstance() {
		//这里可以保证只实例化一次 
        //即在第一次调用时实例化 
        //以后调用便不会再实例化 
		if (instance == null) {
			instance = new ActivityStack();
		}
		return instance;
	}

	/**
	 * 将当前Activity推入栈中 
	 */
	public void pushActivity(Activity activity) {
		if (activity != null) {
			activityStack.add(activity);
		}
	}
	
	/**
	 * 获取当前栈顶的Activity并移除它
	 */
	public Activity popActivity() {
		if (!activityStack.isEmpty()) {
			return activityStack.pop();
		}
		return null;
	}

	/**
	 *	获取当前栈顶Activity 
	 */
	public Activity peekActivity() {
		if (!activityStack.isEmpty()) {
			return activityStack.peek();
		}
		return null;
	}
	
	/**
	 * 	清空栈中存储的Activity
	 */
	public void clearActivity() {
		activityStack.clear();
	}

	/**
	 * 栈中存储Activity数目
	 */
	public int getStackSize() {
		return activityStack.size();
	}

    /**这样当JVM从内存中反序列化地"组装"一个新对象时,就会自动调用这个 
	 * readResolve方法来返回我们指定好的对象了, 单例规则也就得到了保证*/
	private Object readResolve() throws ObjectStreamException{
		  return instance;//返回该类的单例对象
    }
	
	/** 退出整个应用,带提示功能 */
    public void showDialog(final Context context) {   
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
        BaseAlertDialog.Builder builder = new BaseAlertDialog.Builder(context);   
        builder.setTitle("温馨提示");   
        builder.setMinWidth(dm.widthPixels-50);
        builder.setMessage("确认退出该应用吗?");
        builder.setPositiveButton(android.R.string.ok,   
                new DialogInterface.OnClickListener() {   
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	dialog.dismiss();
                    	((Activity)context).finish();
                    }   
                });   
        builder.setNegativeButton(android.R.string.cancel, null); 
        builder.create().show();
    }  
}