package com.xinwei.lte.web.uem.model;

import org.apache.poi.ss.formula.functions.T;

public interface ParaConverter<T> {
	String convert(T Para);
}
