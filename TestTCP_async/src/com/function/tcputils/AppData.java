package com.function.tcputils;

import java.util.Arrays;

/**–≠“È≤„*/
public class AppData {
	public byte from;
	public byte to;
	public String src_id;
	public String dst_id;
	public byte[] sn;
	public byte cmd;
	public int len;
	public byte[] data;

	@Override
	public String toString() {
		return "AppData [from=" + from + ", to=" + to + ", src_id=" + src_id + ", dst_id=" + dst_id + ", sn="
				+ Arrays.toString(sn) + ", cmd=" + cmd + ", len=" + len + ", data=" + Arrays.toString(data) + "]";
	}

}
