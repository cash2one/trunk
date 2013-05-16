package com.shandagames.android.location;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.shandagames.android.bean.CellInfo;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.LocationManager;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

public class LBSManager extends PhoneStateListener {
	private static LBSManager lbsManager = null;

	private LocationManager locationManager;
	private TelephonyManager telephonyManager;

	private int phoneType = -1;

	private Vector<GSMCell> gsmCells = new Vector<GSMCell>();
	private Vector<CDMACell> cdmaCells = new Vector<CDMACell>();
	private List<CellInfo> cellInfos = new LinkedList<CellInfo>();

	private LBSManager(Context context) {
		this.locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		this.telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

		if (this.telephonyManager != null) {
			this.telephonyManager.listen(this, LISTEN_CELL_LOCATION);
		}
	}

	public static LBSManager getInstance(){
        return lbsManager;
	}

	public static void init(Context context) {
		lbsManager = new LBSManager(context);
	}
	
	
	private void resetCell() {
        this.gsmCells.clear();
        this.cdmaCells.clear();
	}
	
	 public void destroy() {
         resetCell();
         this.telephonyManager.listen(this, LISTEN_NONE);
         lbsManager = null;
	 }

	
	@TargetApi(5)
	@Override
	public void onCellLocationChanged(CellLocation location) {
		super.onCellLocationChanged(location);
		if ((location instanceof GsmCellLocation)) {
			phoneType = 1;
			gsmNetwork(cellInfos);
		} else {
			try {
				phoneType = 2;
				// check if CdmaCellLocation is exists.
				Class.forName("android.telephony.cdma.CdmaCellLocation");
				CdmaCellLocation cdma = (CdmaCellLocation) location;
				cdmaNetwork(cellInfos);
			} catch (ClassNotFoundException localClassNotFoundException) {
			}
		}

	}

	@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		super.onServiceStateChanged(serviceState);
		if (serviceState.getState() != 0) {
			if (phoneType == 1) {
				GSMCell gsmCell = new GSMCell(-1, -1, System.currentTimeMillis());
				gsmCells.add(0, gsmCell);
				Vector<GSMCell> tempCells = new Vector<GSMCell>(new HashSet<GSMCell>(gsmCells));
				Collections.sort(gsmCells);
				if (tempCells.size() > 10) {
					tempCells.subList(10, tempCells.size()).clear();
				}
				gsmCells.clear();
				gsmCells.addAll(tempCells);
			} else if (phoneType == 2) {
				CDMACell cdmaCell = new CDMACell();
				cdmaCell.stationId = -1;
				cdmaCell.networkId = -1;
				cdmaCell.systemId = -1;
				cdmaCell.time = System.currentTimeMillis();
				cdmaCell.lat = 0;
				cdmaCell.lon = 0;

				cdmaCells.add(0, cdmaCell);
				Vector<CDMACell> tempCells = new Vector<CDMACell>(new HashSet<CDMACell>(cdmaCells));
				Collections.sort(cdmaCells);
				if (tempCells.size() > 10) {
					tempCells.subList(10, tempCells.size()).clear();
				}
				cdmaCells.clear();
				cdmaCells.addAll(tempCells);
			}
		}

	}

	@TargetApi(5)
	private void gsmNetwork(List<CellInfo> cellInfos) {
		GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
		String mcc = telephonyManager.getNetworkOperator().substring(0, 3);
		String mnc = telephonyManager.getNetworkOperator().substring(3, 5);
		
		if (location.getCid()!=-1 && location.getLac()!=-1) {
			CellInfo info = new CellInfo();
			info.setCellId(location.getCid());
			info.setLocationAreaCode(location.getLac());
			info.setMobileCountryCode(mcc);
			info.setMobileNetworkCode(mnc);
			info.setTime(System.currentTimeMillis());
			info.setRadioType("gsm");
			cellInfos.add(info);
		}

		// 前面获取到的都是单个基站的信息，接下来再获取周围邻近基站信息以辅助通过基站定位的精准性
		// 获得邻近基站信息
		List<NeighboringCellInfo> list = telephonyManager.getNeighboringCellInfo();
		if (list != null && list.size() > 0) {
			for (NeighboringCellInfo loc : list) {
				if (loc.getCid()!=-1 && loc.getLac()!=-1) {
					CellInfo cell = new CellInfo();
					cell.setCellId(loc.getCid());
					cell.setLocationAreaCode(loc.getLac());
					cell.setMobileCountryCode(mcc);
					cell.setMobileNetworkCode(mnc);
					cell.setRadioType("gsm");
					cellInfos.add(cell);
				}
			}
		}
	}
	
	@TargetApi(5)
	private void cdmaNetwork(List<CellInfo> cellInfos) {
		CdmaCellLocation location = (CdmaCellLocation) telephonyManager.getCellLocation();
		String mcc = telephonyManager.getNetworkOperator().substring(0, 3);
		String mnc = String.valueOf(location.getSystemId());
		int lac = location.getNetworkId();
		
		CellInfo info = new CellInfo();
		info.setCellId(location.getBaseStationId());
		info.setLocationAreaCode(lac);
		info.setMobileNetworkCode(mnc);
		info.setMobileCountryCode(mcc);
		info.setRadioType("cdma");
		info.setTime(System.currentTimeMillis());
		cellInfos.add(info);

		// 前面获取到的都是单个基站的信息，接下来再获取周围邻近基站信息以辅助通过基站定位的精准性
		// 获得邻近基站信息
		List<NeighboringCellInfo> list = telephonyManager.getNeighboringCellInfo();
		if (list != null && list.size() > 0) {
			for (NeighboringCellInfo loc : list) {
				if (loc.getCid()!=-1 && loc.getLac()!=-1) {
					CellInfo cell = new CellInfo();
					cell.setCellId(loc.getCid());
					cell.setLocationAreaCode(lac);
					cell.setMobileCountryCode(mcc);
					cell.setMobileNetworkCode(mnc);
					cell.setRadioType("cdma");
					cellInfos.add(cell);
				}
			}
		}
	}
	
	public static class GSMCell implements Comparable<GSMCell> {
		public int cid = -1;
		public int lac = -1;
		public long time = 0L;

		public GSMCell(int cid, int lac, long time) {
			this.cid = cid;
			this.lac = lac;
			this.time = time;
		}

		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof GSMCell)) {
				return false;
			}
			GSMCell cell = (GSMCell) o;
			return (this.cid == cell.cid) && (this.lac == cell.lac);
		}

		@Override
		public int compareTo(GSMCell another) {
			if (this.time == another.time)
				return 0;
			if (this.time < another.time) {
				return 1;
			}
			return -1;
		}

		public int hashCode() {
			int result = 17;
			result = 31 * result + this.cid;
			result = 31 * result + this.lac;
			return result;
		}
	}

	public static class CDMACell implements Comparable<CDMACell> {
		public int lat = 2147483647;
		public int lon = 2147483647;
		public int stationId = -1;
		public int networkId = -1;
		public int systemId = -1;
		public long time = 0L;

		@Override
		public int compareTo(CDMACell another) {
			if (this.time == another.time)
				return 0;
			if (this.time < another.time) {
				return 1;
			}
			return -1;
		}

		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof CDMACell)) {
				return false;
			}
			CDMACell cell = (CDMACell) o;

			return (this.stationId == cell.stationId)
					&& (this.networkId == cell.networkId)
					&& (this.systemId == cell.systemId);
		}

		public int hashCode() {
			int result = 17;
			result = 31 * result + this.stationId;
			result = 31 * result + this.networkId;
			result = 31 * result + this.systemId;
			return result;
		}
	}

}
