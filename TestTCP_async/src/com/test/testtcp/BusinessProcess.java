package com.test.testtcp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.function.tcputils.TcpProcess;
import com.guozheng.okhttputils.CallBackUtil;
import com.guozheng.okhttputils.HttpProcess;
import com.test.testtcp.Ink_BusinessProcess.JsonUtil;
import com.test.testtcp.ThemeDataModle_Ink.ErrorType;
import com.test.testtcp.ThemeDataModle_Ink.PageInfo;

import android.content.Context;

/** ��ƽ̨ҵ��ͨѶ��������*/
public class BusinessProcess {
	private static final String TAG = "BusinessProcess";

	public static final int SEND_MODE_HTTP = 1;
	public static final int SEND_MODE_TCP = 2;
	public static final int SEND_MODE_HTTP_DOWNLOAD = 3;

	private TcpProcess mTcpProcess;
	private HttpProcess mHttpProcess;
	private Context mContext;
	protected BusinessProcessCallBack businessProcessCallBack;

	public interface BusinessProcessCallBack {
		//ҵ��ص�
		public void analysisCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object);

		//����ʧ�ܳ�ʱ��
		public void connectFail();
	}

	public BusinessProcess(TcpProcess tcpProcess, HttpProcess mHttpProcess, Context context,
			BusinessProcessCallBack businessProcessCallBack) {
		this.mTcpProcess = tcpProcess;
		this.mHttpProcess = mHttpProcess;
		this.mContext = context;
		this.businessProcessCallBack = businessProcessCallBack;
	}

	/**�ص������Ϣ*/
	protected void sendCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object) {
		businessProcessCallBack.analysisCallback(cmd, errorType, pageInfo, object);
	}

	/**json��������*/
	protected void unJsonFail(byte cmd) {
		ErrorType errorType = new ErrorType();
		errorType.id = "9999";
		errorType.message = "json��������";
		sendCallback(cmd, errorType, null, null);
	}

	/**Data��Ϊ��*/
	protected void unDataNull(byte cmd) {
		ErrorType errorType = new ErrorType();
		errorType.id = "9999";
		errorType.message = "Data��Ϊ��";
		sendCallback(cmd, errorType, null, null);
	}
  
	/**��������*/
	protected void send(byte cmd, JSONObject json, int sendMode) {
		JSONObject data = new JSONObject();
		data = pack(json);
		if (sendMode == SEND_MODE_TCP) {
			if (mTcpProcess != null) {
				mTcpProcess.send(cmd, data.toString());
			}

		} else if (sendMode == SEND_MODE_HTTP) {
			if (mHttpProcess != null) {
				mHttpProcess.send(cmd, data.toString());
			}
		}  
	}

	/**��������*/
	protected void send(byte cmd, String url, CallBackUtil.CallBackFile callBackFile,int sendMode) {
		if (sendMode == SEND_MODE_HTTP_DOWNLOAD) {
			if (mHttpProcess != null) {
				mHttpProcess.httpDownload(url,callBackFile);
			}
		}
	}

	/**ͨ������ӿ�*/
	protected void common(byte cmd, String equCode, int sendMode) {
		JSONObject json = new JSONObject();
		if (equCode != null) {
			try {
				json.put("equCode", equCode);
				send(cmd, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**ͨ�ý��*/
	protected void unCommon(byte cmd, String sjson) {
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					sendCallback(cmd, errorType, null, null);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	/**���ɽ��ճ�ʱ����*/
	protected void errorBuild() {
		ErrorType errorType = new ErrorType();
		errorType.id = "9999";
		errorType.message = "����ʱ";
	}

	/**�����*/
	public JSONObject pack(JSONObject data) {
		JSONObject packdata = new JSONObject();

		try {
			packdata.put("appId", "1101");
			packdata.put("tranId", "");
			packdata.put("keyId", "EQU_CODE");
			packdata.put("keyValue", "123456");
			packdata.put("versionCode", "");
			packdata.put("data", data);

			return packdata;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String gettime(String pattern) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		String date = sDateFormat.format(new Date());
		return date;
	}
}
