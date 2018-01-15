/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

/**
 * 
 * 基站实时性能数据实体
 * 
 * @author fanhaoyu
 * 
 */

public class RealTimePerfData {

	private Long id;
	private Long neId;
	private Long pfStartTime;
	private String pfStartTime2;
	private Long pfEndTime;
	private String pfEndTime2;

	private Long totalUT;
	private Long actMacSess;

	private Long totalTakenPwr_1;
	private Long totalDownPwr_1;
	private Long scgmaskOccupied_1;
	private Long scgmaskAvailable_1;
	private Long totalBW_1;

	private Long totalTakenPwr_2;
	private Long totalDownPwr_2;
	private Long scgmaskOccupied_2;
	private Long scgmaskAvailable_2;
	private Long totalBW_2;

	private Long totalTakenPwr_3;
	private Long totalDownPwr_3;
	private Long scgmaskOccupied_3;
	private Long scgmaskAvailable_3;
	private Long totalBW_3;

	private Long totalTakenPwr_4;
	private Long totalDownPwr_4;
	private Long scgmaskOccupied_4;
	private Long scgmaskAvailable_4;
	private Long totalBW_4;

	private Long totalTakenPwr_5;
	private Long totalDownPwr_5;
	private Long scgmaskOccupied_5;
	private Long scgmaskAvailable_5;
	private Long totalBW_5;

	private Long totalTakenPwr_6;
	private Long totalDownPwr_6;
	private Long scgmaskOccupied_6;
	private Long scgmaskAvailable_6;
	private Long totalBW_6;

	private Long totalTakenPwr_7;
	private Long totalDownPwr_7;
	private Long scgmaskOccupied_7;
	private Long scgmaskAvailable_7;
	private Long totalBW_7;

	private Long totalTakenPwr_8;
	private Long totalDownPwr_8;
	private Long scgmaskOccupied_8;
	private Long scgmaskAvailable_8;
	private Long totalBW_8;

	private Long upScgmaskOccupied_1;
	private Long upScgmaskAvailable_1;
	private Long upCI_1_1;
	private Long upCI_1_2;
	private Long upCI_1_3;
	private Long upCI_1_4;
	private Long upCI_1_5;

	private Long upScgmaskOccupied_2;
	private Long upScgmaskAvailable_2;
	private Long upCI_2_1;
	private Long upCI_2_2;
	private Long upCI_2_3;
	private Long upCI_2_4;
	private Long upCI_2_5;

	private Long upScgmaskOccupied_3;
	private Long upScgmaskAvailable_3;
	private Long upCI_3_1;
	private Long upCI_3_2;
	private Long upCI_3_3;
	private Long upCI_3_4;
	private Long upCI_3_5;

	private Long upScgmaskOccupied_4;
	private Long upScgmaskAvailable_4;
	private Long upCI_4_1;
	private Long upCI_4_2;
	private Long upCI_4_3;
	private Long upCI_4_4;
	private Long upCI_4_5;

	private Long upScgmaskOccupied_5;
	private Long upScgmaskAvailable_5;
	private Long upCI_5_1;
	private Long upCI_5_2;
	private Long upCI_5_3;
	private Long upCI_5_4;
	private Long upCI_5_5;

	private Long upScgmaskOccupied_6;
	private Long upScgmaskAvailable_6;
	private Long upCI_6_1;
	private Long upCI_6_2;
	private Long upCI_6_3;
	private Long upCI_6_4;
	private Long upCI_6_5;

	private Long upScgmaskOccupied_7;
	private Long upScgmaskAvailable_7;
	private Long upCI_7_1;
	private Long upCI_7_2;
	private Long upCI_7_3;
	private Long upCI_7_4;
	private Long upCI_7_5;

	private Long upScgmaskOccupied_8;
	private Long upScgmaskAvailable_8;
	private Long upCI_8_1;
	private Long upCI_8_2;
	private Long upCI_8_3;
	private Long upCI_8_4;
	private Long upCI_8_5;

	/**
	 * 上行占用信道数
	 * 
	 * @return
	 */
	public Long getUpScgmaskOccupiedTotal() {
		long db = 0;
		if (getUpScgmaskOccupied_1() != null)
			db += getUpScgmaskOccupied_1().longValue();
		if (getUpScgmaskOccupied_2() != null)
			db += getUpScgmaskOccupied_2().longValue();
		if (getUpScgmaskOccupied_3() != null)
			db += getUpScgmaskOccupied_3().longValue();
		if (getUpScgmaskOccupied_4() != null)
			db += getUpScgmaskOccupied_4().longValue();
		if (getUpScgmaskOccupied_5() != null)
			db += getUpScgmaskOccupied_5().longValue();
		if (getUpScgmaskOccupied_6() != null)
			db += getUpScgmaskOccupied_6().longValue();
		if (getUpScgmaskOccupied_7() != null)
			db += getUpScgmaskOccupied_7().longValue();
		if (getUpScgmaskOccupied_8() != null)
			db += getUpScgmaskOccupied_8().longValue();
		return db;
	}

