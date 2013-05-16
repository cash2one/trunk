package com.shandagames.android.common;

import com.shandagames.android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * @file QPopupWindow.java
 * @create 2013-2-1 下午03:36:13
 * @author lilong
 * @description TODO
 */
public final class QPopupWindow {

	private Context context;
	private PopupWindow popupWindow;
	private LinearLayout contentView;
	private OnClickListener mOnClickListener;

	public QPopupWindow(Context context) {
		this.context = context;
	}
	
	/** 初始化显示PopupWindow显示内容 */
	public void setItems(String ...args) {
		contentView = new LinearLayout(context);
		contentView.setOrientation(LinearLayout.VERTICAL);
		contentView.setBackgroundResource(R.drawable.popup_dialog_background);
		int leftMargin = dip2px(context, 20);
		contentView.setPadding(leftMargin, 0, leftMargin, 0);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		for (int i=0;i<args.length;i++) {
			Button btnAlert = new Button(context);
			btnAlert.setText(args[i]);
			btnAlert.setTypeface(Typeface.DEFAULT_BOLD);
			btnAlert.setOnClickListener(mOnClickListener);
			btnAlert.setBackgroundResource(R.drawable.popup_dialog_button);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, 
					LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				lp.topMargin = dip2px(context, 20);
			} else {
				lp.topMargin = dip2px(context, 5);
			}
			contentView.addView(btnAlert, lp);
		}
		
		Button btnCancel = new Button(context);
		btnCancel.setText(android.R.string.cancel);
		btnCancel.setTextColor(Color.WHITE);
		btnCancel.setTypeface(Typeface.DEFAULT_BOLD);
		btnCancel.setBackgroundResource(R.drawable.popup_dialog_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT);
		int topMargin = dip2px(context, 15);
		lp.setMargins(0, topMargin, 0, topMargin);
		contentView.addView(btnCancel, lp);
		
		makePopupWindow();
	}
	
	/** 初始化PopupWindow显示参数  */
	private void makePopupWindow() {
		if (popupWindow == null) {
			popupWindow = new PopupWindow(contentView, 
					LayoutParams.FILL_PARENT, 
					LayoutParams.WRAP_CONTENT, true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
			popupWindow.setAnimationStyle(R.style.Theme_PopDownMenu);
		}
	}
	
	/** 显示PopupWindow */
	public void show(View v) {
		if (popupWindow != null && !popupWindow.isShowing()) {
			popupWindow.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}
	
	/** 关闭PopupWindow显示 */
	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}
	
	public void setOnClickListener(OnClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}
	
	private int dip2px(Context context, float dip) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int)(dip * density + 0.5);
	}
}
