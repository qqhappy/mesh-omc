/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-20	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public class NingBoPowerSupplyMessagePaserHelpler {
	public static final int HAVEWARN = 1;
	public static final int HAVENOTWARN = 0;

	public static final NingBoPowerSupplyMessagePaserHelpler instance = new NingBoPowerSupplyMessagePaserHelpler();
	
	private NingBoPowerSupplyMessagePaserHelpler() {
		
	}
	
	public static NingBoPowerSupplyMessagePaserHelpler getInstance() {
		return instance;
	}
	
	public boolean parse(NingBoPowerSupplyMessage msg, StringBuffer strReturn)
			throws Exception {
		NingBoPowerSupplyMessage powerMsg = (NingBoPowerSupplyMessage) msg;

		if (powerMsg.getCid2() != 0) {
			// 返回错误。
//			System.out.println(this.parseResultByCID2(powerMsg));
//			return false;
			throw new Exception(this.parseResultByCID2(powerMsg));
		}

		// 电源告警信息
		StringBuffer warnBuffer = new StringBuffer();

		byte[] info = powerMsg.getInfo();

		int num = 0;
		int index = 0;
		int warnStatus = HAVENOTWARN;

		switch (powerMsg.getCid1()) {
		// 交流，告警数据。
		case 0x40:
			strReturn.setLength(0);

			strReturn.append(OmpAppContext
					.getMessage("alternating_current.alarm_a_phase_voltage"));
			num = (info[index++] & 0xff) * 256 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);

			strReturn.append("\r\n");
			strReturn.append(OmpAppContext
					.getMessage("alternating_current.alarm_a_phase_current"));
			num = (info[index++] & 0xff) * 256 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);

			num = info[index++] & 0xff;
			strReturn.append("\r\n");

			if ((num & 0xff) == 0) {
				strReturn
						.append(OmpAppContext
								.getMessage("alternating_current.alarm_a_phase_no_alarm"));
			} else {
				warnStatus = HAVEWARN;
				warnBuffer.append(getJiaoLiuWarnStr(num, "A"));
			}

			strReturn.append("\r\n");
			strReturn.append(OmpAppContext
					.getMessage("alternating_current.alarm_b_phase_voltage"));
			num = (info[index++] & 0xff) * 256 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);

			strReturn.append(num);
			strReturn.append("\r\n");
			strReturn.append(OmpAppContext
					.getMessage("alternating_current.alarm_b_phase_current"));
			num = (info[index++] & 0xff) * 256 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);

			num = info[index++] & 0xff;
			strReturn.append("\r\n");

			if ((num & 0xff) == 0) {
				strReturn
						.append(OmpAppContext
								.getMessage("alternating_current.alarm_b_phase_no_alarm"));
			} else {
				warnStatus = HAVEWARN;
				warnBuffer.append(getJiaoLiuWarnStr(num, "B"));
			}

			strReturn.append("\r\n");
			strReturn.append(OmpAppContext
					.getMessage("alternating_current.alarm_c_phase_voltage"));
			num = (info[index++] & 0xff) * 256 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);

			strReturn.append(num);
			strReturn.append("\r\n");
			strReturn.append(OmpAppContext
					.getMessage("alternating_current.alarm_c_phase_current"));
			num = (info[index++] & 0xff) * 256 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);

			num = info[index++] & 0xff;
			strReturn.append("\r\n");

			if ((num & 0xff) == 0) {
				strReturn
						.append(OmpAppContext
								.getMessage("alternating_current.alarm_c_phase_no_alarm"));
			} else {
				warnStatus = HAVEWARN;
				warnBuffer.append(getJiaoLiuWarnStr(num, "C"));
			}
			break;
		// 整流，告警信息。
		case 0x41:
			index = 0;
			strReturn.setLength(0);

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_address"));
			num = info[index++] & 0xff;
			strReturn.append(num);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_output_voltage"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_output_current"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_fan_speed"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_temperature"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_alarm_info"));
			strReturn.append("\r\n");
			num = info[index++] & 0xff;

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_limitthe_current_flag"));
			strReturn.append(((num & 0x01) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_power_normal") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_power_alarm")
							+ "\r\n");
			if ((num & 0x01) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer
						.append(OmpAppContext
								.getMessage("rectification.alarm_limitthe_current_flag")
								+ OmpAppContext
										.getMessage("rectification.alarm_power_alarm")
								+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_failure"));
			strReturn.append(((num & 0x02) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_power_normal") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_power_alarm")
							+ "\r\n");
			if ((num & 0x02) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append(OmpAppContext
						.getMessage("rectification.alarm_module_failure")
						+ OmpAppContext
								.getMessage("rectification.alarm_power_alarm")
						+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_switch"));
			strReturn.append(((num & 0x04) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_switch_on") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_switch_off")
							+ "\r\n");
			if ((num & 0x04) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append(OmpAppContext
						.getMessage("rectification.alarm_module_switch")
						+ OmpAppContext
								.getMessage("rectification.alarm_switch_off")
						+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_sleep"));
			strReturn.append(((num & 0x08) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_power_normal") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_power_sleep")
							+ "\r\n");
			if ((num & 0x08) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append(OmpAppContext
						.getMessage("rectification.alarm_module_sleep")
						+ OmpAppContext
								.getMessage("rectification.alarm_power_sleep")
						+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_fan_failure"));
			strReturn.append(((num & 0x10) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_power_normal") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_power_alarm")
							+ "\r\n");
			if ((num & 0x10) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append(OmpAppContext
						.getMessage("rectification.alarm_module_fan_failure")
						+ OmpAppContext
								.getMessage("rectification.alarm_power_alarm")
						+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_current_failure"));
			strReturn.append(((num & 0x20) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_power_normal") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_power_alarm")
							+ "\r\n");
			if ((num & 0x20) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append(OmpAppContext
						.getMessage("rectification.alarm_current_failure")
						+ OmpAppContext
								.getMessage("rectification.alarm_power_alarm")
						+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_protection"));
			strReturn.append(((num & 0x40) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_power_normal") + "\r\n"
					: OmpAppContext
							.getMessage("rectification.alarm_power_alarm")
							+ "\r\n");
			if ((num & 0x40) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append(OmpAppContext
						.getMessage("rectification.alarm_module_protection")
						+ OmpAppContext
								.getMessage("rectification.alarm_power_alarm")
						+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_in_position"));
			strReturn.append(((num & 0x80) == 0) ? OmpAppContext
					.getMessage("rectification.alarm_module_in_position")
					+ "\r\n" : OmpAppContext
					.getMessage("rectification.alarm_module_out_position")
					+ "\r\n");
			strReturn.append("\r\n");
			if ((num & 0x40) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer
						.append(OmpAppContext
								.getMessage("rectification.alarm_module_in_position")
								+ OmpAppContext
										.getMessage("rectification.alarm_module_out_position")
								+ "\r\n");
			}

			strReturn.append(OmpAppContext
					.getMessage("rectification.alarm_module_protection_type"));
			num = info[index++] & 0xff;
			strReturn.append(getZhengLiuWarnStr(num));
			if ((num & 0xff) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer
						.append(OmpAppContext
								.getMessage("rectification.alarm_module_protection_type")
								+ getZhengLiuWarnStr(num) + "\r\n");
			}
			break;
		// 直流，告警数据
		case 0x42:
			index = 0;
			strReturn.setLength(0);

			strReturn.append(OmpAppContext
					.getMessage("direct_current.alarm_busbar_voltage"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			// ESS_LOGGER.info("voltagelow voltage = " + num/100);

			if ((float) num / 100 <= 48 && (float) num / 100 != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append("busbar voltagelow voltage alarm "
						+ (float) num / 100 + "V");
			}

			strReturn.append(OmpAppContext
					.getMessage("direct_current.alarm_battery_current"));
			num = (info[index++] & 0x0f) * 0x0100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("direct_current.alarm_load_current"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 100);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("direct_current.alarm_battery_temperature"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);
			strReturn.append("\r\n");

			strReturn
					.append(OmpAppContext
							.getMessage("direct_current.alarm_environment_temperature"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);
			strReturn.append("\r\n");

			strReturn
					.append(OmpAppContext
							.getMessage("direct_current.alarm_rectifier_module_number"));
			num = info[index++] & 0xff;
			strReturn.append(num);
			strReturn.append("\r\n");

			strReturn.append(OmpAppContext
					.getMessage("direct_current.alarm_environment_humidity"));
			num = (info[index++] & 0xff) * 0x100 + (info[index++] & 0xff);
			strReturn.append((float) num / 10);
			strReturn.append("\r\n");

			num = info[index++] & 0xff;
			strReturn.append(getZhiLiuWarnStr(num));
			strReturn.append("\r\n");
			if ((num & 0xff) != 0) {
				// warnStatus = HAVEWARN;
				// warnBuffer.append(getZhiLiuWarnStr(num));
			}

			num = info[index++] & 0xff;
			strReturn.append(getZhiLiuWarnStr1(num));
			strReturn.append("\r\n");
			if ((num & 0xff) != 0) {
				// warnStatus = HAVEWARN;
				// warnBuffer.append(getZhiLiuWarnStr1(num));
			}

			num = info[index++] & 0xff;
			strReturn.append(getZhiLiuWarnStr2(num));
			strReturn.append("\r\n");
			if ((num & 0xff) != 0) {
				// warnStatus = HAVEWARN;
				// warnBuffer.append(getZhiLiuWarnStr2(num));
			}

			num = info[index++] & 0xff;
			strReturn.append(getZhiLiuWarnStr3(num));
			strReturn.append("\r\n");
			// if((num & 0xff) != 0) {
			// warnStatus = HAVEWARN;
			// warnBuffer.append(getZhiLiuWarnStr3(num));
			// }

			num = info[index++] & 0xff;
			strReturn.append(getZhiLiuWarnStr4(num));
			strReturn.append("\r\n");
			// if((num & 0xff) != 0) {
			// 烟雾,水侵,防雷告警屏蔽
			// warnStatus = HAVEWARN;
			// warnBuffer.append(getZhiLiuWarnStr4(num));
			// }
			// ESS_LOGGER.info("getZhiLiuWarnStr4(num) : " +
			// getZhiLiuWarnStr4(num));
			// ESS_LOGGER.info("access_alarm flag = " + num);
			if (((num & 0xff) & 0x04) != 0) {
				warnStatus = HAVEWARN;
				warnBuffer.append("open door alarm" + "\r\n");
			}

			strReturn.append("\r\n");
			break;
		}
		
		if (warnStatus == HAVEWARN) {
			strReturn.replace(0, strReturn.length(), warnBuffer.toString());
			return false;
		}
		return true;
	}

	/**
	 * 解析标准电源返回消息CID2字段含义。
	 * 
	 * @return String
	 */
	protected String parseResultByCID2(NingBoPowerSupplyMessage powerMsg) {
		int result = powerMsg.getCid2() & 0xff;
		String ret = "";
		switch (result) {
		case 0x00:
			ret = OmpAppContext.getMessage("rectification.alarm_power_normal");
			break;
		case 0x01:
			ret = OmpAppContext.getMessage("current_error.ver_fault");
			break;
		case 0x02:
			ret = OmpAppContext.getMessage("current_error.chksum_fault");
			break;
		case 0x03:
			ret = OmpAppContext.getMessage("current_error.lchsum_fault");
			break;
		case 0x04:
			ret = OmpAppContext
					.getMessage("current_error.cid2_invalidation_fault");
			break;
		case 0x05:
			ret = OmpAppContext.getMessage("current_error.order_format_fault");
			break;
		case 0x06:
			ret = OmpAppContext
					.getMessage("current_error.invalid_format_fault");
			break;
		case 0xE0:
			ret = OmpAppContext
					.getMessage("current_error.order_process_failure");
			break;
		default:
			ret = OmpAppContext.getMessage("current_error.default_fault");
		}
		return ret;
	}

	/**
	 * 获取交流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getJiaoLiuWarnStr(int parstatus, String flag) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(flag
						+ OmpAppContext
								.getMessage("current_error.voltage_phase_loss_alarm")
						+ "\r\n");
				break;
			case 0x02:
				buffer.append(flag
						+ OmpAppContext
								.getMessage("current_error.low_voltage_alarm")
						+ "\r\n");
				break;
			case 0x04:
				buffer.append(flag
						+ OmpAppContext
								.getMessage("current_error.high_voltage_alarm")
						+ "\r\n");
				break;
			case 0x10:
				buffer.append(flag
						+ OmpAppContext
								.getMessage("current_error.ac_high_current_alarm")
						+ "\r\n");
				break;
			case 0x20:
				buffer.append(flag
						+ OmpAppContext
								.getMessage("current_error.ac_air_switch_alarm")
						+ "\r\n");
				break;
			case 0x40:
				if (flag.equals("A")) {
					buffer.append(OmpAppContext
							.getMessage("current_error.diesel_engine_stop")
							+ "\r\n");
				} else if (flag.equals("B")) {
					buffer.append(OmpAppContext
							.getMessage("current_error.battery_symmetrical_fault_alarm")
							+ "\r\n");
				}
				break;
			default:
				buffer.append(flag
						+ OmpAppContext
								.getMessage("current_error.other_failure")
						+ "\r\n");
			}
		}

		return buffer.toString();
	}

	/**
	 * 获取整流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhengLiuWarnStr(int parstatus) {
		String str = "";
		int itemStates = parstatus & 0xff;
		switch (itemStates) {
		case 0x00:
			str = OmpAppContext.getMessage("rectification.alarm_power_normal");
			break;
		case 0x01:
			str = OmpAppContext
					.getMessage("current_error.short_circuit_protection");
			break;
		case 0x02:
			str = OmpAppContext
					.getMessage("current_error.over_temperature_protection");
			break;
		case 0x03:
			str = OmpAppContext
					.getMessage("current_error.over_voltage_protection");
			break;
		case 0x04:
			str = OmpAppContext.getMessage("current_error.ac_over_voltage");
			break;
		case 0x05:
			str = OmpAppContext.getMessage("current_error.ac_under_voltage");
			break;
		case 0x06:
			str = OmpAppContext.getMessage("current_error.ac_loss");
			break;
		default:
			str = OmpAppContext.getMessage("current_error.default_fault");
			break;
		}
		return str;
	}

	/**
	 * 获取直流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhiLiuWarnStr(int parstatus) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(OmpAppContext
						.getMessage("current_error.1-way_low_voltage_alerm")
						+ "\r\n");
				break;
			case 0x02:
				buffer.append(OmpAppContext
						.getMessage("current_error.system_high_current_alarm")
						+ "\r\n");
				break;
			case 0x04:
				buffer.append(OmpAppContext
						.getMessage("current_error.battery_low_voltage_alarm")
						+ "\r\n");
				break;
			case 0x10:
				buffer.append(OmpAppContext
						.getMessage("current_error.output_over_voltage_alarm")
						+ "\r\n");
				break;
			case 0x20:
				// 电池温度传感器故障屏蔽
				// buffer.append(OmpAppContext.getMessage("current_error.battery_temper_alarm")+"\r\n"
				// );
				break;
			}

		}

		return buffer.toString();
	}

	/**
	 * 获取直流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhiLiuWarnStr1(int parstatus) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(OmpAppContext
						.getMessage("current_error.battery_high_current_alarm")
						+ "\r\n");
				break;
			case 0x02:
				buffer.append(OmpAppContext
						.getMessage("current_error.battery_fuse_break_off")
						+ "\r\n");
				break;
			// case 0x04:
			// buffer.append(OmpAppContext.getMessage("current_error.load_fuse_break_off")+"\r\n"
			// );
			// break;
			case 0x10:
				buffer.append(OmpAppContext
						.getMessage("current_error.environment_temper_low_alarm")
						+ "\r\n");
				break;
			case 0x20:
				buffer.append(OmpAppContext
						.getMessage("current_error.environment_temper_high_alarm")
						+ "\r\n");
				break;
			case 0x40:
				buffer.append(OmpAppContext
						.getMessage("current_error.2-way_low_voltage_alerm")
						+ "\r\n");
				break;
			}
		}

		return buffer.toString();
	}

	/**
	 * 获取直流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhiLiuWarnStr2(int parstatus) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_4_off") + "\r\n");
				break;
			case 0x02:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_3_off") + "\r\n");
				break;
			case 0x04:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_2_off") + "\r\n");
				break;
			case 0x10:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_1_off") + "\r\n");
				break;
			case 0x20:
				buffer.append(OmpAppContext
						.getMessage("current_error.battery_temperature_low_alarm")
						+ "\r\n");
				break;
			case 0x40:
				buffer.append(OmpAppContext
						.getMessage("current_error.battery_temperature_high_alarm")
						+ "\r\n");
				break;
			}
		}

		return buffer.toString();
	}

	/**
	 * 获取直流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhiLiuWarnStr3(int parstatus) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(OmpAppContext
						.getMessage("current_error.battery_test") + "\r\n");
				break;
			case 0x02:
				buffer.append(OmpAppContext
						.getMessage("current_error.fast_charge") + "\r\n");
				break;
			case 0x04:
				buffer.append(OmpAppContext
						.getMessage("current_error.float_charge") + "\r\n");
				break;
			case 0x10:
				buffer.append(OmpAppContext
						.getMessage("current_error.average_charge") + "\r\n");
				break;
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取直流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhiLiuWarnStr4(int parstatus) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(OmpAppContext
						.getMessage("current_error.smoke_alarm") + "\r\n");
				break;
			case 0x02:
				buffer.append(OmpAppContext
						.getMessage("current_error.water_alarm") + "\r\n");
				break;
			case 0x04:
				buffer.append(OmpAppContext
						.getMessage("current_error.lightning_protection_alarm")
						+ "\r\n");
				break;
			case 0x08:
				buffer.append(OmpAppContext
						.getMessage("current_error.access_alarm") + "\r\n");
				break;
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取直流告警状态
	 * 
	 * @param parstatus
	 * @return
	 */
	protected String getZhiLiuWarnStr5(int parstatus) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			int ItempStatus = (parstatus & (0x01 << i));
			switch (ItempStatus) {
			case 0x00:
				break;
			case 0x01:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_12_off") + "\r\n");
				break;
			case 0x02:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_11_off") + "\r\n");
				break;
			case 0x04:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_10_off") + "\r\n");
				break;
			case 0x08:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_9_off") + "\r\n");
				break;
			case 0x10:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_8_off") + "\r\n");
				break;
			case 0x20:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_7_off") + "\r\n");
				break;
			case 0x40:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_6_off") + "\r\n");
				break;
			case 0x80:
				buffer.append(OmpAppContext
						.getMessage("current_error.sensor_5_off") + "\r\n");
				break;
			}
		}
		return buffer.toString();
	}
}
