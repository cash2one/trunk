package com.shandagames.android;

import com.shandagames.android.app.BaseActivity;
import com.shandagames.android.util.ImageHelper;
import com.shandagames.android.util.SmileyParser;
import com.shandagames.android.util.ToastUtil;
import com.shandagames.android.util.WeiboParser;
import com.shandagames.android.util.WeiboParser.OnSpannableClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WordsActivity extends BaseActivity implements OnSpannableClickListener, OnClickListener {

	private String url;
	private Bitmap bitmap;
	private ImageView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 10;
		lp.topMargin = 20;
		
		TextView txtView = new TextView(this);
		txtView.setText("http://www.weibo.com/selience");
		Linkify.addLinks(txtView, Linkify.ALL);
		layout.addView(txtView, lp);
		
		txtView = new TextView(this);
		String html = "<a href=\"http://www.weibo.com/selience\">HTML超链接</a><img src=\"icon.png\"/>";
		CharSequence text = Html.fromHtml(html, new ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				Drawable drawable = getResources().getDrawable(R.raw.emo_im_cool);
				drawable.setBounds(0, -5, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()-5);
				return drawable;
			}
		}, null);
		txtView.setText(text);
		txtView.setMovementMethod(LinkMovementMethod.getInstance());
		layout.addView(txtView, lp);
		
		txtView = new TextView(this);
		String str = "评论 [开心]@selience[天使]你好，今天好冷啊![眨眼]#你是我的奇遇#[吐舌]http://www.weibo.com/selience[大笑]";
		CharSequence content = SmileyParser.getInstance().addSmileySpans(str);
		txtView.setText(WeiboParser.formatContent(content, this));
		txtView.setTextColor(Color.WHITE);
		txtView.setMovementMethod(LinkMovementMethod.getInstance());
		layout.addView(txtView, lp);
		
		txtView = new TextView(this);
		text = getResources().getQuantityString(R.plurals.numberOfSongsAvailable, 20, 20);
		txtView.setText(text);
		layout.addView(txtView);
		
		url = "mnt/sdcard/Video/MV-Genie.mp4";
		bitmap = ImageHelper.createVideoThumbnail(url);
		bitmap = ImageHelper.createImageThumbnail(bitmap, 512, 240);
		bitmap = ImageHelper.createReflectedImage(this, bitmap);
		
		videoView = new ImageView(this);
		videoView.setImageBitmap(bitmap);
		videoView.setOnClickListener(this);
		layout.addView(videoView, lp);
		setContentView(layout);
		
		//url = Environment.getExternalStorageDirectory() + "/Music/turanxiangqini.mp3";
		//System.out.println("MusicAlbum--title:" + ImageHelper.getAlbumFromCode(url, MediaMetadataRetriever.METADATA_KEY_TITLE));
	}

	@Override
	public void onLinkedUser(String content) {
		// TODO Auto-generated method stub
		ToastUtil.showMessage(this, content);
	}

	@Override
	public void onLinkedTheme(String content) {
		// TODO Auto-generated method stub
		ToastUtil.showMessage(this, content);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v==	videoView) {
			Intent intent = new Intent(this, VideoActivity.class); 
	        intent.putExtra("mediaUri", Uri.parse(url));
            startActivity(intent);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (bitmap!=null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
}
