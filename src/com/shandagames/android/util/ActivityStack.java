package com.shandagames.android.util;

import java.io.ObjectStreamException;
import java.util.Stack;
import com.shandagames.android.app.BaseAlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;

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
	
	private final static byte[] lock = new byte[0];
	
	 //定义一个私有的静态全局变量来保存该类的唯一实例
	private static ActivityStack instance;
	
	private Stack<Activity> activitystack;

	/**
	 * 构造函数必须是私有的 
	 * 这样在外部便无法使用 new 来创建该类的实例 
	 */
	private ActivityStack() {
	}

	public static ActivityStack getInstance() {
		synchronized (lock) {
			//这里可以保证只实例化一次 
	        //即在第一次调用时实例化 
	        //以后调用便不会再实例化 
			if (instance == null) {
				instance = new ActivityStack();
			}
			return instance;
		}
	}

	/**这样当JVM从内存中反序列化地"组装"一个新对象时,就会自动调用这个 
	 * readResolve方法来返回我们指定好的对象了, 单例规则也就得到了保证*/
	private Object readResolve() throws ObjectStreamException{
		  return instance;//返回该类的单例对象
    }
	
	
	/** 获取当前栈顶Activity */
	public Activity peekActivity() {
		synchronized (lock) {
			Activity activity = null;
			if (activitystack != null) {
				activity = activitystack.lastElement();
			}
			return activity;
		}
	}

	/** 将当前Activity推入栈中 */
	public void pushActivity(Activity activity) {
		synchronized (lock) {
			if (activitystack == null) {
				activitystack = new Stack<Activity>();
			}
			activitystack.add(activity);
		}
	}
	
	/** 从当前栈集合中移除Activity */
	public void removeActivity(Activity activity) {
		synchronized (lock) {
			if (activity != null) {
				activitystack.remove(activity);
			}
		}
	}
	
	/** 从栈中弹出当前的Activity实例  */
	public void popActivity(Activity activity) {
		synchronized (lock) {
			if (activity != null) {
				activity.finish();
				activitystack.remove(activity);
				activity = null;
			}
		}
	}

	/** 弹出栈中所有Activity */
	public void clearActivity() {
		synchronized (lock) {
			while (true) {
				Activity activity = peekActivity();
				if (activity == null) {
					break;
				}
				popActivity(activity);
			}
		}
	}

	/** 取出指定Activity实例  */
	public Activity peekActivity(String clazzName) {
		synchronized (lock) {
			if (clazzName!=null && clazzName.length()>0) {
				while (true) {
					Activity activity = peekActivity();
					if (activity.getClass().getCanonicalName().equals(clazzName)) {
						return activity;
					}
				}
			}
			return null;
		}
	}
	
	/** 搜索指定Activity在栈中的位置 ,从1开始 */
	public int search(Activity activity) {
		synchronized (lock) {
			if (activity != null) {
				return activitystack.search(activity);
			}
			return 0;
		}
	}
	
	/** 栈中存储Activity数目  */
	public int getStackSize() {
		synchronized (lock) {
			return activitystack.size();
		}
	}

	/** 栈中存储Activity是否为空  */
	public boolean isEmpty() {
		synchronized (lock) {
			return activitystack.isEmpty();
		}
	}

	@Override
	public String toString() {
		return "Activitystack=" + activitystack;
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