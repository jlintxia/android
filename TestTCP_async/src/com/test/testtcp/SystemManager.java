package com.test.testtcp;

import android.util.Log;

public class SystemManager {
	public static SystemManager Instacne = new SystemManager();

	private static final boolean isLogInfoOn = true;
	private static final boolean isLogErrOn = true;
	private static final boolean isLogWarnOn = true;
	private static final boolean isLogDbgOn = true;


	public static void LOGI(String tag, String msg) {
		if (isLogInfoOn) {
			Log.i(tag, msg);
		}
	}

	public static void LOGE(String tag, String msg) {
		if (isLogErrOn) {
			Log.e(tag, msg);
		}
	}

	public static void LOGW(String tag, String msg) {
		if (isLogWarnOn) {
			Log.w(tag, msg);
		}
	}

	public static void LOGD(String tag, String msg) {
		if (isLogDbgOn) {
			Log.d(tag, msg);
		}
	}

	public static void LogHex(String tag, byte[] data, int datalen) {
		String hexStr = "";
		for (int i = 0; i < datalen; i++) {

			if ((i % 16) == 0) {
				//Log.d(tag, hexStr);
				System.out.println("发送的数据1："+hexStr);
				hexStr = "";
			}
			if (Integer.toHexString(data[i] & 0xff).length() == 1) {
				hexStr += "0";
			}
			hexStr += Integer.toHexString(data[i] & 0xff).toUpperCase();
		}
		//Log.d(tag, hexStr);
		System.out.println("发送的数据2："+hexStr);
	}

	//char转int类型
	public static int charToInt(char c) {
		int i = 0;
		if ((c & 0x80) != 0) {
			i = c | 0xffffff00;
		} else {
			i = (int) c;
		}
		return i;
	}

	public static void LOGI(String tag, String msg, String reMark) {
		if (isLogInfoOn) {
			Log.i(tag, msg + " (" + reMark + ")");
		}
	}

	public static void LOGE(String tag, String msg, String reMark) {
		if (isLogErrOn) {
			Log.e(tag, msg + " (" + reMark + ")");
		}
	}

	public static void LOGW(String tag, String msg, String reMark) {
		if (isLogWarnOn) {
			Log.w(tag, msg + " (" + reMark + ")");
		}
	}

	public static void LOGD(String tag, String msg, String reMark) {
		if (isLogDbgOn) {
			Log.d(tag, msg + " (" + reMark + ")");
		}
	}

	public static void LogHex(String tag, byte[] data, int datalen, String reMark) {
		String hexStr = "";
		for (int i = 0; i < datalen; i++) {

			if ((i % 16) == 0) {
				Log.d(tag, hexStr);
				hexStr = "";
			}
			if (Integer.toHexString(data[i] & 0xff).length() == 1) {
				hexStr += "0";
			}
			hexStr += Integer.toHexString(data[i] & 0xff).toUpperCase() + " ";
		}
		Log.d(tag, hexStr + " (" + reMark + ")");
	}

}
