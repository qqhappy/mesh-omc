/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * 
 * TConfWeakVoiceFault的包装类 加入了TConfFaultSwitch的值,但是没有加入它的idx和moId
 * 
 * @author tiance
 * 
 */

public class TConfWeakVoiceFaultWithSwitch extends TConfWeakVoiceFault
		implements java.io.Serializable, Listable {
	// 语音弱化开关
	private int switchFlag;

	public int getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(int switchFlag) {
		this.switchFlag = switchFlag;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.switchFlag", String
						.valueOf(this.getSwitchFlag())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.flag", String
						.valueOf(this.getFlag())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.voice_user_list_file",
				String.valueOf(this.getVoice_user_list_file())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.voice_user_list_file2",
				String.valueOf(this.getVoice_user_list_file2())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.trunk_list_file",
				String.valueOf(this.getTrunk_list_file())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.multi_call_idle_time",
				String.valueOf(this.getMulti_call_idle_time())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.voice_max_time", String
						.valueOf(this.getVoice_max_time())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.multi_call_max_time",
				String.valueOf(this.getMulti_call_max_time())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.delay_interval", String
						.valueOf(this.getDelay_interval())));
		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.division_code", this
						.getDivision_code()));

		allProperties.add(new FieldProperty(0,
				"listable.TConfWeakVoiceFaultWithSwitch.codeTable", ""));

		for (int i = 0; i < TConfDnInfos.size(); i++) {
			TConfDnInfo info = TConfDnInfos.get(i);
			String prefix = info.getDn_prefix();
			String len = String.valueOf(info.getDn_len());

			allProperties.add(new FieldProperty(1,
					"listable.TConfWeakVoiceFaultWithSwitch.codeTableTitle",
					(prefix.length() == 0 ? " " : prefix) + "/" + len));
		}

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "t_conf_fault_switch";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + switchFlag;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TConfWeakVoiceFaultWithSwitch other = (TConfWeakVoiceFaultWithSwitch) obj;
		if (switchFlag != other.switchFlag)
			return false;
		return true;
	}
	
	
	
	
}
