/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-6	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.tree;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 树节点信息模型
 * 
 * @author chenjunhua
 * 
 */

public interface ITreeNode<T extends Serializable> extends Serializable {

	/**
	 * 获取父节点
	 * 
	 * @return 父节点
	 */
	public ITreeNode<T> getParent();

	/**
	 * 设置父节点
	 * 
	 * @param parent
	 */
	public void setParent(ITreeNode<T> parent);

	/**
	 * 获取子孙节点
	 * 
	 * @return
	 */
	public List<ITreeNode<T>> getChildren();

	/**
	 * 增加孩子节点
	 * 
	 * @param child
	 */
	public void addChild(ITreeNode<T> child);

	/**
	 * 删除孩子节点
	 * 
	 * @param child
	 */
	public void removeChild(ITreeNode<T> child);

	/**
	 * 获取节点信息模型
	 * 
	 * @return
	 */
	public T getNodeInfo();

	/**
	 * 设置节点信息模型
	 * 
	 * @param nodeInfo
	 */
	public void setNodeInfo(T nodeInfo);

	/**
	 * 创建按层级遍历的迭代器
	 * 
	 * @return
	 */
	public Iterator<ITreeNode<T>> createLevelOrderIterator();

}
