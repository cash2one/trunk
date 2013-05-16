package com.shandagames.android;

import com.shandagames.android.util.ToastUtil;
/*
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;*/
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

public class VPlayerActivity extends Activity {

	private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Video/MV-Genie.mp4";
	/*
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!LibsChecker.checkVitamioLibs(this, io.vov.vitamio.R.string.vitamio_init_decoders, io.vov.vitamio.R.raw.libarm))
			return;

		setContentView(R.layout.videoview);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		Uri pathUri = this.getIntent().getData();
		//path = "/mnt/sdcard/flash.swf";
		pathUri=Uri.parse("http://download.yinpoo.com/1453/7449/MV-%E6%9B%B2%E5%A9%89%E5%A9%B7-%E6%88%91%E7%9A%84%E6%AD%8C%E5%A3%B0%E9%87%8C-%E9%9F%B3%E6%89%91%E7%BD%91-7449.mp4");
		//pathUri =Uri.parse("http://player.youku.com/player.php/sid/XNDgyNjYwNDQw/v.swf");
		if (pathUri != null)
			mVideoView.setVideoURI(pathUri);
		else
			mVideoView.setVideoPath(path);
		
		mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer media, int arg1, int arg2) {
				ToastUtil.showMessage(VPlayerActivity.this, "未能识别播放格式");
				return false;
			}
		});
		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mVideoView.setSubShown(true);
			}
		});
		mVideoView.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer media, int progress) {
			}
		});
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.requestFocus();
	}

	private int mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null)
			mVideoView.setVideoLayout(mLayout, 0);
		super.onConfigurationChanged(newConfig);
	}*/

}
