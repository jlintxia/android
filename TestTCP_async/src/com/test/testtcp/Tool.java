package com.test.testtcp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class Tool {
	/**
	 * 计算md5
	 * 
	 * @param string
	 * @return
	 */
	public static String getMd5ByString(String string) {
		if (TextUtils.isEmpty(string)) {
			return null;
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(string.getBytes());
			String result = "";
			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result += temp;
			}
			return result.toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTime() {

		long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳

		String str = String.valueOf(time);

		return str;

	}
}
