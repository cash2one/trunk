package com.shandagames.android.receiver;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateBroadcastReceiver extends PhoneStateListener {

	public static interface OnNetworkConnectedListener {
        public void onNetworkConnecting();
        public void onNetworkConnected();
        public void onNetworkDisConnected();
    }
	
	private OnNetworkConnectedListener onNetworkConnectedListener;
	
	@Override
	public void onDataConnectionStateChanged(int state) {
		// TODO Auto-generated method stub
		if (onNetworkConnectedListener != null) {
			switch (state) {
				case TelephonyManager.DATA_CONNECTING:
					onNetworkConnectedListener.onNetworkConnecting(); //网络正在连接
					break;
				case TelephonyManager.DATA_CONNECTED:
					onNetworkConnectedListener.onNetworkConnected(); //网络已经连接
					break;
				case TelephonyManager.DATA_DISCONNECTED:
					onNetworkConnectedListener.onNetworkDisConnected(); //网络断开连接
					break;
			}
		}
	}

	public void setOnNetworkConnectedListener(OnNetworkConnectedListener listener) {
        this.onNetworkConnectedListener = listener;
    }
	
	public void bind(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		tm.listen(this, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }
	
	public void unbind(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		tm.listen(this, PhoneStateListener.LISTEN_NONE);
	}
}
