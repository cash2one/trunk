package com.shandagames.android.util;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

public class AlertUtil {

	public static AlertDialog showAlert(Context context, int msgId, int titleId) {
		return showAlert(context, context.getString(msgId), context.getString(titleId));
	}
	
	public static AlertDialog showAlert(Context context, String msg, String title) {
		final Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	
	public static AlertDialog showAlert(Context context, CharSequence title, CharSequence message,
            CharSequence positiveButtontxt, DialogInterface.OnClickListener positiveListener,
            CharSequence negativeButtontxt, DialogInterface.OnClickListener negativeListener) {
		final Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveButtontxt, positiveListener);
		builder.setNegativeButton(negativeButtontxt, negativeListener);
		final AlertDialog alert = builder.create();
		alert.show();
		return alert;
	}
	
	
	
	/**
	 * Creates a new ProgressDialog
	 */
	public static ProgressDialog showProgressDialog(final Activity activity, String title, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);
        progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                activity.onKeyDown(keyCode, event);
                return false;
            }
        });
        return progressDialog;
    }
	
    /**
     * Creates a AlertDialog that shows a list of elements. The listener's onClick method gets
     * called when the user taps a list item.
     */
    public static <T> Dialog showListDialog(final Activity context, String dialogTitle,
            final List<T> elements, final DialogClickListener<T> listener,
            final boolean closeOnSelect) {
        return showListDialog(context, dialogTitle, elements, listener, closeOnSelect, 0);
    }

    public static <T> Dialog showListDialog(final Activity context, String dialogTitle,
            final List<T> elements, final DialogClickListener<T> listener,
            final boolean closeOnSelect, int selectedItem) {
        final int entriesSize = elements.size();
        String[] entries = new String[entriesSize];
        for (int i = 0; i < entriesSize; i++) {
            entries[i] = elements.get(i).toString();
        }

        Builder builder = new AlertDialog.Builder(context);
        if (dialogTitle != null) {
            builder.setTitle(dialogTitle);
        }
        builder.setSingleChoiceItems(entries, selectedItem, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (closeOnSelect)
                    dialog.dismiss();
                listener.onClick(which, elements.get(which));
            }
        });

        return builder.create();
    }
    
    
    public static interface DialogClickListener<T> {

        void onClick(int index, T element);

    }
}
