package com.xinwei.minas.server.enb.enbapi;

public class OutDataInfo {
	String tableName; //表名
	String fieldName;//字段名
	String fieldValue; //字段值
}

class OamInputDate {
	int Scene;//场景
	int FreqBand;//频段
	int SysBandWidth;//带宽
	int SfCfg;//子帧配比
	int RruType;//RRU类型
	int AnNum;//天线数	
}