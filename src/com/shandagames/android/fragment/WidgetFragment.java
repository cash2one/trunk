package com.shandagames.android.fragment;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shandagames.android.HomeActivity;
import com.shandagames.android.PullToRefreshActivity;
import com.shandagames.android.R;
import com.shandagames.android.SlidingPanelActivity;
import com.shandagames.android.SlidingUpPanelActivity;
import com.shandagames.android.app.BaseListFragment;
import com.shandagames.android.fragment.ProgressDialogFragment.OnDownloadProgressListener;
import com.shandagames.android.jni.NativeSample;
import com.shandagames.android.jni.NativeSample01;
import com.shandagames.android.jni.NativeSample02;
import com.shandagames.android.log.Log;
import com.shandagames.android.network.RequestExecutor;
import com.shandagames.android.service.SocketService;
import com.shandagames.android.support.IntentSupport;
import com.shandagames.android.task.DownloadFileTask;
import com.shandagames.android.task.ITaskListener;
import com.shandagames.android.util.NetworkUtils;
import com.shandagames.android.util.ToastUtil;

public class WidgetFragment extends BaseListFragment implements OnItemClickListener {
	private static final int ACTIVITY_REQUEST_CODE_SPEEK_VOICE = 0x100;
	
	private static final String REQUEST_DOWNLOAD_FILE_TASK = "DOWNLOAD_FILE_TASK";
	
	private Activity activity;
	private ListView listView;
	
