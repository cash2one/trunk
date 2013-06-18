package com.shandagames.android.app;

import com.shandagames.android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author lilong
 * @version 2012-7-17 下午2:08:42
 * 
 */
public class BaseAlertDialog extends Dialog {

	public BaseAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseAlertDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	protected BaseAlertDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder implements View.OnClickListener {

		private Context context;
		private Dialog dialog;
		private ImageView mIcon;
		private TextView txtTitle;
		private TextView txtMessage;
		private Button btnPositive;
		private Button btnNegative;
		private View dialogView;
		private ViewGroup contentView;
		private int minWidth = -1;

		private DialogInterface.OnClickListener mPositiveClickListener,
				mNegativeClickListener;

		public Builder(Context context) {
			this.context = context;
			ensureUi();
		}

		public void ensureUi() {
			dialog = new BaseAlertDialog(context, R.style.Theme_Dialog);
			dialogView = View.inflate(context, R.layout.alert_dialog_layout, null);
			contentView = (FrameLayout) dialogView.findViewById(R.id.dialog_fl_content);
			mIcon = (ImageView) dialogView.findViewById(R.id.dialog_iv_icon);
			txtTitle = (TextView) dialogView.findViewById(R.id.dialog_tv_title);
			txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);
			btnPositive = (Button) dialogView.findViewById(R.id.btn_confirm);
			btnNegative = (Button) dialogView.findViewById(R.id.btn_cancel);

			btnPositive.setOnClickListener(this);
			btnNegative.setOnClickListener(this);
		}

		public Dialog create() {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					minWidth, LayoutParams.MATCH_PARENT);
			dialog.setContentView(dialogView, lp);
			return dialog;
		}

		public ViewGroup getContentView() {
			return this.contentView;
		}
		
		public Builder setMinWidth(int minWidth) {
			this.minWidth = minWidth;
			return this;
		}

		public Builder setIcon(int resId) {
			mIcon.setImageResource(resId);
			return this;
		}

		public Builder setTitle(CharSequence title) {
			txtTitle.setText(title);
			return this;
		}

		public Builder setTitle(int resId) {
			txtTitle.setText(resId);
			return this;
		}

		public Builder setMessage(CharSequence message) {
			txtMessage.setText(message);
			return this;
		}

		public Builder setMessage(int resId) {
			txtMessage.setText(resId);
			return this;
		}

		public Builder setContentView(View view) {
			contentView.addView(view);
			view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			if (minWidth == -1) {
				minWidth = view.getMeasuredWidth();
			}
			return this;
		}

		public Builder setContentView(int resId) {
			View view = View.inflate(context, resId, contentView);
			return setContentView(view);
		}

		public Builder setPositiveButton(CharSequence positiveButtonText,
				DialogInterface.OnClickListener listener) {
			btnPositive.setText(positiveButtonText);
			this.mPositiveClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonResId,
				DialogInterface.OnClickListener listener) {
			btnPositive.setText(positiveButtonResId);
			this.mPositiveClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(CharSequence negativeButtonText,
				DialogInterface.OnClickListener listener) {
			btnNegative.setText(negativeButtonText);
			this.mNegativeClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonResId,
				DialogInterface.OnClickListener listener) {
			btnNegative.setText(negativeButtonResId);
			this.mNegativeClickListener = listener;
			return this;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == btnPositive) {
				if (mPositiveClickListener!=null) {
					mPositiveClickListener.onClick(dialog, 
							DialogInterface.BUTTON_POSITIVE);
				}
			} else if (v == btnNegative) {
				if (mNegativeClickListener!=null) {
					mNegativeClickListener.onClick(dialog, 
							DialogInterface.BUTTON_NEGATIVE);
				}
				dialog.dismiss();
			}
		}

	}
}
