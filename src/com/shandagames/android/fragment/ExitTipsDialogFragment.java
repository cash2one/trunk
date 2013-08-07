package com.shandagames.android.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.shandagames.android.app.BaseAlertDialog;
import com.shandagames.android.app.BaseDialogFragment;
import com.shandagames.android.util.UIUtils;

public class ExitTipsDialogFragment extends BaseDialogFragment implements DialogInterface.OnClickListener {

	private BaseAlertDialog.Builder builder;
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final Activity activity = getActivity();
		builder = new BaseAlertDialog.Builder(activity);
		builder.setTitle("温馨提示");
		builder.setMinWidth((int)UIUtils.getScreenWidth(activity) - 50);
		builder.setMessage("确认退出该应用吗?");
		builder.setPositiveButton(android.R.string.ok, this);
		builder.setNegativeButton(android.R.string.cancel, null);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which ==DialogInterface.BUTTON_POSITIVE) {
			getActivity().finish();
		}
	}
}
