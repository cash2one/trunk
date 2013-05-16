package com.shandagames.android.bean;

/**
 * 基站信息
 * @author selience
 */
public class CellInfo  implements Comparable<CellInfo> {
	
	/** 基站id，用来找到基站的位置 */
	private int cellId = -1;
	/** 移动国家码，共3位，中国为460，即imsi前3位 */
	private String mobileCountryCode = "460";
	/** 移动网络码，共2位，在中国，移动的代码为00和02，联通的代码为01，电信的代码为03，即imsi第4~5位 */
	private String mobileNetworkCode = "0";
	/** 地区区域码 */
	private int locationAreaCode = -1;
	/** 信号类型[选 gsm|cdma|wcdma] */
	private String radioType = "";
	// 当前的时间
	private long time = 0L;

	public CellInfo() {
	}

	public CellInfo(int cellId, String mobileCountryCode,
			String mobileNetworkCode, int locationAreaCode, String radioType, long time) {
		super();
		this.cellId = cellId;
		this.mobileCountryCode = mobileCountryCode;
		this.mobileNetworkCode = mobileNetworkCode;
		this.locationAreaCode = locationAreaCode;
		this.radioType = radioType;
		this.time = time;
	}


	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public String getMobileCountryCode() {
		return mobileCountryCode;
	}

	public void setMobileCountryCode(String mobileCountryCode) {
		this.mobileCountryCode = mobileCountryCode;
	}

	public String getMobileNetworkCode() {
		return mobileNetworkCode;
	}

	public void setMobileNetworkCode(String mobileNetworkCode) {
		this.mobileNetworkCode = mobileNetworkCode;
	}

	public int getLocationAreaCode() {
		return locationAreaCode;
	}

	public void setLocationAreaCode(int locationAreaCode) {
		this.locationAreaCode = locationAreaCode;
	}

	public String getRadioType() {
		return radioType;
	}

	public void setRadioType(String radioType) {
		this.radioType = radioType;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int compareTo(CellInfo another) {
		// TODO Auto-generated method stub
		if (this.time == another.time)
			return 0;
		if (this.time < another.time) {
			return 1;
		}
		return -1;
	}

}
