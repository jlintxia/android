package com.test.testtcp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.function.tcputils.TcpProcess;
import com.function.tcputils.TcpProcess.TcpCallback;
import com.function.tcputils.TcpProtocol;
import com.guozheng.okhttputils.CallBackUtil.CallBackFile;
import com.guozheng.okhttputils.CallBackUtil.CallBackString;
import com.guozheng.okhttputils.CallBackUtil;
import com.guozheng.okhttputils.HttpProcess;
import com.test.testtcp.BusinessManage.OnBusinessListener;
import com.test.testtcp.ThemeDataModle_Ink.BrInfo;
import com.test.testtcp.ThemeDataModle_Ink.CabinetInit;
import com.test.testtcp.ThemeDataModle_Ink.EquInfoType;
import com.test.testtcp.ThemeDataModle_Ink.ErrorType;
import com.test.testtcp.ThemeDataModle_Ink.OperatePadbookType;
import com.test.testtcp.ThemeDataModle_Ink.PadbookType;
import com.test.testtcp.ThemeDataModle_Ink.PadbookUserType;
import com.test.testtcp.ThemeDataModle_Ink.Padbook_order_deposit;
import com.test.testtcp.ThemeDataModle_Ink.PadbookaisleInstType;
import com.test.testtcp.ThemeDataModle_Ink.PageInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import okhttp3.Call;

public class MainActivity extends Activity implements OnBusinessListener, OnClickListener {
	private static final String TAG = "MainActivity";
	private TcpProcess mTcpProcess;
	private HttpProcess mHttpProcess;
	private BusinessManage mBusinessManage;
	private Button sync_time, sync_org, equ_regist, sub_deposit, que_deposit, sub_deposit_return, que_userinfo,
			que_regoin, que_stuborrowrecord, init_cabinet, register_user, que_user, sync_user, init_aisle, que_equ_para,
			que_edition, que_ink_info, ink_updown, padbookaisleinst_operate, get_screensaver, borrow_ink, return_ink,
			sysn_equ_info, get_equ_info, sysn_screen;

	private String depositOrder;//������
	private int pic = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sync_time = (Button) findViewById(R.id.sync_time);
		sync_org = (Button) findViewById(R.id.sync_org);
		equ_regist = (Button) findViewById(R.id.equ_regist);
		sub_deposit = (Button) findViewById(R.id.sub_deposit);
		que_deposit = (Button) findViewById(R.id.que_deposit);
		sub_deposit_return = (Button) findViewById(R.id.sub_deposit_return);
		que_userinfo = (Button) findViewById(R.id.que_userinfo);
		que_stuborrowrecord = (Button) findViewById(R.id.que_stuborrowrecord);
		que_regoin = (Button) findViewById(R.id.que_regoin);
		init_cabinet = (Button) findViewById(R.id.init_cabinet);
		register_user = (Button) findViewById(R.id.register_user);
		que_user = (Button) findViewById(R.id.que_user);
		sync_user = (Button) findViewById(R.id.sync_user);
		init_aisle = (Button) findViewById(R.id.init_aisle);
		que_equ_para = (Button) findViewById(R.id.que_equ_para);
		que_edition = (Button) findViewById(R.id.que_edition);
		que_ink_info = (Button) findViewById(R.id.que_ink_info);
		ink_updown = (Button) findViewById(R.id.ink_updown);
		padbookaisleinst_operate = (Button) findViewById(R.id.padbookaisleinst_operate);
		get_screensaver = (Button) findViewById(R.id.get_screensaver);

		sysn_equ_info = (Button) findViewById(R.id.sysn_equ_info);
		get_equ_info = (Button) findViewById(R.id.get_equ_info);

		borrow_ink = (Button) findViewById(R.id.borrow_ink);
		return_ink = (Button) findViewById(R.id.return_ink);

		sysn_screen = (Button) findViewById(R.id.sysn_screen);

