package com.function.tcputils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.function.tool.MyEncrypt;
import com.test.testtcp.SystemManager;

import android.util.Log;

public class TcpProtocol {
	private static final String TAG = "TcpProtocol";

	public static final byte PACKET_STX = (byte) 0xF3;
	public static final byte PACKET_ETX = (byte) 0xF4;   

	public static final byte[] FinalBao = { 0x38, 0x33, 0x35, 0x39, 0x32, 0x36, 0x30, 0x30 };//8字节固定数据
	public byte[] PacketKey = new byte[8];//加密密钥 启动会话analisys中获取
	public static byte[] ServerSn = new byte[8];//解包时填入，用于应答平台请求使用
	public static byte EncryptType = Encrypt.ENCRYPT_TYPE_NO;//解包后根据平台信息修改

	public byte[] myID = new byte[8];
	public String serverID = "00000000";//每次解包时填入
	public byte[] MyKey = new byte[8];//加密密钥 启动鉴权时生成填入

	public static String charset = "UTF-8";

	/**设备类型*///FROM为接入系统即下发命令，为其他即平台应答设备
	public static class FormToType {
		public static final byte SYSTEM_BOOK = (byte) 0x00;//借书柜系统
		public static final byte SYSTEM_LNK = (byte) 0x01;//墨水瓶柜系统
		public static final byte SYSTEM_SMART_HONE = (byte) 0XA2;//智能家居
		//===============================================
		public static final byte SYSTEM_ACCESS = (byte) 0xB0;//接入系统
	}

	/**加密模式*/
	public static class Encrypt {
		public static final byte ENCRYPT_TYPE_NO = 0x00; //不加密
		public static final byte ENCRYPT_TYPE_3DES = 0x01; //3DES
		public static final byte ENCRYPT_TYPE_DES = 0x02; //DES
		public static final byte ENCRYPT_TYPE_AES = 0x03; //AES
		public static final byte ENCRYPT_TYPE_RSA = 0x04; //RSA
	}

	/**命令代码*/
	public static class Cmd {
		public static final byte PACKET_CMD_START = (byte) 0x01; //开始连接
		public static final byte PACKET_CMD_END = (byte) 0x02; //结束会话
		public static final byte PACKET_CMD_AUTH = (byte) 0x03; //鉴权
		public static final byte PACKET_CMD_HEART = (byte) 0x04; //心跳
	}

	/*
	 * 解包APP
	 * app 需要解析的app数据域
	 * applen  长度
	 * 返回值  AppData 解析后的数据
	 */
	public AppData unpackApp(byte[] app, int applen) {

		AppData appdata = new AppData();  
		int len = 0;
		//判断从哪个系统过来的数据
		appdata.from = app[len];
		SystemManager.LOGI(TAG, "from =" + appdata.from);
		len += 2;

		int src_len = (int) (app[len] & 0xFF);
		len += 1;
		serverID = new String(app, len, src_len);
		appdata.src_id = serverID;
		len += src_len;

		int dst_len = (int) (app[len] & 0xFF);
		len += 1;
		String dst_id = new String(app, len, dst_len);
		appdata.dst_id = dst_id;
		len += dst_len;

		System.arraycopy(app, len, ServerSn, 0, 8);//不校验sn
		appdata.sn = ServerSn;
		len += 8;

		appdata.cmd = app[len];
		len++;

		int length = (int) ((app[len] & 0xFF) | ((app[len + 1] & 0xFF) << 8));
		len += 2;

		byte[] data = new byte[length];
		System.arraycopy(app, len, data, 0, length);
		appdata.data = data;
		appdata.len = length;
		return appdata;
	}

	/**请求打包
	 * @throws UnsupportedEncodingException */
	public int request(AppData appData, byte[] out) throws UnsupportedEncodingException {
		byte[] outApp = new byte[15 + appData.len + appData.src_id.getBytes(TcpProtocol.charset).length
				+ appData.dst_id.getBytes(TcpProtocol.charset).length + appData.len];
		int applen = packApp(appData, outApp);
		return pack(outApp, applen, out);
	}

