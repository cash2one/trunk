package com.shandagames.android;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shandagames.android.base.BaseActivity;
import com.shandagames.android.constant.Constants;
import com.shandagames.android.widget.wheel.OnWheelChangedListener;
import com.shandagames.android.widget.wheel.OnWheelScrollListener;
import com.shandagames.android.widget.wheel.WheelView;
import com.shandagames.android.widget.wheel.adapters.ArrayWheelAdapter;
import com.shandagames.android.widget.wheel.adapters.NumericWheelAdapter;

/**
 * @file WheelViewActivity.java
 * @create 2013-5-10 下午04:36:45
 * @author lilong
 * @description TODO
 */
public class WheelViewActivity extends BaseActivity {
    // Scrolling flag
    private boolean scrolling = false;
    
	private WheelView provinceWheel;
	private WheelView cityWheel; 
	
	private WheelView yearWheel;
	private WheelView monthWheel;
	private WheelView dayWheel;
	
	@Override
	protected void _onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.wheelview);
		ensureUi();
		initDatePicker();
	}

	private void ensureUi() {
		provinceWheel = (WheelView) findViewById(R.id.country);
		cityWheel = (WheelView) findViewById(R.id.city);
		provinceWheel.setViewAdapter(new ArrayWheelAdapter<String>(this, Constants.PROVINCES));
		provinceWheel.setCurrentItem(1);
		updateCities(cityWheel, Constants.CITIES, provinceWheel.getCurrentItem());
		provinceWheel.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			    if (!scrolling) {
			        updateCities(cityWheel, Constants.CITIES, newValue);
			    }
			}
		});
		provinceWheel.addScrollingListener( new OnWheelScrollListener() {
			
			@Override
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
			
			@Override
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateCities(cityWheel, Constants.CITIES, provinceWheel.getCurrentItem());
            }
        });
	}
	
	private void initDatePicker() {
		Calendar calendar = Calendar.getInstance();
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH);
		
		yearWheel = (WheelView) findViewById(R.id.year);
		monthWheel = (WheelView)findViewById(R.id.month);
		dayWheel = (WheelView)findViewById(R.id.day);
		
		yearWheel.setViewAdapter(new DateNumericAdapter(this, curYear, curYear + 10, 0));
		yearWheel.setCurrentItem(curYear);
		yearWheel.addChangingListener(listener);
		
		monthWheel.setViewAdapter(new DateArrayAdapter(this, Constants.MONTHS_CN, curMonth));
		monthWheel.setCurrentItem(curMonth);
        monthWheel.addChangingListener(listener);
        
        //day
        updateDays(yearWheel, monthWheel, dayWheel);
        dayWheel.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
	}
	
	OnWheelChangedListener listener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateDays(yearWheel, monthWheel, dayWheel);
        }
    };
	
	/**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this, cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);        
    }
    
    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
