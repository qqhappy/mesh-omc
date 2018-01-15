/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.xinwei.minas.zk.core.xnode.vo.ZkBtsStatusVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagNodeLockVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkSagVO;

/**
 * 
 * ZK节点助手
 * 
 * @author chenjunhua
 * 
 */

public class ZkNodeUtils {
	
	/**
	 * 在ZK树下查找指定SAGID的节点
	 * @param root
	 * @param sagId
	 * @return
	 */
	public static ZkNode findSagNodeBySagId(ZkNode root, Long sagId) {
		Iterator<ZkNode> itr = root.createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_SAG) {
				ZkSagVO zkSagVO = (ZkSagVO)node.getZkNodeVO();
				if (zkSagVO.getSagId().longValue() == sagId.longValue()) {
					return node;
				}
			}			
		}
		return null;
	}
	
	/**
	 * 在ZK树下查找指定节点类型的所有节点
	 * @param root
	 * @param nodeType
	 * @return
	 */
	public static List<ZkNode> findByNodeType(ZkNode root, int nodeType) {
		List<ZkNode> list = new LinkedList();
		Iterator<ZkNode> itr = root.createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			if (node.getNodeType() == nodeType) {
				list.add(node);
			}			
		}
		return list;
	}
	
	/**
	 * 查找活跃的SAG列表
	 * @param root
	 * @return
	 */
	public static List<ZkSagNodeLockVO> findActiveSagList(ZkNode root) {
		List<ZkSagNodeLockVO> list = new LinkedList();
		Iterator<ZkNode> itr = root.createLevelOrderIterator();		
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_RULES) {
				Set<ZkNode> children = node.getChildren();
				for (ZkNode zkNode : children) {
					ZkSagNodeLockVO zkSagNodeLockVO = (ZkSagNodeLockVO)zkNode.getZkNodeVO();
					list.add(zkSagNodeLockVO);
				}
			}			
		}
		return list;
	}
	
	
	/**
	 * 判断SAG是否活跃
	 * @param root
	 * @param sagId
	 * @return
	 */
	public static boolean isSagActive(ZkNode root, Long sagId) {
		List<ZkSagNodeLockVO> nodeVoList = findActiveSagList(root);
		for (ZkSagNodeLockVO vo : nodeVoList) {
			if (vo.getSagId() == sagId.longValue()) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 获取指定基站ID的状态
	 * @param root
	 * @param btsId
	 * @return
	 */
	public static ZkBtsStatusVO getBtsStatus(ZkNode root, Long btsId) {
		ZkBtsStatusVO zkBtsStatusVO = new ZkBtsStatusVO();
		Iterator<ZkNode> itr = root.createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			if (node.getNodeType() == ZkNodeConstant.NODE_TYPE_BTS) {
				ZkBtsVO zkBtsVO = (ZkBtsVO)node.getZkNodeVO();
				if (zkBtsVO.getBtsId().longValue() == btsId.longValue()) {
					ZkNode btsStatusNode = node.getFirstChild().getFirstChild();
					if (btsStatusNode != null) {
						return (ZkBtsStatusVO)btsStatusNode.getZkNodeVO();
					}
				}
			}			
		}
		return zkBtsStatusVO;
	}
	
	
	/**
	 * 比较RULES下子节点
	 * @param node1
	 * @param node2
	 * @return
	 */
	public static int compareZkRulesSagLockNode(ZkNode node1, ZkNode node2) {
		if (node1 != null && node2 != null) {
			String name1 = node1.getName();
			String name2 = node2.getName();
			name1 = name1.substring(name1.lastIndexOf("-")+1) ;
			name2 = name2.substring(name2.lastIndexOf("-")+1) ;
			return name1.compareToIgnoreCase(name2);
		}
		return 0;
	}
	
	
	/**
	 * 对节点进行排序
	 * @param zkNodes
	 * @return
	 */
	public static List<ZkNode> sort(Set<ZkNode> zkNodes) {
		List<ZkNode> list = new LinkedList();
		list.addAll(zkNodes);
		Collections.sort(list, new Comparator<ZkNode>(){
			@Override
			public int compare(ZkNode node1, ZkNode node2) {
				if (node1.getNodeType() == ZkNodeConstant.NODE_TYPE_SAGLINEUP) {
					// 比较RULES下子节点
					return compareZkRulesSagLockNode(node1, node2);
				}
				// 其余节点
				return node1.compareTo(node2);
			}
			
		});
		return list;
	}

}