	/**
	 * 上行可用信道数
	 * 
	 * @return
	 */
	public Long getUpScgmaskAvailableTotal() {
		long db = 0;
		if (getUpScgmaskAvailable_1() != null)
			db += getUpScgmaskAvailable_1().longValue();
		if (getUpScgmaskAvailable_2() != null)
			db += getUpScgmaskAvailable_2().longValue();
		if (getUpScgmaskAvailable_3() != null)
			db += getUpScgmaskAvailable_3().longValue();
		if (getUpScgmaskAvailable_4() != null)
			db += getUpScgmaskAvailable_4().longValue();
		if (getUpScgmaskAvailable_5() != null)
			db += getUpScgmaskAvailable_5().longValue();
		if (getUpScgmaskAvailable_6() != null)
			db += getUpScgmaskAvailable_6().longValue();
		if (getUpScgmaskAvailable_7() != null)
			db += getUpScgmaskAvailable_7().longValue();
		if (getUpScgmaskAvailable_8() != null)
			db += getUpScgmaskAvailable_8().longValue();
		return db;
	}

	/**
	 * 下行平均消耗功率
	 * 
	 * @return
	 */
	public Double getTotalTakenPwrAvg() {
		double db = 0;
		int downlinknum = 0;
		if (getTotalTakenPwr_1() != null) {
			db += getTotalTakenPwr_1().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_2() != null) {
			db += getTotalTakenPwr_2().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_3() != null) {
			db += getTotalTakenPwr_3().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_4() != null) {
			db += getTotalTakenPwr_4().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_5() != null) {
			db += getTotalTakenPwr_5().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_6() != null) {
			db += getTotalTakenPwr_6().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_7() != null) {
			db += getTotalTakenPwr_7().doubleValue();
			downlinknum++;
		}
		if (getTotalTakenPwr_8() != null) {
			db += getTotalTakenPwr_8().doubleValue();
			downlinknum++;
		}
		if (downlinknum == 0)
			return db;
		else
			return db / downlinknum;
	}

	/**
	 * 下行平均消耗功率
	 * 
	 * @return
	 */
	public Double getTotalDownPwrAvg() {
		double db = 0;
		int downlinknum = 0;
		if (getTotalDownPwr_1() != null) {
			db += getTotalDownPwr_1().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_2() != null) {
			db += getTotalDownPwr_2().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_3() != null) {
			db += getTotalDownPwr_3().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_4() != null) {
			db += getTotalDownPwr_4().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_5() != null) {
			db += getTotalDownPwr_5().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_6() != null) {
			db += getTotalDownPwr_6().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_7() != null) {
			db += getTotalDownPwr_7().doubleValue();
			downlinknum++;
		}
		if (getTotalDownPwr_8() != null) {
			db += getTotalDownPwr_8().doubleValue();
			downlinknum++;
		}
		if (downlinknum == 0)
			return db;
		else
			return db / downlinknum;
	}

	public Long getScgmaskOccupiedTotal() {
		long db = 0;
		if (getScgmaskOccupied_1() != null)
			db += getScgmaskOccupied_1().longValue();
		if (getScgmaskOccupied_2() != null)
			db += getScgmaskOccupied_2().longValue();
		if (getScgmaskOccupied_3() != null)
			db += getScgmaskOccupied_3().longValue();
		if (getScgmaskOccupied_4() != null)
			db += getScgmaskOccupied_4().longValue();
		if (getScgmaskOccupied_5() != null)
			db += getScgmaskOccupied_5().longValue();
		if (getScgmaskOccupied_6() != null)
			db += getScgmaskOccupied_6().longValue();
		if (getScgmaskOccupied_7() != null)
			db += getScgmaskOccupied_7().longValue();
		if (getScgmaskOccupied_8() != null)
			db += getScgmaskOccupied_8().longValue();
		return db;
	}

	public Long getScgmaskAvailableTotal() {
		long db = 0;
		if (getScgmaskAvailable_1() != null)
			db += getScgmaskAvailable_1().longValue();
		if (getScgmaskAvailable_2() != null)
			db += getScgmaskAvailable_2().longValue();
		if (getScgmaskAvailable_3() != null)
			db += getScgmaskAvailable_3().longValue();
		if (getScgmaskAvailable_4() != null)
			db += getScgmaskAvailable_4().longValue();
		if (getScgmaskAvailable_5() != null)
			db += getScgmaskAvailable_5().longValue();
		if (getScgmaskAvailable_6() != null)
			db += getScgmaskAvailable_6().longValue();
		if (getScgmaskAvailable_7() != null)
			db += getScgmaskAvailable_7().longValue();
		if (getScgmaskAvailable_8() != null)
			db += getScgmaskAvailable_8().longValue();
		return db;
	}

