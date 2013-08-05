/**
 * 
 */
package com.shandagames.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.shandagames.android.adapter.MenuAdapter;
import com.shandagames.android.app.ActivityStack;
import com.shandagames.android.app.AndroidApplication;
import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.ballons.BallonsActivity;
import com.shandagames.android.common.ExtendOptionsMenu;
import com.shandagames.android.constant.PreferenceSettings;
import com.shandagames.android.http.BetterHttpApiV1;
import com.shandagames.android.jni.NativeSample;
import com.shandagames.android.jni.NativeSample01;
import com.shandagames.android.jni.NativeSample02;
import com.shandagames.android.log.Log;
import com.shandagames.android.network.Request;
import com.shandagames.android.network.RequestExecutor;
import com.shandagames.android.network.CountingOutputStream.Progress;
import com.shandagames.android.parser.Result;
import com.shandagames.android.support.IntentSupport;
import com.shandagames.android.support.StrOperate;
import com.shandagames.android.task.GenericTask;
import com.shandagames.android.task.TaskListener;
import com.shandagames.android.util.UIUtils;
import com.shandagames.android.util.BrightNessHelper;
import com.shandagames.android.util.ToastUtil;
import com.shandagames.android.view.ExtendMediaPicker;
import com.shandagames.android.view.ExtendMediaPicker.OnMediaPickerListener;
import com.shandagames.android.zing.IntentIntegrator;
import com.shandagames.android.zing.IntentResult;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.UiModeManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @file MainActivity.java
 * @create 2012-11-14 下午5:20:01
 * @author Jacky.Lee
 * @description TODO
 */
