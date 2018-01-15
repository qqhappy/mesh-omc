package com.xinwei.minas.server.mcbts.facade.layer3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.WeakFaultFacade;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfo;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfoPK;
import com.xinwei.minas.mcbts.core.model.layer3.TConfFaultSwitch;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFaultWithSwitch;
import com.xinwei.minas.server.mcbts.service.layer3.WeakFaultService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 故障弱化基本业务门面实现
 * 
 * @author yinbinqiang
 * 
 */
public class WeakFaultFacadeImpl extends UnicastRemoteObject implements
		WeakFaultFacade {

	private WeakFaultService service;
	private SequenceService sequenceService;

	public WeakFaultFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(WeakFaultService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public TConfFaultSwitch querySwitchByMoId(Long moId)
			throws RemoteException, Exception {
		return service.querySwitchByMoId(moId);
	}

	@Override
	public TConfWeakVoiceFault queryVoiceByMoId(Long moId)
			throws RemoteException, Exception {
		return service.queryVoiceByMoId(moId);
	}

	@Override
	public void config(OperObject operObject, TConfFaultSwitch faultSwitch,
			TConfWeakVoiceFault weakVoiceFault) throws RemoteException,
			Exception {
		Long idx = sequenceService.getNext();
		if (faultSwitch.getIdx() == null) {
			faultSwitch.setIdx(idx);
		}
		if (weakVoiceFault.getIdx() == null) {
			weakVoiceFault.setIdx(idx);
		}
		service.config(faultSwitch, weakVoiceFault);
	}

	/**
	 * 从数据库获取配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public TConfWeakVoiceFaultWithSwitch queryFromEMS(Long moId)
			throws RemoteException, Exception {
		// 此模型需要包装上两个其它的模型:TConfFaultSwitch,TConfWeakVoiceFault
		TConfFaultSwitch tConfFaultSwitch = service.querySwitchByMoId(moId);
		TConfWeakVoiceFault tConfWeakVoiceFault = service
				.queryVoiceByMoId(moId);

		return transferValues(tConfFaultSwitch, tConfWeakVoiceFault);
	}

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public TConfWeakVoiceFaultWithSwitch queryFromNE(Long moId)
			throws RemoteException, Exception {
		// 此模型需要包装上两个其它的模型:TConfFaultSwitch,TConfWeakVoiceFault
		TConfFaultSwitch tConfFaultSwitch = service.querySwitchFromNE(moId);
		TConfWeakVoiceFault tConfWeakVoiceFault = service.queryVoiceFromNE(moId);

		return transferValues(tConfFaultSwitch, tConfWeakVoiceFault);
	}

	private TConfWeakVoiceFaultWithSwitch transferValues(TConfFaultSwitch s,
			TConfWeakVoiceFault f) {
		TConfWeakVoiceFaultWithSwitch result = new TConfWeakVoiceFaultWithSwitch();
		result.setSwitchFlag(s.getFlag());

		result.setFlag(f.getFlag());
		result.setDelay_interval(f.getDelay_interval());
		result.setDivision_code(f.getDivision_code());
		result.setMulti_call_idle_time(f.getMulti_call_idle_time());
		result.setMulti_call_max_time(f.getMulti_call_max_time());
		result.setTrunk_list_file(f.getTrunk_list_file());
		result.setVoice_max_time(f.getVoice_max_time());
		result.setVoice_user_list_file(f.getVoice_user_list_file());
		result.setVoice_user_list_file2(f.getVoice_user_list_file2());

		result.TConfDnInfos.addAll(f.TConfDnInfos);

		return result;
	}

}
