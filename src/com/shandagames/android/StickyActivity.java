/**
 * 
 */
package com.shandagames.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.R;

/**
 * @file StickyActivity.java
 * @create 2012-8-30 下午4:38:35
 * @author lilong
 * @description TODO
 */
public class StickyActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sticky);
		findViewById(R.id.mybutton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "hej", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
