package com.shandagames.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import com.shandagames.android.R;
import com.shandagames.android.app.BaseDialogFragment;

public class UnsaveDialogFragment extends BaseDialogFragment {
	
	public static final String INTENT_KEY_IS_NAVIGATE_UP = "is_navigate_up";
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final Activity activity = getActivity();
		View view = View.inflate(activity, R.layout.grid_layout, null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setView(view);
		return builder.create();
	}
}
