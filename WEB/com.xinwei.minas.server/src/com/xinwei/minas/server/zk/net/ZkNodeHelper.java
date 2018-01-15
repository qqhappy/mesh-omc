/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.net;

import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_ACCESSGROUP;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_BTS;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_BTSSTATE;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_BTS_GROUP;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_BTS_INFO;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_GROUPEDBTS;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_NKCLI;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_NKCLI_CLIENT;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_NKCLI_SERVER;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_NULL;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_RULES;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAG;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAGDATA;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAGEXIST;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAGLINEUP;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAGLINK;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAGPAYLOAD;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAG_GROUP;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAG_INFO;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SAG_ROOT;
import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.NODE_TYPE_SERVSAG;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeHeader;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeStat;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkAccessGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsInfoVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsServiceSagVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsStatusVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkGroupedBtsVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkRulesVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagDataVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagExistVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagGroupVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagInfoVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagNodeLockVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagPayLoadVo;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagRootVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagVO;

/**
 * 
 * ZK节点助手
 * 
 * @author chenjunhua
 * 
 */

public class ZkNodeHelper {

	private static final Log log = LogFactory.getLog(ZkNodeHelper.class);

	/**
	 * 查询SAG业务树
	 * 
	 * @param connector
	 * @return 树的根节点
	 * @throws Exception
	 */
	public static ZkNode querySagBizTree(ZkClusterConnector connector)
			throws Exception {
		String rootPath = ZkNodeConstant.SAG_ROOT_PATH;
		// 如果根路径不存在，自动创建一个并返回
		if (!connector.exists(rootPath)) {
			return createDefaultRoot(connector);
		}
		//
		ZkNode root = new ZkNode(rootPath);
		buildTree(connector, root);
		return root;
	}

	/**
	 * 创建默认的根节点
	 * 
	 * @param connector
	 * @return
	 * @throws Exception
	 */
	public static ZkNode createDefaultRoot(ZkClusterConnector connector)
			throws Exception {
		ZkNode sagRoot = new ZkNode(ZkNodeConstant.SAG_ROOT_PATH);
		sagRoot.setHeader(new ZkNodeHeader(ZkNodeConstant.NODE_TYPE_SAG_ROOT));
		ZkSagRootVO sagRootVO = new ZkSagRootVO();
		sagRootVO.setMaxSubNode(5);
		sagRoot.setZkNodeVO(sagRootVO);
		connector.create(ZkNodeConstant.SAG_ROOT_PATH, sagRoot.encode(),
				CreateMode.PERSISTENT);
		return sagRoot;
	}

	/**
	 * 构造ZK业务树
	 * 
	 * @param connector
	 * @param zkNode
	 */
	public static void buildTree(ZkClusterConnector connector, ZkNode zkNode) {
		// 获取节点路径
		String path = zkNode.getPath();
		// 如果路径不存在，则退出
		if (!connector.exists(path)) {
			return;
		}
		// 构造ZK节点
		buildZKNode(connector, zkNode);

		// 获取孩子节点
		List<String> childrenName = connector.getChildren(path);
		for (String childName : childrenName) {
			String childPath = path + "/" + childName;
			ZkNode child = new ZkNode(childPath);
			zkNode.addChild(child);
			// 递归调用
			buildTree(connector, child);
		}
	}

	/**
	 * 构造ZK节点
	 * 
	 * @param zkNode
	 */
	public static void buildZKNode(ZkClusterConnector connector, ZkNode zkNode) {
		// 获取节点内容
		Stat stat = new Stat();
		byte[] content = connector.readData(zkNode.getPath(), stat);

		byte[] headerContent = new byte[ZkNodeHeader.LEN];
		System.arraycopy(content, 0, headerContent, 0, ZkNodeHeader.LEN);
		// 头部
		ZkNodeHeader header = new ZkNodeHeader(headerContent);
		zkNode.setHeader(header);
		zkNode.setStat(buildZkNodeStat(stat));
		// 节点类型
		int nodeType = header.getNodeType();
		// 内容实体
		ZkNodeVO zkNodeVO = createZkNodeVO(nodeType);

		zkNodeVO.decode(content, ZkNodeHeader.LEN);
		zkNode.setZkNodeVO(zkNodeVO);
	}

