package com.shandagames.android;

import java.util.HashMap;
import java.util.Map;
import com.shandagames.android.R;
import com.shandagames.android.support.DisplaySupport;
import com.shandagames.android.widget.PinnedExpandableListView;
import com.shandagames.android.widget.PinnedExpandableListView.PinnedExpandableListViewAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @author  lilong
 * @version 2012-7-19 下午4:55:35
 *
 */
public class PinnedExpandableActivity extends Activity {

	private PinnedExpandableListView listView;
	
	private String[] groups = {
            "神族","虫族","人族","神族","虫族","人族"
    };
    private String[][] childs = {
            {"狂战士","龙骑士","黑暗圣堂"},
            {"小狗","飞龙","自爆妃子"},
            {"步兵","伞兵","护士mm"},
            {"狂战士","龙骑士","黑暗圣堂"},
            {"小狗","飞龙","自爆妃子"},
            {"步兵","伞兵","护士mm"}
    };
                
    
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinnedheader_expand_header);
		
		listView = (PinnedExpandableListView)findViewById(android.R.id.list);
		listView.setPinnedHeaderView(getLayoutInflater().inflate(R.layout.pinnedheader_list_header, null));
	    listView.setDividerHeight(0);
		listView.setAdapter(new ExpandableAdapter(this));
	}
	
	public class ExpandableAdapter extends BaseExpandableListAdapter implements PinnedExpandableListViewAdapter {

		private Context mContext;
		
		private Map<Integer, Integer> saveSelectedGroups;
		
		public ExpandableAdapter(Context mContext) {
			this.mContext = mContext;
			this.saveSelectedGroups = new HashMap<Integer, Integer>();
		}
		
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return childs[groupPosition].length;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groups[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childs[groupPosition][childPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView txtView = getTextView();
			txtView.setText(getGroup(groupPosition).toString());
			//txtView.setBackgroundColor(R.color.pinned_header_background);
			return txtView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
            TextView textView = getTextView();//调用定义的getTextView()方法
            textView.setText(getChild(groupPosition,childPosition).toString());//添加数据
            textView.setPadding(100, 0, 0, 0);
			return textView;
		}

		@Override
		// 自列表选项是否可点击
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
		private TextView getTextView(){
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, DisplaySupport.dip2px(mContext, 45));
            TextView textView = new TextView(mContext);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(lp);
            textView.setTextSize(20);
            return textView;
        }

		@Override
		public int getPinnedHeaderState(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			final int childCount = getChildrenCount(groupPosition);
			if(childPosition == childCount - 1){  
				return PINNED_HEADER_PUSHED_UP; 
			} else if(childPosition == -1 && !listView.isGroupExpanded(groupPosition) || groupPosition == 0){ 
				return PINNED_HEADER_GONE; 
			} else{
				return PINNED_HEADER_VISIBLE;
			}
		}

		@Override
		public void configurePinnedHeader(View header, int groupPosition,
				int childPosition, int alpha) {
			// TODO Auto-generated method stub
			((TextView)header.findViewById(R.id.header_text)).setText(groups[groupPosition]);
		}

		@Override
		public void setGroupClickStatus(int groupPosition, int status) {
			// TODO Auto-generated method stub
			saveSelectedGroups.put(groupPosition, status);
		}

		@Override
		public int getGroupClickStatus(int groupPosition) {
			// TODO Auto-generated method stub
			if (saveSelectedGroups.containsKey(groupPosition)) {
				return saveSelectedGroups.get(groupPosition);
			}
			return 0;
		}
	}
	
}
