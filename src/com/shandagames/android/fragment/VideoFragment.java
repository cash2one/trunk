package com.shandagames.android.fragment;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import com.shandagames.android.app.BaseFragment;

public class VideoFragment extends BaseFragment {

	private Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return new VideoView(activity);
	}


	private class VideoView extends SurfaceView implements SurfaceHolder.Callback {

		private SurfaceHolder surfaceHolder;
		private MediaPlayer mediaPlayer;
		
		public VideoView(Context context) {
			super(context);
			
			surfaceHolder = getHolder();
			surfaceHolder.addCallback(this);
			
			mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setLooping(true);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDataSource("/mnt/sdcard/Video/MV-Genie.mp4");
			} catch (IOException e) {
				release();
				e.printStackTrace();
			} 
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mediaPlayer.setDisplay(holder);
			try {
				mediaPlayer.prepare();
				mediaPlayer.start();
			} catch (IOException e) {
				release();
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			release();
		}
		
		private void release() {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}
	
}
