package com.shandagames.android.download;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DownloadReceiver extends ResultReceiver {

	public DownloadReceiver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	public DownloadReceiver(Handler handler, ProgressDialog dialog) {
		this(handler);
	}
	
	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		
	}

}
