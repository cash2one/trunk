/**
 * 
 */
package com.shandagames.android;

import com.shandagames.android.R;
import com.shandagames.android.widget.CircleProgress;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @file DirectionalActivity.java
 * @create 2012-9-14 下午6:38:13
 * @author lilong
 * @description TODO
 */
public class CircleProgressActivity extends Activity implements
		OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnAddMain; // 增加进度值
	private Button mBtnAddSub; // 减少进度值
	private ImageButton mImageBtn; // 清除进度值

	private CircleProgress mCircleProgressBar1;
	private CircleProgress mCircleProgressBar2;
	private CircleProgress mCircleProgressBar3;

	private int progress = 0;
	private int subProgress = 0;

	private Button mBtnStartButton; // 开启动画
	private Button mBtnSTopButton; // 结束动画
	private CircleProgress mCircleProgressBar4;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_progress_layout);

		initView();
	}

	public void initView() {
		mBtnAddMain = (Button) findViewById(R.id.buttonAddMainPro);
		mBtnAddSub = (Button) findViewById(R.id.buttonAddSubPro);
		mImageBtn = (ImageButton) findViewById(R.id.buttonImage);

		mBtnStartButton = (Button) findViewById(R.id.buttonStart);
		mBtnSTopButton = (Button) findViewById(R.id.buttonStop);

		mBtnAddMain.setOnClickListener(this);
		mBtnAddSub.setOnClickListener(this);
		mImageBtn.setOnClickListener(this);

		mBtnStartButton.setOnClickListener(this);
		mBtnSTopButton.setOnClickListener(this);

		mCircleProgressBar1 = (CircleProgress) findViewById(R.id.roundBar1);
		mCircleProgressBar2 = (CircleProgress) findViewById(R.id.roundBar2);
		mCircleProgressBar3 = (CircleProgress) findViewById(R.id.roundBar3);
		mCircleProgressBar4 = (CircleProgress) findViewById(R.id.roundBar4);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.buttonAddMainPro:
			add();
			break;
		case R.id.buttonAddSubPro:
			Sub();
			break;
		case R.id.buttonImage:
			clear();
			break;
		case R.id.buttonStart:
			start();
			break;
		case R.id.buttonStop:
			stop();
			break;
		}
	}

	public void add() {
		progress += 5;
		if (progress > 100) {
			progress = 0;
		}

		mCircleProgressBar1.setMainProgress(progress);
		mCircleProgressBar2.setMainProgress(progress);
		mCircleProgressBar3.setMainProgress(progress);

	}

	public void Sub() {
		subProgress += 5;
		if (subProgress > 100) {
			subProgress = 0;
		}

		mCircleProgressBar1.setSubProgress(subProgress);
		mCircleProgressBar2.setSubProgress(subProgress);
		mCircleProgressBar3.setSubProgress(subProgress);

	}

	public void clear() {
		progress = 0;
		subProgress = 0;

		mCircleProgressBar1.setMainProgress(0);
		mCircleProgressBar2.setMainProgress(0);
		mCircleProgressBar3.setMainProgress(0);

		mCircleProgressBar1.setSubProgress(0);
		mCircleProgressBar2.setSubProgress(0);
		mCircleProgressBar3.setSubProgress(0);

	}

	public void start() {
		mCircleProgressBar4.startCartoom(10);
	}

	public void stop() {
		mCircleProgressBar4.stopCartoom();
	}
}
