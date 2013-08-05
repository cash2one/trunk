package com.shandagames.android.util;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public final class PopupWindowUtil {

	private PopupWindowUtil() {
	}

	 /** 设置PopupWindow显示位置 ：显示在View上方 */
    public static PopupWindow showPopupWindowAtTop(PopupWindow popup, View anchor) {
    	int[] location = new int[2];
    	anchor.getLocationOnScreen(location);
    	popup.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1]-popup.getHeight());
    	return popup;
    }
    
    /** 设置PopupWindow显示位置 ：显示在View下方 */
    public static PopupWindow showPopupWindowAtBotton(PopupWindow popup, View anchor) {
    	popup.showAsDropDown(anchor);
    	return popup;
    }
    
    /** 设置PopupWindow显示位置 ：显示在View左边 */
    public static PopupWindow showPopupWindowAtLeft(PopupWindow popup, View anchor) {
    	int[] location = new int[2];
    	anchor.getLocationOnScreen(location);
    	popup.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0]-popup.getWidth(), location[1]);
    	return popup;
    }
    
    /** 设置PopupWindow显示位置 ：显示在View左边 */
    public static PopupWindow showPopupWindowAtRight(PopupWindow popup, View anchor) {
    	int[] location = new int[2];
    	anchor.getLocationOnScreen(location);
    	popup.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0]+popup.getWidth(), location[1]);
    	return popup;
    }
}
