package com.test.testtcp;

/**业务命令*/
public class BusinessCmd {
	/**同步区域信息接口HTTP*/
	public static final byte PACKET_CMD_SYNC_REGION = (byte) 0x11;
	/**同步场所信息接口HTTP*/
	public static final byte PACKET_CMD_SYNC_SITE = (byte) 0x12;
	/**同步时间 接口HTTP*/
	public static final byte PACKET_CMD_SYNC_TIME = (byte) 0x13;
	/**管理员注册终端设备接口HTTP*/
	public static final byte PACKET_CMD_REGISTER = (byte) 0x14;

	/**墨水瓶上下架*/
	public static final byte PACKET_CMD_SUB_RETURNINFO = (byte) 0x15;
	/**获取屏保资源接口*/
	public static final byte PACKET_CMD_SYSN_SCREEN = (byte) 0x16;
	/**借墨水屏数据提交接口*/
	public static final byte PACKET_CMD_INK_BORROW = (byte) 0x17;
	/**还墨水屏数据提交接口*/
	public static final byte PACKET_CMD_INK_RETRUN = (byte) 0x18;
	/**设备参数查询接口*/
	public static final byte PACKET_CMD_QUE_EQUPARAMINFO = (byte) 0x19;
	/**查询版本号接口*/
	public static final byte PACKET_CMD_QUE_VERSION = (byte) 0x20;
	/**设备墨水屏信息操作接口*/
	public static final byte PACKET_CMD_OPERATION_EQUINK = (byte) 0x21;
	/**根据用户查询借书记录接口*/
	public static final byte PACKET_CMD_QUE_USER_BORROW_RECORD = (byte) 0x22;
	/**设备信息同步接口*/
	public static final byte PACKET_CMD_SYSN_EQUINFO = (byte) 0x23;
	/**向平台查询设备货道信息接口*/
	public static final byte PACKET_CMD_QUE_AISLE = (byte) 0x24;
	/**设备告警信息*/
	public static final byte PACKET_CMD_EQU_ALARM = (byte) 0x25;
	/**同步机构数据表*/
	public static final byte PACKET_CMD_SYSN_ORG = (byte) 0x26;
	/**设备日志上传*/
	public static final byte PACKET_CMD_SUB_LOG = (byte) 0x27;
	/**查询墨水屏信息接口*/
	public static final byte PACKET_CMD_QUE_INKINFO = (byte) 0x28;

	/**押金信息提交HTTP*/
	public static final byte PACKET_CMD_SUB_DEPOSIT = (byte) 0x29;
	/**押金支付结果查询HTTP*/
	public static final byte PACKET_CMD_QUE_DEPOSIT = (byte) 0x30;

	/** 用户注册*/
	public static final byte PACKET_CMD_REGISTER_USER = (byte) 0x31;
	/** 查询用户信息*/
	public static final byte PACKET_CMD_QUE_USER = (byte) 0x32;
	/** 同步用户信息*/
	public static final byte PACKET_CMD_SYNC_USER = (byte) 0x33;
	/** 初始化货道*/
	public static final byte PACKET_CMD_INIT_AISLE = (byte) 0x34;
	/** 初始化货柜*/
	public static final byte PACKET_CMD_INIT_CABINET = (byte) 0x35;
	/** 修改货柜信息*/
	public static final byte PACKET_CMD_CHANGE_CABINET = (byte) 0x36;
	/** 墨水屏入库*/
	public static final byte PACKET_CMD_INK_WAREHOUSING = (byte) 0x37;
	/** 设备参数设置*/
	public static final byte PACKET_CMD_SET_EQUPARAM = (byte) 0x38;
	/**墨水屏借阅*/
	public static final byte PACKET_CMD_BORROW_INK = (byte) 0x39;
	/**墨水屏归还*/
	public static final byte PACKET_CMD_INIT_RETURN_INK = (byte) 0x40;
	/**设备信息查询接口*/
	public static final byte PACKET_CMD_QUE_EQUINFO = (byte) 0x41;
	/**超星清理屏信息*/
	public static final byte PACKET_CMD_QUE_CLEAN = (byte) 0x42;
}