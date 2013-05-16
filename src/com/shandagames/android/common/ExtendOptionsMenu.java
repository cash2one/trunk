package com.shandagames.android.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shandagames.android.R;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

/**
 * @author  lilong
 * @version 2012-7-24 下午5:11:24
 *
 */
public class ExtendOptionsMenu implements OnItemClickListener {

	private static final int MENU_ITEM_MORE = 11;
	
	private Context mContext;
	private Resources mResources;
	
	private GridView mGridView;
	private PopupWindow popupWindow;
	
	private OnItemClickListener mOnItemClickListener;
	
	private boolean isMore;// menu菜单翻页控制
	
	/** 菜单文字1 **/    
	String[] menu_name = { "搜索", "文件管理", "下载管理", "全屏", "网址", "书签",            
			"加入书签", "分享页面", "退出", "夜间模式", "刷新", "更多" };
	/** 菜单文字2 **/    
	String[] submenu_name = { "自动横屏", "笔选模式", "阅读模式", "浏览模式", "快捷翻页",            
			"检查更新", "检查网络", "定时刷新", "设置", "帮助", "关于", "返回" };
	
	String[] menu_image = { "menu_search",            
			"menu_filemanager", "menu_downmanager",            
			"menu_fullscreen", "menu_inputurl",            
			"menu_bookmark", "menu_bookmark_sync_import",            
			"menu_sharepage", "menu_quit",            
			"menu_nightmode", "menu_refresh",            
			"menu_more" };
	
	String[] submenu_image = { "menu_auto_landscape",            
			"menu_penselectmodel", "menu_page_attr",            
			"menu_novel_mode", "menu_page_updown",            
			"menu_checkupdate", "menu_checknet",            
			"menu_refreshtimer", "menu_syssettings",            
			"menu_help", "menu_about", "menu_return" };
	
	public ExtendOptionsMenu(Context context) {
		this.mContext = context;
		this.mResources = context.getResources();
		init();
	}
	
	private void init() {
		mGridView = new GridView(mContext);
		mGridView.setNumColumns(4);
		mGridView.setHorizontalSpacing(10);
		mGridView.setVerticalSpacing(10);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setOnItemClickListener(this);
		setAdapterData(menu_name, menu_image);
	}

	private void setAdapterData(String[] menus_name,String[] menus_image) {
		/** 数据源  **/
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		/** 添加数据 **/
		 for (int i = 0; i < menus_name.length; i++) {            
			 HashMap<String, Object> map = new HashMap<String, Object>();            
			 map.put("itemImage", menus_image[i]);            
			 map.put("itemText", menus_name[i]);            
			 data.add(map);        
		}
		/** 数据适配器  **/
		ImageAdapter mAdapter = new ImageAdapter(mContext,
				data,
				R.layout.options_menu_list_item,
				new String[]{"itemImage","itemText"},
				new int[]{R.id.image,R.id.title}
		);
		/** 绑定数据源 **/
		mGridView.setAdapter(mAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(position) {
			case MENU_ITEM_MORE:
				if(!isMore) {
					setAdapterData(submenu_name,submenu_image);
					isMore=true;
				}
				else {
					setAdapterData(menu_name,menu_image);
					isMore=false;
				}
				parent.setSelection(MENU_ITEM_MORE);//选中项为ITEM_MORE
				break;
			default:
				if (mOnItemClickListener!=null) {
					mOnItemClickListener.onItemClick(parent, view, position, id);
				}
				break;
		}
	}
	
	public void show(View anchor) {
		if (popupWindow == null) {
			popupWindow = new PopupWindow(mGridView,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,true);
			popupWindow.setBackgroundDrawable(mResources.getDrawable(R.drawable.options_menu_bg_frame));
			popupWindow.setFocusable(true); //menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
			popupWindow.setOutsideTouchable(false); //设置触摸获取焦点
		}
		popupWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
	}
	
	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	private class ImageAdapter extends SimpleAdapter {

		private AssetManager mAsset;
		
		public ImageAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.mAsset = context.getAssets();
		}

		@Override
		public void setViewImage(ImageView v, String value) {
			InputStream in = null;
			try {
				in = mAsset.open("menu"+File.separator+value+".png");
				v.setImageBitmap(BitmapFactory.decodeStream(in));
			} catch (Exception ex) {
				Log.e("setViewImage", ex.getLocalizedMessage());
			} finally {
				if (in != null){
					try {
						in.close();
					} catch (IOException ex) {
					}
				}
			}
		}
	}

}