	/**
	 * 构造ZkNodeStat
	 * 
	 * @param stat
	 */
	public static ZkNodeStat buildZkNodeStat(Stat stat) {
		ZkNodeStat zkNodeStat = new ZkNodeStat();
		zkNodeStat.setAversion(stat.getAversion());
		zkNodeStat.setCtime(stat.getCtime());
		zkNodeStat.setCversion(stat.getCversion());
		zkNodeStat.setCzxid(stat.getCzxid());
		zkNodeStat.setDataLength(stat.getDataLength());
		zkNodeStat.setEphemeralOwner(stat.getEphemeralOwner());
		zkNodeStat.setMtime(stat.getMtime());
		zkNodeStat.setMzxid(stat.getMzxid());
		zkNodeStat.setNumChildren(stat.getNumChildren());
		zkNodeStat.setPzxid(stat.getPzxid());
		zkNodeStat.setVersion(stat.getVersion());
		return zkNodeStat;
	}

	/**
	 * 根据节点类型创建节点内容实体
	 * 
	 * @param nodeType
	 *            节点类型
	 * @return
	 */
	public static ZkNodeVO createZkNodeVO(int nodeType) {
		ZkNodeVO nodeVO = null;
		switch (nodeType) {
		case NODE_TYPE_SAG_ROOT:
			nodeVO = new ZkSagRootVO();
			break;
		case NODE_TYPE_SAG_GROUP:
			nodeVO = new ZkSagGroupVO();
			break;
		case NODE_TYPE_BTS_INFO:
			nodeVO = new ZkBtsInfoVO();
			break;
		case NODE_TYPE_RULES:
			nodeVO = new ZkRulesVO();
			break;
		case NODE_TYPE_SAG_INFO:
			nodeVO = new ZkSagInfoVO();
			break;
		case NODE_TYPE_BTS_GROUP:
			nodeVO = new ZkBtsGroupVO();
			break;
		case NODE_TYPE_BTS:
			nodeVO = new ZkBtsVO();
			break;
		case NODE_TYPE_SAG:
			nodeVO = new ZkSagVO();
			break;
		case NODE_TYPE_SAGPAYLOAD:
			nodeVO = new ZkSagPayLoadVo();
			break;
		case NODE_TYPE_SAGDATA:
			nodeVO = new ZkSagDataVO();
			break;
		case NODE_TYPE_BTSSTATE:
			nodeVO = new ZkBtsStatusVO();
			break;
		case NODE_TYPE_SERVSAG:// bts组下服务节点
			nodeVO = new ZkBtsServiceSagVO();
			break;
		case NODE_TYPE_SAGLINK:// bts下的sag链路数据
			nodeVO = new ZkBtsSagLinkVO();
			break;
		case NODE_TYPE_SAGEXIST: // sag存在节点，临时节点
			nodeVO = new ZkSagExistVO();
			break;
		case NODE_TYPE_SAGLINEUP: // 排队节点，临时节点
			nodeVO = new ZkSagNodeLockVO();
			break;
		case NODE_TYPE_ACCESSGROUP:
			nodeVO = new ZkAccessGroupVO();
			break;
		case NODE_TYPE_GROUPEDBTS:// sag的服务bts组
			nodeVO = new ZkGroupedBtsVO();
			break;
		case NODE_TYPE_NULL:
			break;
		case NODE_TYPE_NKCLI:
			break;
		case NODE_TYPE_NKCLI_SERVER:
			break;
		case NODE_TYPE_NKCLI_CLIENT:
			break;
		default:
			break;
		}
		return nodeVO;
	}

