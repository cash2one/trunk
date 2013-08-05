package com.shandagames.android;

import com.shandagames.android.app.BaseActivity;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoActivity extends BaseActivity implements OnPreparedListener, OnErrorListener {

	private VideoView videoView;
	private ProgressBar progressBar;
	private MediaController mediaController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		videoView = new VideoView(this);
		setContentView(videoView);
		
		progressBar = (ProgressBar) getLayoutInflater().inflate(R.layout.progress, null); 
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		addContentView(progressBar, lp);
		
		Uri uri = getIntent().getParcelableExtra("mediaUri");
		mediaController = new MediaController(this);
		videoView.setMediaController(mediaController);
		videoView.setOnPreparedListener(this);
		videoView.setOnErrorListener(this);
		videoView.setVideoURI(uri);
		videoView.requestFocus();  
	}
	
	@Override
	public void onDestroy() {
		videoView.stopPlayback();
		videoView = null;
		super.onDestroy();
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.GONE);
		videoView.start();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
