/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-24	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * 
 * @author tiance
 * 
 */

public class Complex {

	private double realPart;
	private double imaginPart;

	public Complex(double r, double i) {
		realPart = r;
		imaginPart = i;
	}

	public Complex() {

		this(0, 0);
	}

	/**
	 * @return Returns the imaginPart.
	 */
	public double getImaginPart() {
		return imaginPart;
	}

	/**
	 * @param imaginPart
	 *            The imaginPart to set.
	 */
	public void setImaginPart(double imaginPart) {
		this.imaginPart = imaginPart;
	}

	/**
	 * @return Returns the realPart.
	 */
	public double getRealPart() {
		return realPart;
	}

	/**
	 * @param realPart
	 *            The realPart to set.
	 */
	public void setRealPart(double realPart) {
		this.realPart = realPart;
	}

	public void addComplex(Complex toAdd) {
		this.imaginPart = this.imaginPart + toAdd.imaginPart;
		this.realPart = this.realPart + toAdd.realPart;
	}

	public void subComplex(Complex toSub) {
		this.imaginPart = this.imaginPart - toSub.imaginPart;
		this.realPart = this.realPart - toSub.realPart;
	}

	public void plugComplex(Complex toAdd) {
		double tempReal = this.realPart * toAdd.realPart - this.imaginPart
				* toAdd.imaginPart;
		double tempImage = this.realPart * toAdd.imaginPart + this.imaginPart
				* toAdd.realPart;
		this.imaginPart = tempImage;
		this.realPart = tempReal;
	}

	public void divComplex(Complex toDiv) {
		double tempDiv = toDiv.imaginPart * toDiv.imaginPart + toDiv.realPart
				* toDiv.realPart;
		double tempReal = (this.realPart * toDiv.realPart + this.imaginPart
				* toDiv.imaginPart)
				/ tempDiv;
		double tempImag = (this.imaginPart * toDiv.realPart - this.realPart
				* toDiv.imaginPart)
				/ tempDiv;
		this.imaginPart = tempImag;
		this.realPart = tempReal;
	}

	public double angle() {
		return Math.atan2(this.imaginPart, this.realPart);
	}

	public Object clone() {
		try {
			Complex ret = (Complex) super.clone();
			ret.imaginPart = this.imaginPart;
			ret.realPart = this.realPart;
			return ret;
		} catch (Exception ex) {
			return null;
		}
	}

	public double abs() {
		double ret = this.imaginPart * this.imaginPart + this.realPart
				* this.realPart;
		return Math.sqrt(ret);
	}

	public static Complex divComplex(Complex num1, Complex num2) {
		double tempDiv = num2.imaginPart * num2.imaginPart + num2.realPart
				* num2.realPart;
		if (tempDiv == 0)
			return new Complex(0, 0);
		double tempReal = (num1.realPart * num2.realPart + num1.imaginPart
				* num2.imaginPart)
				/ tempDiv;
		double tempImag = (num1.imaginPart * num2.realPart - num1.realPart
				* num2.imaginPart)
				/ tempDiv;
		Complex ret = new Complex();
		ret.imaginPart = tempImag;
		ret.realPart = tempReal;
		return ret;
	}

	public static void main(String[] args) {
		Complex ret = new Complex(30, -4);
		System.out.println(getValue(ret));

	}

	private static double getValue(Complex calData) {
		double ret = 0;
		ret = calData.angle() * 180 / Math.PI;
		return ret;
	}

	public double ABS2() {
		double ret = 0;
		ret = this.abs();
		if (ret == 0)
			return 0;
		ret = Math.log(ret) / Math.log(10);
		ret = ret * 20;
		return ret;
	}
}
