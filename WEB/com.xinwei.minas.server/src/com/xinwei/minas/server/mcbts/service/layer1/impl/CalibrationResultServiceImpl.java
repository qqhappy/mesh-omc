/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.layer1.CalibGenConfigItem;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationGeneralConfig;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResultDataInfo;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResultGeneralConfig;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResultGeneralConfigItem;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibGenConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfSubCalibGenConfigDAO;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationDataService;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationResultService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 校准结果服务类
 * 
 * 
 * @author zhaolingling
 * 
 */

public class CalibrationResultServiceImpl implements CalibrationResultService {

	private static Log log = LogFactory
			.getLog(CalibrationResultServiceImpl.class);

	// private static McBts mcbts;

	private String localPath;
	// 校准数据的服务类
	private CalibrationDataService calibrationDataService;
	// 普通config的dao
	private TConfCalibGenConfigDAO genConfigDAO;
	// 天线信息的dao
	private TConfSubCalibGenConfigDAO subGenConfigDAO;

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public CalibrationResultServiceImpl() {
		super();
	}

	public void setGenConfigDAO(TConfCalibGenConfigDAO genConfigDAO) {
		this.genConfigDAO = genConfigDAO;
	}

	public void setSubGenConfigDAO(TConfSubCalibGenConfigDAO subGenConfigDAO) {
		this.subGenConfigDAO = subGenConfigDAO;
	}

	/**
	 * 提供给校准数据用于展现天线发送和接收的校准参数的方法
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	@Override
	public CalibrationResult getCalibrationResult(Long moId) throws Exception {
		Object[] obj = queryByMoId(moId);

		if (obj == null || obj[0] == null)
			return null;

		return (CalibrationResult) obj[0];
	}

	/**
	 * 查询校准结果信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	@Override
	public Object[] queryByMoId(Long moId) throws Exception {
		McBts mcbts = McBtsCache.getInstance().queryByMoId(moId);

		if (mcbts == null) {
			return null;
		}

		// 把两个文件都下载到服务器中并加载,路径plugins/mcbts/system/calibration
		File[] files = new File[2];

		// 如果latest也没有找到, 从目录中获取文件
		File[] filesMatched = listFilesByBtsId(mcbts, localPath
				+ File.separator + "bts_" + String.valueOf(mcbts.getHexBtsId()));

		if (filesMatched != null) {
			// 如果目录中找到文件执行如下代码:
			if (filesMatched.length >= 2) {
				// 如果找到>2个文件,根据名字进行从大到小排序,然后获取最近的两个文件
				sortFile(filesMatched);

				files[0] = filesMatched[0];
				files[1] = filesMatched[1];
			} else if (filesMatched.length == 1) {
				// 如果只有1个文件,给files[0]
				files[0] = filesMatched[0];
			}
		}

		Object[] ret = new Object[4];

		if (files[0] == null)
			return null;

		ret[0] = getCalibrationData(mcbts, files[0]);
		ret[2] = files[0].getName();
		if (files[1] != null) {
			ret[1] = getCalibrationData(mcbts, files[1]);
			ret[3] = files[1].getName();
		}

		return ret;
	}

	private File[] listFilesByBtsId(final McBts mcbts, String folderName) {
		File folder = new File(folderName);
		return folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains(String.valueOf(mcbts.getBtsId())))
					return true;
				else
					return false;
			}
		});
	}

	private void sortFile(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				long name1 = Long.parseLong(f1.getName().split("_")[1]);
				long name2 = Long.parseLong(f2.getName().split("_")[1]);

				if (name1 > name2)
					return -1;
				else if (name1 < name2)
					return 1;
				return 0;
			}
		});
	}

	// 校验文件是否格式完整
	private static void checkValidation(File file) throws Exception {
		// 验证文件长度,如果不符合,抛出异常
		if (file.length() == (CalibrationResultGeneralConfig.LEGNTH + 32 * CalibrationResultDataInfo.LEGNTH)
				|| file.length() == (CalibrationResultGeneralConfig.LEGNTH + 8 * CalibrationResultDataInfo.LEGNTH)) {
			// 分别是大基站的长度和小基站的长度, OK
			return;
		}
		log.error("calibration result file size error!");
		throw new Exception(
				OmpAppContext.getMessage("mcbts_calibresult_file_size_error"));
	}

	/**
	 * 更新数据库
	 * 
	 * @param file
	 * @throws Exception
	 */
	@Override
	public void updateCalibGenericConfig(Long moId, File file) throws Exception {
		McBts mcbts = McBtsCache.getInstance().queryByMoId(moId);

		CalibrationResult result = getCalibrationData(mcbts, file, true);

		CalibrationResultGeneralConfig generalConfig = result
				.getCalibrationResultGeneralConfig();

		// 合成器发送增益
		int syn_tx_gain = generalConfig.getSYN_TXGain();
		// 合成器接收增益
		int syn_rx_gain = generalConfig.getSYN_RXGain();
		// 天线增益及结果
		CalibrationResultGeneralConfigItem[] genConfItem = generalConfig
				.getCalibrationGeneralItem();

		// 获取<校准数据>的模型
		calibrationDataService = AppContext.getCtx().getBean(
				CalibrationDataService.class);
		CalibrationDataInfo dataInfo = calibrationDataService
				.queryCalibrationDataConfigByMoId(moId);

		CalibrationGeneralConfig config = dataInfo.getGenConfig();

		config.setSynTxGain(syn_tx_gain);
		config.setSynRxGain(syn_rx_gain);

		// 更新一般信息
		genConfigDAO.saveOrUpdate(config);

		List<CalibGenConfigItem> items = config.getGenItemList();

		for (int i = 0; i < genConfItem.length; i++) {
			CalibrationResultGeneralConfigItem resultItem = genConfItem[i];

			for (int j = 0; j < items.size(); j++) {
				CalibGenConfigItem item = items.get(j);
				// i为校准结果的天线索引
				if (item.getAntennaIndex().intValue() == i) {
					item.setRxGain(resultItem.getmRX_GAIN());
					item.setTxGain(resultItem.getmTX_GAIN());
					item.setCalibrationResult(resultItem
							.getAntennaCalibrationResult());
					item.setPredH(new String(resultItem.getmPRED_H()));
				}
			}
		}

		subGenConfigDAO.saveOrUpdate(items);

	}

