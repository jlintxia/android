package com.test.testtcp;

/**ҵ������*/
public class BusinessCmd {
	/**ͬ��������Ϣ�ӿ�HTTP*/
	public static final byte PACKET_CMD_SYNC_REGION = (byte) 0x11;
	/**ͬ��������Ϣ�ӿ�HTTP*/
	public static final byte PACKET_CMD_SYNC_SITE = (byte) 0x12;
	/**ͬ��ʱ�� �ӿ�HTTP*/
	public static final byte PACKET_CMD_SYNC_TIME = (byte) 0x13;
	/**����Աע���ն��豸�ӿ�HTTP*/
	public static final byte PACKET_CMD_REGISTER = (byte) 0x14;

	/**īˮƿ���¼�*/
	public static final byte PACKET_CMD_SUB_RETURNINFO = (byte) 0x15;
	/**��ȡ������Դ�ӿ�*/
	public static final byte PACKET_CMD_SYSN_SCREEN = (byte) 0x16;
	/**��īˮ�������ύ�ӿ�*/
	public static final byte PACKET_CMD_INK_BORROW = (byte) 0x17;
	/**��īˮ�������ύ�ӿ�*/
	public static final byte PACKET_CMD_INK_RETRUN = (byte) 0x18;
	/**�豸������ѯ�ӿ�*/
	public static final byte PACKET_CMD_QUE_EQUPARAMINFO = (byte) 0x19;
	/**��ѯ�汾�Žӿ�*/
	public static final byte PACKET_CMD_QUE_VERSION = (byte) 0x20;
	/**�豸īˮ����Ϣ�����ӿ�*/
	public static final byte PACKET_CMD_OPERATION_EQUINK = (byte) 0x21;
	/**�����û���ѯ�����¼�ӿ�*/
	public static final byte PACKET_CMD_QUE_USER_BORROW_RECORD = (byte) 0x22;
	/**�豸��Ϣͬ���ӿ�*/
	public static final byte PACKET_CMD_SYSN_EQUINFO = (byte) 0x23;
	/**��ƽ̨��ѯ�豸������Ϣ�ӿ�*/
	public static final byte PACKET_CMD_QUE_AISLE = (byte) 0x24;
	/**�豸�澯��Ϣ*/
	public static final byte PACKET_CMD_EQU_ALARM = (byte) 0x25;
	/**ͬ���������ݱ�*/
	public static final byte PACKET_CMD_SYSN_ORG = (byte) 0x26;
	/**�豸��־�ϴ�*/
	public static final byte PACKET_CMD_SUB_LOG = (byte) 0x27;
	/**��ѯīˮ����Ϣ�ӿ�*/
	public static final byte PACKET_CMD_QUE_INKINFO = (byte) 0x28;

	/**Ѻ����Ϣ�ύHTTP*/
	public static final byte PACKET_CMD_SUB_DEPOSIT = (byte) 0x29;
	/**Ѻ��֧�������ѯHTTP*/
	public static final byte PACKET_CMD_QUE_DEPOSIT = (byte) 0x30;

	/** �û�ע��*/
	public static final byte PACKET_CMD_REGISTER_USER = (byte) 0x31;
	/** ��ѯ�û���Ϣ*/
	public static final byte PACKET_CMD_QUE_USER = (byte) 0x32;
	/** ͬ���û���Ϣ*/
	public static final byte PACKET_CMD_SYNC_USER = (byte) 0x33;
	/** ��ʼ������*/
	public static final byte PACKET_CMD_INIT_AISLE = (byte) 0x34;
	/** ��ʼ������*/
	public static final byte PACKET_CMD_INIT_CABINET = (byte) 0x35;
	/** �޸Ļ�����Ϣ*/
	public static final byte PACKET_CMD_CHANGE_CABINET = (byte) 0x36;
	/** īˮ�����*/
	public static final byte PACKET_CMD_INK_WAREHOUSING = (byte) 0x37;
	/** �豸��������*/
	public static final byte PACKET_CMD_SET_EQUPARAM = (byte) 0x38;
	/**īˮ������*/
	public static final byte PACKET_CMD_BORROW_INK = (byte) 0x39;
	/**īˮ���黹*/
	public static final byte PACKET_CMD_INIT_RETURN_INK = (byte) 0x40;
	/**�豸��Ϣ��ѯ�ӿ�*/
	public static final byte PACKET_CMD_QUE_EQUINFO = (byte) 0x41;
	/**������������Ϣ*/
	public static final byte PACKET_CMD_QUE_CLEAN = (byte) 0x42;
}