/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-30	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.micro.microapi;

/**
 * 
 * eNodeB��������ģ��
 * 
 * @author chenjunhua
 * 
 */

public class MicroEnbSceneInput {

	// 0: McLTE���վ
	public static final int XW7400 = 0;
	// 200��McLTEһ�廯��վ(McLTE΢վ)
	public static final int XW7102 = 200;

	// ��������
	public static final int SCENE_1 = 1;
	// ��������
	public static final int SCENE_2 = 2;
	// ��Զ����
	public static final int SCENE_3 = 3;
	// ��������
	public static final int SCENE_4 = 4;
	// ��������
	public static final int SCENE_5 = 5;

	// 38Ƶ��
	public static final int freqBandId38 = 1;
	// 39Ƶ��
	public static final int freqBandId39 = 2;
	// 40Ƶ��
	public static final int freqBandId40 = 3;
	// 53Ƶ��
	public static final int freqBandId53 = 4;
	// 58Ƶ��
	public static final int freqBandId58 = 5;
	// 61Ƶ��
	public static final int freqBandId61 = 6;
	// 62Ƶ��
	public static final int freqBandId62 = 7;
	// 63Ƶ��
	public static final int freqBandId63 = 8;

	// ����5M
	public static final int bandwidth5M = 1;
	// ����10M
	public static final int bandwidth10M = 2;
	// ����15M
	public static final int bandwidth15M = 3;
	// ����20M
	public static final int bandwidth20M = 4;

	// ���0
	public static final int sfCfg0 = 1;
	// ���1
	public static final int sfCfg1 = 2;
	// ���2
	public static final int sfCfg2 = 3;

	// RRU8A10
	public static final int RRU8A10 = 1;
	// RRU8B10
	public static final int RRU8B10 = 2;
	// RRU8C10
	public static final int RRU8C10 = 3;
	// RRU8D10
	public static final int RRU8D10 = 4;
	// RRU8E10
	public static final int RRU8E10 = 5;
	// RRU8F10
	public static final int RRU8F10 = 6;
	// RRU8G10
	public static final int RRU8G10 = 7;
	// RRU8H10
	public static final int RRU8H10 = 8;
	// RRU8I10
	public static final int RRU8I10 = 9;
	// RRU8J10
	public static final int RRU8J10 = 10;
	// RRU8K10
	public static final int RRU8K10 = 11;
	// RRU4B10_FF
	public static final int RRU4B10_FF = 12;
	// RRU4B30_CF
	public static final int RRU4B30_CF = 13;
	// RRU4D30_CF
	public static final int RRU4D30_CF = 14;
	// RRU4E10_FF
	public static final int RRU4E10_FF = 15;
	// RRU4E30_CF
	public static final int RRU4E30_CF = 16;
	// RRU2J30_CC
	public static final int RRU2J30_CC = 17;
	// RRU4K30_CF
	public static final int RRU4K30_CF = 18;
	// RRU4J30_CF
	public static final int RRU4J30_CF = 19;

	// 1����
	public static final int anNum1 = 1;
	// 2����
	public static final int anNum2 = 2;
	// 4����
	public static final int anNum4 = 3;
	// 8����
	public static final int anNum8 = 4;

	// eNodeB���ͱ��
	private int enbTypeId;

	// �������
	private int sceneId;

	// Ƶ�α��
	private int freqBandId;

	// ������
	private int bandwidthId;
	// ��֡���
	private int sfCfgId;
	// ������
	private int AnNumId;
	// RRU���ͱ��
	private int rruTypeId;

	public int getEnbTypeId() {
		return enbTypeId;
	}

	public void setEnbTypeId(int enbTypeId) {
		this.enbTypeId = enbTypeId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getFreqBandId() {
		return freqBandId;
	}

	public void setFreqBandId(int freqBandId) {
		this.freqBandId = freqBandId;
	}

	public int getBandwidthId() {
		return bandwidthId;
	}

	public void setBandwidthId(int bandwidthId) {
		this.bandwidthId = bandwidthId;
	}

	public int getSfCfgId() {
		return sfCfgId;
	}

	public void setSfCfgId(int sfCfgId) {
		this.sfCfgId = sfCfgId;
	}

	public int getRruTypeId() {
		return rruTypeId;
	}

	public void setRruTypeId(int rruTypeId) {
		this.rruTypeId = rruTypeId;
	}

	public int getAnNumId() {
		return AnNumId;
	}

	public void setAnMumId(int AnNumId) {
		this.AnNumId = AnNumId;
	}
}
