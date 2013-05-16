package com.shandagames.android.crash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @file CrashHandler.java
 * @create 2012-8-15 下午5:08:28
 * @author lilong
 * @description 异常处理类，当程序发生Uncaught异常的时候, 由该类 来接管程序,并记录 发送错误报告.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

	/** Debug Log tag */
	private static final String TAG = "CrashHandler";
	/** 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能 */
	public static final boolean DEBUG = true;
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** 发送Crash日志到服务器监听函数  */
	private OnCrashHandlerListener mCrashHandlerListener;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;

	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String PACKAGE_NAME = "packageName";
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".log";

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取ExceptionHandler实例 ,单例模式 */
	public synchronized static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		sendPreviousReportsToServer();
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {// 如果自己处理了异常,则不会弹出错误对话框,则需要手动退出app
			try {
				// sleep一会后结束程序
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "Error: ", e);
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10); // 非正常退出
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成, 可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @return true 代表处理该异常,不再向上抛异常， false
	 *         代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
	 *         简单来说就是true不会弹出那个错误提示框，false就会弹出
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序出错啦:" + msg, Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}

		}.start();
		// 收集设备信息
		collectCrashDeviceInfo(mContext);
		// 保存错误报告文件
		saveCrashInfoToFile(ex);
		// 发送错误报告到服务器
		sendCrashReportsToServer(mContext);

		return true;
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 */
	private void sendCrashReportsToServer(Context mContext) {
		if (mCrashHandlerListener == null) return;
		String[] crFiles = getCrashReportFiles(mContext);
		Log.d(TAG, "Collect Crash Count:" + crFiles.length);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));
			List<File> files = new ArrayList<File>();
			for (String fileName : sortedFiles) {
				File dir = mContext.getFilesDir();
				File cr = new File(dir, fileName);
				files.add(cr);
				Log.d(TAG, "Crash File:" + cr.getAbsolutePath());
			}
			postReport(files); // 发送服务器
		}
	}

	/**
	 * 使用HTTP Post 发送错误报告到服务器 上传的时候还可以将该app的version,该手机的机型等信息一并发送的服务器
	 * Android的兼容性众所周知，所以可能错误不是每个手机都会报错,还是有针对性的去debug比较好
	 */
	private void postReport(List<File> attachments) {
		if (mCrashHandlerListener != null) {
			mCrashHandlerListener.handleCrashResponse(attachments);
		}
	}

	/**
	 * 获取错误报告文件名
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context mContext) {
		File filesDir = mContext.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.startsWith("crash-") && name.endsWith(CRASH_REPORTER_EXTENSION));
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mDeviceCrashInfo.setProperty(STACK_TRACE, result);

		try {
			long timestamp = System.currentTimeMillis();
			String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
			File filesDir = mContext.getFilesDir(); // Crash根目录
			File crFile = new File(filesDir, fileName); // Crash文件目录
			FileOutputStream trace = new FileOutputStream(crFile);
			mDeviceCrashInfo.store(trace, null); // 存储Crash文件
			trace.flush();
			trace.close();
			crFile = filesDir = null;
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing report file...", e);
		}
		return null;
	}

	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.setProperty(PACKAGE_NAME, pi.packageName);
				mDeviceCrashInfo.setProperty(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.setProperty(VERSION_CODE, pi.versionCode + "");
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
		} 
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.setProperty(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				Log.e(TAG, "Error while collect crash info", e);
			}
		}
	}
	
	public void setOnCrashHandlerListener(OnCrashHandlerListener mCrashHandlerListener) {
		this.mCrashHandlerListener = mCrashHandlerListener;
	}
	
	
	static public interface OnCrashHandlerListener {
		/**
         * Processes the responses send crash files to the HTTP server
         * 
         * @param attachments params of the send crash files
         */
		void handleCrashResponse(final List<File> attachments);
	}
}
