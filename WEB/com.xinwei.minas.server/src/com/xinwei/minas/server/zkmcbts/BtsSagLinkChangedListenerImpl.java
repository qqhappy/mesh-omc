/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.zkmcbts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.SAGParamService;
import com.xinwei.minas.server.zk.net.ZkClusterConnectorManager;
import com.xinwei.minas.zk.core.listener.BtsSagLinkChangedListener;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;

/**
 * 
 * BTS��SAG��·�仯������
 * 
 * @author chenjunhua
 * 
 */

public class BtsSagLinkChangedListenerImpl implements BtsSagLinkChangedListener {

	private static final Logger logger = Logger
			.getLogger(BtsSagLinkChangedListenerImpl.class);

	private McBtsBasicService mcBtsBasicService;

	private SAGParamService sagParamService;

	private ZkClusterConnectorManager connectorManager;

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	public BtsSagLinkChangedListenerImpl() {
	}

	@Override
	public void linkChanged(final ZkBtsVO zkBts,
			final ZkBtsSagLinkVO newBtsSagLink) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				logger.debug("mcbts-sag link changed: " + zkBts + ", "
						+ newBtsSagLink);
				McBts mcBts = getMcBtsByZkBtsId(zkBts.getBtsId());
				if (mcBts != null) {
					try {
						// �޸�BTS-SAG��·����
						mcBts.setSagDeviceId(newBtsSagLink.getSagId());
						mcBts.setSagVoiceIp(newBtsSagLink.getSagMediaIP());
						mcBts.setSagSignalIp(newBtsSagLink.getSagSignalIp());
						mcBts.setSagMediaPort(newBtsSagLink.getSagMediaPort());
						mcBts.setSagSignalPort(newBtsSagLink.getSagSignalPort());
						mcBts.setSagSignalPointCode((int) newBtsSagLink
								.getSagDPID());
						mcBtsBasicService.modify(mcBts);
						// �޸Ļ�վ����SAG��·��Ϣ
						TConfBackupSag backupSag = sagParamService
								.queryByMoId(mcBts.getMoId());
						if (backupSag != null) {
							// ���û�վ����SAG��·��Ϣ
							backupSag.setSAGID(0L);
							backupSag.setLocationAreaID(-1L);
							backupSag.setsAGIPforVoice(0L);
							backupSag.setsAGIPforsignal(0L);
							backupSag.setSAGRxPortForSignal(0);
							backupSag.setSAGTxPortForSignal(0);
							backupSag.setSAGRxPortForVoice(0);
							backupSag.setSAGTxPortForVoice(0);
							backupSag.setBTSSignalPointCode(0);
							backupSag.setSAGSignalPointCode(0);
							backupSag.setBTSSignalPointCode(0);
							backupSag.setRsv(0);
							backupSag.setNatAPKey(1);
							sagParamService.config(backupSag);
						}
					} catch (Exception e) {
						logger.error("failed to modify bts-sag link.", e);
						// TODO: ���ɸ澯
					} finally {
					}
				} else {
					logger.warn("mcbts do not found. zkBtsId="
							+ zkBts.getBtsId());
				}

			}

		});

	}

	@Override
	public void linkDeleted(ZkBtsVO zkBts, ZkBtsSagLinkVO oldBtsSagLink) {
		logger.debug("mcbts-sag link deleted: " + zkBts + ", " + oldBtsSagLink);
		// TODO: ��ʱ�����κδ���
	}

	public void setMcBtsBasicService(McBtsBasicService mcBtsBasicService) {
		this.mcBtsBasicService = mcBtsBasicService;
	}

	public void setConnectorManager(ZkClusterConnectorManager connectorManager) {
		this.connectorManager = connectorManager;
		this.connectorManager.setBtsSagLinkChangedListener(this);
	}

	public void setSagParamService(SAGParamService sagParamService) {
		this.sagParamService = sagParamService;
	}

	private McBts getMcBtsByZkBtsId(Long zkBtsId) {
		// remark: 2013-06-05 ��������ȷ��ZK���ϵĻ�վID��EMS�Ļ�վIDһ��
		logger.info("**********************zkBtsId=" + zkBtsId);
		return McBtsCache.getInstance().queryByBtsId(zkBtsId);
	}

}
