/*
 * Created on 2005-12-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xinwei.minas.mcbts.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.xinwei.minas.mcbts.core.model.McBtsFreqType;

/**
 * @author user
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FreqConvertUtil {

	private double typeStartFreq = 1700;

	private double startValue = 1785; // m 2300

	private double bandWidth = 20; // m 400

	private double baseBandWidth = 5; // 单位m

	private double step = 0.05; // m

	private int freqType = 0;

	private String typeName = "1.8G";

	private static ResourceBundle prop;
	static {
		try {
			prop = ResourceBundle
					.getBundle("com.xinwei.minas.mcbts.core.utils.btsFreq");
		} catch (Exception ex) {
		}
	}

	public int getFreqType() {
		return freqType;
	}

	public void setFreqType(int freqType) {
		this.freqType = freqType;
		initValue();
	}

	public double getStartFreqValue(int value) {
		return typeStartFreq + value * step - baseBandWidth / 2;
	}

	public double getMidFreqValue(int value) {
		return typeStartFreq + value * step;
	}

	public double getLastFreqValue(int value) {
		return typeStartFreq + value * step + baseBandWidth / 2;
	}

	public int getStartFreqOffset(double value) {
		// +0.5 是为了进行四舍五入
		return (int) ((value - typeStartFreq) / step + 0.5);
	}

	public int getMiddleFreqOffset(double value) {
		// +0.5 是为了进行四舍五入
		return (int) ((value - typeStartFreq) / step + 0.5);
	}

	public int getLastFreqOffset(double value) {
		return (int) ((value - typeStartFreq) / step + 0.5);
	}

	public int getStartOffsetByMidOffset(long midOffset) {
		return (int) (midOffset - baseBandWidth / step / 2);
	}

	public int getMaxOffset() {
		return (int) ((startValue + bandWidth - typeStartFreq - baseBandWidth / 2) / step);
	}

	public int getMinOffset() {
		return (int) ((startValue - typeStartFreq + baseBandWidth / 2.0) / step);
	}

	public static List<Integer> getSystemSupportedFreqTypes() {
		List<Integer> freqTypes = new ArrayList<Integer>();
		Set<String> keySet = prop.keySet();
		for (String key : keySet) {
			if (key.startsWith("freqname_")) {
				int type = Integer
						.parseInt(key.substring("freqname_".length()));
				freqTypes.add(type);
			}
		}

		Collections.sort(freqTypes);
		return freqTypes;
	}

	public static List<McBtsFreqType> getSystemSupportedFreqMap() {
		List<McBtsFreqType> mapList = new ArrayList<McBtsFreqType>();
		List<Integer> freqTypes = new ArrayList<Integer>();
		Set<String> keySet = prop.keySet();
		for (String key : keySet) {
			if (key.startsWith("freqname_")) {
				McBtsFreqType map = new McBtsFreqType();
				int type = Integer
						.parseInt(key.substring("freqname_".length()));
				freqTypes.add(type);
				String value = prop.getString(key);
				map.setFreqType(type);
				map.setFreqName(value);
				mapList.add(map);
			}
		}

		Collections.sort(mapList);

		return mapList;
	}

	private void initValue() {
		try {
			typeStartFreq = Double.parseDouble(prop.getString("typestartfreq_"
					+ freqType));
			startValue = Double.parseDouble(prop.getString("startfreq_"
					+ freqType));
			bandWidth = Double.parseDouble(prop.getString("bandwidth_"
					+ freqType));
			baseBandWidth = Double.parseDouble(prop.getString("baseBandWidth_"
					+ freqType));
			step = Double.parseDouble(prop.getString("step_" + freqType));
			typeName = prop.getString("freqname_" + freqType);
		} catch (Exception ex) {
			typeStartFreq = 1700;
			startValue = 1785; // m 2300
			bandWidth = 20; // m 400
			baseBandWidth = 5; // 单位m
			step = 0.05;// m
			typeName = "1.8G";
		}
	}

	public static void main(String[] args) {
		List<Integer> allFreqTypes = FreqConvertUtil
				.getSystemSupportedFreqTypes();
		FreqConvertUtil util = new FreqConvertUtil();

		for (Integer freqType : allFreqTypes) {
			util.setFreqType(freqType);
			System.out.println("type " + freqType + " =" + util.getTypeName());
			System.out.println("min offset=" + util.getMinOffset());
			System.out.println("max offset=" + util.getMaxOffset());
			System.out.println("min value="
					+ util.getMidFreqValue(util.getMinOffset()));
			System.out.println("max value="
					+ util.getMidFreqValue(util.getMaxOffset()));
		}
	}

	public double getTypeStartFreq() {
		return typeStartFreq;
	}

	public void setTypeStartFreq(double typeStartFreq) {
		this.typeStartFreq = typeStartFreq;
	}

	/**
	 * @return Returns the bandWidth.
	 */
	public double getBandWidth() {
		return bandWidth;
	}

	/**
	 * @param bandWidth
	 *            The bandWidth to set.
	 */
	public void setBandWidth(double bandWidth) {
		this.bandWidth = bandWidth;
	}

	/**
	 * @return Returns the baseBandWidth.
	 */
	public double getBaseBandWidth() {
		return baseBandWidth;
	}

	/**
	 * @param baseBandWidth
	 *            The baseBandWidth to set.
	 */
	public void setBaseBandWidth(double baseBandWidth) {
		this.baseBandWidth = baseBandWidth;
	}

	/**
	 * @return Returns the startValue.
	 */
	public double getStartValue() {
		return startValue;
	}

	/**
	 * @param startValue
	 *            The startValue to set.
	 */
	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}

	/**
	 * @return Returns the step.
	 */
	public double getStep() {
		return step;
	}

	/**
	 * @param step
	 *            The step to set.
	 */
	public void setStep(double step) {
		this.step = step;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
