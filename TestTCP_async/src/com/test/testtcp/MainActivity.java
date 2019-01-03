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

	private String depositOrder;//订单号
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

					//墨水屏上下架*/
					case BusinessCmd.PACKET_CMD_SUB_RETURNINFO:
						break;
					//获取屏保资源接口*/
					case BusinessCmd.PACKET_CMD_SYSN_SCREEN:
						mBusinessManage.getBusinessProcess().unGet_Screensaver(cmd, sjson);
						break;
					//借墨水屏数据提交接口*/
					case BusinessCmd.PACKET_CMD_INK_BORROW:
						mBusinessManage.getBusinessProcess().unBorrow_Ink(cmd, sjson);
						break;
					//还墨水屏数据提交接口*/
					case BusinessCmd.PACKET_CMD_INK_RETRUN:
						mBusinessManage.getBusinessProcess().unSub_Return(cmd, sjson);

						break;
					//设备参数查询接口*/
					case BusinessCmd.PACKET_CMD_QUE_EQUPARAMINFO:
						mBusinessManage.getBusinessProcess().unGet_Equparam(cmd, sjson);
						break;
					//查询版本号接口*/
					case BusinessCmd.PACKET_CMD_QUE_VERSION:
						mBusinessManage.getBusinessProcess().unQue_version(cmd, sjson);
						break;
					//设备墨水屏信息操作接口*/
					case BusinessCmd.PACKET_CMD_OPERATION_EQUINK:
						mBusinessManage.getBusinessProcess().unSync_OperatePadbookInfo(cmd, sjson);
						break;
					//根据用户查询借书记录接口*/
					case BusinessCmd.PACKET_CMD_QUE_USER_BORROW_RECORD:
						mBusinessManage.getBusinessProcess().unQue_UserBorrowRecord(cmd, sjson);
						break;
					//设备信息同步接口*/
					case BusinessCmd.PACKET_CMD_SYSN_EQUINFO:
						mBusinessManage.getBusinessProcess().unSync_EquInfo(cmd, sjson);
						break;
					//设备信息查询接口*/
					case BusinessCmd.PACKET_CMD_QUE_EQUINFO:
						mBusinessManage.getBusinessProcess().unGet_EquInfo(cmd, sjson);
						break;
					//向平台查询设备货道信息接口*/
					case BusinessCmd.PACKET_CMD_QUE_AISLE:
						break;
					//设备告警信息*/
					case BusinessCmd.PACKET_CMD_EQU_ALARM:
						break;
					//同步机构数据表*/
					case BusinessCmd.PACKET_CMD_SYSN_ORG:
						break;
					//设备日志上传*/
					case BusinessCmd.PACKET_CMD_SUB_LOG:
						break;
					//查询墨水屏信息接口*/
					case BusinessCmd.PACKET_CMD_QUE_INKINFO:
						mBusinessManage.getBusinessProcess().unQue_padBookInfo(cmd, sjson);
						break;
					//用户注册*/
					case BusinessCmd.PACKET_CMD_REGISTER_USER:
						break;
					//查询用户信息*/
					case BusinessCmd.PACKET_CMD_QUE_USER:
						mBusinessManage.getBusinessProcess().unQue_UserInfo(cmd, sjson);
						break;
					//同步用户信息*/
					case BusinessCmd.PACKET_CMD_SYNC_USER:
						break;
					// 初始化货道*/
					case BusinessCmd.PACKET_CMD_INIT_AISLE:
						mBusinessManage.getBusinessProcess().unInit_aisle(cmd, sjson);
						break;
					// 初始化货柜*/
					case BusinessCmd.PACKET_CMD_INIT_CABINET:
						mBusinessManage.getBusinessProcess().unInit_cabinet(cmd, sjson);
						break;
					//  修改货柜信息*/
					case BusinessCmd.PACKET_CMD_CHANGE_CABINET:
						break;
					//  墨水屏入库*/
					case BusinessCmd.PACKET_CMD_INK_WAREHOUSING:
						break;
					// 设备参数设置*/
					case BusinessCmd.PACKET_CMD_SET_EQUPARAM:
						break;
					// 墨水屏借阅*/
					case BusinessCmd.PACKET_CMD_BORROW_INK:
						mBusinessManage.getBusinessProcess().unBorrow_Ink(cmd, sjson);
						break;
					// 墨水屏归还*/
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
				//同步区域信息接口HTTP 
				case BusinessCmd.PACKET_CMD_SYNC_REGION:
					break;
				//同步场所信息接口HTTP*/
				case BusinessCmd.PACKET_CMD_SYNC_SITE:
					break;
				//同步时间 接口HTTP*/
				case BusinessCmd.PACKET_CMD_SYNC_TIME:
					mBusinessManage.getBusinessProcess().unSync_Time(true, cmd, response);
					break;
				//管理员注册终端设备接口HTTP*/
				case BusinessCmd.PACKET_CMD_REGISTER:
					mBusinessManage.getBusinessProcess().unRegister_User(cmd, response);
					break;
				//押金信息提交HTTP*/
				case BusinessCmd.PACKET_CMD_SUB_DEPOSIT:
					break;
				//押金支付结果查询HTTP*/
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
		//同步区域信息接口HTTP 
		case BusinessCmd.PACKET_CMD_SYNC_REGION:
			break;
		//同步场所信息接口HTTP*/
		case BusinessCmd.PACKET_CMD_SYNC_SITE:
			break;
		//同步时间 接口HTTP*/
		case BusinessCmd.PACKET_CMD_SYNC_TIME:
			break;
		//管理员注册终端设备接口HTTP*/
		case BusinessCmd.PACKET_CMD_REGISTER:
			break;

		//墨水屏上下架*/
		case BusinessCmd.PACKET_CMD_SUB_RETURNINFO:
			break;
		//获取屏保资源接口*/
		case BusinessCmd.PACKET_CMD_SYSN_SCREEN:
			break;
		//借墨水屏数据提交接口*/
		case BusinessCmd.PACKET_CMD_INK_BORROW:
			break;
		//还墨水屏数据提交接口*/
		case BusinessCmd.PACKET_CMD_INK_RETRUN:
			break;
		//设备参数查询接口*/
		case BusinessCmd.PACKET_CMD_QUE_EQUPARAMINFO:
			break;
		//查询版本号接口*/
		case BusinessCmd.PACKET_CMD_QUE_VERSION:
			break;
		//设备墨水屏信息操作接口*/
		case BusinessCmd.PACKET_CMD_OPERATION_EQUINK:
			break;
		//根据用户查询借书记录接口*/
		case BusinessCmd.PACKET_CMD_QUE_USER_BORROW_RECORD:
			break;
		//设备信息同步接口*/
		case BusinessCmd.PACKET_CMD_SYSN_EQUINFO:
			break;
		//向平台查询设备货道信息接口*/
		case BusinessCmd.PACKET_CMD_QUE_AISLE:
			break;
		//设备告警信息*/
		case BusinessCmd.PACKET_CMD_EQU_ALARM:
			break;
		//同步机构数据表*/
		case BusinessCmd.PACKET_CMD_SYSN_ORG:
			break;
		//设备日志上传*/
		case BusinessCmd.PACKET_CMD_SUB_LOG:
			break;
		//查询墨水屏信息接口*/
		case BusinessCmd.PACKET_CMD_QUE_INKINFO:
			break;
		//押金信息提交HTTP*/
		case BusinessCmd.PACKET_CMD_SUB_DEPOSIT:
			break;
		//押金支付结果查询HTTP*/
		case BusinessCmd.PACKET_CMD_QUE_DEPOSIT:
			break;

		//用户注册*/
		case BusinessCmd.PACKET_CMD_REGISTER_USER:
			break;
		//查询用户信息*/
		case BusinessCmd.PACKET_CMD_QUE_USER:
			break;
		//同步用户信息*/
		case BusinessCmd.PACKET_CMD_SYNC_USER:
			break;
		// 初始化货道*/
		case BusinessCmd.PACKET_CMD_INIT_AISLE:
			break;
		// 初始化货柜*/
		case BusinessCmd.PACKET_CMD_INIT_CABINET:
			break;
		//  修改货柜信息*/
		case BusinessCmd.PACKET_CMD_CHANGE_CABINET:
			break;
		//  墨水屏入库*/
		case BusinessCmd.PACKET_CMD_INK_WAREHOUSING:
			break;
		// 设备参数设置*/
		case BusinessCmd.PACKET_CMD_SET_EQUPARAM:
			break;
		// 墨水屏借阅*/
		case BusinessCmd.PACKET_CMD_BORROW_INK:
			break;
		// 墨水屏归还*/
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
				deposit.userId = "12345678";//用户ID
				depositOrder = getMyUUID();
				deposit.depositOrder = depositOrder;//押金流水
				deposit.orderType = "1";//操作类型,1-充值押金，2-退款
				deposit.creatTime = gettime("yyyyMMddHHmmss");//创建时间
				deposit.orderStatus = "10000";//订单状态10000：未支付50000：完成20000：支付成功20001：支付失败40000：退款成功40001：退款失败50001：撤销99999：异常
				deposit.payType = "01";//支付方式：01：微信支付02：支付宝支付
				deposit.orderAmount = "0.01";//交易总金额
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
				deposit.userId = "12345678";//用户ID
				deposit.depositOrder = depositOrder;//押金流水
				deposit.orderType = "2";//操作类型,1-充值押金，2-退款
				deposit.creatTime = gettime("yyyyMMddHHmmss");//创建时间
				deposit.orderStatus = "10000";//订单状态10000：未支付50000：完成20000：支付成功20001：支付失败40000：退款成功40001：退款失败50001：撤销99999：异常
				deposit.payType = "01";//支付方式：01：微信支付02：支付宝支付
				deposit.orderAmount = "0.01";//交易总金额
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
				userType.userId = "123";//用户主键
				userType.userName = "123";//用户名
				userType.realName = "123";//真实姓名
				userType.password = "123";//密码
				userType.userType = "123";//用户类型
				userType.sex = "123";//性别
				userType.phone = "123";//电话
				userType.email = "123";//邮箱
				userType.certCode = "123";//身份证号
				userType.address = "123";//联系地址
				userType.status = "00";//帐号状态  正常：00  冻结（未交押金）：01  挂失：02  注销：03
				userType.siteId = "123";//场所ID
				userType.areaId = "123";//区域ID
				userType.openType = "01";//押金支付类型1-微信,2-支付宝，3-其他
				userType.deposit = "123";//押金金额
				userType.createTime = "123";//创建时间
				userType.updateTime = "123";//更新时间
				userType.pwdExpiredTime = "123";//密码失效时间
				userType.roleId = "123";//角色ID
				userType.cardNum = "123";//卡号
				mBusinessManage.getBusinessProcess().sync_UserInfo(userType, BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;

		//----------------------------------------------------------------------------------------------
		case R.id.equ_regist: {//管理员注册设备
			if (mBusinessManage != null) {
				EquInfoType equ = new EquInfoType();
				equ.equCode = "123456"; //设备编码
				equ.equSn = "12345678"; //控制板SN
				equ.equType = "00";//00借书柜，01墨水屏柜 	//设备类型
				equ.equName = "测试";//	设备别名
				equ.siteId = "1";//场所ID
				equ.siteName = "超星图书馆";//场所名称
				equ.provinceId = "25000000";//省id
				equ.provinceName = "福建省";//省名
				equ.cityId = "25010000";//市id
				equ.cityName = "福州市";//市名
				equ.areaId = "25010100";//区id
				equ.areaName = "鼓楼区";//区名
				mBusinessManage.getBusinessProcess().equRegist(equ, BusinessProcess.SEND_MODE_HTTP);
			}
		}
			break;
		case R.id.sync_time: {//同步时间
			if (mBusinessManage != null) {
				mBusinessManage.getBusinessProcess().sync_Time("123456", BusinessProcess.SEND_MODE_HTTP);
			}
		}

			break;
		case R.id.que_userinfo: {//查询用户信息

			if (mBusinessManage != null) {
				PadbookUserType userType = new PadbookUserType();
				userType.cardNum = "";//	人员id
				mBusinessManage.getBusinessProcess().que_UserInfo(userType, BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;
		case R.id.init_cabinet: {//初始化货柜

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
		case R.id.init_aisle: { //初始化货道
			if (mBusinessManage != null) {
				List<PadbookaisleInstType> padbookaisleInstTypeList = new ArrayList<ThemeDataModle_Ink.PadbookaisleInstType>();
				PadbookaisleInstType padbookaisleInstType = new PadbookaisleInstType();
				padbookaisleInstType.cabinetId = "1";//货柜id
				padbookaisleInstType.equtype = "123";//设备类型
				padbookaisleInstType.aislecode = "1";//货道编码
				padbookaisleInstType.cabinetCode = "1";//货柜编码
				padbookaisleInstType.capacityNum = "1";//货道容量
				padbookaisleInstType.storageNum = "0";//库存数量
				padbookaisleInstType.remark = "123";//备注
				padbookaisleInstType.aislestatus = "00";//货道状态		00：正常		01：故障02：禁用
				padbookaisleInstType.rowIndex = "1";//行索引
				padbookaisleInstType.colIndex = "1";//列索引
				padbookaisleInstTypeList.add(padbookaisleInstType);

				PadbookaisleInstType padbookaisleInstType2 = new PadbookaisleInstType();
				padbookaisleInstType2.cabinetId = "1";//货柜id
				padbookaisleInstType2.equtype = "123";//设备类型
				padbookaisleInstType2.aislecode = "2";//货道编码
				padbookaisleInstType2.cabinetCode = "1";//货柜编码
				padbookaisleInstType2.capacityNum = "1";//货道容量
				padbookaisleInstType2.storageNum = "0";//库存数量
				padbookaisleInstType2.remark = "1234";//备注
				padbookaisleInstType2.aislestatus = "00";//货道状态		00：正常		01：故障02：禁用
				padbookaisleInstType2.rowIndex = "1";//行索引
				padbookaisleInstType2.colIndex = "1";//列索引
				padbookaisleInstTypeList.add(padbookaisleInstType2);

				mBusinessManage.getBusinessProcess().init_aisle(padbookaisleInstTypeList, "ttttt777",
						BusinessProcess.SEND_MODE_TCP);
			}
		}
			break;

		case R.id.que_equ_para://获取设备参数
			mBusinessManage.getBusinessProcess().get_EquParam("123456", BusinessProcess.SEND_MODE_TCP);
			break;
		case R.id.que_edition://查询版本号
			mBusinessManage.getBusinessProcess().que_version("1101", "00", "123456", BusinessProcess.SEND_MODE_HTTP);
			break;
		case R.id.que_stuborrowrecord: {//根据学生查询借书记录接口
			mBusinessManage.getBusinessProcess().que_UserBorrowRecord("123456", "123456", "123456", "123456",
					BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.sysn_equ_info: {//设备信息同步
			EquInfoType equInfoType = new EquInfoType();
			equInfoType.equCode = "123456";//设备编码
			equInfoType.equSn = "123456";//设备板卡SN
			equInfoType.equType = "01";//设备类型00 借书柜 01墨水瓶柜 02 智能家居
			equInfoType.equName = "123456";//设备名称

			equInfoType.siteId = "1";//场所ID
			equInfoType.siteName = "123456";//场所名称
			equInfoType.provinceId = "350000";//省id
			equInfoType.provinceName = "福建";//省名
			equInfoType.cityId = "350100";//市id
			equInfoType.cityName = "福州";//市名
			equInfoType.areaId = "350101";//区id
			equInfoType.areaName = "鼓楼";//区名
			mBusinessManage.getBusinessProcess().sync_EquInfo("uuuu", equInfoType, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.get_equ_info: {//设备信息查询
			mBusinessManage.getBusinessProcess().get_EquInfo("uuuu", BusinessProcess.SEND_MODE_TCP);
		}
			break;

		case R.id.que_ink_info: {//查询墨水屏信息
			mBusinessManage.getBusinessProcess().que_padBookInfo("6061810001729", null, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.ink_updown: {//墨水屏上下架
			PadbookType padbookType = new PadbookType();
			padbookType.padbookCode = "6061810001729";
			padbookType.padbookId = "99";
			padbookType.padbookMac = "555";
			padbookType.padbookName = "墨水瓶3";
			padbookType.status = "2";

			mBusinessManage.getBusinessProcess().UpDown_Ink(padbookType, "123456", PadbookType.TYPE_UP,
					BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.padbookaisleinst_operate: {//设备墨水屏信息操作接口
			OperatePadbookType operatePadbookType = new OperatePadbookType();

			PadbookaisleInstType padbookaisleInstType = new PadbookaisleInstType();
			padbookaisleInstType.cabinetId = "123";//货柜id
			padbookaisleInstType.equtype = "123";//设备类型
			padbookaisleInstType.aislecode = "123";//货道编码
			padbookaisleInstType.cabinetCode = "123";//货柜编码
			padbookaisleInstType.capacityNum = "123";//货道容量
			padbookaisleInstType.storageNum = "123";//库存数量
			padbookaisleInstType.remark = "123";//备注
			padbookaisleInstType.aislestatus = "123";//货道状态		00：正常		01：故障02：禁用
			padbookaisleInstType.rowIndex = "123";//行索引
			padbookaisleInstType.colIndex = "123";//列索引
			padbookaisleInstType.padbookCode = "123";//墨水屏编码
			padbookaisleInstType.padbookName = "123";//	墨水屏名称
			padbookaisleInstType.status = "123";//墨水屏状态
			operatePadbookType.padbookInfo = padbookaisleInstType;

			mBusinessManage.getBusinessProcess().sync_OperatePadbookInfo("123456", operatePadbookType,
					BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.get_screensaver: {//墨水屏上下架
			mBusinessManage.getBusinessProcess().get_Screensaver("123456", BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.borrow_ink: {//墨水屏借出
			BrInfo brInfo = new BrInfo();
			brInfo.equ_code = "123";//设备编码
			brInfo.cabinet_id = "123";//货柜id
			brInfo.aisle_id = "123";//	货道id
			brInfo.user_id = "123";//用户id
			brInfo.cert_code = "123";//身份证号
			brInfo.site_id = "123";//场所id
			brInfo.area_id = "123";//区域id
			brInfo.padbook_code = "123";//墨水屏id
			brInfo.state = "1";//	状态1-借出，2-已还
			mBusinessManage.getBusinessProcess().borrow_Ink(brInfo, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.return_ink: {//墨水屏归还
			BrInfo brInfo = new BrInfo();
			brInfo.equ_code = "123";//设备编码
			brInfo.cabinet_id = "123";//货柜id
			brInfo.aisle_id = "123";//	货道id
			brInfo.user_id = "123";//用户id
			brInfo.cert_code = "123";//身份证号
			brInfo.site_id = "123";//场所id
			brInfo.area_id = "123";//区域id
			brInfo.padbook_code = "123";//墨水屏id
			brInfo.state = "2";//	状态1-借出，2-已还
			mBusinessManage.getBusinessProcess().Return_Ink(brInfo, BusinessProcess.SEND_MODE_TCP);
		}
			break;
		case R.id.sysn_screen: {//获取屏保
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