	public Long getTotalBWTotal() {
		long db = 0;
		if (getTotalBW_1() != null)
			db += getTotalBW_1().longValue();
		if (getTotalBW_2() != null)
			db += getTotalBW_2().longValue();
		if (getTotalBW_3() != null)
			db += getTotalBW_3().longValue();
		if (getTotalBW_4() != null)
			db += getTotalBW_4().longValue();
		if (getTotalBW_5() != null)
			db += getTotalBW_5().longValue();
		if (getTotalBW_6() != null)
			db += getTotalBW_6().longValue();
		if (getTotalBW_7() != null)
			db += getTotalBW_7().longValue();
		if (getTotalBW_8() != null)
			db += getTotalBW_8().longValue();
		return db;
	}

	public Long getTotalDownTSBW() {
		long db = 0;
		if (getTotalTakenPwr_1() != null) {
			db += getTotalBW_1().longValue();
		}
		if (getTotalTakenPwr_2() != null) {
			db += getTotalBW_2().longValue();
		}
		if (getTotalTakenPwr_3() != null) {
			db += getTotalBW_3().longValue();
		}
		if (getTotalTakenPwr_4() != null) {
			db += getTotalBW_4().longValue();
		}
		if (getTotalTakenPwr_5() != null) {
			db += getTotalBW_5().longValue();
		}
		if (getTotalTakenPwr_6() != null) {
			db += getTotalBW_6().longValue();
		}
		if (getTotalTakenPwr_7() != null) {
			db += getTotalBW_7().longValue();
		}
		if (getTotalTakenPwr_8() != null) {
			db += getTotalBW_8().longValue();
		}
		return db;
	}

	public Long getTotalUpTSBW() {
		long db = 0;
		if (getUpScgmaskAvailable_1() != null) {
			db += getTotalBW_1().longValue();
		}
		if (getUpScgmaskAvailable_2() != null) {
			db += getTotalBW_2().longValue();
		}
		if (getUpScgmaskAvailable_3() != null) {
			db += getTotalBW_3().longValue();
		}
		if (getUpScgmaskAvailable_4() != null) {
			db += getTotalBW_4().longValue();
		}
		if (getUpScgmaskAvailable_5() != null) {
			db += getTotalBW_5().longValue();
		}
		if (getUpScgmaskAvailable_6() != null) {
			db += getTotalBW_6().longValue();
		}
		if (getUpScgmaskAvailable_7() != null) {
			db += getTotalBW_7().longValue();
		}
		if (getUpScgmaskAvailable_8() != null) {
			db += getTotalBW_8().longValue();
		}
		return db;
	}

	// 计算CI的平均值
	public Double getAvgCI() {
		long ci = 0L;
		int num = 0;
		if (upCI_1_1 != null) {
			ci += upCI_1_1.longValue();
			ci += upCI_1_2.longValue();
			ci += upCI_1_3.longValue();
			ci += upCI_1_4.longValue();
			ci += upCI_1_5.longValue();
			num += 5;
		}
		if (upCI_2_1 != null) {
			ci += upCI_2_1.longValue();
			ci += upCI_2_2.longValue();
			ci += upCI_2_3.longValue();
			ci += upCI_2_4.longValue();
			ci += upCI_2_5.longValue();
			num += 5;
		}
		if (upCI_3_1 != null) {
			ci += upCI_3_1.longValue();
			ci += upCI_3_2.longValue();
			ci += upCI_3_3.longValue();
			ci += upCI_3_4.longValue();
			ci += upCI_3_5.longValue();
			num += 5;
		}
		if (upCI_4_1 != null) {
			ci += upCI_4_1.longValue();
			ci += upCI_4_2.longValue();
			ci += upCI_4_3.longValue();
			ci += upCI_4_4.longValue();
			ci += upCI_4_5.longValue();
			num += 5;
		}
		if (upCI_5_1 != null) {
			ci += upCI_5_1.longValue();
			ci += upCI_5_2.longValue();
			ci += upCI_5_3.longValue();
			ci += upCI_5_4.longValue();
			ci += upCI_5_5.longValue();
			num += 5;
		}
		if (upCI_6_1 != null) {
			ci += upCI_6_1.longValue();
			ci += upCI_6_2.longValue();
			ci += upCI_6_3.longValue();
			ci += upCI_6_4.longValue();
			ci += upCI_6_5.longValue();
			num += 5;
		}
		if (upCI_7_1 != null) {
			ci += upCI_7_1.longValue();
			ci += upCI_7_2.longValue();
			ci += upCI_7_3.longValue();
			ci += upCI_7_4.longValue();
			ci += upCI_7_5.longValue();
			num += 5;
		}
		if (upCI_8_1 != null) {
			ci += upCI_8_1.longValue();
			ci += upCI_8_2.longValue();
			ci += upCI_8_3.longValue();
			ci += upCI_8_4.longValue();
			ci += upCI_8_5.longValue();
			num += 5;
		}
		if (num != 0)
			return ((double) ci) / num;
		else
			return 0d;
	}