	private static CalibrationResult getCalibrationData(McBts mcbts, File file)
			throws Exception {
		return getCalibrationData(mcbts, file, false);
	}

	// 从文件中读取Calibration对象
	private static CalibrationResult getCalibrationData(McBts mcbts, File file,
			boolean isOnlyGeneral) throws Exception {
		byte bf[] = new byte[2000];
		CalibrationResult ret = new CalibrationResult();

		checkValidation(file);
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			// 读取文件
			fin.read(bf, 0, CalibrationResultGeneralConfig.LEGNTH);
			// 解析文件
			ret.setCalibrationResultGeneralConfig(getGeneralConfig(bf));

			// 根据基站类型判断是否是微蜂窝
			boolean isMicroBeehive = mcbts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS ? true
					: false;

			// 如果只解析普通模型,这里就返回
			if (isOnlyGeneral) {
				return ret;
			}

			// 创建校准结果数据模型
			CalibrationResultDataInfo caldata;
			for (int i = 0; i < 8; i++) {
				caldata = new CalibrationResultDataInfo();
				// 如果不是微蜂窝 或者i<2
				if (!isMicroBeehive || i < 2) {
					fin.read(bf, 0, CalibrationResultDataInfo.LEGNTH);
					caldata.decode(bf);
				} else {
					caldata.setAntennalIndex(i);
					caldata.setType(CalibrationResultDataInfo.CALIBRATION_TYPE_RXCAL_I);
				}
				ret.getLstNotiRX_I().add(caldata);
			}
			for (int i = 0; i < 8; i++) {
				caldata = new CalibrationResultDataInfo();
				if (!isMicroBeehive || i < 2) {
					fin.read(bf, 0, CalibrationResultDataInfo.LEGNTH);
					caldata.decode(bf);
				} else {
					caldata.setAntennalIndex(i);
					caldata.setType(CalibrationResultDataInfo.CALIBRATION_TYPE_RXCAL_Q);
				}
				ret.getLstNotiRX_Q().add(caldata);
			}
			for (int i = 0; i < 8; i++) {
				caldata = new CalibrationResultDataInfo();
				if (!isMicroBeehive || i < 2) {
					fin.read(bf, 0, CalibrationResultDataInfo.LEGNTH);
					caldata.decode(bf);
				} else {
					caldata.setAntennalIndex(i);
					caldata.setType(CalibrationResultDataInfo.CALIBRATION_TYPE_TXCAL_I);
				}
				ret.getLstNotiTX_I().add(caldata);
			}
			for (int i = 0; i < 8; i++) {
				caldata = new CalibrationResultDataInfo();
				if (!isMicroBeehive || i < 2) {
					fin.read(bf, 0, CalibrationResultDataInfo.LEGNTH);
					caldata.decode(bf);
				} else {
					caldata.setAntennalIndex(i);
					caldata.setType(CalibrationResultDataInfo.CALIBRATION_TYPE_TXCAL_Q);
				}
				ret.getLstNotiTX_Q().add(caldata);
			}
			return ret;

		} catch (Exception e) {
			throw e;
		} finally {
			if (fin != null) {
				fin.close();
			}
		}
	}

	private static CalibrationResultGeneralConfig getGeneralConfig(byte[] bf) {
		// 创建校准结果通用模型, 可以沿用
		CalibrationResultGeneralConfig noti = new CalibrationResultGeneralConfig();
		try {
			noti.parse(bf);
		} catch (Exception e) {
			log.error(e);
		}

		return noti;
	}
}
