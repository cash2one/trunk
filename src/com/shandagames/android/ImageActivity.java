package com.shandagames.android;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.shandagames.android.base.BaseActivity;
import com.shandagames.android.view.SampleSurfaceView;
import com.shandagames.android.view.WPEditText;
import com.shandagames.android.view.WPEditText.EditTextImeBackListener;
import com.shandagames.android.view.WPEditText.OnSelectionChangedListener;
import com.shandagames.android.view.WPEditText.OnTextContextMenuItemListener;

public class ImageActivity extends BaseActivity implements
		EditTextImeBackListener, OnSelectionChangedListener,
		OnTextContextMenuItemListener {

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// setContentView(new SampleSurfaceView(this));
		LinearLayout mPanel = new LinearLayout(this);
		mPanel.setOrientation(LinearLayout.VERTICAL);
		setContentView(mPanel);

		WPEditText mContentEditText = new WPEditText(this);
		mPanel.addView(mContentEditText);

		mContentEditText.setMinLines(4);
		mContentEditText.setInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
		mContentEditText.setOnEditTextImeBackListener(this);
		mContentEditText.setOnSelectionChangedListener(this);
		mContentEditText.setOnTextContextMenuItemListener(this);
	}

	@Override
	public void onImeBack(WPEditText ctrl, String text) {
		// TODO Auto-generated method stub
		System.err.println("onImeBack..." + text);
	}

	@Override
	public void onSelectionChanged(WPEditText ctrl, int selStart, int selEnd) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMenuItemChanged(WPEditText ctrl, int id) {
		// TODO Auto-generated method stub
		String message = "";
		switch (id) {
		case android.R.id.copy:
			message = "拷贝";
			break;
		case android.R.id.cut:
			message = "剪切";
			break;
		case android.R.id.paste:
			message = "粘贴";
			break;
		}
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}


}
