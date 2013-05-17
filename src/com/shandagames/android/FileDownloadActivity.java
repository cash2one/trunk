package com.shandagames.android;

import com.shandagames.android.download.DownloadCastReceiver;
import com.shandagames.android.download.DownloadService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FileDownloadActivity extends Activity {

	/** Called when the activity is first created. */
	private static final String TAG = "FileDownloadActivity";

	public static final String[] APK_URLS = {
		"http://img.yingyonghui.com/apk/16457/com.rovio.angrybirdsspace.ads.1332528395706.apk",
		"http://img.yingyonghui.com/apk/15951/com.galapagossoft.trialx2_winter.1328012793227.apk",
		"http://cdn1.down.apk.gfan.com/asdf/Pfiles/2012/3/26/181157_0502c0c3-f9d1-460b-ba1d-a3bad959b1fa.apk",
		"http://static.nduoa.com/apk/258/258681/com.gameloft.android.GAND.GloftAsp6.asphalt6.apk",
		"http://cdn1.down.apk.gfan.com/asdf/Pfiles/2011/12/5/100522_b73bb8d2-2c92-4399-89c7-07a9238392be.apk",
		"http://file.m.163.com/app/free/201106/16/com.gameloft.android.TBFV.GloftGTHP.ML.apk"};
	
	private DownloadCastReceiver mReceiver;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(this, DownloadService.class);
		intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.DOWNLOAD_TYPE_START);
		startService(intent);
		
		mReceiver = new DownloadCastReceiver();
		mReceiver.bind(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mReceiver.unbind(this);
	}
}
