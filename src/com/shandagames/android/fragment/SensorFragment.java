package com.shandagames.android.fragment;

import java.util.List;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.shandagames.android.app.BaseFragment;

public class SensorFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ScrollView scrollView = new ScrollView(getActivity());
		TextView txtView = new TextView(getActivity());
		txtView.setText(showIt());
		scrollView.addView(txtView);
		return scrollView;
	}
	
	@Override
	public boolean isCleanStack() {
		return true;
	}

	public String showIt() {
		// 获得传感器管理器
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);

		StringBuilder sb = new StringBuilder();
		// 显示有多少个传感器
		sb.append("\t该手机有" + allSensors.size() + "个传感器：\n\n");

		String typeName = "";
		// 显示每个传感器的具体信息
		for (Sensor s : allSensors) {
			typeName = SensorTypeName.getSensorTypeName(s.getType());
			sb.append(String.format("\t类型:%s\n", typeName));
			sb.append(String.format("\t设备名称:%s\n", s.getName()));
			sb.append(String.format("\t设备版本:%s\n", s.getVersion()));
			sb.append(String.format("\t供应商:%s\n", s.getVendor()));
			sb.append("\n");
		}
		return sb.toString();
	}

	static class SensorTypeName {
        private static String[] itsNames;
        
        static {
            itsNames = new String[20];
            itsNames[0] = "未知";
            itsNames[Sensor.TYPE_ACCELEROMETER] = "加速度";
            itsNames[Sensor.TYPE_MAGNETIC_FIELD] = "磁力";
            itsNames[Sensor.TYPE_ORIENTATION] = "方向";
            itsNames[Sensor.TYPE_GYROSCOPE] = "陀螺仪";
            itsNames[Sensor.TYPE_LIGHT] = "光线感应";
            itsNames[Sensor.TYPE_PRESSURE] = "压力";
            itsNames[Sensor.TYPE_TEMPERATURE] = "温度";
            itsNames[Sensor.TYPE_PROXIMITY] = "接近,距离传感器";
            itsNames[Sensor.TYPE_GRAVITY] = "重力";
            itsNames[Sensor.TYPE_LINEAR_ACCELERATION] = "线性加速度";
            itsNames[Sensor.TYPE_ROTATION_VECTOR] = "旋转矢量";
            itsNames[Sensor.TYPE_RELATIVE_HUMIDITY] = "TYPE_RELATIVE_HUMIDITY";
            itsNames[Sensor.TYPE_AMBIENT_TEMPERATURE] = "TYPE_AMBIENT_TEMPERATURE";
            itsNames[13] = "TYPE_AMBIENT_TEMPERATURE";
            itsNames[14] = "TYPE_MAGNETIC_FIELD_UNCALIBRATED";
            //itsNames[Sensor.TYPE_GAME_ROTATION_VECTOR] = "TYPE_GAME_ROTATION_VECTOR";
        }
        
        public static String getSensorTypeName(int type){
            if(type > 0 && type < itsNames.length){
                return itsNames[type];
            }
            return "未知";
        }
    }
}