	// 计算CI的最大值
	public Long getMaxCI() {
		Long ci = 0L;
		ci = calcCI(ci, upCI_1_1);
		ci = calcCI(ci, upCI_1_2);
		ci = calcCI(ci, upCI_1_3);
		ci = calcCI(ci, upCI_1_4);
		ci = calcCI(ci, upCI_1_5);

		ci = calcCI(ci, upCI_2_1);
		ci = calcCI(ci, upCI_2_2);
		ci = calcCI(ci, upCI_2_3);
		ci = calcCI(ci, upCI_2_4);
		ci = calcCI(ci, upCI_2_5);

		ci = calcCI(ci, upCI_3_1);
		ci = calcCI(ci, upCI_3_2);
		ci = calcCI(ci, upCI_3_3);
		ci = calcCI(ci, upCI_3_4);
		ci = calcCI(ci, upCI_3_5);

		ci = calcCI(ci, upCI_4_1);
		ci = calcCI(ci, upCI_4_2);
		ci = calcCI(ci, upCI_4_3);
		ci = calcCI(ci, upCI_4_4);
		ci = calcCI(ci, upCI_4_5);

		ci = calcCI(ci, upCI_5_1);
		ci = calcCI(ci, upCI_5_2);
		ci = calcCI(ci, upCI_5_3);
		ci = calcCI(ci, upCI_5_4);
		ci = calcCI(ci, upCI_5_5);

		ci = calcCI(ci, upCI_6_1);
		ci = calcCI(ci, upCI_6_2);
		ci = calcCI(ci, upCI_6_3);
		ci = calcCI(ci, upCI_6_4);
		ci = calcCI(ci, upCI_6_5);

		ci = calcCI(ci, upCI_7_1);
		ci = calcCI(ci, upCI_7_2);
		ci = calcCI(ci, upCI_7_3);
		ci = calcCI(ci, upCI_7_4);
		ci = calcCI(ci, upCI_7_5);

		ci = calcCI(ci, upCI_8_1);
		ci = calcCI(ci, upCI_8_2);
		ci = calcCI(ci, upCI_8_3);
		ci = calcCI(ci, upCI_8_4);
		ci = calcCI(ci, upCI_8_5);

		return ci;
	}

	private Long calcCI(Long old, Long val) {
		if (val != null && val.longValue() > old.longValue())
			return val;
		else
			return old;
	}

	// //////////////////////////////////////////
	public Long getActMacSess() {
		return actMacSess;
	}

	public void setActMacSess(Long actMacSess) {
		this.actMacSess = actMacSess;
	}

	public Long getTotalUT() {
		return totalUT;
	}

	public void setTotalUT(Long totalUT) {
		this.totalUT = totalUT;
	}

	// ///////////////////////////////////////////
	public Long getScgmaskAvailable_1() {
		return scgmaskAvailable_1;
	}

	public void setScgmaskAvailable_1(Long scgmaskAvailable_1) {
		this.scgmaskAvailable_1 = scgmaskAvailable_1;
	}

	public Long getScgmaskOccupied_1() {
		return scgmaskOccupied_1;
	}

	public void setScgmaskOccupied_1(Long scgmaskOccupied_1) {
		this.scgmaskOccupied_1 = scgmaskOccupied_1;
	}

	public Long getTotalBW_1() {
		return totalBW_1;
	}

	public void setTotalBW_1(Long totalBW_1) {
		this.totalBW_1 = totalBW_1;
	}

	public Long getTotalDownPwr_1() {
		return totalDownPwr_1;
	}

	public void setTotalDownPwr_1(Long totalDownPwr_1) {
		this.totalDownPwr_1 = totalDownPwr_1;
	}

	public Long getTotalTakenPwr_1() {
		return totalTakenPwr_1;
	}

	public void setTotalTakenPwr_1(Long totalTakenPwr_1) {
		this.totalTakenPwr_1 = totalTakenPwr_1;
	}

	public Long getScgmaskAvailable_2() {
		return scgmaskAvailable_2;
	}

	public void setScgmaskAvailable_2(Long scgmaskAvailable_2) {
		this.scgmaskAvailable_2 = scgmaskAvailable_2;
	}

	public Long getScgmaskOccupied_2() {
		return scgmaskOccupied_2;
	}

	public void setScgmaskOccupied_2(Long scgmaskOccupied_2) {
		this.scgmaskOccupied_2 = scgmaskOccupied_2;
	}

	public Long getTotalBW_2() {
		return totalBW_2;
	}

	public void setTotalBW_2(Long totalBW_2) {
		this.totalBW_2 = totalBW_2;
	}

	public Long getTotalDownPwr_2() {
		return totalDownPwr_2;
	}

	public void setTotalDownPwr_2(Long totalDownPwr_2) {
		this.totalDownPwr_2 = totalDownPwr_2;
	}

	public Long getTotalTakenPwr_2() {
		return totalTakenPwr_2;
	}

	public void setTotalTakenPwr_2(Long totalTakenPwr_2) {
		this.totalTakenPwr_2 = totalTakenPwr_2;
	}

	public Long getScgmaskAvailable_3() {
		return scgmaskAvailable_3;
	}

