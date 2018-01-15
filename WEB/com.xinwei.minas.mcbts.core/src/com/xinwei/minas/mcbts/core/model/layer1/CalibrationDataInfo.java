/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * 
 * У׼��������ģ��
 * 
 * @author jiayi
 * 
 */

public class CalibrationDataInfo implements Serializable, Listable {

	// ��Ƶ��Ϣ
	private RFConfig rfConfig;

	// У׼������Ϣ
	private CalibrationGeneralConfig genConfig;

	// У׼���߲���
	private List<AntennaCalibrationConfig> antConfigList;

	public CalibrationDataInfo() {
	}

	/**
	 * @return the rfConfig
	 */
	public RFConfig getRfConfig() {
		return rfConfig;
	}

	/**
	 * @param rfConfig
	 *            the rfConfig to set
	 */
	public void setRfConfig(RFConfig rfConfig) {
		this.rfConfig = rfConfig;
	}

	/**
	 * @return the genConfig
	 */
	public CalibrationGeneralConfig getGenConfig() {
		return genConfig;
	}

	/**
	 * @param genConfig
	 *            the genConfig to set
	 */
	public void setGenConfig(CalibrationGeneralConfig genConfig) {
		this.genConfig = genConfig;
	}

	/**
	 * @return the antConfigList
	 */
	public List<AntennaCalibrationConfig> getAntConfigList() {
		return antConfigList;
	}

	/**
	 * @param antConfigList
	 *            the antConfigList to set
	 */
	public void setAntConfigList(List<AntennaCalibrationConfig> antConfigList) {
		this.antConfigList = antConfigList;
	}

	@Override
	public List<FieldProperty> listAll() {

		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		// ��Ƶ��Ϣ
		allProperties.add(new FieldProperty(0,
				"listable.CalibrationDataInfo.rfConfig", ""));
		allProperties.addAll(rfConfig.listAll());
		// У׼������Ϣ
		allProperties.add(new FieldProperty(0,
				"listable.CalibrationDataInfo.genConfig", ""));
		allProperties.addAll(genConfig.listAll());
		// У׼���߲���
		// allProperties.add(new FieldProperty(0,
		// "listable.CalibrationDataInfo.antConfigList", ""));

		Map<Integer, Map<Integer, List<AntennaCalibItem>>> antennaMap = new LinkedHashMap<Integer, Map<Integer, List<AntennaCalibItem>>>();
		Map<Integer, List<AntennaCalibItem>> antenna0 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna1 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna2 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna3 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna4 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna5 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna6 = new HashMap<Integer, List<AntennaCalibItem>>();
		Map<Integer, List<AntennaCalibItem>> antenna7 = new HashMap<Integer, List<AntennaCalibItem>>();

		antennaMap.put(0, antenna0);
		antennaMap.put(1, antenna1);
		antennaMap.put(2, antenna2);
		antennaMap.put(3, antenna3);
		antennaMap.put(4, antenna4);
		antennaMap.put(5, antenna5);
		antennaMap.put(6, antenna6);
		antennaMap.put(7, antenna7);

		if (antConfigList == null)
			return allProperties;

		for (AntennaCalibrationConfig config : antConfigList) {
			Integer antIdx = config.getAntennaIndex();
			Integer dataType = config.getDataType();
			List<AntennaCalibItem> antItemList = config.getAntItemList();

			switch (antIdx) {
			case 0:
				antenna0.put(dataType, antItemList);
				break;
			case 1:
				antenna1.put(dataType, antItemList);
				break;
			case 2:
				antenna2.put(dataType, antItemList);
				break;
			case 3:
				antenna3.put(dataType, antItemList);
				break;
			case 4:
				antenna4.put(dataType, antItemList);
				break;
			case 5:
				antenna5.put(dataType, antItemList);
				break;
			case 6:
				antenna6.put(dataType, antItemList);
				break;
			case 7:
				antenna7.put(dataType, antItemList);
				break;
			default:
				break;
			}
		}

		// for (Map.Entry<Integer, Map<Integer, List<AntennaCalibItem>>> antenna
		// : antennaMap
		// .entrySet()) {
		// List<FieldProperty> finalList = new ArrayList<FieldProperty>();
		//
		// // ͨ��antenna��key�õ�����1,����2....
		// Integer antennaIndex = antenna.getKey();
		// switch (antennaIndex) {
		// case 0:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna0",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 1:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna1",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 2:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna2",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 3:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna3",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 4:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna4",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 5:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna5",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 6:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna6",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// case 7:
		// finalList.add(new FieldProperty(1,
		// "listable.AntennaCalibrationConfig.antenna7",
		// "listable.AntennaCalibrationConfig.antennaType"));
		// break;
		// }
		//
		// // ͨ��antenna��value�õ�������Ϣ
		// Map<Integer, List<AntennaCalibItem>> antennaTypeMap = antenna
		// .getValue();

		// Map<Integer, String> antennaValue = new LinkedHashMap<Integer,
		// String>(); // Ҫ�浽���յ��б��һ��map��ʽ,��<1,
		// // TXCAL_I,TXCAL_Q,RXCAL_I,RXCAL_Q>
		// for (int i = 1; i < 5; i++) {
		// List<AntennaCalibItem> antennaTypeValue = antennaTypeMap.get(i); //
		// ԭʼ��antenna�����б�
		// if (antennaValue.size() == 0) {
		// for (int j = 0; j < antennaTypeValue.size(); j++) {
		// antennaValue.put(j, String.valueOf(antennaTypeValue
		// .get(j).getCarrierData()));
		// }
		// } else {
		// for (int j = 0; j < antennaTypeValue.size(); j++) {
		// String value = antennaValue.get(j);
		// value = value + "/"
		// + antennaTypeValue.get(j).getCarrierData();
		// antennaValue.put(j, value);
		// }
		// }
		// }

		// ��map��ʽ������,�����б�
		// for (int k = 0; k < antennaValue.size(); k++) {
		// finalList.add(new FieldProperty(2, String.valueOf(k),
		// antennaValue.get(k)));
		// }
		//
		// allProperties.addAll(finalList);
		// }

		// TODO
		return allProperties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((genConfig == null) ? 0 : genConfig.hashCode());
		result = prime * result
				+ ((rfConfig == null) ? 0 : rfConfig.hashCode());
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
		CalibrationDataInfo other = (CalibrationDataInfo) obj;
		if (genConfig == null) {
			if (other.genConfig != null)
				return false;
		} else if (!genConfig.equals(other.genConfig))
			return false;
		if (rfConfig == null) {
			if (other.rfConfig != null)
				return false;
		} else if (!rfConfig.equals(other.rfConfig))
			return false;
		return true;
	}

	@Override
	public String getBizName() {
		return "mcbts_calibdata";
	}
}
