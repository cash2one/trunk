package com.shandagames.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.shandagames.android.base.BaseFragment;


public class MusicFragment extends BaseFragment {
	public static final String FRAGMENT_MUSIC_EXTRA = "MUSIC_EXTRA";
	private static final String FRAGMENT_MUSIC_URL_DEFAULT = "http://app.9ku.com/hao123/";
	
	public static MusicFragment newInstance(String musicUrl) {
		MusicFragment musicFragment = new MusicFragment();
		Bundle bundle = new Bundle();
		bundle.putString(FRAGMENT_MUSIC_EXTRA, musicUrl);
		musicFragment.setArguments(bundle);
		return musicFragment;
	}
	
	private MusicFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		WebView webView = new WebView(getActivity());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		String musicUrl = FRAGMENT_MUSIC_URL_DEFAULT;
		if (getArguments()!=null && getArguments().containsKey(FRAGMENT_MUSIC_EXTRA)) {
			musicUrl = getArguments().getString(FRAGMENT_MUSIC_EXTRA);
		}
		webView.loadUrl(musicUrl);
		return webView;
	}

}