	public void setScgmaskAvailable_3(Long scgmaskAvailable_3) {
		this.scgmaskAvailable_3 = scgmaskAvailable_3;
	}

	public Long getScgmaskOccupied_3() {
		return scgmaskOccupied_3;
	}

	public void setScgmaskOccupied_3(Long scgmaskOccupied_3) {
		this.scgmaskOccupied_3 = scgmaskOccupied_3;
	}

	public Long getTotalBW_3() {
		return totalBW_3;
	}

	public void setTotalBW_3(Long totalBW_3) {
		this.totalBW_3 = totalBW_3;
	}

	public Long getTotalDownPwr_3() {
		return totalDownPwr_3;
	}

	public void setTotalDownPwr_3(Long totalDownPwr_3) {
		this.totalDownPwr_3 = totalDownPwr_3;
	}

	public Long getTotalTakenPwr_3() {
		return totalTakenPwr_3;
	}

	public void setTotalTakenPwr_3(Long totalTakenPwr_3) {
		this.totalTakenPwr_3 = totalTakenPwr_3;
	}

	public Long getScgmaskAvailable_4() {
		return scgmaskAvailable_4;
	}

	public void setScgmaskAvailable_4(Long scgmaskAvailable_4) {
		this.scgmaskAvailable_4 = scgmaskAvailable_4;
	}

	public Long getScgmaskOccupied_4() {
		return scgmaskOccupied_4;
	}

	public void setScgmaskOccupied_4(Long scgmaskOccupied_4) {
		this.scgmaskOccupied_4 = scgmaskOccupied_4;
	}

	public Long getTotalBW_4() {
		return totalBW_4;
	}

	public void setTotalBW_4(Long totalBW_4) {
		this.totalBW_4 = totalBW_4;
	}

	public Long getTotalDownPwr_4() {
		return totalDownPwr_4;
	}

	public void setTotalDownPwr_4(Long totalDownPwr_4) {
		this.totalDownPwr_4 = totalDownPwr_4;
	}

	public Long getTotalTakenPwr_4() {
		return totalTakenPwr_4;
	}

	public void setTotalTakenPwr_4(Long totalTakenPwr_4) {
		this.totalTakenPwr_4 = totalTakenPwr_4;
	}

	public Long getScgmaskAvailable_5() {
		return scgmaskAvailable_5;
	}

	public void setScgmaskAvailable_5(Long scgmaskAvailable_5) {
		this.scgmaskAvailable_5 = scgmaskAvailable_5;
	}

	public Long getScgmaskOccupied_5() {
		return scgmaskOccupied_5;
	}

	public void setScgmaskOccupied_5(Long scgmaskOccupied_5) {
		this.scgmaskOccupied_5 = scgmaskOccupied_5;
	}

	public Long getTotalBW_5() {
		return totalBW_5;
	}

	public void setTotalBW_5(Long totalBW_5) {
		this.totalBW_5 = totalBW_5;
	}

	public Long getTotalDownPwr_5() {
		return totalDownPwr_5;
	}

	public void setTotalDownPwr_5(Long totalDownPwr_5) {
		this.totalDownPwr_5 = totalDownPwr_5;
	}

	public Long getTotalTakenPwr_5() {
		return totalTakenPwr_5;
	}

	public void setTotalTakenPwr_5(Long totalTakenPwr_5) {
		this.totalTakenPwr_5 = totalTakenPwr_5;
	}

	public Long getScgmaskAvailable_6() {
		return scgmaskAvailable_6;
	}

	public void setScgmaskAvailable_6(Long scgmaskAvailable_6) {
		this.scgmaskAvailable_6 = scgmaskAvailable_6;
	}

	public Long getScgmaskOccupied_6() {
		return scgmaskOccupied_6;
	}

	public void setScgmaskOccupied_6(Long scgmaskOccupied_6) {
		this.scgmaskOccupied_6 = scgmaskOccupied_6;
	}

	public Long getTotalBW_6() {
		return totalBW_6;
	}

	public void setTotalBW_6(Long totalBW_6) {
		this.totalBW_6 = totalBW_6;
	}

	public Long getTotalDownPwr_6() {
		return totalDownPwr_6;
	}

	public void setTotalDownPwr_6(Long totalDownPwr_6) {
		this.totalDownPwr_6 = totalDownPwr_6;
	}

	public Long getTotalTakenPwr_6() {
		return totalTakenPwr_6;
	}

	public void setTotalTakenPwr_6(Long totalTakenPwr_6) {
		this.totalTakenPwr_6 = totalTakenPwr_6;
	}

	public Long getScgmaskAvailable_7() {
		return scgmaskAvailable_7;
	}

	public void setScgmaskAvailable_7(Long scgmaskAvailable_7) {
		this.scgmaskAvailable_7 = scgmaskAvailable_7;
	}

	public Long getScgmaskOccupied_7() {
		return scgmaskOccupied_7;
	}

	public void setScgmaskOccupied_7(Long scgmaskOccupied_7) {
		this.scgmaskOccupied_7 = scgmaskOccupied_7;
	}

