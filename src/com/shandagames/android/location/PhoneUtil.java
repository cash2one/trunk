package com.shandagames.android.location;

import android.content.Context;
import java.util.List;
import com.shandagames.android.bean.CellInfo;

public final class PhoneUtil {
	public static final String CELL_GSM = "gsm";
	public static final String CELL_CDMA = "cdma";
	private static final int aJ = 17;

	public static String getMacXml(List<MacInfo> paramList) {
		if ((paramList == null) || (paramList.size() <= 0))
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paramList.size(); i++) {
			if ((paramList.get(i) == null)
					|| (((MacInfo) paramList.get(i)).mac.length() != aJ))
				continue;
			sb.append("<mac ");
			sb.append("macDbm=\"" + ((MacInfo) paramList.get(i)).dbm + "\"");
			sb.append(">");
			sb.append(((MacInfo) paramList.get(i)).mac);
			sb.append("</mac>");
		}
		return sb.toString();
	}

	public static String getCellXml(List<CellInfo> paramList) {
		if ((paramList == null) || (paramList.size() <= 0))
			return "";
		StringBuilder sb = new StringBuilder();
		for (CellInfo cellInfo : paramList) {
			sb.append("<cell ");
			sb.append("mcc=\"" + cellInfo.getMobileCountryCode() + "\" ");
			sb.append("mnc=\"" + cellInfo.getMobileNetworkCode() + "\" ");
			sb.append("lac=\"" + cellInfo.getLocationAreaCode() + "\" ");
			sb.append("type=\"" + cellInfo.getRadioType() + "\" ");
			/*sb.append("stationId=\""
					+ ((CellInfo) paramList.get(i)).stationId + "\" ");
			sb.append("networkId=\""
					+ ((CellInfo) paramList.get(i)).networkId + "\" ");
			sb.append("systemId=\"" + ((CellInfo) paramList.get(i)).systemId
					+ "\" ");
			sb.append("dbm=\"" + ((CellInfo) paramList.get(i)).dbm + "\" ");
			sb.append(" >");
			sb.append(((CellInfo) paramList.get(i)).cellid);*/
			sb.append("</cell>");
		}
		return sb.toString();
	}

	/**
	  * 拼装json请求参数，拼装基站信息
	  *
	  * 入参：{'version': '1.1.0','host': 'maps.google.com','home_mobile_country_code': 460,
	  *       'home_mobile_network_code': 14136,'radio_type': 'cdma','request_address': true,
	  *       'address_language': 'zh_CN','cell_towers':[{'cell_id': '12835','location_area_code': 6,
	  *       'mobile_country_code': 460,'mobile_network_code': 14136,'age': 0}]}
	  * 出参：{"location":{"latitude":26.0673834,"longitude":119.3119936,
	  *       "address":{"country":"ä¸­å½","country_code":"CN","region":"ç¦å»ºç","city":"ç¦å·å¸",
	  *       "street":"äºä¸ä¸­è·¯","street_number":"128å·"},"accuracy":935.0},
	  *       "access_token":"2:xiU8YrSifFHUAvRJ:aj9k70VJMRWo_9_G"}
	  * 请求路径：http://maps.google.cn/maps/geo?key=abcdefg&q=26.0673834,119.3119936
      */
	public static String getRequestParams(List<CellInfo> cellInfos) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (cellInfos != null && cellInfos.size() > 0) {
			sb.append("'version': '1.1.0',"); // google api 版本[必]
			sb.append("'host': 'maps.google.com',"); // 服务器域名[必]
			sb.append("'home_mobile_country_code': " + cellInfos.get(0).getMobileCountryCode() + ","); // 移动用户所属国家代号[选// 中国460]
			sb.append("'home_mobile_network_code': " + cellInfos.get(0).getMobileNetworkCode() + ","); // 移动系统号码[默认0]
			sb.append("'radio_type': '" + cellInfos.get(0).getRadioType() + "',"); // 信号类型[选 gsm|cdma|wcdma]
			sb.append("'request_address': true,"); // 是否返回数据[必]
			sb.append("'address_language': 'zh_CN',"); // 反馈数据语言[选 中国 zh_CN]
			sb.append("'cell_towers':["); // 移动基站参数对象[必]
			for (CellInfo cellInfo : cellInfos) {
				sb.append("{");
				sb.append("'cell_id': '" + cellInfo.getCellId() + "',"); // 基站ID[必]
				sb.append("'location_area_code': " + cellInfo.getLocationAreaCode() + ","); // 地区区域码[必]
				sb.append("'mobile_country_code': " + cellInfo.getMobileCountryCode() + ",");
				sb.append("'mobile_network_code': " + cellInfo.getMobileNetworkCode() + ",");
				sb.append("'age': 0"); // 使用好久的数据库[选 默认0表示使用最新的数据库]
				sb.append("},");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
		}
		sb.append("}");
		System.out.println("request param:" + sb); // 请求json参数格式
		return sb.toString();
	}
	
	public static List<CellInfo> getCellInfoList(Context context) {
		return new CellIDInfoManager(context).getCellInfoList();
	}

	public static class MacInfo {
		public String mac;
		public String dbm;

		public MacInfo(String mac, String dbm) {
			this.mac = mac;
			this.dbm = dbm;
		}
	}

	/*
	public static class CellInfo {
		public static final int MAX_CID = 65535;
		public static final int MAX_LAC = 65535;
		
		public String mcc; // mobileCountryCode移动国家代码（中国的为460）
		public String mnc; // mobileNetworkCode移动网络号码（中国移动为00，中国联通为01）
		public String lac;  // locationAreaCode位置区域码    
		public String cellid; // cellId基站编号，是个16位的数据（范围是0到65535）
		public String type; //联通移动gsm，电信cdma  
		public String stationId;
		public String networkId;
		public String systemId;
		public String dbm;
		
		public CellInfo() {}
		
		public CellInfo(String mcc, String mnc, String lac, String cellid,
				String type, String stationId, String networkId,
				String systemId, String dbm) {
			this.mcc = mcc;
			this.mnc = mnc;
			this.lac = lac;
			this.cellid = cellid;
			this.type = type;
			this.stationId = stationId;
			this.networkId = networkId;
			this.systemId = systemId;
			this.dbm = dbm;
		}
		
	}*/
}
