/**
 * 
 */
package com.shandagames.android.maps;

import java.util.ArrayList;
import java.util.List;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

/**
 * @file MyCustomItemizedOverlay.java
 * @create 2012-8-27 上午11:14:42
 * @author lilong
 * @description TODO
 */
public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context context;
	
	private List<OverlayItem> overlayItemList = new ArrayList<OverlayItem>(); 
	
	/**
	 * @param defaultMarker
	 */
	public CustomItemizedOverlay(Drawable defaultMarker) {
		//绑定显示的图片
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	public CustomItemizedOverlay(Drawable marker, Context context) {  
        super(boundCenterBottom(marker));  
        this.context = context;  
    }  
	
	@Override
	//创建OverlayItem对象
	protected OverlayItem createItem(int index) {
		// TODO Auto-generated method stub
		return overlayItemList.get(index);
	}

	@Override
	//返回当前Overlay中包含的OverlayItem对象
	public int size() {
		// TODO Auto-generated method stub
		return overlayItemList.size();
	}

	//用于将生成好的OverlayItem添加到list当中 
	public void addOverlay(OverlayItem overlayItem) {  
        overlayItemList.add(overlayItem);  
        //有新的OverlayItem进来，必须先调用这个方法。
        this.populate();  
    }  
	
	@Override  
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {  
        //super.draw(canvas, mapView, shadow);  
        // Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换   
        Projection projection = mapView.getProjection();  
        // 遍历所有的OverlayItem   
        for (int index = this.size() - 1; index >= 0; index--) {  
            // 得到给定索引的item   
            OverlayItem overLayItem = getItem(index);  
            // 把经纬度变换到相对于MapView左上角的屏幕像素坐标   
            Point point = projection.toPixels(overLayItem.getPoint(), null);  
  
            Paint paintText = new Paint();  
            paintText.setColor(Color.RED);  
            paintText.setTextSize(13);  
            // 绘制文本   
            canvas.drawText(overLayItem.getTitle(), point.x + 10, point.y - 15, paintText);  
        }  
    }  
	
	@Override  
    // 处理点击事件   
    protected boolean onTap(int i) {  
        setFocus(overlayItemList.get(i));  
        Toast.makeText(context, overlayItemList.get(i).getSnippet(), Toast.LENGTH_SHORT).show();  
        return true;  
    }  
}
