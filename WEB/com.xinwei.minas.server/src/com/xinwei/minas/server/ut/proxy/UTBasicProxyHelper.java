/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.PhoneNumberUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * UTBasicProxy的帮助类
 * 
 * 
 * @author tiance
 * 
 */

public class UTBasicProxyHelper {
	private static final String CHARSET_US_ASCII = "US-ASCII";
	private static final String CHARSET_GBK = "GBK";

	private static Log log = LogFactory.getLog(UTBasicProxyHelper.class);

	public static HlrUdpMessage convertFromConditionToMessage(UTCondition utc,
			String hlrIp, int hlrPort) {
		byte[] buf = new byte[1024];
		int offset = encodeCondition(buf, utc);

		offset = encodeSortAndAmount(buf, utc, offset);

		HlrUdpMessage message = new HlrUdpMessage();

		byte[] content = new byte[offset];
		System.arraycopy(buf, 0, content, 0, offset);
		message.setContent(content);

		return message;
	}

	/**
	 * 将待发送的对象中的排序方式编码
	 * 
	 * @param buf
	 * @param utc
	 * @param offset
	 * @return
	 */
	private static int encodeSortAndAmount(byte[] buf, UTCondition utc,
			int offset) {
		ByteUtils
				.putNumber(buf, offset, String.valueOf(utc.getQueryCount()), 1);
		offset += 1;

		ByteUtils.putNumber(buf, offset, String.valueOf(utc.getSortBy()), 1);
		offset += 1;

		ByteUtils.putNumber(buf, offset, String.valueOf(utc.getPidIndex()), 4);
		offset += 4;

		ByteUtils.putNumber(buf, offset, String.valueOf(utc.getPageShift()), 2);
		offset += 2;

		// utc里-1表示降序,1表示升序;字节流里1表示降序,0表示升序
		ByteUtils.putNumber(buf, offset,
				String.valueOf(utc.getSortType() == 1 ? 0 : 1), 1);
		offset += 1;

		return offset;
	}

	/**
	 * 将待发送的对象中的条件编码, 仅编码条件
	 * 
	 * @param buf
	 * @param utc
	 * @param offset
	 * @return offset
	 */
	private static int encodeCondition(byte[] buf, UTCondition utc) {
		int offset = 0;

		int sum = sumCondition(utc);

		boolean doDefault = false;
		
		//去掉默认搜索条件
//		if (sum == 0) {
//			sum = 1;
//			doDefault = true;
//		}

		// 条件的数量
		ByteUtils.putNumber(buf, offset, String.valueOf(sum), 1);
		offset++;

		if (utc.getUid() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_UID), 1);
			offset++;

