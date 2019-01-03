package com.guozheng.okhttputils;

import java.util.HashMap;
import java.util.Map;

import com.test.testtcp.BusinessCmd;
import com.test.testtcp.SystemManager;
import com.test.testtcp.Tool;

import android.content.Context;

public class HttpProcess {
	private static final String TAG = "HttpProcess";

	private Context mContext;
	private CallBackUtil.CallBackString callBackString;

	public HttpProcess(Context mContext) {
		this.mContext = mContext;
	}

	public void setHttpCallBack(CallBackUtil.CallBackString callBackString) {
		this.callBackString = callBackString;

	}

	//提供businessManage调用
	public void send(byte cmd, String json) {
		String url = null;
		switch (cmd) {
		//同步区域信息接口
		case BusinessCmd.PACKET_CMD_SYNC_REGION:
			url = "http://192.168.10.111:8090/zyschool/api/auth/qryAreaInfo";
			break;
		//同步场所信息接口
		case BusinessCmd.PACKET_CMD_SYNC_SITE:
			break;
		//同步时间 接口
		case BusinessCmd.PACKET_CMD_SYNC_TIME:
			url = "http://192.168.10.119:8080/zypadbook/api/interface/serverTime";
			break;
		//管理员注册终端设备接口
		case BusinessCmd.PACKET_CMD_REGISTER:
//			url = "http://192.168.10.119:8080/zyschool/padbookmachine/equRegist";
//			url = "http://192.168.10.119:8080/zypadbook/api/equInfo/equRegist";
			url = "http://192.168.10.162:8080/platform-admin/api/auth/regiestEqu";
			break;
		//押金信息提交 
		case BusinessCmd.PACKET_CMD_SUB_DEPOSIT:
			url = "http://192.168.10.107:8089/zyschool/pay/depositResult";
			break;
		//押金支付结果查询 
		case BusinessCmd.PACKET_CMD_QUE_DEPOSIT:
			url = "http://192.168.10.107:8089/zyschool/pay/querydepositResult";
			break;
		//超星清理屏信息
		//屏保获取
		case BusinessCmd.PACKET_CMD_SYSN_SCREEN:
			url = "http://book.jieyueji.cn/server/manage?cmd=getScreenUrl&t=30001&sort=ID&order=desc&offset=0&limit=100&search=0";
			break;
		case BusinessCmd.PACKET_CMD_QUE_VERSION:
			url = "http://192.168.10.119:28080/web-unified/doorplate/getEquVersionInfo.action";
//			url = "http://192.168.10.119:28080/web-unified/doorplate/getVersionInfo.action";
			break;
		default:
			break;
		}
		if (url != null) {
			httpPost(cmd, url, json);
			return;
		} else {
			if (cmd == BusinessCmd.PACKET_CMD_QUE_CLEAN) {
				url = "http://reader.chaoxingbook.com";
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("device_code", "123456");
				paramsMap.put("library_id", "1");
				httpPost_CX(url, paramsMap);
			}
		}
		SystemManager.LOGD(TAG, "cmd = " + cmd + "url is null");

	}

	/** post 请求项目平台 */
	private void httpPost(byte cmd, String url, String json) {
		SystemManager.LOGD(TAG, "===== 发送请求 =====cmd = " + url + " json " + json);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json; charset=UTF-8");
		OkhttpUtil.okHttpPostJson(cmd, mContext, url, json, headerMap, callBackString);

	}

	/** Get 请求超星 */
	public void httpGet(String url, Map<String, String> paramsMap) {
		String time = Tool.getTime();
		String token = Tool.getMd5ByString(time + "t&jrCPNpJgrFkmjE");
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("token", token);
		headMap.put("timestamp", time);
		OkhttpUtil.okHttpGet((byte) 0, mContext, url, paramsMap, headMap, callBackString);
	}

	/** Get 请求超星 */
	public void httpGet2(String url, Map<String, String> paramsMap) {
		String time = Tool.getTime();
		String token = Tool.getMd5ByString(time + "t&jrCPNpJgrFkmjE");
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("token", token);
		headMap.put("timestamp", time);
		OkhttpUtil.okHttpGet((byte) 0, mContext, url, paramsMap, null, callBackString);
	}

	/** post 请求超星 */
	public void httpPost_CX(String url, Map<String, String> paramsMap) {
		String time = Tool.getTime();
		String token = Tool.getMd5ByString(time + "t&jrCPNpJgrFkmjE");
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("token", token);
		headMap.put("timestamp", time);
		OkhttpUtil.okHttpPost((byte) 0, mContext, url, paramsMap, headMap, callBackString);
	}

	/** 下载*/
	public void httpDownload(String url, CallBackUtil.CallBackFile callBackFile) {
		OkhttpUtil.okHttpDownloadFile((byte) 0x80, mContext, url, callBackFile);
	}
}
