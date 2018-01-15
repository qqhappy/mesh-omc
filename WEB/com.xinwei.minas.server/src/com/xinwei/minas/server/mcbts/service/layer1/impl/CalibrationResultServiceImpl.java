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
 * У׼���������
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
	// У׼���ݵķ�����
	private CalibrationDataService calibrationDataService;
	// ��ͨconfig��dao
	private TConfCalibGenConfigDAO genConfigDAO;
	// ������Ϣ��dao
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
	 * �ṩ��У׼��������չ�����߷��ͺͽ��յ�У׼�����ķ���
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
	 * ��ѯУ׼�����Ϣ
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

		// �������ļ������ص��������в�����,·��plugins/mcbts/system/calibration
		File[] files = new File[2];

		// ���latestҲû���ҵ�, ��Ŀ¼�л�ȡ�ļ�
		File[] filesMatched = listFilesByBtsId(mcbts, localPath
				+ File.separator + "bts_" + String.valueOf(mcbts.getHexBtsId()));

		if (filesMatched != null) {
			// ���Ŀ¼���ҵ��ļ�ִ�����´���:
			if (filesMatched.length >= 2) {
				// ����ҵ�>2���ļ�,�������ֽ��дӴ�С����,Ȼ���ȡ����������ļ�
				sortFile(filesMatched);

				files[0] = filesMatched[0];
				files[1] = filesMatched[1];
			} else if (filesMatched.length == 1) {
				// ���ֻ��1���ļ�,��files[0]
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

	// У���ļ��Ƿ��ʽ����
	private static void checkValidation(File file) throws Exception {
		// ��֤�ļ�����,���������,�׳��쳣
		if (file.length() == (CalibrationResultGeneralConfig.LEGNTH + 32 * CalibrationResultDataInfo.LEGNTH)
				|| file.length() == (CalibrationResultGeneralConfig.LEGNTH + 8 * CalibrationResultDataInfo.LEGNTH)) {
			// �ֱ��Ǵ��վ�ĳ��Ⱥ�С��վ�ĳ���, OK
			return;
		}
		log.error("calibration result file size error!");
		throw new Exception(
				OmpAppContext.getMessage("mcbts_calibresult_file_size_error"));
	}

	/**
	 * �������ݿ�
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

		// �ϳ�����������
		int syn_tx_gain = generalConfig.getSYN_TXGain();
		// �ϳ�����������
		int syn_rx_gain = generalConfig.getSYN_RXGain();
		// �������漰���
		CalibrationResultGeneralConfigItem[] genConfItem = generalConfig
				.getCalibrationGeneralItem();

		// ��ȡ<У׼����>��ģ��
		calibrationDataService = AppContext.getCtx().getBean(
				CalibrationDataService.class);
		CalibrationDataInfo dataInfo = calibrationDataService
				.queryCalibrationDataConfigByMoId(moId);

		CalibrationGeneralConfig config = dataInfo.getGenConfig();

		config.setSynTxGain(syn_tx_gain);
		config.setSynRxGain(syn_rx_gain);

		// ����һ����Ϣ
		genConfigDAO.saveOrUpdate(config);

		List<CalibGenConfigItem> items = config.getGenItemList();

		for (int i = 0; i < genConfItem.length; i++) {
			CalibrationResultGeneralConfigItem resultItem = genConfItem[i];

			for (int j = 0; j < items.size(); j++) {
				CalibGenConfigItem item = items.get(j);
				// iΪУ׼�������������
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

	// ���ļ��ж�ȡCalibration����
	private static CalibrationResult getCalibrationData(McBts mcbts, File file,
			boolean isOnlyGeneral) throws Exception {
		byte bf[] = new byte[2000];
		CalibrationResult ret = new CalibrationResult();

		checkValidation(file);
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			// ��ȡ�ļ�
			fin.read(bf, 0, CalibrationResultGeneralConfig.LEGNTH);
			// �����ļ�
			ret.setCalibrationResultGeneralConfig(getGeneralConfig(bf));

			// ���ݻ�վ�����ж��Ƿ���΢����
			boolean isMicroBeehive = mcbts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS ? true
					: false;

			// ���ֻ������ͨģ��,����ͷ���
			if (isOnlyGeneral) {
				return ret;
			}

			// ����У׼�������ģ��
			CalibrationResultDataInfo caldata;
			for (int i = 0; i < 8; i++) {
				caldata = new CalibrationResultDataInfo();
				// �������΢���� ����i<2
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
		// ����У׼���ͨ��ģ��, ��������
		CalibrationResultGeneralConfig noti = new CalibrationResultGeneralConfig();
		try {
			noti.parse(bf);
		} catch (Exception e) {
			log.error(e);
		}

		return noti;
	}
}