		sync_time.setOnClickListener(this);
		sync_org.setOnClickListener(this);
		equ_regist.setOnClickListener(this);
		sub_deposit.setOnClickListener(this);
		que_deposit.setOnClickListener(this);
		sub_deposit_return.setOnClickListener(this);
		que_userinfo.setOnClickListener(this);
		que_stuborrowrecord.setOnClickListener(this);
		que_regoin.setOnClickListener(this);
		init_cabinet.setOnClickListener(this);
		register_user.setOnClickListener(this);
		que_user.setOnClickListener(this);
		sync_user.setOnClickListener(this);
		init_aisle.setOnClickListener(this);
		que_equ_para.setOnClickListener(this);
		que_edition.setOnClickListener(this);
		que_ink_info.setOnClickListener(this);
		ink_updown.setOnClickListener(this);
		padbookaisleinst_operate.setOnClickListener(this);
		get_screensaver.setOnClickListener(this);
		borrow_ink.setOnClickListener(this);
		return_ink.setOnClickListener(this);
		sysn_equ_info.setOnClickListener(this);
		get_equ_info.setOnClickListener(this);

		sysn_screen.setOnClickListener(this);

		mTcpProcess = new TcpProcess(new TcpCallback() {

			@Override
			public void request(byte cmd, String json) {
				Log.d(TAG, "request  cmd = " + cmd + " json = " + json);

			}

			@Override
			public void connectResult(byte cmd, boolean ret) {

			}

			@Override
			public void answer(byte cmd, String json) {

			}

			@Override
			public void tcpOff() {
				if (mBusinessManage != null) {
					mBusinessManage.connectFail();
				}
			}

			@Override
			public void answer_async(boolean receive, byte cmd, String sjson) {
				Log.d(TAG, "answer_async = " + cmd + " json = " + sjson + "  receive= " + receive);
				if (receive) {
					switch (cmd) {

					//īˮ�����¼�*/
					case BusinessCmd.PACKET_CMD_SUB_RETURNINFO:
						break;
					//��ȡ������Դ�ӿ�*/
					case BusinessCmd.PACKET_CMD_SYSN_SCREEN:
						mBusinessManage.getBusinessProcess().unGet_Screensaver(cmd, sjson);
						break;
					//��īˮ�������ύ�ӿ�*/
					case BusinessCmd.PACKET_CMD_INK_BORROW:
						mBusinessManage.getBusinessProcess().unBorrow_Ink(cmd, sjson);
						break;
					//��īˮ�������ύ�ӿ�*/
					case BusinessCmd.PACKET_CMD_INK_RETRUN:
						mBusinessManage.getBusinessProcess().unSub_Return(cmd, sjson);

						break;
					//�豸������ѯ�ӿ�*/
					case BusinessCmd.PACKET_CMD_QUE_EQUPARAMINFO:
						mBusinessManage.getBusinessProcess().unGet_Equparam(cmd, sjson);
						break;
					//��ѯ�汾�Žӿ�*/
					case BusinessCmd.PACKET_CMD_QUE_VERSION:
						mBusinessManage.getBusinessProcess().unQue_version(cmd, sjson);
						break;
					//�豸īˮ����Ϣ�����ӿ�*/
					case BusinessCmd.PACKET_CMD_OPERATION_EQUINK:
						mBusinessManage.getBusinessProcess().unSync_OperatePadbookInfo(cmd, sjson);
						break;
					//�����û���ѯ�����¼�ӿ�*/
					case BusinessCmd.PACKET_CMD_QUE_USER_BORROW_RECORD:
						mBusinessManage.getBusinessProcess().unQue_UserBorrowRecord(cmd, sjson);
						break;
					//�豸��Ϣͬ���ӿ�*/
					case BusinessCmd.PACKET_CMD_SYSN_EQUINFO:
						mBusinessManage.getBusinessProcess().unSync_EquInfo(cmd, sjson);
						break;
					//�豸��Ϣ��ѯ�ӿ�*/
					case BusinessCmd.PACKET_CMD_QUE_EQUINFO:
						mBusinessManage.getBusinessProcess().unGet_EquInfo(cmd, sjson);
						break;
					//��ƽ̨��ѯ�豸������Ϣ�ӿ�*/
					case BusinessCmd.PACKET_CMD_QUE_AISLE:
						break;
					//�豸�澯��Ϣ*/
					case BusinessCmd.PACKET_CMD_EQU_ALARM:
						break;
					//ͬ���������ݱ�*/
					case BusinessCmd.PACKET_CMD_SYSN_ORG:
						break;
					//�豸��־�ϴ�*/
					case BusinessCmd.PACKET_CMD_SUB_LOG:
						break;
					//��ѯīˮ����Ϣ�ӿ�*/
					case BusinessCmd.PACKET_CMD_QUE_INKINFO:
						mBusinessManage.getBusinessProcess().unQue_padBookInfo(cmd, sjson);
						break;
					//�û�ע��*/
					case BusinessCmd.PACKET_CMD_REGISTER_USER:
						break;
					//��ѯ�û���Ϣ*/
					case BusinessCmd.PACKET_CMD_QUE_USER:
						mBusinessManage.getBusinessProcess().unQue_UserInfo(cmd, sjson);
						break;
					//ͬ���û���Ϣ*/
					case BusinessCmd.PACKET_CMD_SYNC_USER:
						break;
					// ��ʼ������*/
					case BusinessCmd.PACKET_CMD_INIT_AISLE:
						mBusinessManage.getBusinessProcess().unInit_aisle(cmd, sjson);
						break;
					// ��ʼ������*/
					case BusinessCmd.PACKET_CMD_INIT_CABINET:
						mBusinessManage.getBusinessProcess().unInit_cabinet(cmd, sjson);
						break;
					//  �޸Ļ�����Ϣ*/
					case BusinessCmd.PACKET_CMD_CHANGE_CABINET:
						break;
					//  īˮ�����*/
					case BusinessCmd.PACKET_CMD_INK_WAREHOUSING:
						break;
					// �豸��������*/
					case BusinessCmd.PACKET_CMD_SET_EQUPARAM:
						break;
					// īˮ������*/
					case BusinessCmd.PACKET_CMD_BORROW_INK:
						mBusinessManage.getBusinessProcess().unBorrow_Ink(cmd, sjson);
						break;
					// īˮ���黹*/
					case BusinessCmd.PACKET_CMD_INIT_RETURN_INK:
						break;

					default:
						break;
					}
				}

			}

		}, this, TcpProtocol.FormToType.SYSTEM_LNK);
		mTcpProcess.init();
//		mTcpProcess.startTcp("101.132.151.249", 8600);
//		mTcpProcess.startTcp("192.168.10.23", 8600);
//		mTcpProcess.startTcp("192.168.10.168", 8600);
		mTcpProcess.startTcp("59.110.234.130", 8602);
		mHttpProcess = new HttpProcess(this);
		mHttpProcess.setHttpCallBack(new CallBackString() {

			@Override
			public void onResponse(byte cmd, String response) {
				Log.e(TAG, "mHttpProcess onResponse response= " + response);
//				mBusinessManage.getBusinessProcess().unChaoXing(cmd, response);
				switch (cmd) {
				case 0: {
					mBusinessManage.getBusinessProcess().unChaoXing_screen(BusinessCmd.PACKET_CMD_SYSN_SCREEN,
							response);
				}
					break;
				//ͬ��������Ϣ�ӿ�HTTP 
				case BusinessCmd.PACKET_CMD_SYNC_REGION:
					break;
				//ͬ��������Ϣ�ӿ�HTTP*/
				case BusinessCmd.PACKET_CMD_SYNC_SITE:
					break;
				//ͬ��ʱ�� �ӿ�HTTP*/
				case BusinessCmd.PACKET_CMD_SYNC_TIME:
					mBusinessManage.getBusinessProcess().unSync_Time(true, cmd, response);
					break;
				//����Աע���ն��豸�ӿ�HTTP*/
				case BusinessCmd.PACKET_CMD_REGISTER:
					mBusinessManage.getBusinessProcess().unRegister_User(cmd, response);
					break;
				//Ѻ����Ϣ�ύHTTP*/
				case BusinessCmd.PACKET_CMD_SUB_DEPOSIT:
					break;
				//Ѻ��֧�������ѯHTTP*/
				case BusinessCmd.PACKET_CMD_QUE_DEPOSIT:
					break;
				default:
					break;
				}
			}

			@Override
			public void onFailure(byte cmd, Call call, Exception e) {
				Log.e(TAG, "mHttpProcess onFailure call = " + call + " e = " + e);
			}
		});

