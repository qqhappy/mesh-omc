/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-7-16	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.EnbProperty;

/**
 * 
 * 场景展示参数
 * 
 * @author chenlong
 * 
 */

public class EnbSceneDataShow {
	
	// 场景展示数据
	private List<EnbProperty> sceneShow = new ArrayList<EnbProperty>();
	
	// 频段展示数据
	private List<EnbProperty> freqBandShow = new ArrayList<EnbProperty>();
	
	// 带宽展示数据
	private List<EnbProperty> bandwidthShow = new ArrayList<EnbProperty>();
	
	// 子帧配比展示数据
	private List<EnbProperty> sfCfgShow = new ArrayList<EnbProperty>();
	
	// 天线数展示数据
	private List<EnbProperty> anNumShow = new ArrayList<EnbProperty>();
	
	// RRU类型展示数据
	private List<EnbProperty> rruTypeShow = new ArrayList<EnbProperty>();
	
	public List<EnbProperty> getSceneShow() {
		return sceneShow;
	}
	
	public void setSceneShow(List<EnbProperty> sceneShow) {
		this.sceneShow = sceneShow;
	}
	
	public List<EnbProperty> getFreqBandShow() {
		return freqBandShow;
	}
	
	public void setFreqBandShow(List<EnbProperty> freqBandShow) {
		this.freqBandShow = freqBandShow;
	}
	
	public List<EnbProperty> getBandwidthShow() {
		return bandwidthShow;
	}
	
	public void setBandwidthShow(List<EnbProperty> bandwidthShow) {
		this.bandwidthShow = bandwidthShow;
	}
	
	public List<EnbProperty> getSfCfgShow() {
		return sfCfgShow;
	}
	
	public void setSfCfgShow(List<EnbProperty> sfCfgShow) {
		this.sfCfgShow = sfCfgShow;
	}
	
	public List<EnbProperty> getAnNumShow() {
		return anNumShow;
	}
	
	public void setAnNumShow(List<EnbProperty> anNumShow) {
		this.anNumShow = anNumShow;
	}
	
	public List<EnbProperty> getRruTypeShow() {
		return rruTypeShow;
	}
	
	public void setRruTypeShow(List<EnbProperty> rruTypeShow) {
		this.rruTypeShow = rruTypeShow;
	}
	
}
