package com.shandagames.android;

import java.util.Random;

import com.shandagames.android.support.IntentSupport;
import com.shandagames.android.widget.KeywordsFlow;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

/**
 * @author  lilong
 * @version 2012-7-27 上午10:26:42
 *
 */
public class CloudTagActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

	static final String[] keywords = {
		"QQ", "Sodino", "APK", "GFW", "铅笔", 
        "短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛",
        "卡西欧 TR-100", "笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人", 
        "内存清理", "地图", "导航", "闹钟", "主题",   
        "通讯录", "播放器", "CSDN leak", "安全", "3D",   
        "美女", "天气", "4743G", "戴尔", "联想",   
        "欧朋", "浏览器", "愤怒的小鸟", "mmShow", "网易公开课",   
        "iciba", "油水关系", "网游App", "互联网", "365日历",   
        "脸部识别", "Chrome", "Safari", "中国版Siri", "A5处理器",   
        "iPhone4S", "摩托 ME525", "魅族 M9", "尼康 S2500" 
	};
	
	private VelocityTracker mTracker;
	
	private KeywordsFlow wordsFlow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		wordsFlow = new KeywordsFlow(this);
		setContentView(wordsFlow);
		
		wordsFlow.setDuration(1000);
		feedKeywordsFlow(wordsFlow);
		wordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		
		wordsFlow.setOnItemClickListener(this);
		wordsFlow.setOnTouchListener(this);
	}

	private void feedKeywordsFlow(KeywordsFlow keywordsFlow) {  
        Random random = new Random();  
        for (int i = 0; i < keywordsFlow.getKeyWordSize(); i++) {  
            int ran = random.nextInt(keywords.length);  
            String tmp = keywords[ran];  
            keywordsFlow.feedKeyword(tmp);  
        }  
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(mTracker==null) {
					//获得VelocityTracker类实例
					mTracker = VelocityTracker.obtain();
				} else {
					mTracker.clear();
				}
				//将事件加入到VelocityTracker类实例中
				mTracker.addMovement(event);
				break;
			case MotionEvent.ACTION_MOVE:
				mTracker.addMovement(event);
				mTracker.computeCurrentVelocity(1000);
				//LogUtil.d("onTouchEvent", mTracker.getXVelocity()+"/"+mTracker.getYVelocity());
				break;
			case MotionEvent.ACTION_UP:
				wordsFlow.rubKeywords();
				feedKeywordsFlow(wordsFlow);
				
				Random random = new Random();
				int[] animations = {KeywordsFlow.ANIMATION_IN, KeywordsFlow.ANIMATION_OUT};
				int ran = random.nextInt(animations.length);
				wordsFlow.go2Show(animations[ran]);
			case MotionEvent.ACTION_CANCEL:
				if(mTracker!=null) {
					mTracker.recycle();
				}
				break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof TextView) {
			String keyword = ((TextView)v).getText().toString();
			Intent intent = IntentSupport.newWebViewIntent("http://www.google.com.hk/#q=" + keyword);
			startActivity(intent);
		}
	}
}
