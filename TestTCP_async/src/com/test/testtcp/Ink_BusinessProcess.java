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

/**īˮ����ƽ̨ҵ��ͨѶ��������*/
public class Ink_BusinessProcess extends BusinessProcess {

	private static final String TAG = "BusinessProcess";

	public static final int SEND_MODE_HTTP = 1;
	public static final int SEND_MODE_TCP = 2;

	private TcpProcess mTcpProcess;

	public interface BusinessProcessCallBack {
		//ҵ��ص�
		public void analysisCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object);

		//����ʧ�ܳ�ʱ��
		public void connectFail();
	}

	public Ink_BusinessProcess(TcpProcess tcpProcess, HttpProcess mHttpProcess, Context context,
			com.test.testtcp.BusinessProcess.BusinessProcessCallBack businessProcessCallBack) {
		super(tcpProcess, mHttpProcess, context, businessProcessCallBack);
	}

	//=================================����ҵ��====================================================
	//=================================����ҵ��====================================================
	/**ͬ������*/
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
		SystemManager.LOGI(TAG, "ͬ������ = " + json.toString());
		send(BusinessCmd.PACKET_CMD_SYNC_REGION, json, sendMode);

	}

	/**����ͬ������*/
	public void unSync_region(boolean receive, byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����ͬ������");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "����ͬ������ = " + json.toString());
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
	/**ͬ������*/
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
		SystemManager.LOGI(TAG, "ͬ������ = " + json.toString());
		send(BusinessCmd.PACKET_CMD_SYNC_SITE, json, sendMode);
	}

	/**����ͬ��ͬ������*/
	public void unSync_SiteType(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����ͬ������");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "����ͬ������ = " + json.toString());
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
	/**ͬ��ʱ��*/
	public void sync_Time(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "ͬ��ʱ��");
		common(BusinessCmd.PACKET_CMD_SYNC_TIME, equCode, sendMode);
	}

	/**����ͬ��ʱ��*/
	public void unSync_Time(boolean receive, byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����ͬ��ʱ��");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "����ͬ��ʱ�� = " + json.toString());
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
	/**����Աע���ն��豸*/
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

				SystemManager.LOGI(TAG, "�豸ע�� = " + json.toString());
				send(BusinessCmd.PACKET_CMD_REGISTER, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**�����豸ע��*/
	public void unEquRegist(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����豸ע��");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**��ȡ������Դ*/
	public void get_Screensaver(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "��ȡ������Դ");
		common(BusinessCmd.PACKET_CMD_SYSN_SCREEN, equCode, sendMode);
	}

	/**������ȡ������Դ*/
	public void unGet_Screensaver(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������ȡ������Դ");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "������ȡ������Դ = " + json.toString());
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
	/**��īˮ�������ύ�ӿ�*/
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
				SystemManager.LOGI(TAG, "��īˮƿ�����ύ = " + json.toString());
				send(BusinessCmd.PACKET_CMD_INK_BORROW, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**������(��/��īˮƿ)�����ύ�ӿ�*/
	public void unSub_Borrow(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������(��/��īˮƿ)����");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**��(��/��īˮƿ)�����ύ�ӿ�*/
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
				SystemManager.LOGI(TAG, "��(��/��īˮƿ)�����ύ = " + json.toString());
				send(BusinessCmd.PACKET_CMD_INK_RETRUN, json, sendMode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**������(��/��īˮƿ)�����ύ�ӿ�*/
	public void unSub_Return(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������(��/��īˮƿ)����");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**�豸������ѯ�ӿ�*/
	public void get_EquParam(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "�豸������ѯ�ӿ�");
		common(BusinessCmd.PACKET_CMD_QUE_EQUPARAMINFO, equCode, sendMode);
	}

	/**�����豸������ѯ�ӿ�*/
	public void unGet_Equparam(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����豸������ѯ");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "�����豸������ѯ = " + json.toString());
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
	/**��ѯ�汾�Žӿ�*/
	public void que_version(String appId, String equType, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "��ѯ�汾�Žӿ�");
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

	/**������ѯ�汾�Žӿ�*/
	public void unQue_version(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������ѯ�汾�Žӿ�");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "������ѯ�汾�Žӿ� = " + json.toString());
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
	/**�豸īˮ����Ϣ�����ӿ�*/
	public void sync_OperatePadbookInfo(String equCode, OperatePadbookType operatePadbookType, int sendMode) {
		SystemManager.LOGI(TAG, "�豸īˮ����Ϣ�����ӿ�");
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

	/**�����豸īˮ����Ϣ�����ӿ�*/
	public void unSync_OperatePadbookInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�豸īˮ����Ϣ�����ӿ�");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**�����û���ѯ�����¼�ӿ� */
	public void que_UserBorrowRecord(String userName, String certCode, String cardNum, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "�����û���ѯ�����¼�ӿ�");
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

	/**���������û���ѯ�����¼�ӿ�*/
	public void unQue_UserBorrowRecord(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "���������û���ѯ�����¼�ӿ�");
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
	/**�豸��Ϣͬ���ӿ�*/
	public void sync_EquInfo(String equCode, EquInfoType equInfo, int sendMode) {
		SystemManager.LOGI(TAG, "�豸��Ϣͬ��");

		JSONObject json = new JSONObject();
		try {
			json.put("equCode", equCode);
			json.put("equInfo", new JSONObject(JSON.toJSONString(equInfo)));
			send(BusinessCmd.PACKET_CMD_SYSN_EQUINFO, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**�����豸��Ϣͬ���ӿ�*/
	public void unSync_EquInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����豸��Ϣͬ��");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**�豸��Ϣ��ѯ�ӿ�*/
	public void get_EquInfo(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "�豸��Ϣ��ѯ��");
		common(BusinessCmd.PACKET_CMD_QUE_EQUINFO, equCode, sendMode);
	}

	/**�����豸��Ϣ��ѯ�ӿ�*/
	public void unGet_EquInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����豸��Ϣ��ѯ��");
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				data = json.getJSONObject("data");
				if (data != null) {
					SystemManager.LOGI(TAG, "�����豸��Ϣ��ѯ�� = " + json.toString());
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
	/**��ƽ̨��ѯ�豸������Ϣ�ӿ�*/
	public void que_EquPadbookInfo(String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "��ƽ̨��ѯ�豸������Ϣ�ӿ�");
		common(BusinessCmd.PACKET_CMD_QUE_AISLE, equCode, sendMode);
	}

	/**��ƽ̨��ѯ�豸������Ϣ�ӿ�*/
	public void unQue_EquPadbookInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "��ƽ̨��ѯ�豸������Ϣ�ӿ�");
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
	/**�豸�澯��Ϣ�ύ�ӿ�*/
	public void sub_Alarm(String equCode, List<AlarmInfoType> alarmInfoList, int sendMode) {
		SystemManager.LOGI(TAG, "�豸�澯��Ϣ�ύ�ӿ�");

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

	/**�����豸�澯��Ϣ�ύ�ӿ�*/
	public void unSub_Alarm(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����豸�澯��Ϣ�ύ�ӿ�");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/** ��ѯīˮ��Ϣ�ӿ�*/
	public void que_padBookInfo(String padbookCode, PageInfo pageInfo, int sendMode) {
		SystemManager.LOGI(TAG, "��ѯīˮ��Ϣ�ӿ�");

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

	/**������ѯīˮ��Ϣ�ӿ�*/
	public void unQue_padBookInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������ѯīˮ��Ϣ�ӿ�");
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
	/**����ͼ���¼��ύ�ӿ�*/

	//===================================================================================
	/**Ѻ����Ϣ����*/
	public void sub_Deposit(Padbook_order_deposit padbook_order_depositInfo, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "Ѻ����Ϣ����");
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

	/**����Ѻ����Ϣ����*/
	public void unSub_Deposit(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����Ѻ����Ϣ����");
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
	/**��ѯ����֧�����*/
	public void que_Deposit(String equCode, String depositOrder, int sendMode) {
		SystemManager.LOGI(TAG, "��ѯ����֧�����");
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

	/**������ѯ����֧�����*/
	public void unQue_Deposit(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������ѯ����֧�����");
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
	/**�û�ע�� */
	public void register_User(PadbookUserType userType, int sendMode) {
		SystemManager.LOGI(TAG, "�û�ע��");
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

	/**�����û�ע��*/
	public void unRegister_User(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����û�ע��");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**��ѯ�û���Ϣ�ӿ�*/
	public void que_UserInfo(PadbookUserType userType, int sendMode) {
		SystemManager.LOGI(TAG, "�豸��ѯ�û���Ϣ�ӿ�");

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

	/**������ѯ�û���Ϣ�ӿ�*/
	public void unQue_UserInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������ѯ�û���Ϣ�ӿ�");
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
	/**ͬ���û���Ϣ�ӿ�*/
	public void sync_UserInfo(PadbookUserType userType, int sendMode) {
		SystemManager.LOGI(TAG, "ͬ���û���Ϣ�ӿ�");

		JSONObject json = new JSONObject();
		try {
			String str = JSON.toJSONString(userType);
			json.put("padbookUserInfo", str);
			send(BusinessCmd.PACKET_CMD_SYNC_USER, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**����ͬ���û���Ϣ�ӿ�*/
	public void unSync_UserInfo(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����ͬ���û���Ϣ�ӿ�");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**īˮ����ʼ������*/
	public void init_aisle(List<PadbookaisleInstType> padbookaisleInstTypeList, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "īˮ����ʼ������");

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

	/**����īˮ����ʼ������*/
	public void unInit_aisle(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����īˮ����ʼ������");
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
	/**īˮ����ʼ������*/
	public void init_cabinet(List<CabinetInit> cabinetInitList, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "īˮ����ʼ������");

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

	/**����īˮ����ʼ������*/
	public void unInit_cabinet(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����īˮ����ʼ������");
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
	/**īˮ�����*/
	public void Ink_Warehousing(List<PadbookType> padbookTypes, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "īˮ�����");

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

	/**����īˮ�����*/
	public void unInk_Warehousing(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����īˮ�����");
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
	/**�豸��������*/
	public void set_equPara(EquParamInfoType equParamInfoType, String equCode, int sendMode) {
		SystemManager.LOGI(TAG, "�豸��������");
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

	/**�����豸��������*/
	public void unset_equPara(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "�����豸��������");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**��īˮƿ*/
	public void borrow_Ink(BrInfo brInfo, int sendMode) {
		SystemManager.LOGI(TAG, "��īˮƿ");
		JSONObject json = new JSONObject();
		try {
			String data = JSON.toJSONString(brInfo);
			json.put("brInfo", new JSONObject(data));
			send(BusinessCmd.PACKET_CMD_BORROW_INK, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**������īˮƿ*/
	public void unBorrow_Ink(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������īˮƿ");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**��īˮƿ*/
	public void Return_Ink(BrInfo brInfo, int sendMode) {
		SystemManager.LOGI(TAG, "��īˮƿ");
		JSONObject json = new JSONObject();
		try {
			String data = JSON.toJSONString(brInfo);
			json.put("brInfo", new JSONObject(data));
			send(BusinessCmd.PACKET_CMD_INIT_RETURN_INK, json, sendMode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**������īˮƿ*/
	public void unReturn_Ink(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "������īˮƿ");
		unCommon(cmd, sjson);
	}

	//===================================================================================
	/**īˮ�����¼�*/
	public void UpDown_Ink(PadbookType padBookInfoType, String equCode, String operation, int sendMode) {
		SystemManager.LOGI(TAG, "īˮ�����¼�");
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

	/**����īˮ�����¼�*/
	public void unUpDown_Ink(byte cmd, String sjson) {
		SystemManager.LOGI(TAG, "����īˮ�����¼�");
		unCommon(cmd, sjson);
	}

	//���ǽӿڽ���========================================================================= 
	protected void unChaoXing(byte cmd, String sjson) {
		JSONObject json = null;
		JSONObject data = null;
		try {
			json = new JSONObject(sjson);
			if (json != null) {
				//����
//				data = json.getJSONObject("data");
//				Log.e(TAG, "data " + data.toString());
//				List<CX_Region> cabinetInitList = JSONArray.parseArray(data.getString("items"), CX_Region.class);
//				Log.e(TAG, "cabinetInitList " + cabinetInitList.size());
//				for (int i = 0; i < cabinetInitList.size(); i++) {
//					Log.e(TAG, "cabinetInitList " + cabinetInitList.get(i).name);
//				}
				//ͼ���
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

	//��ȡ����
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
			//����
//			  JSONArray family = json.getJSONArray("");
			//ͼ���
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

	//������Դ����
	public void downScreen(String url,CallBackUtil.CallBackFile callBackFile ) {
		send((byte)0, url,callBackFile, SEND_MODE_HTTP_DOWNLOAD);
	}

	//====================================================================================
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