public class MainActivity extends BaseActivity implements OnItemClickListener, 
	OnInitListener, OnMediaPickerListener, TaskListener {

	private static final int ID_DIALOG_DATE = 0;
	private static final int ID_DIALOG_TIME = 1;
	private static final int ID_DIALOG_LOADING = 2;
	private static final int ID_DIALOG_WAITING = 3;
	
	private static final int ACTIVITY_REQUEST_CODE_SPEEK_VOICE = 0;
	private static final int ACTIVITY_REQUEST_CODE_SHARE_WEIBO = 1;
	
	private static final String DOWNLOAD_FILE_TASK_ID = "DOWNLOAD_FILE_WITH_PROGRESS";
	
	private TextToSpeech tts;
	private ExtendMediaPicker mMediaPicker;
	private ExtendOptionsMenu mOptionsMenu;
	private ProgressDialog waitingDialog;
	private NotificationManager mNotificationManager;
	private DownloadFileFromURL downloadFileTask;
	
	private boolean mIsNightMode;
	private int mYear, mMonth, mDay, mHour, mMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
		
		mMediaPicker = new ExtendMediaPicker(this);
		mMediaPicker.setOnMediaPickerListener(this);
		
    	Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}
	
	private void initialize() {
		if (AndroidApplication.getInstance().isReady()) {
			setContentView(R.layout.main);
			getWindow().setBackgroundDrawable(null);
			ensureUi();
			shareMessage();
			// 更新升级
		} else {
			// 注册登录
			//finish();
		}
	}
	
	protected void onResume() {
		super.onResume();
		((AndroidApplication)getApplication()).requestLocationUpdates(false);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		//系统有可能杀掉Activity，在onCreate中同时处理
		super.onNewIntent(intent);
	}
	
	public void showUserGuide() {
		if (!PreferenceSettings.isFisrtRun(AndroidApplication.mUserPrefs)) {
	        startActivity(new Intent(this, StartActivity.class));
			PreferenceSettings.storeIsFirstRun(AndroidApplication.mUserPrefs);
		} 
	}
	
	public void shareMessage() {
		Intent intent = getIntent();
    	if(Intent.ACTION_SEND.equals(intent.getAction())){
    		String text=null;
    		String imagePath=null;
    		if(intent.hasExtra(Intent.EXTRA_TEXT)){
    			text = intent.getStringExtra(Intent.EXTRA_TEXT);
    		}
    		if(intent.hasExtra(Intent.EXTRA_STREAM)){
    			Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        		if(uri!=null){
        			 String scheme = uri.getScheme();
                     if (scheme.equals("content")) {
                         ContentResolver contentResolver = getContentResolver();
                         Cursor cursor = contentResolver.query(uri, null, null, null, null);
                         cursor.moveToFirst();
                         imagePath = cursor.getString(cursor.getColumnIndexOrThrow(Images.Media.DATA));
                     }else if(scheme.equals("file")){
                    	 imagePath=uri.getPath();
                     }
        		}
    		}
    		if(text!=null){
    			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    		}
    		if(imagePath!=null){
    			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    		}
    	}
	}
	
	private void ensureUi() {
		String[] arr = getResources().getStringArray(R.array.simple_menu_list);
		GridView gridView = (GridView) findViewById(android.R.id.list);
		List<String> listData = java.util.Arrays.asList(arr);
		MenuAdapter mAdapter = new MenuAdapter(this);
		mAdapter.addAll(listData, false);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);
	}

	@TargetApi(8)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (position) {
			case 0:
				shareWeibo();
				break;
			case 1:
				judgeSpeekVoice();
				break;
			case 2:
				startActivity(new Intent(this, CloudTagActivity.class));
				break;
			case 3:
				Location location = ((AndroidApplication)getApplication()).getLastKnownLocation();
				System.out.println("show location:" + location);
				GetAddressFromLocation(location);
				if (location != null) {
					intent = new Intent(this, PlacesActivity.class);
					intent.putExtra("location", location);
					startActivity(intent);
				}
				break;
			case 4:
				startActivity(new Intent(this, StickyActivity.class));
				break;
			case 5:
				startActivity(new Intent(this, SlideMenuActivity.class));
				break;
			case 6:
				startActivity(new Intent(this, PhoneGapActivity.class));
				break;
			case 7:
				mMediaPicker.showImagePicker();
				break;
			case 8:
				String url="http://shouji.baidu.com/download/1426l/AppSearch_Android_1426l.apk";
				downloadFileTask = new DownloadFileFromURL(DOWNLOAD_FILE_TASK_ID, this);
				downloadFileTask.execute(url);
				break;
			case 9:
				showNotification();
				break;
			case 10:
				tts = new TextToSpeech(this, this);
				// 文字转化成语音
				tts.speak("hello word", TextToSpeech.QUEUE_FLUSH, null);
				break;
			case 11:
				String subject = "Feedback on your app";
				String message = "Hi, \n\nYour Feedback sample app rocks! I would like to give you some feedback:";
				intent = IntentSupport.newShareIntent(this, subject, message, "Send feedback");
				startActivity(intent);
				break;
			case 12:
				startActivity(new Intent(this, SettingsActivity.class));
				break;
			case 13:
				startActivity(new Intent(this, TabHostActivity.class));
				break;
			case 14:
				startActivity(new Intent(this, CircleProgressActivity.class));
				break;
			case 15:
				startActivity(new Intent(this, WordsActivity.class));
				break;
			case 16:
				startActivity(new Intent(this, PullToRefreshActivity.class));
				break;
			case 17:
				startActivity(new Intent(this, WebPlayerActivity.class));
				break;
			case 18:
				mMediaPicker.showVideoPicker();
				break;
			case 19:
				intent = new Intent(this, VPlayerActivity.class);
				startActivity(intent);
				break;
			case 20:
				UiModeManager umm = (UiModeManager)getSystemService(Context.UI_MODE_SERVICE);
				if (umm.getCurrentModeType()!=Configuration.UI_MODE_TYPE_CAR) {
					umm.enableCarMode(0);
					umm.setNightMode(UiModeManager.MODE_NIGHT_YES);
				} else {
					umm.disableCarMode(0);
					umm.setNightMode(UiModeManager.MODE_NIGHT_NO);
				}
				break;
			case 21:
				NativeSample mNative = new NativeSample();
				ToastUtil.showMessage(this, mNative.sayHi());
				
				NativeSample01 mNative01 = new NativeSample01();
				Log.i("NativeSample", mNative01.add(100, 120)+"");
				Log.i("NativeSample", mNative01.sayHelloInC("Hello World"));
				int[] number = mNative01.intMethod(new int[]{1,2,3,4,5});
				Log.i("NativeSample", number[0]+"");
				
				NativeSample02 mNateve02 = new NativeSample02();
				mNateve02.callCcode();
				mNateve02.callCcode1();
				mNateve02.callCcode2();
				break;
			case 22:
				startActivity(new Intent(this, BallonsActivity.class));
				break;
			case 23:
				changeBrightNess();
				break;
			case 24:
				if (!mIsNightMode) {
					changeTheme("com.android.demo");
					mIsNightMode = true;
				} else {
					findViewById(R.id.main).setBackgroundColor(getResources().getColor(R.color.main_background));
					mIsNightMode = false;
				}
				break;
			case 25:
				//IntentIntegrator.initiateScan(this);
				startActivity(new Intent(this, BarCodeActivity.class));
				break;
			case 26:
				startActivity(new Intent(this, ActionBarActivity.class));
				break;
			case 27:
				startActivity(new Intent(this, SegmentedRadioActivity.class));
				break;
			case 28:
				startActivity(new Intent(this, WiFiSettingActivity.class));
				break;
			case 29:
				startActivity(new Intent(this, PathActivity.class));
				break;
			case 30:
				startActivity(new Intent(this, ImageActivity.class));
				break;
			case 31:
				startActivity(IntentSupport.newManageApplicationIntent());
				break;
			case 32:
				startActivity(new Intent(this, WheelViewActivity.class));
				break;
			case 33:
				showDialog(ID_DIALOG_DATE);
				break;
			case 34:
				startActivity(new Intent(this, FileDownloadActivity.class));
				break;
			case 35:
				startActivity(new Intent(this, PhotoViewerActivity.class));
				break;
		}
	}

	private void changeBrightNess() {
		int width = UIUtils.dip2px(this, getResources().getDisplayMetrics().widthPixels-150);
		int height = UIUtils.dip2px(this, 10);
		int brightness = BrightNessHelper.getScreenBrightness(this);

		SeekBar seekBar = new SeekBar(this);
		seekBar.setMax(255);
		seekBar.setProgress(brightness);
		seekBar.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				float brightnessValue = BrightNessHelper.setBrightness(MainActivity.this, progress);
				Log.d("onProgressChanged:" + brightnessValue);
			}
		});
		
		new AlertDialog.Builder(this)
		.setTitle("更改屏幕亮度")
		.setView(seekBar)
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				BrightNessHelper.startAutoBrightness(MainActivity.this);
			}
		})
		.create()
		.show();
	}
	
	private void changeTheme(String packageName) {
		try {//忽略权限警告
			Context context = createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
			int resId = context.getResources().getIdentifier("main_background", "color", packageName);
			if (resId > 0) {
				findViewById(R.id.main).setBackgroundColor(context.getResources().getColor(resId));
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void shareWeibo() {
		new AlertDialog.Builder(this)
		.setTitle("分享内容")
		.setSingleChoiceItems(R.array.share_message, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				String[] items = {"sina","tencent","qq","douban","renren"};
				Intent intent = new Intent(MainActivity.this, WeiboViewActivity.class);
				intent.putExtra("type", items[which]);
				startActivityForResult(intent, ACTIVITY_REQUEST_CODE_SHARE_WEIBO);
			}
		})
		.create()
		.show();
	}
	
	private void shareAppMessage() {
		Intent intent = getIntent();
		if (intent.getAction()!=null && Intent.ACTION_SEND.equals(intent.getAction())) {
			if(intent.hasExtra(Intent.EXTRA_TEXT)){
				String text = intent.getStringExtra(Intent.EXTRA_TEXT);
				ToastUtil.showMessage(getBaseContext(), !StrOperate.hasValue(text)?text:"may not share text message");
    		}
			if(intent.hasExtra(Intent.EXTRA_STREAM)){
				Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				if (uri != null) {
					String filePath = "may not share stream message";
					String scheme = uri.getScheme();
					if (scheme.equals("content")) {
						filePath = UIUtils.getRealPathFromURI(this, uri);
					} else if (scheme.equals("file")) {
						filePath = uri.getPath();
					}
					ToastUtil.showMessage(getBaseContext(), filePath);
				}
			}
		} 
	}
	
	private void judgeSpeekVoice() {
        try {
			//语音识别模式,ACTION_RECOGNIZE_SPEECH 捕捉文字序列,ACTION_WEB_SEARCH 网络搜索
			Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "语音搜索");
			startActivityForResult(speechIntent, ACTIVITY_REQUEST_CODE_SPEEK_VOICE);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(this, "未发现语音识别设备", Toast.LENGTH_LONG).show();
		}
	}
	
	private void showNotification() {
		Intent intent = new Intent(this, WordsActivity.class);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0x20000, new NotificationCompat.Builder(this)
			 	.setSmallIcon(android.R.drawable.sym_def_app_icon)
		        .setTicker("Ticker: test")
		        .setContentTitle("Title: test")
		        .setContentInfo("Content Info")
		        .setContentText("Content Text")
		        .setAutoCancel(true)
		        .setWhen(System.currentTimeMillis())
		        .setDefaults(Notification.DEFAULT_VIBRATE)
		        .setVibrate(new long[]{0, 100, 200, 300})
		        .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
		        .getNotification()
		);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		((AndroidApplication)getApplication()).removeLocationUpdates();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
	}
	
	@Override
	public void onTaskStart(String taskName) {
		// TODO Auto-generated method stub
		showDialog(ID_DIALOG_WAITING);
	}

	@Override
	public void onTaskFinished(String taskName, Object result) {
		// TODO Auto-generated method stub
		dismissDialog(ID_DIALOG_WAITING);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void openOptionsMenu() {
		mOptionsMenu = new ExtendOptionsMenu(this);
		mOptionsMenu.show(getWindow().getDecorView());
	}
	
	/*@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		mOptionsMenu = new ExtendOptionsMenu(this);
		mOptionsMenu.show(getWindow().getDecorView());
		return false;
	}*/
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case ID_DIALOG_DATE:
			DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					showDialog(ID_DIALOG_TIME);
				}
			}, mYear, mMonth, mDay);
			dpd.setTitle("");
			return dpd;
		case ID_DIALOG_TIME:
			TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					mHour = hourOfDay;
					mMinute = minute;

					Date d = new Date(mYear - 1900, mMonth, mDay, mHour, mMinute);
					long timestamp = d.getTime();

					try {
						int flags = 0;
						flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
						flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
						flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;
						flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
						String formattedDate = DateUtils.formatDateTime(
								MainActivity.this, timestamp, flags);
						Toast.makeText(MainActivity.this, formattedDate, Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, mHour, mMinute, false);
			tpd.setTitle("");
			return tpd;
		case ID_DIALOG_LOADING:
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage("Loading");
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(true);
			return loadingDialog;
		case ID_DIALOG_WAITING:
			waitingDialog = new ProgressDialog(this);
			waitingDialog.setMessage("Downloading file. Please wait...");
			waitingDialog.setMax(100);
			waitingDialog.setIndeterminate(false);
			waitingDialog.setCancelable(true);
			waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			waitingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					downloadFileTask.cancelTask(true);
					dialog.dismiss();
				}
			});
			return waitingDialog;
		}
		return super.onCreateDialog(id, args);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK) {
			if (mOptionsMenu != null) {
				mOptionsMenu.dismiss();
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
			ActivityStack.getInstance().showDialog(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
			case ACTIVITY_REQUEST_CODE_SPEEK_VOICE:// 语音输入
				if(data!=null && resultCode==RESULT_OK) {
					List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					StringBuilder sb = new StringBuilder();
					for (String entry : list) {
						sb.append(entry+",");
					}
					Toast.makeText(this, sb.deleteCharAt(sb.length()-1), Toast.LENGTH_LONG).show();
				}
				break;
			case ACTIVITY_REQUEST_CODE_SHARE_WEIBO:
				if(resultCode==RESULT_OK && data != null) {
					String weibo = data.getStringExtra("weibo");
					String accessToken = data.getStringExtra("accessToken");
					String expire = data.getStringExtra("expire");
					
					if(weibo.equals("sina")) {
						Toast.makeText(this, "accessToken="+accessToken+",expire="+expire, Toast.LENGTH_LONG).show();
					} else if(weibo.equals("qq")) {
						Toast.makeText(this, "accessToken="+accessToken+",expire="+expire, Toast.LENGTH_LONG).show();
					}
				}
				break;
			case IntentIntegrator.REQUEST_CODE:
				if (data!=null) {
					IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
					Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				mMediaPicker.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public void onSelectedMediaChanged(String mediaUri) {
		// TODO Auto-generated method stub
		if (mediaUri!=null) {
			try {
				Toast.makeText(this, mediaUri, Toast.LENGTH_SHORT).show();
				
				if (mediaUri.endsWith(".mp4")) {
					Intent intent = new Intent(this, VideoActivity.class);
					intent.putExtra("mediaUri", Uri.parse(mediaUri));
					startActivity(intent);
				} else {
					Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(mediaUri));
					System.out.println("onSelectedMediaChanged >> " + bitmap);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		//TTS Engine初始化完成
		if (status == TextToSpeech.SUCCESS) {
			//设置发音语言
			int result = tts.setLanguage(Locale.US);
			// tts.setPitch(5); // set pitch level
			// tts.setSpeechRate(2); // set speech speed rate
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Toast.makeText(this, "Language is not supported", Toast.LENGTH_LONG).show();
			} else {
				tts.speak("hello word", TextToSpeech.QUEUE_FLUSH, null);
				tts.setOnUtteranceCompletedListener(null); //语音朗读完毕回调
			}
		} else {
			Toast.makeText(this, "Language is not supported", Toast.LENGTH_LONG).show();
		}
	}

	
	private void GetAddressFromLocation(Location location) {
		if (location == null) return;
		String url = "http://maps.google.com/maps/api/geocode/json";
		String queryString ="latlng="+location.getLatitude()+","+location.getLongitude()+"&sensor=true&language=zh_CN";
		RequestExecutor.doAsync(new Request(url, queryString){
			@Override
			protected void onSuccess(String content) {
				// TODO Auto-generated method stub
				try {
					JSONObject json = new JSONObject(content);
					JSONArray mJsonArray = json.getJSONArray("results");
					if (mJsonArray.length() > 0) {
						JSONObject mJsonObject = mJsonArray.optJSONObject(0);
						if (mJsonObject!=JSONObject.NULL) {
							String address = mJsonObject.optString("formatted_address", "Unknown Location");
							ToastUtil.showMessage(MainActivity.this, address);
						}
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}

			@Override
			protected void onFailure(String message) {
				// TODO Auto-generated method stub
				ToastUtil.showMessage(MainActivity.this, message);
			}
		});
	}
	
	
	class DownloadFileFromURL extends GenericTask<Result> {
		private File file;
		
		protected DownloadFileFromURL(String taskName, TaskListener taskListener) {
			super(taskName, taskListener);
			// TODO Auto-generated constructor stub
			this.file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".apk");
		}

		@Override
		protected Result doInBackground(String... params) {
			// TODO Auto-generated method stub
			BetterHttpApiV1 httpRequest = BetterHttpApiV1.getInstance();
			httpRequest.getRequest().downloadFileToDisk(params[0], file.getAbsolutePath(), new Progress() {
				@Override
				public void transferred(long num, long totalSize) {
					// TODO Auto-generated method stub
					publishProgress((int)((num * 100) / totalSize));
				}
			});
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			waitingDialog.setProgress((int)values[0]);
		}

		@Override
		protected void onPostExecute(Result result) {
			// TODO Auto-generated method stub
			waitingDialog.dismiss();
			startActivity(IntentSupport.newInstallApkIntent(file));
		}
		
	}

}