	public Long getTotalBW_7() {
		return totalBW_7;
	}

	public void setTotalBW_7(Long totalBW_7) {
		this.totalBW_7 = totalBW_7;
	}

	public Long getTotalDownPwr_7() {
		return totalDownPwr_7;
	}

	public void setTotalDownPwr_7(Long totalDownPwr_7) {
		this.totalDownPwr_7 = totalDownPwr_7;
	}

	public Long getTotalTakenPwr_7() {
		return totalTakenPwr_7;
	}

	public void setTotalTakenPwr_7(Long totalTakenPwr_7) {
		this.totalTakenPwr_7 = totalTakenPwr_7;
	}

	public Long getScgmaskAvailable_8() {
		return scgmaskAvailable_8;
	}

	public void setScgmaskAvailable_8(Long scgmaskAvailable_8) {
		this.scgmaskAvailable_8 = scgmaskAvailable_8;
	}

	public Long getScgmaskOccupied_8() {
		return scgmaskOccupied_8;
	}

	public void setScgmaskOccupied_8(Long scgmaskOccupied_8) {
		this.scgmaskOccupied_8 = scgmaskOccupied_8;
	}

	public Long getTotalBW_8() {
		return totalBW_8;
	}

	public void setTotalBW_8(Long totalBW_8) {
		this.totalBW_8 = totalBW_8;
	}

	public Long getTotalDownPwr_8() {
		return totalDownPwr_8;
	}

	public void setTotalDownPwr_8(Long totalDownPwr_8) {
		this.totalDownPwr_8 = totalDownPwr_8;
	}

	public Long getTotalTakenPwr_8() {
		return totalTakenPwr_8;
	}

