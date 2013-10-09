package com.shandagames.android.log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LogcatService extends Service implements Runnable {

	private static final String DELETE_INTENT = "android.intent.action.DELETE";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(this).start();
		return START_STICKY;
	}

	@Override
	public void run() {
		Process process = null;
		InputStream is = null;
		DataInputStream dis = null;
		try {
			String[] cmds = { "logcat", "-c" };
			String shellCmd = "logcat";
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(cmds).waitFor();
			process = runtime.exec(shellCmd);
			is = process.getInputStream();
			dis = new DataInputStream(is);
			String line = "";
			while ((line = dis.readUTF()) != null) {
				if (line.contains(DELETE_INTENT) && line.contains(getPackageName())) {
					System.err.println("Uninstall App!");
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (is != null) {
					is.close();
				}
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
