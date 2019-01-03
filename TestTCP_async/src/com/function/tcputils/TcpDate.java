package com.function.tcputils;

import android.util.Log;   

/**≥–‘ÿ≤„*/
public class TcpDate {
	private static final String TAG = "TcpDate";
	public byte stx;
	public int len;
	public byte enc;
	public byte[] app;
	public int chk;
	public byte etx;

	public boolean checkPacket() {
		if (chk == countCheck(app, len)) {
			return true;
		}
		return false;
	}

	private int countCheck(byte[] app, int length) {
		int check = 0;
		int i = 0;
		for (i = 0; i < length; i++) {
			check += (byte) app[i] & 0xFF;
		}
		return check;
	}

}