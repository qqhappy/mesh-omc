package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

import com.xinwei.minas.core.model.Mo;

/**
 * 负载均衡实体类
 * 
 * @author yinbinqiang
 * 
 */
public class TConfLoadBalance implements Serializable {

	// 记录索引
	private Long idx;

	// MO编号（全局唯一,系统自动生成）
	private long moId;

	// 配置开关
	private Integer algorithm_switch;

	// 基站高负载门限
	private Integer load_high_threshold;

	// 基站间负载平衡消息发送周期
	private Integer load_msg_send_period;

	// 基站负载差值门限
	private Integer load_diff_threshold;

	// 邻接站功率高于切换门限连续累计次数
	private Integer neighbor_bts_power_over_num;

	// 负载平衡信号强度余量
	private Integer load_balance_signal_remains;

	// 终端负载平衡周期
	private Integer ut_load_balance_period;

	// 加权用户算法参数
	private Integer user_algorithm_param;

	// 维持带宽负载均衡算法开关
	private Integer arithmetic_switch;

	public TConfLoadBalance() {

	}

//	public TConfLoadBalance(long moId, int typeId, String name, String desc,
//			int manageStateCode) {
//		super(moId, typeId, name, desc, manageStateCode);
//	}

	public java.lang.Long getIdx() {
		return idx;
	}

	public void setIdx(java.lang.Long idx) {
		this.idx = idx;
	}

	public Integer getAlgorithm_switch() {
		return algorithm_switch;
	}

	public void setAlgorithm_switch(Integer algorithm_switch) {
		this.algorithm_switch = algorithm_switch;
	}

	public Integer getLoad_high_threshold() {
		return load_high_threshold;
	}

	public void setLoad_high_threshold(Integer load_high_threshold) {
		this.load_high_threshold = load_high_threshold;
	}

	public Integer getLoad_msg_send_period() {
		return load_msg_send_period;
	}

	public void setLoad_msg_send_period(Integer load_msg_send_period) {
		this.load_msg_send_period = load_msg_send_period;
	}

	public Integer getLoad_diff_threshold() {
		return load_diff_threshold;
	}

	public void setLoad_diff_threshold(Integer load_diff_threshold) {
		this.load_diff_threshold = load_diff_threshold;
	}

	public Integer getNeighbor_bts_power_over_num() {
		return neighbor_bts_power_over_num;
	}

	public void setNeighbor_bts_power_over_num(
			Integer neighbor_bts_power_over_num) {
		this.neighbor_bts_power_over_num = neighbor_bts_power_over_num;
	}

	public Integer getLoad_balance_signal_remains() {
		return load_balance_signal_remains;
	}

	public void setLoad_balance_signal_remains(
			Integer load_balance_signal_remains) {
		this.load_balance_signal_remains = load_balance_signal_remains;
	}

	public Integer getUt_load_balance_period() {
		return ut_load_balance_period;
	}

	public void setUt_load_balance_period(Integer ut_load_balance_period) {
		this.ut_load_balance_period = ut_load_balance_period;
	}

	public Integer getUser_algorithm_param() {
		return user_algorithm_param;
	}

	public void setUser_algorithm_param(Integer user_algorithm_param) {
		this.user_algorithm_param = user_algorithm_param;
	}

	public Integer getArithmetic_switch() {
		return arithmetic_switch;
	}

	public void setArithmetic_switch(Integer arithmetic_switch) {
		this.arithmetic_switch = arithmetic_switch;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}
}
