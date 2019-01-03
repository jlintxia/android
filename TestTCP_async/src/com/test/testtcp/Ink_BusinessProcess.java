package com.test.testtcp;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.function.tcputils.TcpProcess;
import com.guozheng.okhttputils.CallBackUtil;
import com.guozheng.okhttputils.HttpProcess;
import com.test.testtcp.ThemeDataModle_Ink.AlarmInfoType;
import com.test.testtcp.ThemeDataModle_Ink.ArtifactVerType;
import com.test.testtcp.ThemeDataModle_Ink.BrInfo;
import com.test.testtcp.ThemeDataModle_Ink.CX_Lib;
import com.test.testtcp.ThemeDataModle_Ink.CX_SCREEN;
import com.test.testtcp.ThemeDataModle_Ink.CabinetInit;
import com.test.testtcp.ThemeDataModle_Ink.EquInfoType;
import com.test.testtcp.ThemeDataModle_Ink.EquParamInfoType;
import com.test.testtcp.ThemeDataModle_Ink.ErrorType;
import com.test.testtcp.ThemeDataModle_Ink.OperatePadbookType;
import com.test.testtcp.ThemeDataModle_Ink.PadbookBrType;
import com.test.testtcp.ThemeDataModle_Ink.PadbookType;
import com.test.testtcp.ThemeDataModle_Ink.PadbookUserType;
import com.test.testtcp.ThemeDataModle_Ink.Padbook_order_deposit;
import com.test.testtcp.ThemeDataModle_Ink.PadbookaisleInstType;
import com.test.testtcp.ThemeDataModle_Ink.PageInfo;
import com.test.testtcp.ThemeDataModle_Ink.QrCodeType;
import com.test.testtcp.ThemeDataModle_Ink.RegionType;
import com.test.testtcp.ThemeDataModle_Ink.ScreenResourceType;
import com.test.testtcp.ThemeDataModle_Ink.SiteType;
import com.test.testtcp.ThemeDataModle_Ink.SubBorrowReturn;
import com.test.testtcp.ThemeDataModle_Ink.Update;

import android.content.Context;
import android.util.Log;

/**墨水屏柜平台业务通讯解析发送*/
public class Ink_BusinessProcess extends BusinessProcess {

	private static final String TAG = "BusinessProcess";

	public static final int SEND_MODE_HTTP = 1;
	public static final int SEND_MODE_TCP = 2;

	private TcpProcess mTcpProcess;

	public interface BusinessProcessCallBack {
		//业务回调
		public void analysisCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object);

