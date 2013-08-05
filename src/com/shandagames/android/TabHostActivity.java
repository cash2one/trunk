package com.shandagames.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.shandagames.android.app.LocalActivityManagerActivity;
import com.shandagames.android.R;
import com.shandagames.android.util.UIUtils;

/**
 * @file MainTabActivity.java
 * @create 2012-8-21 下午5:25:51
 * @author lilong
 * @description TODO
 */
public class TabHostActivity extends LocalActivityManagerActivity implements OnCheckedChangeListener {

	private TabHost tabHost;
	private RadioGroup radioGroup;
	private ImageView moveImage;
	
	private int startLeft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		moveImage = (ImageView) findViewById(R.id.iv_move);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
		
        tabHost.setup(getLocalActivityManager());
		
		TabSpec recentContactSpec=tabHost.newTabSpec("RecentContact");
	    recentContactSpec.setIndicator("News");
        Intent recentContactIntent = new Intent(this, PinnedHeaderActivity.class);
        recentContactSpec.setContent(recentContactIntent);
        
        TabSpec contactBookSpec=tabHost.newTabSpec("ContactBook");
	    contactBookSpec.setIndicator("Topic");
	    Intent contactBookIntent = new Intent(this,ContactActivity.class);
	    contactBookSpec.setContent(contactBookIntent);
		
        TabSpec smsMessageSpec = tabHost.newTabSpec("SmsMessage");
        smsMessageSpec.setIndicator("Picture");
        Intent smsMessageIntent = new Intent(this, PinnedExpandableActivity.class);
        smsMessageSpec.setContent(smsMessageIntent);
        
        TabSpec settingSpec = tabHost.newTabSpec("Setting");
        settingSpec.setIndicator("Follow");
        Intent settingIntent = new Intent(this, BaiduMapActivity.class);
        settingSpec.setContent(settingIntent);
        
        TabSpec voteSpec = tabHost.newTabSpec("Vote");
        voteSpec.setIndicator("Vote");
        Intent voteIntent = new Intent(this, PlacesMapActivity.class);
        voteSpec.setContent(voteIntent);
        
        tabHost.addTab(recentContactSpec);
        tabHost.addTab(contactBookSpec);
        tabHost.addTab(smsMessageSpec);
        tabHost.addTab(settingSpec);
        tabHost.addTab(voteSpec);

        tabHost.setCurrentTab(0);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
			case R.id.radio_news:
				tabHost.setCurrentTabByTag("RecentContact");
				UIUtils.moveFrontAnimation(moveImage, startLeft, 0, 0, 0);
				startLeft = 0;
				break;
			case R.id.radio_topic:
				tabHost.setCurrentTabByTag("ContactBook");
				UIUtils.moveFrontAnimation(moveImage, startLeft, moveImage.getWidth(), 0, 0);
				startLeft = moveImage.getWidth();
				break;
			case R.id.radio_pic:
				tabHost.setCurrentTabByTag("SmsMessage");
				UIUtils.moveFrontAnimation(moveImage, startLeft, moveImage.getWidth() * 2, 0, 0);
				startLeft = moveImage.getWidth() * 2;
				break;
			case R.id.radio_follow:
				tabHost.setCurrentTabByTag("Setting");
				UIUtils.moveFrontAnimation(moveImage, startLeft, moveImage.getWidth() * 3, 0, 0);
				startLeft = moveImage.getWidth() * 3;
				break;
			case R.id.radio_vote:
				tabHost.setCurrentTabByTag("Vote");
				UIUtils.moveFrontAnimation(moveImage, startLeft, moveImage.getWidth() * 4, 0, 0);
				startLeft = moveImage.getWidth() * 4;
				break;
			default:
				break;
		}
	}
	
}
