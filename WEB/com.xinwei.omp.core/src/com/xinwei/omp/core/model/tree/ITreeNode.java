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
 * ���ڵ���Ϣģ��
 * 
 * @author chenjunhua
 * 
 */

public interface ITreeNode<T extends Serializable> extends Serializable {

	/**
	 * ��ȡ���ڵ�
	 * 
	 * @return ���ڵ�
	 */
	public ITreeNode<T> getParent();

	/**
	 * ���ø��ڵ�
	 * 
	 * @param parent
	 */
	public void setParent(ITreeNode<T> parent);

	/**
	 * ��ȡ����ڵ�
	 * 
	 * @return
	 */
	public List<ITreeNode<T>> getChildren();

	/**
	 * ���Ӻ��ӽڵ�
	 * 
	 * @param child
	 */
	public void addChild(ITreeNode<T> child);

	/**
	 * ɾ�����ӽڵ�
	 * 
	 * @param child
	 */
	public void removeChild(ITreeNode<T> child);

	/**
	 * ��ȡ�ڵ���Ϣģ��
	 * 
	 * @return
	 */
	public T getNodeInfo();

	/**
	 * ���ýڵ���Ϣģ��
	 * 
	 * @param nodeInfo
	 */
	public void setNodeInfo(T nodeInfo);

	/**
	 * �������㼶�����ĵ�����
	 * 
	 * @return
	 */
	public Iterator<ITreeNode<T>> createLevelOrderIterator();

}
