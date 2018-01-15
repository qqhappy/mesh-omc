package com.xinwei.lte.web.enb.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.lte.web.enb.model.RoleTypeDD;
import com.xinwei.minas.core.facade.conf.XMoBizConfigFacade;
import com.xinwei.minas.core.model.MoTypeDD;

/**
 * 
 * @author xieyang
 * 
 */

public class FieldLevelCache {

	// 记录日志
	private final Log log = LogFactory.getLog(FieldLevelCache.class);

	private static final FieldLevelCache instance = new FieldLevelCache();

	// 基站类型*基站版本�?-表名--级别--该级别的字段列表
	private Map<String, Map<String, Map<String, List<String>>>> map;

	private FieldLevelCache() {
	}

	public static FieldLevelCache getInstance() {
		return instance;
	}

	// 初始化缓�?
	public synchronized void initialize(XMoBizConfigFacade facade)
			throws Exception {
		map = facade.getTableFieldLevelConfig(MoTypeDD.ENODEB);
		// 初始化缓存成�?
		log.debug("init FieldLevelCache success");
	}

	/**
	 * 获取指定角色类型能够查看的字段列�?
	 * 
	 * @param roleId
	 * @param protocolVersion
	 * @param tableName
	 * @return
	 */
	public List<String> getFieldsBy(int roleId, int enbTypeId,
			String protocolVersion, String tableName) {
		String key = createKey(enbTypeId, protocolVersion);
		// 按照版本前三位获取配�?
		List<String> fieldList = new ArrayList<String>();
		if (map != null) {
			Map<String, Map<String, List<String>>> tableMap = map.get(key);
			if (tableMap != null) {
				// map<Level,fields>
				Map<String, List<String>> levelMap = tableMap.get(tableName);
				List<String> aList = levelMap
						.get(RoleTypeDD.ADMINISTRATOR_ROLE);
				List<String> bList = levelMap
						.get(RoleTypeDD.ADVANCEDOPERATOR_ROLE);
				List<String> cList = levelMap.get(RoleTypeDD.General_Role);
				if (roleId == RoleTypeDD.ADMINISTRATOR_ROLE_TYPE_ID) {
					// 管理员可查看A、B、C
					if (aList != null)
						fieldList.addAll(aList);
					if (bList != null)
						fieldList.addAll(bList);
					if (cList != null)
						fieldList.addAll(cList);
				} else if (roleId == RoleTypeDD.ADVANCEDOPERATOR_ROLE_TYPE_ID) {
					// 高级操作员可查看A、B
					if (aList != null)
						fieldList.addAll(aList);
					if (bList != null)
						fieldList.addAll(bList);
				} else if (roleId == RoleTypeDD.MONITOR_ROLE_TYPE_ID
						|| roleId == RoleTypeDD.PRIMARYOPERATOR_ROLE_TYPE_ID) {
					// 初级操作员和监视员只能查看A
					if (aList != null)
						fieldList.addAll(aList);
				}
			}
		}
		return fieldList;
	}

	private String createKey(int enbTypeId, String version) {
		return enbTypeId + "*" + version;
	}

	public Map<String, List<String>> getFieldMapBy(int roleId, int enbTypeId,
			String protocolVersion, String tableName) {
		String key = createKey(enbTypeId, protocolVersion);
		// 按照版本前三位获取配�?
		Map<String, List<String>> fieldMap = new HashMap<String, List<String>>();
		if (map != null) {
			Map<String, Map<String, List<String>>> tableMap = map.get(key);
			if (tableMap != null) {
				// map<Level,fields>
				Map<String, List<String>> levelMap = tableMap.get(tableName);
				List<String> aList = levelMap
						.get(RoleTypeDD.ADMINISTRATOR_ROLE);
				List<String> bList = levelMap
						.get(RoleTypeDD.ADVANCEDOPERATOR_ROLE);
				List<String> cList = levelMap.get(RoleTypeDD.General_Role);
				if (roleId == RoleTypeDD.ADMINISTRATOR_ROLE_TYPE_ID) {
					// 管理员可查看A、B、C
					if (aList != null)
						fieldMap.put(RoleTypeDD.ADMINISTRATOR_ROLE, aList);
					if (bList != null)
						fieldMap.put(RoleTypeDD.ADVANCEDOPERATOR_ROLE, bList);
					if (cList != null)
						fieldMap.put(RoleTypeDD.General_Role, cList);
				} else if (roleId == RoleTypeDD.ADVANCEDOPERATOR_ROLE_TYPE_ID) {
					// 高级操作员可查看A、B
					if (aList != null)
						fieldMap.put(RoleTypeDD.ADMINISTRATOR_ROLE, aList);
					if (bList != null)
						fieldMap.put(RoleTypeDD.ADVANCEDOPERATOR_ROLE, bList);
				} else if (roleId == RoleTypeDD.MONITOR_ROLE_TYPE_ID
						|| roleId == RoleTypeDD.PRIMARYOPERATOR_ROLE_TYPE_ID) {
					// 初级操作员和监视员只能查看A
					if (aList != null)
						fieldMap.put(RoleTypeDD.ADMINISTRATOR_ROLE, aList);
				}
			}
		}
		return fieldMap;
	}

}
