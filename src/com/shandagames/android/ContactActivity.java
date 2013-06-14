package com.shandagames.android;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.widget.LetterView;
import com.shandagames.android.widget.LetterView.OnTouchChangedListener;
import com.shandagames.android.R;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class ContactActivity extends BaseActivity implements TextWatcher, OnTouchChangedListener {

	private EditText editSearch;
	private LetterView letterView;
	private TextView txtView;
	
	private WindowManager wm;
	
	@Override
	public void _onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.contact_layout);
		ensureUi();
	}

	private void ensureUi() {
		editSearch = (EditText) findViewById(R.id.searchEdit);
        editSearch.addTextChangedListener(this);
        
        letterView = (LetterView) findViewById(R.id.lv_slidebar);
        letterView.setOnTouchChangedListener(this);
        
        initPopupWindow();
	}
	
	private void initPopupWindow() {
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		txtView = new TextView(this);
		txtView.setTextSize(40);
		txtView.setTypeface(Typeface.DEFAULT_BOLD);
		txtView.setVisibility(View.INVISIBLE);
		LayoutParams wl = new LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.TYPE_APPLICATION,
				LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		wm.addView(txtView, wl);
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDestroy() {
		wm.removeView(txtView);
		super.onDestroy();
	}

	@Override
	public void onTouchLetterChanged(String str) {
		// TODO Auto-generated method stub
		txtView.setVisibility(View.VISIBLE);
		txtView.setText(str);
	}

	@Override
	public void onTouchLetterCancel() {
		// TODO Auto-generated method stub
		txtView.setVisibility(View.INVISIBLE);
	}
	
}
