package com.shandagames.android.adapter;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.shandagames.android.R;
import com.shandagames.android.base.AndroidApplication;
import com.shandagames.android.base.BaseGroupAdapter;
import com.shandagames.android.cache.core.RemoteResourceManager;
import com.shandagames.android.cache.core.RemoteResourceManager.RemoteResourceManagerObserver;

/**
 * @file MenuAdapter.java
 * @create 2013-3-19 下午01:31:57
 * @author Jacky.Lee
 * @description TODO
 */
public class MenuAdapter extends BaseGroupAdapter<String> {

	private Resources mResource;
	private RemoteResourceManager mRrm;
	private RemoteResourceManagerObserver mResourcesObserver;
	
	public MenuAdapter(Context context) {
		super(context);
		mResource = context.getResources();
//		mRrm = AndroidApplication.getInstance().getRemoteResourceManager();
//		mResourcesObserver = new RemoteResourceManagerObserver(this);
//		mRrm.addObserver(mResourcesObserver);
		// TODO Auto-generated constructor stub
	}

	public void removeObserver() {
		mResourcesObserver.removeCallbacks();
		mRrm.deleteObserver(mResourcesObserver);
	}
	
	@Override
	public View newView(LayoutInflater inflater, View v, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		v = inflater.inflate(R.layout.main_list_item, null);
		viewHolder.txtTextView = (TextView) v.findViewById(R.id.txt_item_menu);
		v.setTag(viewHolder);
		return v;
	}

	@Override
	public void bindView(View v, String item) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		viewHolder.txtTextView.setText(item);
		
		/*Uri photoUri = Uri.parse("https://lh6.googleusercontent.com/-FMHR7Vy3PgI/T3R4rOXlEKI/AAAAAAAAAGs/VeXrDNDBkaw/s1024/sample_image_16.jpg");
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(mRrm.getInputStream(photoUri));
			viewHolder.txtTextView.setBackgroundDrawable(new BitmapDrawable(mResource, bitmap));
		} catch (IOException e) {
			mRrm.request(photoUri);
		}*/
		
	}

	static class ViewHolder {

		TextView txtTextView;
	}

}
