/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * ZK�ڵ�ģ��
 * 
 * @author chenjunhua
 * 
 */

@SuppressWarnings("serial")
public class ZkNode implements Serializable, Cloneable, Comparable<ZkNode> {

	// �ڵ�����·��
	private String path;

	// �ڵ�����Header
	private ZkNodeHeader header;

	// �ڵ�����ʵ��
	private ZkNodeVO zkNodeVO;

	// �ڵ�ͳ����Ϣ
	private ZkNodeStat stat;

	// ���ӽڵ��б�
	private Set<ZkNode> children = new ConcurrentSkipListSet<ZkNode>();

	// ���ڵ�
	private ZkNode parent = null;

	public ZkNode(String path) {
		this.setPath(path);
	}

	/**
	 * ��������α���������
	 * 
	 * @return
	 */
	public Iterator<ZkNode> createLevelOrderIterator() {
		return new LevelOrderIterator(this);
	}

	/**
	 * ����·�����ҽڵ�
	 * 
	 * @param path
	 * @return
	 */
	public ZkNode findByPath(String path) {
		Iterator<ZkNode> itr = createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			if (node.getPath().equals(path)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * ��ȡ�ڵ����ڲ㼶
	 * 
	 * @return
	 */
	public int getLayer() {
		String[] names = path.split("/");
		if (names == null) {
			return 1;
		}
		return names.length - 1;
	}

	/**
	 * ��ȡҶ�ӽڵ����Ŀ
	 * 
	 * @return
	 */
	public int getLeafNum() {
		int leafNum = 0;
		Iterator<ZkNode> itr = createLevelOrderIterator();
		while (itr.hasNext()) {
			ZkNode node = itr.next();
			if (node.isLeaf()) {
				leafNum++;
			}
		}
		return leafNum;
	}

	/**
	 * ��ȡָ���ڵ�����ֵ�
	 * 
	 * @param node
	 * @return
	 */
	public List<ZkNode> getLeftBrothers(ZkNode node) {
		List<ZkNode> leftBrothers = new LinkedList<ZkNode>();
		for (ZkNode child : children) {
			if (!child.equals(node)) {
				leftBrothers.add(child);
			} else {
				break;
			}
		}
		return leftBrothers;
	}

	/**
	 * ��ĳ�ڵ���뵽ZK����
	 * 
	 * @param zkNode
	 */
	public boolean addNodeToTree(ZkNode zkNode) {
		ZkNode parent = this.findByPath(zkNode.getParentPath());
		if (parent != null) {
			return parent.addChild(zkNode);
		}
		return false;
	}

	/**
	 * ���ڵ�������Ƴ�
	 * 
	 * @param zkNode
	 */
	public boolean removeNodeFromTree(ZkNode zkNode) {
		ZkNode parent = this.findByPath(zkNode.getParentPath());
		if (parent != null) {
			return parent.removeChild(zkNode);
		}
		return false;
	}

	/**
	 * ��ȡ�ڵ�����
	 * 
	 * @return
	 */
	public int getNodeType() {
		if (header != null) {
			return header.getNodeType();
		}
		return -1;
	}

	public String getParentPath() {
		return path.substring(0, path.lastIndexOf("/"));
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		int index = path.lastIndexOf("/");
		if (index >= 0) {
			return path.substring(index + 1);
		}
		return "";
	}

	public ZkNodeHeader getHeader() {
		return header;
	}

	public void setHeader(ZkNodeHeader header) {
		this.header = header;
	}

	public ZkNodeVO getZkNodeVO() {
		return zkNodeVO;
	}

	public void setZkNodeVO(ZkNodeVO zkNodeVO) {
		this.zkNodeVO = zkNodeVO;
	}

	public int getChildrenNum() {
		return children.size();
	}

	public boolean isLeaf() {
		return getChildrenNum() == 0;
	}

	public List<ZkNode> getTreeNodeList() {
		List<ZkNode> nodeList = new ArrayList<ZkNode>();
		Iterator<ZkNode> iterator = createLevelOrderIterator();
		while (iterator.hasNext()) {
			nodeList.add(iterator.next());
		}
		return nodeList;
	}

	public ZkNode getFirstChild() {
		ZkNode[] zkNodes = (ZkNode[]) children.toArray(new ZkNode[0]);
		if (zkNodes != null && zkNodes.length != 0) {
			return zkNodes[0];
		}
		return null;
	}

	public Set<ZkNode> getChildren() {
		return children;
	}

	public void setChildren(Set<ZkNode> children) {
		this.children = children;
	}

	public boolean addChild(ZkNode child) {
		boolean isAdded = children.add(child);
		if (isAdded) {
			child.setParent(this);
		}
		return isAdded;
	}

	public boolean removeChild(ZkNode child) {
		child.setParent(null);
		return children.remove(child);
	}

	public ZkNode getParent() {
		return parent;
	}

	public void setParent(ZkNode parent) {
		this.parent = parent;
	}

	public ZkNodeStat getStat() {
		return stat;
	}

	public void setStat(ZkNodeStat stat) {
		this.stat = stat;
	}

	@Override
	public String toString() {
		return "ZkNode [path=" + path + ", header=" + header + ", zkNodeVO="
				+ zkNodeVO + ", childNum=" + getChildrenNum() + "]";
	}

	public byte[] encode() {
		byte[] buf = new byte[5120];
		int offset = 0;
		System.arraycopy(this.header.encode(), 0, buf, offset, ZkNodeHeader.LEN);
		offset += ZkNodeHeader.LEN;
		byte[] voBuf = this.zkNodeVO.encode();
		System.arraycopy(voBuf, 0, buf, offset, voBuf.length);
		offset += voBuf.length;
		// copy��ʵ���ֽڳ��ȵĻ���
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	public void decode(byte[] buf, int offset) {
		this.header.decode(buf, offset);
		offset += ZkNodeHeader.LEN;
		this.zkNodeVO.decode(buf, offset);
	}

	@Override
	public ZkNode clone() {
		try {
			return (ZkNode) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZkNode other = (ZkNode) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (!(obj instanceof ZkNode))
	// return false;
	// ZkNode other = (ZkNode) obj;
	// if (children == null) {
	// if (other.children != null)
	// return false;
	// } else if (!children.equals(other.children))
	// return false;
	// if (parent == null) {
	// if (other.parent != null)
	// return false;
	// } else if (!parent.equals(other.parent))
	// return false;
	// if (path == null) {
	// if (other.path != null)
	// return false;
	// } else if (!path.equals(other.path))
	// return false;
	// if (stat == null) {
	// if (other.stat != null)
	// return false;
	// } else if (!stat.equals(other.stat))
	// return false;
	// if (zkNodeVO == null) {
	// if (other.zkNodeVO != null)
	// return false;
	// } else if (!zkNodeVO.equals(other.zkNodeVO))
	// return false;
	// return true;
	// }

	public void trace() {
		Iterator<ZkNode> itr = createLevelOrderIterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}

	class LevelOrderIterator implements Iterator<ZkNode> {

		private BlockingQueue<ZkNode> queue = new LinkedBlockingQueue<ZkNode>();

		public LevelOrderIterator(ZkNode root) {
			try {
				queue.offer(root);
			} catch (Exception e) {
			}
		}

		public boolean hasNext() {
			return !queue.isEmpty();
		}

		public ZkNode next() {
			try {
				ZkNode node = queue.take();
				Set<ZkNode> children = node.getChildren();
				List<ZkNode> list = ZkNodeUtils.sort(children);
				for (ZkNode child : list) {
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

	@Override
	public int compareTo(ZkNode node) {
		return getName().compareToIgnoreCase(node.getName());
	}
}
