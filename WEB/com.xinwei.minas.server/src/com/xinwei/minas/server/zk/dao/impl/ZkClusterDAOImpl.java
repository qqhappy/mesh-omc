/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.client.HostProvider;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.xinwei.minas.server.zk.dao.ZkClusterDAO;
import com.xinwei.minas.zk.core.basic.ZkCluster;
import com.xinwei.minas.zk.core.basic.ZkHost;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author liuzhongyan
 * 
 */

public class ZkClusterDAOImpl extends JdbcDaoSupport implements ZkClusterDAO {
	/**
	 * 查询ZooKeeper集群列表
	 * 
	 * @return
	 */
	public List<ZkCluster> queryZkClusters() throws Exception {
		String sql = "select * from zk_basic";
		List<ZkCluster> zkList = getJdbcTemplate().query(sql,
				new zkClusterMapper());
		return zkList;
	}

	/**
	 * 根据ID查询集群信息
	 * 
	 * @param zkClusterId
	 *            集群ID
	 * @return
	 */
	public ZkCluster queryZkClusterById(Long zkClusterId) throws Exception {
		String sql = "select * from zk_basic where id = " + zkClusterId;
		List<ZkCluster> zkList = getJdbcTemplate().query(sql,
				new zkClusterMapper());
		if (zkList == null || zkList.isEmpty()) {
			return null;
		}
		return zkList.get(0);
	}

	/**
	 * 增加ZooKeeper集群
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void addZkCluster(ZkCluster zkCluster) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("insert into zk_basic values (" + zkCluster.getId()
				+ ",'" + zkCluster.getName() + "',");
		str.append("'" + zkCluster.getZkServersString() + "')");
		getJdbcTemplate().update(str.toString());
	}

	/**
	 * 修改ZooKeeper集群
	 * 
	 * @param zkCluster
	 * @throws Exception
	 */
	public void modifyZkCluster(ZkCluster zkCluster) throws Exception {
		StringBuffer str = new StringBuffer();
		str.append("update zk_basic set  name = '" + zkCluster.getName()
				+ "',");
		str.append("host = '" + zkCluster.getZkServersString()
				+ "'  where id = " + zkCluster.getId());
		getJdbcTemplate().update(str.toString());
	}

	/**
	 * 删除ZooKeeper集群(需要断开与ZK的连接)
	 * 
	 * @param zkClusterId
	 * @throws Exception
	 */
	public void deleteZkCluster(Long zkClusterId) throws Exception {
		String sql = "delete from zk_basic where id = " + zkClusterId;
		getJdbcTemplate().update(sql);
	}

	class zkClusterMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			ZkCluster zkC = new ZkCluster();
			zkC.setId(rs.getLong("id"));
			zkC.setName(rs.getString("name"));
			String hostStr = rs.getString("host");
			List<ZkHost> zkHostList = new ArrayList();
			if (hostStr.indexOf(",") > 0) {
				String[] host = hostStr.split(",");

				for (int i = 0; i < host.length; i++) {
					String[] hostlist = host[i].split(":");
					ZkHost zkhost = new ZkHost();
					zkhost.setHost(hostlist[0]);
					zkhost.setPort(Integer.parseInt(hostlist[1]));
					zkHostList.add(zkhost);
				}
			} else {
				String[] hostlist = hostStr.split(":");
				ZkHost zkhost = new ZkHost();
				zkhost.setHost(hostlist[0]);
				zkhost.setPort(Integer.parseInt(hostlist[1]));
				zkHostList.add(zkhost);
			}

			zkC.setZkHosts(zkHostList);
			return zkC;
		}

	}

}
