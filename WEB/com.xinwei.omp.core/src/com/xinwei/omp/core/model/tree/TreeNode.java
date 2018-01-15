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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * 树形节点模型
 * 
 * @author chenjunhua 
 * 
 */

public class TreeNode<T extends Serializable> implements ITreeNode<T> {

	/**
	 * 节点信息
	 */
	private T nodeInfo;

	/**
	 * 父节点
	 */
	private ITreeNode<T> parent;

	/**
	 * 子孙节点
	 */
	private List<ITreeNode<T>> children = new ArrayList();
	
	public TreeNode(T nodeInfo) {
		this.setNodeInfo(nodeInfo);
	}

	public ITreeNode<T> getParent() {
		return parent;
	}
	
	public void setParent(ITreeNode<T> parent) {
		this.parent = parent;
	}

	public List<ITreeNode<T>> getChildren() {
		return children;
	}

	public void addChild(ITreeNode<T> child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(ITreeNode<T> child) {
		child.setParent(null);
		children.remove(child);		
	}

	public T getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(T nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	
	public Iterator<ITreeNode<T>> createLevelOrderIterator() {
		return new LevelOrderIterator(this);
	}

	/**
	 * 按层次遍历迭代器
	 * @author chenjunhua
	 *
	 */
	class LevelOrderIterator implements Iterator<ITreeNode<T>> {
	    
		private ITreeNode<T> root;
		
		private BlockingQueue<ITreeNode<T>> queue = new LinkedBlockingQueue();
		
		public LevelOrderIterator(ITreeNode<T> root) {
			this.root = root;
			List<ITreeNode<T>> children = root.getChildren();
			for (ITreeNode<T> child : children) {
				try {
					queue.offer(child);
				} catch (Exception e) {
				}
			}
		}
		
	    public boolean hasNext() {
	    	return !queue.isEmpty();
	    }

	    public ITreeNode<T> next() {
	    	try {
	    		ITreeNode<T> node = queue.take();
	    		List<ITreeNode<T>> children = node.getChildren();
				for (ITreeNode<T> child : children) {
					try {
						queue.offer(child);
					} catch (Exception e) {
					}
				}
	    		return node;
			} catch (InterruptedException e) {
				return null;
			}
	    }

	    public void remove() {
	    	
	    }
	}
	
}
