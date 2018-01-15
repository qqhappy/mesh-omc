package com.xinwei.lte.web.uem.model;

public class UEConfigModel extends UEModel{
	
	private String Location;
	private String Latitude;
	private String Longitude;
	private String DataReportPeriod;
	private String ControlReportPeriod;
	private String BitRate;
	private String DataBit;
	private String StopBit;
	private String CheckBit;
	private String AlarmMaskBit;	
	private String BussinessType;
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getDataReportPeriod() {
		return DataReportPeriod;
	}
	public void setDataReportPeriod(String dataReportPeriod) {
		DataReportPeriod = dataReportPeriod;
	}
	public String getControlReportPeriod() {
		return ControlReportPeriod;
	}
	public void setControlReportPeriod(String controlReportPeriod) {
		ControlReportPeriod = controlReportPeriod;
	}
	public String getBitRate() {
		return BitRate;
	}
	public void setBitRate(String bitRate) {
		BitRate = bitRate;
	}
	public String getDataBit() {
		return DataBit;
	}
	public void setDataBit(String dataBit) {
		DataBit = dataBit;
	}
	public String getStopBit() {
		return StopBit;
	}
	public void setStopBit(String stopBit) {
		StopBit = stopBit;
	}
	public String getCheckBit() {
		return CheckBit;
	}
	public void setCheckBit(String checkBit) {
		CheckBit = checkBit;
	}
	public String getAlarmMaskBit() {
		return AlarmMaskBit;
	}
	public void setAlarmMaskBit(String alarmMaskBit) {
		AlarmMaskBit = alarmMaskBit;
	}
	public String getBussinessType() {
		return BussinessType;
	}
	public void setBussinessType(String bussinessType) {
		BussinessType = bussinessType;
	}
	
	
}