		//连接失败超时等
		public void connectFail();
	}

	public Ink_BusinessProcess(TcpProcess tcpProcess, HttpProcess mHttpProcess, Context context,
			com.test.testtcp.BusinessProcess.BusinessProcessCallBack businessProcessCallBack) {
		super(tcpProcess, mHttpProcess, context, businessProcessCallBack);
	}

	//=================================具体业务====================================================
	//=================================具体业务====================================================
	/**同步区域*/
	public void sync_region(String userName, PageInfo pageInfo, int sendMode) {
		JSONObject json = new JSONObject();
		try {
			json.put("userName", userName);
			JSONObject data = null;
			if (pageInfo != null) {
				data = new JSONObject(JSON.toJSONString(pageInfo));
			}
			json.put("pageInfo", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		SystemManager.LOGI(TAG, "同步区域 = " + json.toString());
		send(BusinessCmd.PACKET_CMD_SYNC_REGION, json, sendMode);

	}

	/**解析同步区域*/
	public void unSync_region(boolean receive, byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析同步区域");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析同步区域 = " + json.toString());
					ErrorType errorType = (ErrorType) json.get("error");
					List<RegionType> regionTypes = (List<RegionType>) json.get("regionInfoList");
					PageInfo pageInfo = (PageInfo) json.get("pageInfo");
					sendCallback(cmd, errorType, pageInfo, regionTypes);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**同步场所*/
	public void sync_SiteType(String userName, PageInfo pageInfo, int sendMode) {
		JSONObject json = new JSONObject();
		if (userName != null) {
			try {
				json.put("userName", userName);
				JSONObject data = null;
				if (pageInfo != null) {
					data = new JSONObject(JSON.toJSONString(pageInfo));
				}
				json.put("pageInfo", data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		SystemManager.LOGI(TAG, "同步场所 = " + json.toString());
		send(BusinessCmd.PACKET_CMD_SYNC_SITE, json, sendMode);
	}

	/**解析同步同步场所*/
	public void unSync_SiteType(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析同步场所");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析同步场所 = " + json.toString());
					ErrorType errorType = (ErrorType) json.get("error");
					List<SiteType> regionTypes = (List<SiteType>) json.get("siteInfoList");
					PageInfo pageInfo = (PageInfo) json.get("pageInfo");
					sendCallback(cmd, errorType, pageInfo, regionTypes);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**同步时间*/
	public void sync_Time(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "同步时间");
		common(BusinessCmd.PACKET_CMD_SYNC_TIME, equCode, sendMode);
	}

	/**解析同步时间*/
	public void unSync_Time(boolean receive, byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析同步时间");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析同步时间 = " + json.toString());
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					String time = (String) data.get("time");
					sendCallback(cmd, errorType, null, time);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**管理员注册终端设备*/
	public void equRegist(EquInfoType equInfoType, int sendMode) {
		JSONObject json = new JSONObject();
		if (equInfoType != null) {
			try {
				json.put("equCode", equInfoType.equCode);
				json.put("equSn", equInfoType.equSn);
				json.put("equName", equInfoType.equName);
				json.put("equType", equInfoType.equType);

				json.put("siteId", equInfoType.siteId);
				json.put("siteName", equInfoType.siteName);
				json.put("provinceId", equInfoType.provinceId);
				json.put("provinceName", equInfoType.provinceName);
				json.put("cityId", equInfoType.cityId);
				json.put("cityName", equInfoType.cityName);
				json.put("areaId", equInfoType.areaId);
				json.put("areaName", equInfoType.areaName);

				SystemManager.LOGI(TAG, "设备注册 = " + json.toString());
				send(BusinessCmd.PACKET_CMD_REGISTER, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析设备注册*/
	public void unEquRegist(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析设备注册");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**获取屏保资源*/
	public void get_Screensaver(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "获取屏保资源");
		common(BusinessCmd.PACKET_CMD_SYSN_SCREEN, equCode, sendMode);
	}

	/**解析获取屏保资源*/
	public void unGet_Screensaver(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析获取屏保资源");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析获取屏保资源 = " + json.toString());
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					ScreenResourceType screenResourceType = (ScreenResourceType) JsonUtil.parseObject(data.get("data"),
							ScreenResourceType.class);
					sendCallback(cmd, errorType, null, screenResourceType);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**借墨水屏数据提交接口*/
	public void sub_Borrow(SubBorrowReturn subBorrowReturn, int sendMode) {
		JSONObject json = new JSONObject();
		org.json.JSONArray list = new org.json.JSONArray();
		if (subBorrowReturn != null) {
			try {
				json.put("userName", subBorrowReturn.userName);
				json.put("certCode", subBorrowReturn.certCode);
				json.put("areaId", subBorrowReturn.areaId);
				json.put("areaCode", subBorrowReturn.areaCode);
				json.put("areaName", subBorrowReturn.areaName);
				json.put("siteId", subBorrowReturn.siteId);
				json.put("siteCode", subBorrowReturn.siteCode);
				json.put("siteName", subBorrowReturn.siteName);
				json.put("equCode", subBorrowReturn.equCode);
				json.put("equName", subBorrowReturn.equName);
				if (subBorrowReturn.padbookInfoList != null) {
					for (int i = 0; i < subBorrowReturn.padbookInfoList.size(); i++) {
						String data = JSON.toJSONString(subBorrowReturn.padbookInfoList.get(i));
						list.put(i, new JSONObject(data));
					}
					json.put("padbookInfoList", list);
				}
				SystemManager.LOGI(TAG, "借墨水瓶数据提交 = " + json.toString());
				send(BusinessCmd.PACKET_CMD_INK_BORROW, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析借(书/借墨水瓶)数据提交接口*/
	public void unSub_Borrow(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析借(书/借墨水瓶)数据");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**还(书/借墨水瓶)数据提交接口*/
	public void sub_Return(SubBorrowReturn subBorrowReturn, int sendMode) {
		JSONObject json = new JSONObject();
		org.json.JSONArray list = new org.json.JSONArray();
		if (subBorrowReturn != null) {
			try {
				json.put("userName", subBorrowReturn.userName);
				json.put("certCode", subBorrowReturn.certCode);
				json.put("areaId", subBorrowReturn.areaId);
				json.put("areaCode", subBorrowReturn.areaCode);
				json.put("areaName", subBorrowReturn.areaName);
				json.put("siteId", subBorrowReturn.siteId);
				json.put("siteCode", subBorrowReturn.siteCode);
				json.put("siteName", subBorrowReturn.siteName);
				json.put("equCode", subBorrowReturn.equCode);
				json.put("equName", subBorrowReturn.equName);
				if (subBorrowReturn.padbookInfoList != null) {
					for (int i = 0; i < subBorrowReturn.padbookInfoList.size(); i++) {
						String data = JSON.toJSONString(subBorrowReturn.padbookInfoList.get(i));
						list.put(i, new JSONObject(data));
					}
					json.put("padbookInfoList", list);
				}
				SystemManager.LOGI(TAG, "还(书/借墨水瓶)数据提交 = " + json.toString());
				send(BusinessCmd.PACKET_CMD_INK_RETRUN, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析还(书/借墨水瓶)数据提交接口*/
	public void unSub_Return(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析还(书/借墨水瓶)数据");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**设备参数查询接口*/
	public void get_EquParam(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "设备参数查询接口");
		common(BusinessCmd.PACKET_CMD_QUE_EQUPARAMINFO, equCode, sendMode);
	}

	/**解析设备参数查询接口*/
	public void unGet_Equparam(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析设备参数查询");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析设备参数查询 = " + json.toString());
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					List<EquParamInfoType> equParamList = JSONArray.parseArray(data.getString("equParamList"),
							EquParamInfoType.class);
					sendCallback(cmd, errorType, null, equParamList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**查询版本号接口*/
	public void que_version(String appId, String equType, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "查询版本号接口");
		JSONObject json = new JSONObject();
		if (appId != null) {
			try {
				json.put("appId", appId);
				json.put("equCode", equCode);
				json.put("equType", equType);
				send(BusinessCmd.PACKET_CMD_QUE_VERSION, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析查询版本号接口*/
	public void unQue_version(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析查询版本号接口");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析查询版本号接口 = " + json.toString());
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					Update update = new Update();
					update.verNo = (String) data.getString("verNo");
					update.verDesc = (String) data.getString("verDesc");
					update.downloadUrl = (String) data.getString("downloadUrl");
					update.md5Check = (String) data.getString("md5Check");
					update.subject = (String) data.getString("subject");
					update.artifactList = JSONArray.parseArray(data.getString("artifactList"), ArtifactVerType.class);

					sendCallback(cmd, errorType, null, update);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**设备墨水屏信息操作接口*/
	public void sync_OperatePadbookInfo(String equCode, OperatePadbookType operatePadbookType, int sendMode) {
		SystemManager.LOGI(TAG, "设备墨水屏信息操作接口");
		JSONObject json = new JSONObject();
		if (equCode != null && operatePadbookType != null) {
			try {
				json.put("equCode", equCode);
				String padbookInfo = JSON.toJSONString(operatePadbookType.padbookInfo);
				json.put("padbookInfo", new JSONObject(padbookInfo));
				json.put("operateType", operatePadbookType.operateType);
				send(BusinessCmd.PACKET_CMD_OPERATION_EQUINK, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析设备墨水屏信息操作接口*/
	public void unSync_OperatePadbookInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "设备墨水屏信息操作接口");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**根据用户查询借书记录接口 */
	public void que_UserBorrowRecord(String userName, String certCode, String cardNum, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "根据用户查询借书记录接口");
		JSONObject json = new JSONObject();
		if (equCode != null && cardNum != null) {
			try {
				json.put("userName", userName);
				json.put("certCode", certCode);
				json.put("equCode", equCode);
				json.put("cardNum", cardNum);
				send(BusinessCmd.PACKET_CMD_QUE_USER_BORROW_RECORD, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析根据用户查询借书记录接口*/
	public void unQue_UserBorrowRecord(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析根据用户查询借书记录接口");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.getString("error"), ErrorType.class);
					List<PadbookBrType> padbookBrList = JSONArray.parseArray(data.getString("padbookBrList"),
							PadbookBrType.class);
					sendCallback(cmd, errorType, null, padbookBrList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**设备信息同步接口*/
	public void sync_EquInfo(String equCode, EquInfoType equInfo, int sendMode) {
		SystemManager.LOGI(TAG, "设备信息同步");

		JSONObject json = new JSONObject();
		try {
			json.put("equCode", equCode);
			json.put("equInfo", new JSONObject(JSON.toJSONString(equInfo)));
			send(BusinessCmd.PACKET_CMD_SYSN_EQUINFO, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**解析设备信息同步接口*/
	public void unSync_EquInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析设备信息同步");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**设备信息查询接口*/
	public void get_EquInfo(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "设备信息查询接");
		common(BusinessCmd.PACKET_CMD_QUE_EQUINFO, equCode, sendMode);
	}

	/**解析设备信息查询接口*/
	public void unGet_EquInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析设备信息查询接");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "解析设备信息查询接 = " + json.toString());
					EquInfoType equInfoType = (EquInfoType) JsonUtil.parseObject(data.get("equInfo"),
							EquInfoType.class);
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					sendCallback(cmd, errorType, null, equInfoType);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**向平台查询设备货道信息接口*/
	public void que_EquPadbookInfo(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "向平台查询设备货道信息接口");
		common(BusinessCmd.PACKET_CMD_QUE_AISLE, equCode, sendMode);
	}

	/**向平台查询设备货道信息接口*/
	public void unQue_EquPadbookInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "向平台查询设备货道信息接口");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) json.get("error");

					List<PadbookType> aisleInstTypeList = JSONArray.parseArray(json.getString("padbookInfoList"),
							PadbookType.class);
					sendCallback(cmd, errorType, null, aisleInstTypeList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**设备告警信息提交接口*/
	public void sub_Alarm(String equCode, List<AlarmInfoType> alarmInfoList, int sendMode) {
		SystemManager.LOGI(TAG, "设备告警信息提交接口");

		JSONObject json = new JSONObject();
		org.json.JSONArray list = new org.json.JSONArray();
		if (equCode != null && alarmInfoList != null) {
			try {
				json.put("equCode", equCode);
				if (alarmInfoList != null) {
					for (int i = 0; i < alarmInfoList.size(); i++) {
						String data = JSON.toJSONString(alarmInfoList.get(i));
						list.put(i, new JSONObject(data));
					}
					json.put("alarmInfoList", list);
				}
				send(BusinessCmd.PACKET_CMD_EQU_ALARM, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**解析设备告警信息提交接口*/
	public void unSub_Alarm(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析设备告警信息提交接口");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/** 查询墨水屏息接口*/
	public void que_padBookInfo(String padbookCode, PageInfo pageInfo, int sendMode) {
		SystemManager.LOGI(TAG, "查询墨水屏息接口");

		JSONObject json = new JSONObject();
		try {
			json.put("padbookCode", padbookCode);
			JSONObject data = null;
			if (pageInfo != null) {
				data = new JSONObject(JSON.toJSONString(pageInfo));
			}
			json.put("pageInfo", data);
			send(BusinessCmd.PACKET_CMD_QUE_INKINFO, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析查询墨水屏息接口*/
	public void unQue_padBookInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析查询墨水屏息接口");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					List<PadbookType> bookInfoList = com.alibaba.fastjson.JSONArray
							.parseArray(data.getString("padbookInfoList"), PadbookType.class);
					PageInfo pageInfo = (PageInfo) JsonUtil.parseObject(data.get("pageInfo"), PageInfo.class);
					sendCallback(cmd, errorType, pageInfo, bookInfoList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}
	//===================================================================================
	/**破损图书下架提交接口*/

	//===================================================================================
	/**押金信息上送*/
	public void sub_Deposit(Padbook_order_deposit padbook_order_depositInfo, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "押金信息上送");
		JSONObject json = new JSONObject();
		try {

			if (padbook_order_depositInfo != null) {
				String str = JSON.toJSONString(padbook_order_depositInfo);
				json.put("padbook_order_depositInfo", str);
				json.put("equCode", equCode);
			}

			send(BusinessCmd.PACKET_CMD_SUB_DEPOSIT, json, sendMode);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析押金信息上送*/
	public void unSub_Deposit(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析押金信息上送");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) data.get("error");
					QrCodeType qrCode = (QrCodeType) data.get("qrCode");
					sendCallback(cmd, errorType, null, qrCode);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**查询订单支付结果*/
	public void que_Deposit(String equCode, String depositOrder, int sendMode) {
		SystemManager.LOGI(TAG, "查询订单支付结果");
		JSONObject json = new JSONObject();
		try {

			if (depositOrder != null) {
				json.put("equCode", equCode);
				json.put("depositOrder", depositOrder);
			}
			send(BusinessCmd.PACKET_CMD_QUE_DEPOSIT, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**解析查询订单支付结果*/
	public void unQue_Deposit(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析查询订单支付结果");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) json.get("error");
					Padbook_order_deposit padbook_order_depositInfo = (Padbook_order_deposit) json
							.get("padbook_order_depositInfo");
					sendCallback(cmd, errorType, null, padbook_order_depositInfo);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**用户注册 */
	public void register_User(PadbookUserType userType, int sendMode) {
		SystemManager.LOGI(TAG, "用户注册");
		JSONObject json = new JSONObject();
		try {
			json.put("userName", userType.userName);
			json.put("realName", userType.realName);
			json.put("password", userType.password);
			json.put("certCode", userType.certCode);
			json.put("phone", userType.phone);
			send(BusinessCmd.PACKET_CMD_REGISTER_USER, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析用户注册*/
	public void unRegister_User(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析用户注册");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**查询用户信息接口*/
	public void que_UserInfo(PadbookUserType userType, int sendMode) {
		SystemManager.LOGI(TAG, "设备查询用户信息接口");

		JSONObject json = new JSONObject();
		try {
			json.put("userName", userType.userName);
			json.put("realName", userType.realName);
			json.put("certCode", userType.certCode);
			json.put("phone", userType.phone);
			json.put("cardNum", userType.cardNum);
			send(BusinessCmd.PACKET_CMD_QUE_USER, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析查询用户信息接口*/
	public void unQue_UserInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析查询用户信息接口");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					List<PadbookUserType> userTypes = JSONArray.parseArray(data.getString("padbookUserList"),
							PadbookUserType.class);
					sendCallback(cmd, errorType, null, userTypes);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**同步用户信息接口*/
	public void sync_UserInfo(PadbookUserType userType, int sendMode) {
		SystemManager.LOGI(TAG, "同步用户信息接口");

		JSONObject json = new JSONObject();
		try {
			String str = JSON.toJSONString(userType);
			json.put("padbookUserInfo", str);
			send(BusinessCmd.PACKET_CMD_SYNC_USER, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析同步用户信息接口*/
	public void unSync_UserInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析同步用户信息接口");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**墨水屏初始化货道*/
	public void init_aisle(List<PadbookaisleInstType> padbookaisleInstTypeList, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "墨水屏初始化货道");

		JSONObject json = new JSONObject();
		org.json.JSONArray list = new org.json.JSONArray();
		try {
			json.put("equCode", equCode);
			if (padbookaisleInstTypeList != null) {
				for (int i = 0; i < padbookaisleInstTypeList.size(); i++) {
					String data = JSON.toJSONString(padbookaisleInstTypeList.get(i));
					list.put(i, new JSONObject(data));
				}
				json.put("padbookaisleInstTypeList", list);
			}
			send(BusinessCmd.PACKET_CMD_INIT_AISLE, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析墨水屏初始化货道*/
	public void unInit_aisle(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析墨水屏初始化货道");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					List<PadbookaisleInstType> padbookaisleInstTypeList = JSONArray
							.parseArray(data.getString("padbookaisleInstTypeList"), PadbookaisleInstType.class);

					sendCallback(cmd, errorType, null, padbookaisleInstTypeList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**墨水屏初始化货柜*/
	public void init_cabinet(List<CabinetInit> cabinetInitList, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "墨水屏初始化货道");

		JSONObject json = new JSONObject();
		org.json.JSONArray list = new org.json.JSONArray();
		try {
			json.put("equCode", equCode);
			if (cabinetInitList != null) {
				for (int i = 0; i < cabinetInitList.size(); i++) {
					String data = JSON.toJSONString(cabinetInitList.get(i));
					list.put(i, new JSONObject(data));
				}
				json.put("cabinetInitList", list);
			}
			send(BusinessCmd.PACKET_CMD_INIT_CABINET, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析墨水屏初始化货柜*/
	public void unInit_cabinet(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析墨水屏初始化货柜");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {

					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.getString("error"), ErrorType.class);
					List<CabinetInit> cabinetInitList = JSONArray.parseArray(data.getString("cabinetInitList"),
							CabinetInit.class);
					sendCallback(cmd, errorType, null, cabinetInitList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**墨水屏入库*/
	public void Ink_Warehousing(List<PadbookType> padbookTypes, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "墨水屏入库");

		JSONObject json = new JSONObject();
		org.json.JSONArray list = new org.json.JSONArray();
		try {
			json.put("equCode", equCode);
			if (padbookTypes != null) {
				for (int i = 0; i < padbookTypes.size(); i++) {
					String data = JSON.toJSONString(padbookTypes.get(i));
					list.put(i, new JSONObject(data));
				}
				json.put("padBookInfoTypeList", list);
			}
			send(BusinessCmd.PACKET_CMD_INK_WAREHOUSING, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析墨水屏入库*/
	public void unInk_Warehousing(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析墨水屏入库");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					ErrorType errorType = (ErrorType) JsonUtil.parseObject(data.get("error"), ErrorType.class);
					List<PadbookType> padBookInfoTypeList = JSONArray.parseArray(json.getString("padBookInfoTypeList"),
							PadbookType.class);
					sendCallback(cmd, errorType, null, padBookInfoTypeList);
				} else {
					unDataNull(cmd);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//===================================================================================
	/**设备参数设置*/
	public void set_equPara(EquParamInfoType equParamInfoType, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "设备参数设置");
		JSONObject json = new JSONObject();
		try {
			json.put("equCode", equCode);
			String equParam = JSON.toJSONString(equParamInfoType);
			json.put("equParam", new JSONObject(equParam));
			send(BusinessCmd.PACKET_CMD_SET_EQUPARAM, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析设备参数设置*/
	public void unset_equPara(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析设备参数设置");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**借墨水瓶*/
	public void borrow_Ink(BrInfo brInfo, int sendMode) {
		SystemManager.LOGI(TAG, "借墨水瓶");
		JSONObject json = new JSONObject();
		try {
			String data = JSON.toJSONString(brInfo);
			json.put("brInfo", new JSONObject(data));
			send(BusinessCmd.PACKET_CMD_BORROW_INK, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析借墨水瓶*/
	public void unBorrow_Ink(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析借墨水瓶");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**还墨水瓶*/
	public void Return_Ink(BrInfo brInfo, int sendMode) {
		SystemManager.LOGI(TAG, "还墨水瓶");
		JSONObject json = new JSONObject();
		try {
			String data = JSON.toJSONString(brInfo);
			json.put("brInfo", new JSONObject(data));
			send(BusinessCmd.PACKET_CMD_INIT_RETURN_INK, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析还墨水瓶*/
	public void unReturn_Ink(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析还墨水瓶");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**墨水屏上下架*/
	public void UpDown_Ink(PadbookType padBookInfoType, String equCode, String operation, int sendMode) {
		SystemManager.LOGI(TAG, "墨水屏上下架");
		JSONObject json = new JSONObject();
		try {
			String data = JSON.toJSONString(padBookInfoType);
			json.put("padbookInfoList", new JSONObject(data));
			json.put("equCode", equCode);
			json.put("operation", operation);
			send(BusinessCmd.PACKET_CMD_SUB_RETURNINFO, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**解析墨水屏上下架*/
	public void unUpDown_Ink(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "解析墨水屏上下架");
		unCommon(cmd, sjson);
	}

	//超星接口解析========================================================================= 
	protected void unChaoXing(byte cmd, String sjson) {
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				//区域
//				data = json.getJSONObject("data");
//				Log.e(TAG, "data " + data.toString());
//				List<CX_Region> cabinetInitList = JSONArray.parseArray(data.getString("items"), CX_Region.class);
//				Log.e(TAG, "cabinetInitList " + cabinetInitList.size());
//				for (int i = 0; i < cabinetInitList.size(); i++) {
//					Log.e(TAG, "cabinetInitList " + cabinetInitList.get(i).name);
//				}
				//图书馆
				data = json.getJSONObject("data");
				Log.e(TAG, "data " + data.toString());
				List<CX_Lib> cabinetInitList = JSONArray.parseArray(data.getString("items"), CX_Lib.class);
				Log.e(TAG, "cabinetInitList " + cabinetInitList.size());
				for (int i = 0; i < cabinetInitList.size(); i++) {
					Log.e(TAG, "cabinetInitList " + cabinetInitList.get(i).name);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			unJsonFail(cmd);
		}
	}

	//获取屏保
	public void unChaoXing_screen(byte cmd, String sjson) {
		com.alibaba.fastjson.JSONObject json = null;
		JSONObject data = null;
//		json =  JSON.parseObject(sjson);
		if (sjson != null) {
			List<CX_SCREEN> cabinetInitList = JSONArray.parseArray(sjson, CX_SCREEN.class);
			for (int i = 0; i < cabinetInitList.size(); i++) {
				Log.e(TAG, "cabinetInitList " + cabinetInitList.get(i).pic);
//			}
			}
			//区域
//			  JSONArray family = json.getJSONArray("");
			//图书馆
//				data = json.getJSONObject("data");
//				Log.e(TAG, "data " + data.toString());
//				
//				Log.e(TAG, "cabinetInitList " + cabinetInitList.size());
//				for (int i = 0; i < cabinetInitList.size(); i++) {
//					Log.e(TAG, "cabinetInitList " + cabinetInitList.get(i).name);
//				}
			sendCallback(cmd, null, null, cabinetInitList);
		}
	}

	//屏保资源下载
	public void downScreen(String url,CallBackUtil.CallBackFile callBackFile ) {
		send((byte)0, url,callBackFile, SEND_MODE_HTTP_DOWNLOAD);
	}

	//====================================================================================
	/**最外层*/
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

	//====================================================
	public static class JsonUtil {
		public static <T> T parseObject(String text, Class<T> clazz) {
			return JSON.parseObject(text, clazz);
		}

		public static <T> T parseObject(Object obj, Class<T> clazz) {
			return JSON.parseObject(JSON.toJSONString(obj), clazz);
		}
	}
}