			ByteUtils.putHexString(buf, offset, utc.getUid());
			offset += 4;
		}
		if (utc.getPid() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_PID), 1);
			offset++;

			ByteUtils.putHexString(buf, offset, utc.getPid());
			offset += 4;
		}
		if (utc.getTelNo() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_TEL_NO), 1);
			offset++;

			byte[] telNoByte = PhoneNumberUtils.telNo2Bytes(utc.getTelNo());
			System.arraycopy(telNoByte, 0, buf, offset, 16);
			offset += 16;
		}
		if (utc.getAlias() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_ALIAS), 1);
			offset++;

			char fillChar = '\0';
			ByteUtils.putString(buf, offset, utc.getAlias(), 51, fillChar,
					CHARSET_US_ASCII);
			offset += 51;
		}
		if (utc.getUtType() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_UT_TYPE), 1);
			offset++;

			ByteUtils
					.putNumber(buf, offset, String.valueOf(utc.getUtType()), 2);
			offset += 2;
		}
		if (utc.getUtStatus() != null || doDefault) {
			// 默认是活动态的查询
			int status = doDefault ? UserTerminal.STATUS_ACTIVE : utc
					.getUtStatus();
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_UT_STATUS), 1);
			offset++;

			ByteUtils.putNumber(buf, offset, String.valueOf(status), 1);
			offset += 1;
		}
		if (utc.getUtGroup() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_GROUP), 1);
			offset++;

			char fillChar = '\0';
			ByteUtils.putString(buf, offset, utc.getUtGroup(), 51, fillChar,
					CHARSET_GBK);
			offset += 51;
		}
		if (utc.getBtsId() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_BTS_ID), 1);
			offset++;

			ByteUtils.putNumber(buf, offset, String.valueOf(utc.getBtsId()), 4);
			offset += 4;
		}
		if (utc.getIp() != null) {
			ByteUtils.putNumber(buf, offset,
					String.valueOf(UTCondition.ID_TYPE_IP), 1);
			offset++;

			ByteUtils.putIp(buf, offset, utc.getIp());
			offset += 4;
		}
		if (utc.getMacIp() != null) {
			ByteUtils.putNumber(buf, offset, String.valueOf(UTCondition.ID_TYPE_MAC_IP), 1);
			offset++;
			ByteUtils.putHexString(buf, offset, utc.getMacIp());
			offset += 6;
		}

		return offset;
	}

	/**
	 * 获取条件对象中,条件的总数
	 * 
	 * @param utc
	 * @return
	 */
	private static int sumCondition(UTCondition utc) {
		int sum = 0;
		if (utc.getUid() != null)
			sum++;
		if (utc.getPid() != null)
			sum++;
		if (utc.getTelNo() != null)
			sum++;
		if (utc.getAlias() != null)
			sum++;
		if (utc.getUtType() != null)
			sum++;
		if (utc.getUtStatus() != null)
			sum++;
		if (utc.getUtGroup() != null)
			sum++;
		if (utc.getBtsId() != null)
			sum++;
		if (utc.getIp() != null)
			sum++;
		if (utc.getMacIp() != null)
			sum++;

		return sum;
	}

	/**
	 * 将应答的消息转换成模型
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static UTQueryResult convertFromMessageToModel(
			List<HlrUdpMessage> response) throws Exception {

		if (response == null || response.isEmpty())
			return null;

		// 要返回的终端列表
		List<UserTerminal> utList = new ArrayList<UserTerminal>();
		UTQueryResult utqr = new UTQueryResult();

		log.debug("Convert ut message to model");

		// 遍历多个包的结果
		for (HlrUdpMessage message : response) {
			byte[] buf = message.getContent();
			int offset = 0;

			// 是否成功
			int suc = ByteUtils.toInt(buf, offset, 1);
			offset += 1;
			if (suc == UTQueryResult.NO_AUTHORITY)
				throw new Exception(OmpAppContext.getMessage("shlr_no_authority"));
			if (suc == UTQueryResult.SHLR_INNER_ERROR)
				throw new Exception(OmpAppContext.getMessage("shlr_inner_error"));

			// 把总数放进要返回的列表中
			int totalResult = ByteUtils.toInt(buf, offset, 2);
			offset += 2;
			if (totalResult == 0)
				return null;
			if (utqr.getTotalNumInHlr() == 0)
				utqr.setTotalNumInHlr(totalResult);

			// 本次查询的结果数量
			int curResult = ByteUtils.toInt(buf, offset, 1);
			offset += 1;
			if (utqr.getResultNum() == 0)
				utqr.setResultNum(curResult);

			// 本包包含的结果数量
			int utAmountInPack = ByteUtils.toInt(buf, offset, 1);
			offset += 1;

			// 遍历包中的所有ut
			for (int i = 0; i < utAmountInPack; i++) {
				UserTerminal ut = new UserTerminal();
				// PID
				ut.setPid(ByteUtils.toHexString(buf, offset, 4));
				offset += 4;
				// UID
				ut.setUid(ByteUtils.toHexString(buf, offset, 4));
				offset += 4;
				// 终端状态
				ut.setStatus(ByteUtils.toInt(buf, offset, 1));
				offset += 1;
				// SAG ID
				ut.setSagId(ByteUtils.toLong(buf, offset, 4));
				offset += 4;
				// BTS ID
				ut.setBtsId(ByteUtils.toLong(buf, offset, 4));
				offset += 4;
				// reg time
				{
					String regTimeStr = ByteUtils.toString(buf, offset, 14,
							"US-ASCII");
					offset += 14;
					SimpleDateFormat dateFormatter = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					try {
						if (StringUtils.isNotBlank(regTimeStr))
							ut.setRegTime(dateFormatter.parse(regTimeStr));
					} catch (ParseException e) {
						log.error("Error paring Strign to Date.");
					}
				}
				// 硬件类型
				ut.setHwType(ByteUtils.toHexString(buf, offset, 2) + "0000");
				offset += 2;
				// 软件类型
				ut.setSwType(ByteUtils.toInt(buf, offset, 1));
				offset += 1;
				// 保留字段
				offset += 1;
				// 当前软件版本
				ut.setActiveSwVersion(ByteUtils.toVersion(buf, offset, 4));
				offset += 4;
				// 备用软件版本
				ut.setStandbySwVersion(ByteUtils.toVersion(buf, offset, 4));
				offset += 4;
				// 硬件版本
				ut.setHwVersion(ByteUtils.toString(buf, offset, 16,
						CHARSET_US_ASCII));
				offset += 16;
				// 终端别名
				ut.setAlias(ByteUtils.toString(buf, offset, 51,
						CHARSET_GBK).trim());
				offset += 51;
				// 终端组别
				ut.setGroup(ByteUtils.toString(buf, offset, 51,
						CHARSET_GBK).trim());
				offset += 51;

				utList.add(ut);
			}

		}
		utqr.addUtList(utList);
		return utqr;
	}
}