	/**打包协议层*/
	private int packApp(AppData appData, byte[] outApp) {
		int len = 0;
		try {
			outApp[len] = appData.from;
			len++;
			outApp[len] = appData.to;
			len++;
			outApp[len] = (byte) (appData.src_id.getBytes(charset).length & 0xff);
			len++;
			System.arraycopy(appData.src_id.getBytes(charset), 0, outApp, len, appData.src_id.getBytes(charset).length);
			len += appData.src_id.getBytes(charset).length;

			outApp[len] = (byte) (appData.dst_id.getBytes(charset).length & 0xff);
			len++;
			System.arraycopy(appData.dst_id.getBytes(charset), 0, outApp, len, appData.dst_id.getBytes(charset).length);
			len += appData.dst_id.getBytes(charset).length;

			System.arraycopy(appData.sn, 0, outApp, len, 8);
			len += 8;
			outApp[len] = appData.cmd;
			len += 1;
			outApp[len] = (byte) (appData.len & 0xFF);
			outApp[len + 1] = (byte) ((appData.len >> 8) & 0xFF);
			len += 2;
			if (appData.len > 0) {
				System.arraycopy(appData.data, 0, outApp, len, appData.len);
				len += appData.len;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return len;

	}

	/**打包承载层*/
	private int pack(byte[] outApp, int applen, byte[] out) {
		int len = 0;
		out[len] = PACKET_STX;
		len++;
		out[len] = (byte) (applen & 0xFF);
		out[len + 1] = (byte) ((applen >> 8) & 0xFF);//APP长度
		len += 2;
		out[len] = EncryptType;//加密模式
		len++;
		int chk = countCheck(outApp, applen);//校验值
		switch (EncryptType) {//加密
		case Encrypt.ENCRYPT_TYPE_3DES: {
			try {
				outApp = MyEncrypt.des3Encrypt(outApp, PacketKey);
				if (outApp == null) {
					return 0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			break;
		case Encrypt.ENCRYPT_TYPE_DES: {
			try {
				outApp = MyEncrypt.desEncrypt(outApp, PacketKey);
				if (outApp == null) {
					return 0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case Encrypt.ENCRYPT_TYPE_AES:
			break;
		case Encrypt.ENCRYPT_TYPE_RSA:
			break;
		}
		System.arraycopy(outApp, 0, out, len, applen);//加密
		len += applen;
		out[len] = (byte) (chk & 0xff);
		out[len + 1] = (byte) ((chk >> 8) & 0xff);
		out[len + 2] = (byte) ((chk >> 16) & 0xff);
		out[len + 3] = (byte) ((chk >> 24) & 0xff);//校验值
		len += 4;
		out[len++] = PACKET_ETX;
		return len;
	}

	/**sn获取 请求方填写*/
	public static byte[] getSn() {
		byte[] date = new byte[9];
		Calendar CD = Calendar.getInstance();
		int YY = CD.get(Calendar.YEAR);
		int MM = CD.get(Calendar.MONTH) + 1;
		int DD = CD.get(Calendar.DATE);
		int HH = CD.get(Calendar.HOUR);
		int NN = CD.get(Calendar.MINUTE);
		int SS = CD.get(Calendar.SECOND);
		int MI = CD.get(Calendar.MILLISECOND);
		date[0] = (byte) (YY & 0xFF);
		date[1] = (byte) (MM & 0xFF);
		date[2] = (byte) (DD & 0xFF);
		date[3] = (byte) (HH & 0xFF);
		date[4] = (byte) (NN & 0xFF);
		date[5] = (byte) (SS & 0xFF);
		date[6] = (byte) (MI & 0xFF);
		date[7] = (byte) ((MM >> 8) & 0xFF);
		SystemManager.LogHex(TAG + "sn", date, 8);
		return date;
	}

	/**域数据相加和*/
	private int countCheck(byte[] app, int length) {
		int check = 0;
		int i = 0;
		for (i = 0; i < length; i++) {
			check += (byte) app[i] & 0xFF;
		}
		return check;
	}

	/**随机数生成*/
	public String getrandom(boolean numberFlag, int length) {

		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);
		return retStr;
	}

	/**byte数组比对*/
	public boolean checkByte(byte[] src, byte[] dst, int len) {
		int i = 0;
		for (i = 0; i < len; i++) {
			if (src[i] != dst[i]) {
				return false;
			}
		}
		return true;
	}
}