	public void setTotalTakenPwr_8(Long totalTakenPwr_8) {
		this.totalTakenPwr_8 = totalTakenPwr_8;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNeId() {
		return neId;
	}

	public void setNeId(Long neId) {
		this.neId = neId;
	}

	public Long getPfEndTime() {
		return pfEndTime;
	}

	public void setPfEndTime(Long pfEndTime) {
		this.pfEndTime = pfEndTime;
	}

	public String getPfEndTime2() {
		return pfEndTime2;
	}

	public void setPfEndTime2(String pfEndTime2) {
		this.pfEndTime2 = pfEndTime2;
	}

	public Long getPfStartTime() {
		return pfStartTime;
	}

	public void setPfStartTime(Long pfStartTime) {
		this.pfStartTime = pfStartTime;
	}

	public String getPfStartTime2() {
		return pfStartTime2;
	}

	public void setPfStartTime2(String pfStartTime2) {
		this.pfStartTime2 = pfStartTime2;
	}

	public Long getUpCI_1_1() {
		return upCI_1_1;
	}

	public void setUpCI_1_1(Long upCI_1_1) {
		this.upCI_1_1 = upCI_1_1;
	}

	public Long getUpCI_1_2() {
		return upCI_1_2;
	}

	public void setUpCI_1_2(Long upCI_1_2) {
		this.upCI_1_2 = upCI_1_2;
	}

	public Long getUpCI_1_3() {
		return upCI_1_3;
	}

	public void setUpCI_1_3(Long upCI_1_3) {
		this.upCI_1_3 = upCI_1_3;
	}

	public Long getUpCI_1_4() {
		return upCI_1_4;
	}

	public void setUpCI_1_4(Long upCI_1_4) {
		this.upCI_1_4 = upCI_1_4;
	}

	public Long getUpCI_1_5() {
		return upCI_1_5;
	}

	public void setUpCI_1_5(Long upCI_1_5) {
		this.upCI_1_5 = upCI_1_5;
	}

	public Long getUpCI_2_1() {
		return upCI_2_1;
	}

	public void setUpCI_2_1(Long upCI_2_1) {
		this.upCI_2_1 = upCI_2_1;
	}

	public Long getUpCI_2_2() {
		return upCI_2_2;
	}

	public void setUpCI_2_2(Long upCI_2_2) {
		this.upCI_2_2 = upCI_2_2;
	}

	public Long getUpCI_2_3() {
		return upCI_2_3;
	}

	public void setUpCI_2_3(Long upCI_2_3) {
		this.upCI_2_3 = upCI_2_3;
	}

	public Long getUpCI_2_4() {
		return upCI_2_4;
	}

	public void setUpCI_2_4(Long upCI_2_4) {
		this.upCI_2_4 = upCI_2_4;
	}

	public Long getUpCI_2_5() {
		return upCI_2_5;
	}

	public void setUpCI_2_5(Long upCI_2_5) {
		this.upCI_2_5 = upCI_2_5;
	}

	public Long getUpCI_3_1() {
		return upCI_3_1;
	}

	public void setUpCI_3_1(Long upCI_3_1) {
		this.upCI_3_1 = upCI_3_1;
	}

	public Long getUpCI_3_2() {
		return upCI_3_2;
	}

	public void setUpCI_3_2(Long upCI_3_2) {
		this.upCI_3_2 = upCI_3_2;
	}

	public Long getUpCI_3_3() {
		return upCI_3_3;
	}

	public void setUpCI_3_3(Long upCI_3_3) {
		this.upCI_3_3 = upCI_3_3;
	}

	public Long getUpCI_3_4() {
		return upCI_3_4;
	}

	public void setUpCI_3_4(Long upCI_3_4) {
		this.upCI_3_4 = upCI_3_4;
	}

	public Long getUpCI_3_5() {
		return upCI_3_5;
	}

	public void setUpCI_3_5(Long upCI_3_5) {
		this.upCI_3_5 = upCI_3_5;
	}

	public Long getUpCI_4_1() {
		return upCI_4_1;
	}

	public void setUpCI_4_1(Long upCI_4_1) {
		this.upCI_4_1 = upCI_4_1;
	}

	public Long getUpCI_4_2() {
		return upCI_4_2;
	}

	public void setUpCI_4_2(Long upCI_4_2) {
		this.upCI_4_2 = upCI_4_2;
	}

	public Long getUpCI_4_3() {
		return upCI_4_3;
	}

	public void setUpCI_4_3(Long upCI_4_3) {
		this.upCI_4_3 = upCI_4_3;
	}

	public Long getUpCI_4_4() {
		return upCI_4_4;
	}

	public void setUpCI_4_4(Long upCI_4_4) {
		this.upCI_4_4 = upCI_4_4;
	}

	public Long getUpCI_4_5() {
		return upCI_4_5;
	}

	public void setUpCI_4_5(Long upCI_4_5) {
		this.upCI_4_5 = upCI_4_5;
	}

	public Long getUpCI_5_1() {
		return upCI_5_1;
	}

	public void setUpCI_5_1(Long upCI_5_1) {
		this.upCI_5_1 = upCI_5_1;
	}

	public Long getUpCI_5_2() {
		return upCI_5_2;
	}

	public void setUpCI_5_2(Long upCI_5_2) {
		this.upCI_5_2 = upCI_5_2;
	}

	public Long getUpCI_5_3() {
		return upCI_5_3;
	}

	public void setUpCI_5_3(Long upCI_5_3) {
		this.upCI_5_3 = upCI_5_3;
	}

	public Long getUpCI_5_4() {
		return upCI_5_4;
	}

	public void setUpCI_5_4(Long upCI_5_4) {
		this.upCI_5_4 = upCI_5_4;
	}

	public Long getUpCI_5_5() {
		return upCI_5_5;
	}

	public void setUpCI_5_5(Long upCI_5_5) {
		this.upCI_5_5 = upCI_5_5;
	}

	public Long getUpCI_6_1() {
		return upCI_6_1;
	}

	public void setUpCI_6_1(Long upCI_6_1) {
		this.upCI_6_1 = upCI_6_1;
	}

	public Long getUpCI_6_2() {
		return upCI_6_2;
	}

	public void setUpCI_6_2(Long upCI_6_2) {
		this.upCI_6_2 = upCI_6_2;
	}

	public Long getUpCI_6_3() {
		return upCI_6_3;
	}

	public void setUpCI_6_3(Long upCI_6_3) {
		this.upCI_6_3 = upCI_6_3;
	}

	public Long getUpCI_6_4() {
		return upCI_6_4;
	}

	public void setUpCI_6_4(Long upCI_6_4) {
		this.upCI_6_4 = upCI_6_4;
	}

	public Long getUpCI_6_5() {
		return upCI_6_5;
	}

	public void setUpCI_6_5(Long upCI_6_5) {
		this.upCI_6_5 = upCI_6_5;
	}

	public Long getUpCI_7_1() {
		return upCI_7_1;
	}

	public void setUpCI_7_1(Long upCI_7_1) {
		this.upCI_7_1 = upCI_7_1;
	}

	public Long getUpCI_7_2() {
		return upCI_7_2;
	}

	public void setUpCI_7_2(Long upCI_7_2) {
		this.upCI_7_2 = upCI_7_2;
	}

	public Long getUpCI_7_3() {
		return upCI_7_3;
	}

	public void setUpCI_7_3(Long upCI_7_3) {
		this.upCI_7_3 = upCI_7_3;
	}

	public Long getUpCI_7_4() {
		return upCI_7_4;
	}

	public void setUpCI_7_4(Long upCI_7_4) {
		this.upCI_7_4 = upCI_7_4;
	}

	public Long getUpCI_7_5() {
		return upCI_7_5;
	}

	public void setUpCI_7_5(Long upCI_7_5) {
		this.upCI_7_5 = upCI_7_5;
	}

	public Long getUpCI_8_1() {
		return upCI_8_1;
	}

	public void setUpCI_8_1(Long upCI_8_1) {
		this.upCI_8_1 = upCI_8_1;
	}

	public Long getUpCI_8_2() {
		return upCI_8_2;
	}

	public void setUpCI_8_2(Long upCI_8_2) {
		this.upCI_8_2 = upCI_8_2;
	}

	public Long getUpCI_8_3() {
		return upCI_8_3;
	}

	public void setUpCI_8_3(Long upCI_8_3) {
		this.upCI_8_3 = upCI_8_3;
	}

	public Long getUpCI_8_4() {
		return upCI_8_4;
	}

	public void setUpCI_8_4(Long upCI_8_4) {
		this.upCI_8_4 = upCI_8_4;
	}

	public Long getUpCI_8_5() {
		return upCI_8_5;
	}

	public void setUpCI_8_5(Long upCI_8_5) {
		this.upCI_8_5 = upCI_8_5;
	}

	public Long getUpScgmaskAvailable_1() {
		return upScgmaskAvailable_1;
	}

	public void setUpScgmaskAvailable_1(Long upScgmaskAvailable_1) {
		this.upScgmaskAvailable_1 = upScgmaskAvailable_1;
	}

	public Long getUpScgmaskAvailable_2() {
		return upScgmaskAvailable_2;
	}

	public void setUpScgmaskAvailable_2(Long upScgmaskAvailable_2) {
		this.upScgmaskAvailable_2 = upScgmaskAvailable_2;
	}

	public Long getUpScgmaskAvailable_3() {
		return upScgmaskAvailable_3;
	}

	public void setUpScgmaskAvailable_3(Long upScgmaskAvailable_3) {
		this.upScgmaskAvailable_3 = upScgmaskAvailable_3;
	}

	public Long getUpScgmaskAvailable_4() {
		return upScgmaskAvailable_4;
	}

	public void setUpScgmaskAvailable_4(Long upScgmaskAvailable_4) {
		this.upScgmaskAvailable_4 = upScgmaskAvailable_4;
	}

	public Long getUpScgmaskAvailable_5() {
		return upScgmaskAvailable_5;
	}

	public void setUpScgmaskAvailable_5(Long upScgmaskAvailable_5) {
		this.upScgmaskAvailable_5 = upScgmaskAvailable_5;
	}

	public Long getUpScgmaskAvailable_6() {
		return upScgmaskAvailable_6;
	}

	public void setUpScgmaskAvailable_6(Long upScgmaskAvailable_6) {
		this.upScgmaskAvailable_6 = upScgmaskAvailable_6;
	}

	public Long getUpScgmaskAvailable_7() {
		return upScgmaskAvailable_7;
	}

	public void setUpScgmaskAvailable_7(Long upScgmaskAvailable_7) {
		this.upScgmaskAvailable_7 = upScgmaskAvailable_7;
	}

	public Long getUpScgmaskAvailable_8() {
		return upScgmaskAvailable_8;
	}

	public void setUpScgmaskAvailable_8(Long upScgmaskAvailable_8) {
		this.upScgmaskAvailable_8 = upScgmaskAvailable_8;
	}

	public Long getUpScgmaskOccupied_1() {
		return upScgmaskOccupied_1;
	}

	public void setUpScgmaskOccupied_1(Long upScgmaskOccupied_1) {
		this.upScgmaskOccupied_1 = upScgmaskOccupied_1;
	}

	public Long getUpScgmaskOccupied_2() {
		return upScgmaskOccupied_2;
	}

	public void setUpScgmaskOccupied_2(Long upScgmaskOccupied_2) {
		this.upScgmaskOccupied_2 = upScgmaskOccupied_2;
	}

	public Long getUpScgmaskOccupied_3() {
		return upScgmaskOccupied_3;
	}

	public void setUpScgmaskOccupied_3(Long upScgmaskOccupied_3) {
		this.upScgmaskOccupied_3 = upScgmaskOccupied_3;
	}

	public Long getUpScgmaskOccupied_4() {
		return upScgmaskOccupied_4;
	}

	public void setUpScgmaskOccupied_4(Long upScgmaskOccupied_4) {
		this.upScgmaskOccupied_4 = upScgmaskOccupied_4;
	}

	public Long getUpScgmaskOccupied_5() {
		return upScgmaskOccupied_5;
	}

	public void setUpScgmaskOccupied_5(Long upScgmaskOccupied_5) {
		this.upScgmaskOccupied_5 = upScgmaskOccupied_5;
	}

	public Long getUpScgmaskOccupied_6() {
		return upScgmaskOccupied_6;
	}

	public void setUpScgmaskOccupied_6(Long upScgmaskOccupied_6) {
		this.upScgmaskOccupied_6 = upScgmaskOccupied_6;
	}

	public Long getUpScgmaskOccupied_7() {
		return upScgmaskOccupied_7;
	}

	public void setUpScgmaskOccupied_7(Long upScgmaskOccupied_7) {
		this.upScgmaskOccupied_7 = upScgmaskOccupied_7;
	}

	public Long getUpScgmaskOccupied_8() {
		return upScgmaskOccupied_8;
	}

	public void setUpScgmaskOccupied_8(Long upScgmaskOccupied_8) {
		this.upScgmaskOccupied_8 = upScgmaskOccupied_8;
	}
}
