package com.xinwei.omp.core.utils;

public class EnvironmentUtils {
	// ����������ģʽ����
	private static final String MINAS_WORK_MODE_SIMPLE = "simple";

	// ����������ģʽ����
	private static final String MINAS_WORK_MODE_NETWORK = "network";

	public static boolean isSimpleMinas() {
		String mode = System.getProperty("minasWorkMode");
		if (mode == null)
			return false;

		return mode.equalsIgnoreCase(MINAS_WORK_MODE_SIMPLE);
	}

	public static String getProperty(String key) {
		if (key == null || key.length() == 0)
			return null;

		return System.getProperty(key);
	}
}
