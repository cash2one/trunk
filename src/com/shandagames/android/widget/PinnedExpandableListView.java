package com.shandagames.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**  
 * A ListView that maintains a header pinned at the top of the list. 
 * The pinned header can be pushed up and dissolved as needed.  
 */ 
public class PinnedExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener, ExpandableListView.OnGroupClickListener {

	/**
	 * Adapter接口 . 列表必须实现此接口 .
	 */
	public interface PinnedExpandableListViewAdapter {

		/**
		 * 固定标题状态：不可见
		 */
		public static final int PINNED_HEADER_GONE = 0;
		/**
		 * 固定标题状态：可见
		 */
		public static final int PINNED_HEADER_VISIBLE = 1;
		/**
		 * 固定标题状态：正在往上推
		 */
		public static final int PINNED_HEADER_PUSHED_UP = 2;
		/**
		 * 获取 Header 的状态
		 * @param groupPosition
		 * @param childPosition
		 * @return PINNED_HEADER_GONE,PINNED_HEADER_VISIBLE,PINNED_HEADER_PUSHED_UP 其中之一
		 */
		public int getPinnedHeaderState(int groupPosition, int childPosition);
		/**
		 * 配置Header, 让Header知道显示的内容
		 * @param header
		 * @param groupPosition
		 * @param childPosition
		 * @param alpha
		 */
		public void configurePinnedHeader(View header, int groupPosition, int childPosition, int alpha);
		/**
		 * 设置组按下的状态 
		 * @param groupPosition
		 * @param status
		 */
		void setGroupClickStatus(int groupPosition, int status);
		/**
		 * 获取组按下的状态
		 * @param groupPosition
		 * @return
		 */
		int getGroupClickStatus(int groupPosition);
	}

	private static final int MAX_ALPHA = 255;

	private PinnedExpandableListViewAdapter mAdapter;
	private View mHeaderView;
	private boolean mHeaderVisible;
	private int mHeaderViewWidth;
	private int mHeaderViewHeight;

	public PinnedExpandableListView(Context context) {
        super(context);
        registerEventListener();
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerEventListener();
    }

    public PinnedExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        registerEventListener();
    }

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	private void registerEventListener() {
		 setOnScrollListener(this);
	     setOnGroupClickListener(this);
	}
	
	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (PinnedExpandableListViewAdapter) adapter;
	}

	/** 点击了Group触发的事件,要根据根据当前点击 Group的状态来判断 */
	@Override
	public boolean onGroupClick(ExpandableListView parent,View v,int groupPosition,long id) {
		if (mAdapter.getGroupClickStatus(groupPosition) == 0) {
			mAdapter.setGroupClickStatus(groupPosition, 1);
			parent.expandGroup(groupPosition);
			parent.setSelectedGroup(groupPosition);
		} else if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
			mAdapter.setGroupClickStatus(groupPosition, 0);
			parent.collapseGroup(groupPosition);
		}
		return true;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}


	private int mOldState = -1;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
		int state = mAdapter.getPinnedHeaderState(groupPos, childPos);
        //只有在状态改变时才layout，这点相当重要，不然可能导致视图不断的刷新
		if (mHeaderView != null && mAdapter != null && state != mOldState) {
			mOldState = state;
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
		}

		configureHeaderView(groupPos, childPos);
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mAdapter == null || ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
			return;
		}

		final int state = mAdapter.getPinnedHeaderState(groupPosition, childPosition);
		switch (state) {
			case PinnedExpandableListViewAdapter.PINNED_HEADER_GONE: {
				mHeaderVisible = false;
				break;
			}
			case PinnedExpandableListViewAdapter.PINNED_HEADER_VISIBLE: {
				mAdapter.configurePinnedHeader(mHeaderView, groupPosition, childPosition, MAX_ALPHA);
				if (mHeaderView.getTop() != 0) {
					mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
				}
				mHeaderVisible = true;
				break;
			}
			case PinnedExpandableListViewAdapter.PINNED_HEADER_PUSHED_UP: {
				final View firstView = getChildAt(0);
				if (firstView == null) {
					break;
				}
				int bottom = firstView.getBottom();
				int headerHeight = mHeaderView.getHeight();
				int y;
				int alpha;
				if (bottom < headerHeight) {
					y = bottom - headerHeight;
					alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
				} else {
					y = 0;
					alpha = MAX_ALPHA;
				}
				mAdapter.configurePinnedHeader(mHeaderView, groupPosition, childPosition, alpha);
				if (mHeaderView.getTop() != y) {
					mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
				}
				mHeaderVisible = true;
				break;
			}
			default:
				break;
		}
	}

	/** 列表界面更新时调用该方法(如滚动时) */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		//分组栏是直接绘制到界面中，而不是加入到ViewGroup中
		if (mHeaderVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	/**
	 * 点击 HeaderView 触发的事件
	 */
	private void setHeaderViewClick() {
		long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());
		int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

		if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
			this.collapseGroup(groupPosition);
			mAdapter.setGroupClickStatus(groupPosition, 0);
		} else{
			this.expandGroup(groupPosition);
			mAdapter.setGroupClickStatus(groupPosition, 1);
		}
		this.setSelectedGroup(groupPosition);
	}
	
	private float mDownX;
	private float mDownY;

	/**
	 * 如果 HeaderView 是可见的 , 此函数用于判断是否点击了 HeaderView, 并对做相应的处理 ,
	 * 因为 HeaderView 是画上去的 , 所以设置事件监听是无效的 , 只有自行控制 .
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mHeaderVisible) {
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mDownX = ev.getX();
					mDownY = ev.getY();
					if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight ) {
						return true;
					}
					break;	
				case MotionEvent.ACTION_UP:
					float x = ev.getX();
					float y = ev.getY();
					float offsetX = Math.abs(x - mDownX);
					float offsetY = Math.abs(y - mDownY);
					// 如果在固定标题内点击了，那么触发事件
					if (x <= mHeaderViewWidth && y <= mHeaderViewHeight && offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight) {
						if (mHeaderView != null) {
							setHeaderViewClick();
						}
						return true;
					}
					break;
				default:
					break;
			}
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
		configureHeaderView(groupPosition, childPosition);
	}

}