		mBusinessManage = new BusinessManage(this, mTcpProcess, mHttpProcess);
		mBusinessManage.setOnBusinessListener(this);

	}

	@Override
	public void sendCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object) {

		switch (cmd) {
		//ͬ��������Ϣ�ӿ�HTTP 
		case BusinessCmd.PACKET_CMD_SYNC_REGION:
			break;
		//ͬ��������Ϣ�ӿ�HTTP*/
		case BusinessCmd.PACKET_CMD_SYNC_SITE:
			break;
		//ͬ��ʱ�� �ӿ�HTTP*/
		case BusinessCmd.PACKET_CMD_SYNC_TIME:
			break;
		//����Աע���ն��豸�ӿ�HTTP*/
		case BusinessCmd.PACKET_CMD_REGISTER:
			break;

		//īˮ�����¼�*/
		case BusinessCmd.PACKET_CMD_SUB_RETURNINFO:
			break;
		//��ȡ������Դ�ӿ�*/
		case BusinessCmd.PACKET_CMD_SYSN_SCREEN:
			break;
		//��īˮ�������ύ�ӿ�*/
		case BusinessCmd.PACKET_CMD_INK_BORROW:
			break;
		//��īˮ�������ύ�ӿ�*/
		case BusinessCmd.PACKET_CMD_INK_RETRUN:
			break;
		//�豸������ѯ�ӿ�*/
		case BusinessCmd.PACKET_CMD_QUE_EQUPARAMINFO:
			break;
		//��ѯ�汾�Žӿ�*/
		case BusinessCmd.PACKET_CMD_QUE_VERSION:
			break;
		//�豸īˮ����Ϣ�����ӿ�*/
		case BusinessCmd.PACKET_CMD_OPERATION_EQUINK:
			break;
		//�����û���ѯ�����¼�ӿ�*/
		case BusinessCmd.PACKET_CMD_QUE_USER_BORROW_RECORD:
			break;
		//�豸��Ϣͬ���ӿ�*/
		case BusinessCmd.PACKET_CMD_SYSN_EQUINFO:
			break;
		//��ƽ̨��ѯ�豸������Ϣ�ӿ�*/
		case BusinessCmd.PACKET_CMD_QUE_AISLE:
			break;
		//�豸�澯��Ϣ*/
		case BusinessCmd.PACKET_CMD_EQU_ALARM:
			break;
		//ͬ���������ݱ�*/
		case BusinessCmd.PACKET_CMD_SYSN_ORG:
			break;
		//�豸��־�ϴ�*/
		case BusinessCmd.PACKET_CMD_SUB_LOG:
			break;
		//��ѯīˮ����Ϣ�ӿ�*/
		case BusinessCmd.PACKET_CMD_QUE_INKINFO:
			break;
		//Ѻ����Ϣ�ύHTTP*/
		case BusinessCmd.PACKET_CMD_SUB_DEPOSIT:
			break;
		//Ѻ��֧�������ѯHTTP*/
		case BusinessCmd.PACKET_CMD_QUE_DEPOSIT:
			break;

		//�û�ע��*/
		case BusinessCmd.PACKET_CMD_REGISTER_USER:
			break;
		//��ѯ�û���Ϣ*/
		case BusinessCmd.PACKET_CMD_QUE_USER:
			break;
		//ͬ���û���Ϣ*/
		case BusinessCmd.PACKET_CMD_SYNC_USER:
			break;
		// ��ʼ������*/
		case BusinessCmd.PACKET_CMD_INIT_AISLE:
			break;
		// ��ʼ������*/
		case BusinessCmd.PACKET_CMD_INIT_CABINET:
			break;
		//  �޸Ļ�����Ϣ*/
		case BusinessCmd.PACKET_CMD_CHANGE_CABINET:
			break;
		//  īˮ�����*/
		case BusinessCmd.PACKET_CMD_INK_WAREHOUSING:
			break;
		// �豸��������*/
		case BusinessCmd.PACKET_CMD_SET_EQUPARAM:
			break;
		// īˮ������*/
		case BusinessCmd.PACKET_CMD_BORROW_INK:
			break;
		// īˮ���黹*/
		case BusinessCmd.PACKET_CMD_INIT_RETURN_INK:
			break;
		default:
			break;
		}

	}

	@Override
	public void connectFail() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sync_org: {
			if (mBusinessManage != null) {
//				mBusinessManage.getBusinessProcess().sync_Org("123456", BusinessProcess.SEND_MODE_TCP);
			}
		}

			break;
		case R.id.que_regoin: {
			String url = "http://dev.los.chaoxingbook.com/api/plat/region";
//			String url = "http://dev.los.chaoxingbook.com/api/plat/library";
			Map<String, String> paramsMap = new HashMap<String, String>();
//			paramsMap.put("parent_id", "");
//			paramsMap.put("province_id", "25000000");
//			paramsMap.put("city_id", "25010000");
//			paramsMap.put("area_id", "25010100");
//			mHttpProcess.httpGet(url, paramsMap);
			mHttpProcess.send(BusinessCmd.PACKET_CMD_QUE_CLEAN, null);
//			String url = "http://dev.los.chaoxingbook.com/api/plat/library";
//			Map<String, String> paramsMap = new HashMap<String, String>();
//			paramsMap.put("perPage", "200");
//			paramsMap.put("page", "2");
//			paramsMap.put("parent_id", "25000000");
//			paramsMap.put("city_id", "25010000");
//			paramsMap.put("area_id", "25010100");
//			mHttpProcess.httpGet(url, paramsMap);
//			if (mBusinessManage != null) {
//				mBusinessManage.getBusinessProcess().sync_region("123456", null, BusinessProcess.SEND_MODE_HTTP);
//			}
		}

			break;

		case R.id.sub_deposit: {
			if (mBusinessManage != null) {
				Padbook_order_deposit deposit = new Padbook_order_deposit();
				deposit.userId = "12345678";//�û�ID
				depositOrder = getMyUUID();
				deposit.depositOrder = depositOrder;//Ѻ����ˮ
				deposit.orderType = "1";//��������,1-��ֵѺ��2-�˿�
				deposit.creatTime = gettime("yyyyMMddHHmmss");//����ʱ��
				deposit.orderStatus = "10000";//����״̬10000��δ֧��50000�����20000��֧���ɹ�20001��֧��ʧ��40000���˿�ɹ�40001���˿�ʧ��50001������99999���쳣
				deposit.payType = "01";//֧����ʽ��01��΢��֧��02��֧����֧��
				deposit.orderAmount = "0.01";//�����ܽ��
				mBusinessManage.getBusinessProcess().sub_Deposit(deposit, "123456", BusinessProcess.SEND_MODE_HTTP);
			}
		}
			break;
		case R.id.que_deposit: {
			if (mBusinessManage != null) {
				mBusinessManage.getBusinessProcess().que_Deposit("123456", depositOrder,
						BusinessProcess.SEND_MODE_HTTP);
			}
		}
			break;

		case R.id.sub_deposit_return: {
			if (mBusinessManage != null) {
				Padbook_order_deposit deposit = new Padbook_order_deposit();
				deposit.userId = "12345678";//�û�ID
				deposit.depositOrder = depositOrder;//Ѻ����ˮ
				deposit.orderType = "2";//��������,1-��ֵѺ��2-�˿�
				deposit.creatTime = gettime("yyyyMMddHHmmss");//����ʱ��
				deposit.orderStatus = "10000";//����״̬10000��δ֧��50000�����20000��֧���ɹ�20001��֧��ʧ��40000���˿�ɹ�40001���˿�ʧ��50001������99999���쳣
				deposit.payType = "01";//֧����ʽ��01��΢��֧��02��֧����֧��
				deposit.orderAmount = "0.01";//�����ܽ��
				mBusinessManage.getBusinessProcess().sub_Deposit(deposit, "123456", BusinessProcess.SEND_MODE_HTTP);
			}
		}
			break;

		case R.id.register_user: {
			if (mBusinessManage != null) {
				PadbookUserType userType = new PadbookUserType();
				userType.userName = "12345";
				userType.realName = "12345";
				userType.password = "12345";
				userType.certCode = "12345";
				userType.phone = "12345";
				mBusinessManage.getBusinessProcess().register_User(userType, BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;
		case R.id.que_user: {
			if (mBusinessManage != null) {
				PadbookUserType userType = new PadbookUserType();
				userType.certCode = "12345";
				mBusinessManage.getBusinessProcess().que_UserInfo(userType, BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;
		case R.id.sync_user: {
			if (mBusinessManage != null) {
				PadbookUserType userType = new PadbookUserType();
				userType.userId = "123";//�û�����
				userType.userName = "123";//�û���
				userType.realName = "123";//��ʵ����
				userType.password = "123";//����
				userType.userType = "123";//�û�����
				userType.sex = "123";//�Ա�
				userType.phone = "123";//�绰
				userType.email = "123";//����
				userType.certCode = "123";//���֤��
				userType.address = "123";//��ϵ��ַ
				userType.status = "00";//�ʺ�״̬  ������00  ���ᣨδ��Ѻ�𣩣�01  ��ʧ��02  ע����03
				userType.siteId = "123";//����ID
				userType.areaId = "123";//����ID
				userType.openType = "01";//Ѻ��֧������1-΢��,2-֧������3-����
				userType.deposit = "123";//Ѻ����
				userType.createTime = "123";//����ʱ��
				userType.updateTime = "123";//����ʱ��
				userType.pwdExpiredTime = "123";//����ʧЧʱ��
				userType.roleId = "123";//��ɫID
				userType.cardNum = "123";//����
				mBusinessManage.getBusinessProcess().sync_UserInfo(userType, BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;

		//----------------------------------------------------------------------------------------------
		case R.id.equ_regist: {//����Աע���豸
			if (mBusinessManage != null) {
				EquInfoType equ = new EquInfoType();
				equ.equCode = "123456"; //�豸����
				equ.equSn = "12345678"; //���ư�SN
				equ.equType = "00";//00�����01īˮ���� 	//�豸����
				equ.equName = "����";//	�豸����
				equ.siteId = "1";//����ID
				equ.siteName = "����ͼ���";//��������
				equ.provinceId = "25000000";//ʡid
				equ.provinceName = "����ʡ";//ʡ��
				equ.cityId = "25010000";//��id
				equ.cityName = "������";//����
				equ.areaId = "25010100";//��id
				equ.areaName = "��¥��";//����
				mBusinessManage.getBusinessProcess().equRegist(equ, BusinessProcess.SEND_MODE_HTTP);
			}
		}
			break;
		case R.id.sync_time: {//ͬ��ʱ��
			if (mBusinessManage != null) {
				mBusinessManage.getBusinessProcess().sync_Time("123456", BusinessProcess.SEND_MODE_HTTP);
			}
		}

			break;
		case R.id.que_userinfo: {//��ѯ�û���Ϣ

			if (mBusinessManage != null) {
				PadbookUserType userType = new PadbookUserType();
				userType.cardNum = "";//	��Աid
				mBusinessManage.getBusinessProcess().que_UserInfo(userType, BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;
		case R.id.init_cabinet: {//��ʼ������

			if (mBusinessManage != null) {

				List<CabinetInit> cabinetInitList = new ArrayList<ThemeDataModle_Ink.CabinetInit>();
				CabinetInit cabinetInit = new CabinetInit();
				cabinetInit.cabinetCode = "1231";
				cabinetInitList.add(cabinetInit);
				CabinetInit cabinetInit2 = new CabinetInit();
				cabinetInit2.cabinetCode = "1232";
				cabinetInitList.add(cabinetInit2);
				mBusinessManage.getBusinessProcess().init_cabinet(cabinetInitList, "890",
						BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;
		case R.id.init_aisle: { //��ʼ������
			if (mBusinessManage != null) {
				List<PadbookaisleInstType> padbookaisleInstTypeList = new ArrayList<ThemeDataModle_Ink.PadbookaisleInstType>();
				PadbookaisleInstType padbookaisleInstType = new PadbookaisleInstType();
				padbookaisleInstType.cabinetId = "1";//����id
				padbookaisleInstType.equtype = "123";//�豸����
				padbookaisleInstType.aislecode = "1";//��������
				padbookaisleInstType.cabinetCode = "1";//�������
				padbookaisleInstType.capacityNum = "1";//��������
				padbookaisleInstType.storageNum = "0";//�������
				padbookaisleInstType.remark = "123";//��ע
				padbookaisleInstType.aislestatus = "00";//����״̬		00������		01������02������
				padbookaisleInstType.rowIndex = "1";//������
				padbookaisleInstType.colIndex = "1";//������
				padbookaisleInstTypeList.add(padbookaisleInstType);

				PadbookaisleInstType padbookaisleInstType2 = new PadbookaisleInstType();
				padbookaisleInstType2.cabinetId = "1";//����id
				padbookaisleInstType2.equtype = "123";//�豸����
				padbookaisleInstType2.aislecode = "2";//��������
				padbookaisleInstType2.cabinetCode = "1";//�������
				padbookaisleInstType2.capacityNum = "1";//��������
				padbookaisleInstType2.storageNum = "0";//�������
				padbookaisleInstType2.remark = "1234";//��ע
				padbookaisleInstType2.aislestatus = "00";//����״̬		00������		01������02������
				padbookaisleInstType2.rowIndex = "1";//������
				padbookaisleInstType2.colIndex = "1";//������
				padbookaisleInstTypeList.add(padbookaisleInstType2);

				mBusinessManage.getBusinessProcess().init_aisle(padbookaisleInstTypeList, "ttttt777",
						BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;

		case R.id.que_equ_para://��ȡ�豸����
			mBusinessManage.getBusinessProcess().get_EquParam("123456", BusinessProcess.SEND_MODE_TCP);
			break;
		case R.id.que_edition://��ѯ�汾��
			mBusinessManage.getBusinessProcess().que_version("1101", "00", "123456", BusinessProcess.SEND_MODE_HTTP);
			break;
		case R.id.que_stuborrowrecord: {//����ѧ����ѯ�����¼�ӿ�
			mBusinessManage.getBusinessProcess().que_UserBorrowRecord("123456", "123456", "123456", "123456",
					BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.sysn_equ_info: {//�豸��Ϣͬ��
			EquInfoType equInfoType = new EquInfoType();
			equInfoType.equCode = "123456";//�豸����
			equInfoType.equSn = "123456";//�豸�忨SN
			equInfoType.equType = "01";//�豸����00 ����� 01īˮƿ�� 02 ���ܼҾ�
			equInfoType.equName = "123456";//�豸����

			equInfoType.siteId = "1";//����ID
			equInfoType.siteName = "123456";//��������
			equInfoType.provinceId = "350000";//ʡid
			equInfoType.provinceName = "����";//ʡ��
			equInfoType.cityId = "350100";//��id
			equInfoType.cityName = "����";//����
			equInfoType.areaId = "350101";//��id
			equInfoType.areaName = "��¥";//����
			mBusinessManage.getBusinessProcess().sync_EquInfo("uuuu", equInfoType, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.get_equ_info: {//�豸��Ϣ��ѯ
			mBusinessManage.getBusinessProcess().get_EquInfo("uuuu", BusinessProcess.SEND_MODE_TCP);
		}
			break;

		case R.id.que_ink_info: {//��ѯīˮ����Ϣ
			mBusinessManage.getBusinessProcess().que_padBookInfo("6061810001729", null, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.ink_updown: {//īˮ�����¼�
			PadbookType padbookType = new PadbookType();
			padbookType.padbookCode = "6061810001729";
			padbookType.padbookId = "99";
			padbookType.padbookMac = "555";
			padbookType.padbookName = "īˮƿ3";
			padbookType.status = "2";

			mBusinessManage.getBusinessProcess().UpDown_Ink(padbookType, "123456", PadbookType.TYPE_UP,
					BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.padbookaisleinst_operate: {//�豸īˮ����Ϣ�����ӿ�
			OperatePadbookType operatePadbookType = new OperatePadbookType();

			PadbookaisleInstType padbookaisleInstType = new PadbookaisleInstType();
			padbookaisleInstType.cabinetId = "123";//����id
			padbookaisleInstType.equtype = "123";//�豸����
			padbookaisleInstType.aislecode = "123";//��������
			padbookaisleInstType.cabinetCode = "123";//�������
			padbookaisleInstType.capacityNum = "123";//��������
			padbookaisleInstType.storageNum = "123";//�������
			padbookaisleInstType.remark = "123";//��ע
			padbookaisleInstType.aislestatus = "123";//����״̬		00������		01������02������
			padbookaisleInstType.rowIndex = "123";//������
			padbookaisleInstType.colIndex = "123";//������
			padbookaisleInstType.padbookCode = "123";//īˮ������
			padbookaisleInstType.padbookName = "123";//	īˮ������
			padbookaisleInstType.status = "123";//īˮ��״̬
			operatePadbookType.padbookInfo = padbookaisleInstType;

			mBusinessManage.getBusinessProcess().sync_OperatePadbookInfo("123456", operatePadbookType,
					BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.get_screensaver: {//īˮ�����¼�
			mBusinessManage.getBusinessProcess().get_Screensaver("123456", BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.borrow_ink: {//īˮ�����
			BrInfo brInfo = new BrInfo();
			brInfo.equ_code = "123";//�豸����
			brInfo.cabinet_id = "123";//����id
			brInfo.aisle_id = "123";//	����id
			brInfo.user_id = "123";//�û�id
			brInfo.cert_code = "123";//���֤��
			brInfo.site_id = "123";//����id
			brInfo.area_id = "123";//����id
			brInfo.padbook_code = "123";//īˮ��id
			brInfo.state = "1";//	״̬1-�����2-�ѻ�
			mBusinessManage.getBusinessProcess().borrow_Ink(brInfo, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.return_ink: {//īˮ���黹
			BrInfo brInfo = new BrInfo();
			brInfo.equ_code = "123";//�豸����
			brInfo.cabinet_id = "123";//����id
			brInfo.aisle_id = "123";//	����id
			brInfo.user_id = "123";//�û�id
			brInfo.cert_code = "123";//���֤��
			brInfo.site_id = "123";//����id
			brInfo.area_id = "123";//����id
			brInfo.padbook_code = "123";//īˮ��id
			brInfo.state = "2";//	״̬1-�����2-�ѻ�
			mBusinessManage.getBusinessProcess().Return_Ink(brInfo, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.sysn_screen: {//��ȡ����
			Log.e(TAG, "sysn_screen");
			String url = "http://book.jieyueji.cn/server/manage";
//			String url = "http://dev.los.chaoxingbook.com/api/plat/library";
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("cmd", "getScreenUrl");
			paramsMap.put("t", "30001");
			paramsMap.put("sort", "ID");
			paramsMap.put("order", "desc");
			paramsMap.put("offset", "0");
			paramsMap.put("limit", "100");
			paramsMap.put("search", "0");
			mHttpProcess.httpGet(url, paramsMap);
		}
			break;
		default:
			break;
		}

	}

	public static String getMyUUID() {
		String id = "";
		id += gettime("yyMMddHHmmss");
		id += getrandom(true, 8);
		return id;
	}

	@SuppressLint("SimpleDateFormat")
	public static String gettime(String pattern) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		String date = sDateFormat.format(new Date());
		return date;
	}

	public static String getrandom(boolean numberFlag, int length) {

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

}