	/**
	 * 注册侦听器
	 * 
	 * @param connector
	 * @param root
	 */
	public static void registerListener(ZkClusterConnector connector,
			ZkNode root) {
		// 注册ZK状态变化侦听器
		connector.subscribeStateChanges(connector.getZkStateListener());
		// 注册ZK节点侦听器
		registerNodeListener(connector, root);
	}

	/**
	 * 注册ZK节点侦听器
	 * 
	 * @param connector
	 * @param root
	 */
	public static void registerNodeListener(ZkClusterConnector connector,
			ZkNode root) {
		Iterator<ZkNode> itr = root.createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			// 注册节点数据变化侦听和孩子节点变化侦听
			String path = node.getPath();
			connector.subscribeDataChanges(path, connector.getZkDataListener());
			connector.subscribeChildChanges(path,
					connector.getZkChildListener());
		}
	}

	/**
	 * 删除侦听器
	 * 
	 * @param connector
	 * @param root
	 */
	public static void deregisterListener(ZkClusterConnector connector,
			ZkNode root) {
		// 删除ZK状态变化侦听器
		connector.unsubscribeStateChanges(connector.getZkStateListener());
		// 删除ZK节点侦听器
		deregisterNodeListener(connector, root);
	}

	/**
	 * 删除ZK节点侦听器
	 * 
	 * @param connector
	 * @param root
	 */
	public static void deregisterNodeListener(ZkClusterConnector connector,
			ZkNode root) {
		Iterator<ZkNode> itr = root.createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			// 删除节点数据变化侦听和孩子节点变化侦听
			String path = node.getPath();
			connector.unsubscribeDataChanges(path,
					connector.getZkDataListener());
			connector.unsubscribeChildChanges(path,
					connector.getZkChildListener());
		}
	}

	/**
	 * 注册一个ZK节点侦听器
	 * 
	 * @param connector
	 * @param node
	 */
	public static void registerOneNodeListener(ZkClusterConnector connector,
			ZkNode node) {
		// 注册节点数据变化侦听和孩子节点变化侦听
		String path = node.getPath();
		log.debug("subscribeDataChanges,	path=" + path);
		connector.subscribeDataChanges(path, connector.getZkDataListener());
		log.debug("subscribeChildChanges,	path=" + path);
		connector.subscribeChildChanges(path, connector.getZkChildListener());

		// TODO: 是否根据节点类型区别对待需要进一步考虑
		// ZkNodeVO zkNodeVO = node.getZkNodeVO();
		// if (zkNodeVO instanceof ZkBtsSagLinkVO
		// || zkNodeVO instanceof ZkBtsStatusVO
		// || zkNodeVO instanceof ZkSagNodeLockVO) {
		// // 对于如下节点，注册节点数据变化侦听器
		// // 1. BtsSagLink节点
		// // 2. 基站链路状态节点
		// // 3. Rules下的SAG锁临时节点
		// log.debug("subscribeDataChanges,	path=" + path);
		// connector.subscribeDataChanges(path, connector.getZkDataListener());
		// }
		// if (zkNodeVO instanceof ZkBtsSagLinkVO
		// || zkNodeVO instanceof ZkRulesVO
		// || zkNodeVO instanceof ZkGroupedBtsVO) {
		// // 对于如下节点，注册孩子节点变化侦听器
		// // 1. BtsSagLink节点
		// // 2. Rules节点
		// // 3. SAG GROUPEDBTS节点
		// log.debug("subscribeChildChanges,	path=" + path);
		// connector.subscribeChildChanges(path,
		// connector.getZkChildListener());
		// }
	}

	/**
	 * 删除一个ZK节点侦听器
	 * 
	 * @param connector
	 * @param node
	 */
	public static void deregisterOneNodeListener(ZkClusterConnector connector,
			ZkNode node) {
		String path = node.getPath();
		log.debug("unsubscribeDataChanges,	path=" + path);
		connector.unsubscribeDataChanges(path, connector.getZkDataListener());
		log.debug("unsubscribeChildChanges,	path=" + path);
		connector.unsubscribeChildChanges(path, connector.getZkChildListener());
	}
}
