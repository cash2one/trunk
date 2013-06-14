package com.shandagames.android;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.widget.arcmenu.MenuRightAnimations;
import com.shandagames.android.widget.arcmenu.SatelliteMenu;
import com.shandagames.android.widget.arcmenu.SatelliteMenuItem;

public class PathActivity extends BaseActivity {

	private boolean areButtonsShowing;
	
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;

	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(android.R.style.Theme_Holo_Light);
		setContentView(R.layout.imitate_path_layout);

		MenuRightAnimations.initOffset(this);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
		composerButtonsShowHideButton.startAnimation(MenuRightAnimations.getRotateAnimation(0, 360, 200));
		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { onClickView(v, false); }
		});
		
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			composerButtonsWrapper.getChildAt(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Toast.makeText(PathActivity.this,"argo=" + arg0.getId() + " click", Toast.LENGTH_SHORT).show();
				}
			});
		}

		
		//方法二实现
		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
		
		float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());
		menu.setSatelliteDistance((int) distance);
//		menu.setTotalSpacingDegree(180);
		menu.setCloseItemsOnClick(true);
		
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.composer_camera));
        items.add(new SatelliteMenuItem(2, R.drawable.composer_music));
        items.add(new SatelliteMenuItem(3, R.drawable.composer_place));
        items.add(new SatelliteMenuItem(4, R.drawable.composer_sleep));
        items.add(new SatelliteMenuItem(5, R.drawable.composer_thought));
        items.add(new SatelliteMenuItem(6, R.drawable.composer_with));
        menu.addItems(items);
        
        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
			@Override
			public void eventOccured(int id) {
				// TODO Auto-generated method stub
				Log.i("sat", "Clicked on " + id);
			}
		});
	}

	public void onClickView(View v, boolean isOnlyClose) {
		if (isOnlyClose) {
			if (areButtonsShowing) {
				MenuRightAnimations.startAnimationsOut(composerButtonsWrapper, 300);
				composerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations.getRotateAnimation(-315, 0, 300));
				areButtonsShowing = !areButtonsShowing;
			}
		} else {
			if (!areButtonsShowing) {
				MenuRightAnimations.startAnimationsIn(composerButtonsWrapper, 300);
				composerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations.getRotateAnimation(0, -315, 300));
			} else {
				MenuRightAnimations.startAnimationsOut(composerButtonsWrapper, 300);
				composerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations.getRotateAnimation(-315, 0, 300));
			}
			areButtonsShowing = !areButtonsShowing;
		}

	}
}
