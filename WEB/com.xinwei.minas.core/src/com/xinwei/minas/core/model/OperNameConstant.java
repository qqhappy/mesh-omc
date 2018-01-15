/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-15	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

/**
 * 
 * 业务名称常量类
 * 
 * @author fanhaoyu
 * 
 */

public class OperNameConstant {

	public static final String MCBTS_LIST = "McBtsList";
	public static final String BTS_MANAGE_STATE = "BTSMangeState";
	public static final String SYNC_FROM_EMS_TO_NE = "SyncFromEMSToNE";
	public static final String SYNC_FROM_NE_TO_EMS = "SyncFromNEToEMS";
	public static final String ANTENNA_LOCK_CONFIG = "AntennaLockConfig";
	public static final String DBS_SERVER = "DBSServer";
	public static final String IPV6_OPERATION = "IPv6Operation";
	public static final String ETHERNET2_OPERATION = "Ethernet2Operation";
	public static final String MCBTS_BATCH_UPGRADE = "McBtsBatchUpgrade";

	public static final String USER_PASSWORD = "UserPassword";
	public static final String USER_MANAGEMENT = "UserManagement";

	public static final String HISTORY_ALARM = "HistoryAlarm";
	public static final String CURRENT_ALARM = "CurrentAlarm";

	public static final String NK_LIST = "NkList";
	public static final String NK_CLUSTER = "NkCluster";
	public static final String ZK_BACKUP_TASK = "ZkBackupTask";
	public static final String ZK_BACKUP = "ZkBackup";

	public static final String SAG_MANAGEMENT = "SagManagement";

	public static final String USER_TERMINAL = "UserTerminal";
	public static final String UT_RESTART = "UTRestart";
	public static final String UT_DETECTIVE = "UTDetective";
	public static final String UPGRADE_UT_SOFTWARE = "UpgradeUTSoftware";
	public static final String UPGRADE_UT_BOOTLOADER = "UpgradeUTBootloader";
	public static final String UT_PERF_DATA = "UTPerfData";
	public static final String UT_MEM_FUNCTION = "UTMemFunction";
	// 批量升级终端
	public static final String UPGRADE_BATCH_UT_SOFTWARE = "UpgradeBatchUTSoftware";
	// 批量升级终端的进度
	public static final String UPGRADE_BATCH_UT_SOFTWARE_Progress = "UpgradeBatchUTSoftwareProgress";

	// 特殊业务
	public static final String SYS_FREQ_CONFIG = "SysEffectiveFreqConfig";
	public static final String SYS_FREQ_PUSHDOWN = "SysEffectiveFreqPushDown";

	public static final String DATA_PACKAGE_FILTER_CONFIG = "DataPackageFilterConfig";
	public static final String DATA_PACKAGE_FILTER_PUSHDOWN = "DataPackageFilterPushDown";

	// TCN1000相关
	/**
	 * 网管配置（SDC配置）
	 */
	public static final String SDC_CONFIG = "SDCConfig";
}