	private TextToSpeech tts;
	private DownloadFileTask downloadTask;
	private ProgressDialogFragment dialogFragment;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
		listView.setOnItemClickListener(this);
		
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(activity, 
				android.R.layout.simple_list_item_1, android.R.id.text1, 
				getResources().getStringArray(R.array.widget_menu_list));
		setListAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
			parseJson();
			break;
		case 1:
			startActivity(IntentSupport.newManageApplicationIntent());
			break;
		case 2:
			connectGtalk();
			break;
		case 3:
			UnsaveDialogFragment unSaveDialogFragment = new UnsaveDialogFragment();
			unSaveDialogFragment.show(getChildFragmentManager(), unSaveDialogFragment.getClass().getName());
			break;
		case 4:
			speekVoice();
			break;
		case 5:
			textToSpeech();
			break;
		case 6:
			startActivity(new Intent(activity, SlidingUpPanelActivity.class));
			break;
		case 7:
			startActivity(new Intent(activity, SlidingPanelActivity.class));
			break;
		case 8:
			startActivity(new Intent(activity, PullToRefreshActivity.class));
			break;
		case 9:
			showNotification();
			break;
		case 10:
			useNdk();
			break;
		case 11:
			shareContent();
			break;
		case 12:
			String url="http://shouji.baidu.com/download/1426l/AppSearch_Android_1426l.apk";
			downloadTask = new DownloadFileTask(REQUEST_DOWNLOAD_FILE_TASK,mTaskListener);
			downloadTask.execute(RequestExecutor.getThreadExecutor(), url);
			break;
		case 13:
			startService(new Intent(activity, SocketService.class));
			break;
		case 14:
			String uriString="http://" + NetworkUtils.ipToString(activity) + ":"+SocketService.CONNECTION_POST;
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(uriString));
			startActivity(intent);
			break;
		}
	}
	
	private void parseJson() {
		setProgressBarIndeterminateVisibility(true);
		final RequestQueue volleyQueue = Volley.newRequestQueue(activity);
		String url = "http://jsonview.com/example.json";
		volleyQueue.add(new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				volleyQueue.cancelAll(this);
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(activity, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
			}
		}));
	}
	
	private void connectGtalk() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//指定Gtalk服务器地址和端口号
					ConnectionConfiguration configuration = new ConnectionConfiguration(
							"talk.google.com", 5222, "gmail.com");
					//根据配置的地址和端口号创建xmpp对象
					XMPPConnection xmppConnection = new XMPPConnection(configuration);
					// 连接gtalk服务器
					xmppConnection.connect(); 
					// 使用账号和密码登陆服务器
					xmppConnection.login("dreamxsky@gmail.com", "5340Selience");
					
					Presence presence = new Presence(Presence.Type.available);
					//登陆成功向gtalk服务器发送一条消息，表明当前处于活动状态
					xmppConnection.sendPacket(presence);
					
					StringBuilder sb = new StringBuilder();
					Collection<RosterEntry> entries = xmppConnection.getRoster().getEntries();
					for (RosterEntry entry : entries) {
						sb.append(entry.getName()+":"+entry.getUser()+"\n");
					}
					if (sb.length()>0) sb=sb.deleteCharAt(sb.length()-1);
					
					ToastUtil.showMessage(activity, sb.toString());
				} catch (XMPPException ex) {
					ToastUtil.showMessage(activity, "登陆失败");
				}
			}
		}).start();
	}

	private void speekVoice() {
        try {
			//语音识别模式,ACTION_RECOGNIZE_SPEECH 捕捉文字序列,ACTION_WEB_SEARCH 网络搜索
			Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "语音搜索");
			startActivityForResult(speechIntent, ACTIVITY_REQUEST_CODE_SPEEK_VOICE);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(activity, "未发现语音识别设备", Toast.LENGTH_LONG).show();
		}
	}

	private void textToSpeech() {
		tts = new TextToSpeech(activity, new OnInitListener() {
			
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = tts.setLanguage(Locale.US); //设置发音语言
					// tts.setPitch(5); // set pitch level
					// tts.setSpeechRate(2); // set speech speed rate
					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						Toast.makeText(activity, "Language is not supported", Toast.LENGTH_LONG).show();
					} else {
						tts.speak("hello word", TextToSpeech.QUEUE_FLUSH, null);
					}
				} else {
					Toast.makeText(activity, "Language is not supported", Toast.LENGTH_LONG).show();
				}
			}
		});
		// 文字转化成语音
		tts.speak("hello word", TextToSpeech.QUEUE_FLUSH, null);
	}
	
	private void showNotification() {
		Intent intent = new Intent(activity, HomeActivity.class);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0x100, new NotificationCompat.Builder(activity)
			 	.setSmallIcon(android.R.drawable.sym_def_app_icon)
		        .setTicker("Ticker: test")
		        .setContentTitle("Title: test")
		        .setContentInfo("Content Info")
		        .setContentText("Content Text")
		        .setAutoCancel(true)
		        .setWhen(System.currentTimeMillis())
		        .setDefaults(Notification.DEFAULT_VIBRATE)
		        .setVibrate(new long[]{500, 500})
		        .setContentIntent(PendingIntent.getActivity(activity, 0, intent, 0))
		        .getNotification());
	}
	
	private void useNdk() {
		NativeSample mNative = new NativeSample();
		ToastUtil.showMessage(activity, mNative.sayHi());
		
		NativeSample01 mNative01 = new NativeSample01();
		Log.i("NativeSample", mNative01.add(100, 120)+"");
		Log.i("NativeSample", mNative01.sayHelloInC("Hello World"));
		int[] number = mNative01.intMethod(new int[]{1,2,3,4,5});
		Log.i("NativeSample", number[0]+"");
		
		NativeSample02 mNateve02 = new NativeSample02();
		mNateve02.callCcode();
		mNateve02.callCcode1();
		mNateve02.callCcode2();
	}
	
	private void shareContent() {
		String subject = "Feedback on your app";
		String message = "Hi, \n\nYour Feedback sample app rocks! I would like to give you some feedback:";
		startActivity(IntentSupport.newShareIntent(activity, subject, message, "Send feedback"));
	}
	
	private ITaskListener mTaskListener = new ITaskListener() {
		
		@Override
		public void onTaskStart(String taskName) {
			// 文件下载
			if (taskName.equals(REQUEST_DOWNLOAD_FILE_TASK)) {
				dialogFragment = new ProgressDialogFragment();
				dialogFragment.setProgressListener(new OnDownloadProgressListener() {
					
					@Override
					public void onCancelTask() {
						// 取消任务
						downloadTask.cancelTask(true);
						downloadTask = null;
					}
				});
				dialogFragment.show(getChildFragmentManager(), dialogFragment.getClass().getName());
			}
		}
		
		@Override
		public void onTaskLoading(Object value) {
			// 更新进度
			dialogFragment.getDialog().setProgress(Integer.valueOf(value.toString()));
		}
		
		@Override
		public void onTaskFinished(String taskName, Object result) {
			// 完成任务
			if (taskName.equals(REQUEST_DOWNLOAD_FILE_TASK)) {
				dialogFragment.dismiss();
			}
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVITY_REQUEST_CODE_SPEEK_VOICE:// 语音输入
			if(data!=null && resultCode==Activity.RESULT_OK) {
				List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				StringBuilder sb = new StringBuilder();
				for (String entry : list) {
					sb.append(entry+",");
				}
				Toast.makeText(activity, sb.deleteCharAt(sb.length()-1), Toast.LENGTH_LONG).show();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (tts != null) {
			tts.stop();
			tts.shutdown();
			tts = null;
		}
	}
}
