package com.shandagames.android;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.shandagames.android.download.DownloadManager;
import com.shandagames.android.download.DownloadService;
import com.shandagames.android.download.DownloadTask;
import com.shandagames.android.download.DownloadTaskListener;
import com.shandagames.android.log.Log;
import com.shandagames.android.support.MemoryStatus;

public class FileDownloadActivity extends ListActivity {

	/** Called when the activity is first created. */
	private static final String TAG = "FileDownloadActivity";

	private static final int MSG_START_DOWNLOAD = 1;
	private static final int MSG_STOP_DOWNLOAD = 2;
	private static final int MSG_PAUSE_DOWNLOAD = 3;
	private static final int MSG_CONTINUE_DOWNLOAD = 4;
	private static final int MSG_INSTALL_APK = 5;
	private static final int MSG_CLOSE_ALL_DOWNLOAD_TASK = 6;

	public static final String[] APK_URLS = {
		"http://img.yingyonghui.com/apk/16457/com.rovio.angrybirdsspace.ads.1332528395706.apk",
		"http://img.yingyonghui.com/apk/15951/com.galapagossoft.trialx2_winter.1328012793227.apk",
		"http://cdn1.down.apk.gfan.com/asdf/Pfiles/2012/3/26/181157_0502c0c3-f9d1-460b-ba1d-a3bad959b1fa.apk",
		"http://static.nduoa.com/apk/258/258681/com.gameloft.android.GAND.GloftAsp6.asphalt6.apk",
		"http://cdn1.down.apk.gfan.com/asdf/Pfiles/2011/12/5/100522_b73bb8d2-2c92-4399-89c7-07a9238392be.apk",
		"http://file.m.163.com/app/free/201106/16/com.gameloft.android.TBFV.GloftGTHP.ML.apk"};
	
	private DownloadManager mgr;
	private DownLoadListAdapter adapter;
	private DownloadService downloadService;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_DOWNLOAD:
				mgr.startManage();
				//startDownload(msg.arg1);
				break;
			case MSG_STOP_DOWNLOAD:
				mgr.close();
				//stopDownload(msg.arg1);
				break;
			case MSG_PAUSE_DOWNLOAD:
				mgr.pauseTask(APK_URLS[msg.arg1]);
				//pauseDownload(msg.arg1);
				break;
			case MSG_CONTINUE_DOWNLOAD:
				mgr.continueTask(APK_URLS[msg.arg1]);
				//continueDownload(msg.arg1);
				break;
			case MSG_CLOSE_ALL_DOWNLOAD_TASK:
				break;
			}
		}
	};

	@Override
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new DownLoadListAdapter(this);
		setListAdapter(adapter);

		mgr = new DownloadManager(this);
		
		/*Intent intent = new Intent(this, DownloadService.class);
		startService(intent);*/
		
		/*mgr = new DownloadManager(this, "temp", downloadTaskListener);
		for (String str : APK_URLS) {
			mgr.addTask(str);
		}*/
	}

	private DownloadTaskListener downloadTaskListener = new DownloadTaskListener() {

		@Override
		public void updateProcess(DownloadTask task) {
			for (int i = 0; i < APK_URLS.length; i++) {
				if (APK_URLS[i].equalsIgnoreCase(task.getUrl())) {
					FileDownloadActivity.this.updateDownload(i, task);
				}
			}
		}

		@Override
		public void finishDownload(DownloadTask mgr) {
			for (int i = 0; i < APK_URLS.length; i++) {
				if (APK_URLS[i].equalsIgnoreCase(mgr.getUrl())) {
					Log.i("TEST", mgr.getUrl()+" " + mgr.getTotalSize() + " " + mgr.getTotalTime() + " " + mgr.getDownloadSpeed());
				}
			}
		}

		@Override
		public void preDownload() {
			Log.i(TAG, "preDownload");
		}

		@Override
		public void errorDownload(int error) {
			Log.i(TAG, "errorDownload");
		}
	};
	
	public void updateDownload(int viewPos, DownloadTask mgr) {
		View convertView = adapter.getView(viewPos, getListView(), null);
		ProgressBar pb = (ProgressBar) convertView
				.findViewById(R.id.progressBar);

		pb.setProgress((int) mgr.getDownloadPercent());

		TextView view = (TextView) convertView.findViewById(R.id.progress_text_view);
		view.setText("" + (int) mgr.getDownloadPercent() + "%" + " "
				+ mgr.getDownloadSpeed() + "kbps" + " "
				+ MemoryStatus.formatSize(mgr.getDownloadSize()) + "/"
				+ MemoryStatus.formatSize(mgr.getTotalSize()));
	}

	private class DownLoadListAdapter extends BaseAdapter implements OnClickListener {
		private Context context;
		public List<View> viewList = new ArrayList<View>();

		public DownLoadListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return APK_URLS.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position < viewList.size()) {
				return viewList.get(position);
			}

			if (convertView == null) {
				convertView = View.inflate(context, R.layout.download_list_item, null);
				viewList.add(convertView);

				convertView.findViewById(R.id.btn_start).setOnClickListener(this);
				convertView.findViewById(R.id.btn_start).setTag(position);
				
				convertView.findViewById(R.id.btn_pause).setOnClickListener(this);
				convertView.findViewById(R.id.btn_pause).setTag(position);
				
				convertView.findViewById(R.id.btn_stop).setOnClickListener(this);
				convertView.findViewById(R.id.btn_stop).setTag(position);
				
				convertView.findViewById(R.id.btn_continue).setOnClickListener(this);
				convertView.findViewById(R.id.btn_continue).setTag(position);
			}

			return convertView;
		}

		@Override
		public void onClick(View v) {
			Message message;
			int viewPos = Integer.valueOf(v.getTag().toString());
			switch (v.getId()) {
				case R.id.btn_start:
					message = new Message();
					message.what = MSG_START_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);
					break;
				case R.id.btn_pause:
					message = new Message();
					message.what = MSG_PAUSE_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);
					break;
				case R.id.btn_stop:
					message = new Message();
					message.what = MSG_STOP_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);
					break;
				case R.id.btn_continue:
					message = new Message();
					message.what = MSG_CONTINUE_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);
					break;
			}
		}

	}
}
