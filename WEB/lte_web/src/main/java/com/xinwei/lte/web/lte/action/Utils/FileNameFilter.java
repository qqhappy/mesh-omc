package com.xinwei.lte.web.lte.action.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * 文件名过滤器
 * 
 * @author zhangqiang
 * 
 */
public class FileNameFilter implements FilenameFilter {

	private Pattern pattern;

	public FileNameFilter(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	public boolean accept(File file, String name) {
		return pattern.matcher(name).matches();
	}
}
