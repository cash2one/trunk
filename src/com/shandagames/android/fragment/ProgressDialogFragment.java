package com.shandagames.android.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.shandagames.android.app.BaseDialogFragment;

public class ProgressDialogFragment extends BaseDialogFragment {
	
	private ProgressDialog progressDialog;

	private OnDownloadProgressListener mProgressListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("Downloading file. Please wait...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setOnDismissListener(this);
		return progressDialog;
	}

	@Override
	public ProgressDialog getDialog() {
		return progressDialog;
	}

	public void setProgressListener(OnDownloadProgressListener onProgressListener) {
		this.mProgressListener = onProgressListener;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		dismiss();
		if (mProgressListener != null) {
			mProgressListener.onCancelTask();
		}
	}
	
	public static interface OnDownloadProgressListener {
		
		void onCancelTask();
	}
}
