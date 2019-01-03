package com.test.testtcp;

import java.util.List;

import com.test.testtcp.ThemeDataModle_Ink.ArtifactVerType;

public class ThemeDataModle_Ink {
	/**������Ϣ��Ϣ */
	public static class SiteType {
		public String siteId;//����ID
		public String siteName;//��������
		public String siteCode;//��������
		public String provinceId;//ʡ����ID
		public String cityId;//������ID
		public String countyId;//(��/��)����ID
		public String status;//״̬
		public String createTime;//����ʱ��

	}

	/**������Ϣ */
	public static class RegionType {
		public String areaId;//����ID
		public String areaCode;//�������
		public String parentCode;//�ϼ��������
		public String areaName;//��������
		public String pingyin;//ƴ������
	}

	/**�軹īˮƿ�����ύ�ӿ�ʹ��*/
	public static class SubBorrowReturn {
		public String userName;//�û���
		public String certCode;//���֤��
		public String areaId;//����Id
		public String areaCode;//�������
		public String areaName;//��������
		public String siteId;//����Id
		public String siteCode;//	��������
		public String siteName;//��������
		public String equCode;//�豸����
		public String equName;//�豸����
		public List<PadbookType> padbookInfoList;//īˮ����Ϣ

	}

	/**īˮ����Ϣ*/
	public static class PadbookType {
		public String padbookId;//īˮ��id
		public String padbookCode;//īˮ������
		public String padbookName;//īˮ������
		public String padbookAuthor;//īˮ������
		public String padbookType;//īˮ������
		public String padbookMac;//īˮ��MAC
		public String padbookVersion;//īˮ���汾
		public String userScope;//ʹ�÷�Χ:1-ѧУ,2-C���û�
		public String status;//״̬ 0-�ϼ�,1-�¼�,2-���
		public String areaId;//����Id
		public String areaName;//��������
		public String siteId;//����Id
		public String siteName;//��������
		public String createDate;//���ʱ��
		public String updateDate;//����ʱ��

		public static String TYPE_UP = "00";//00 �ϼ� 01 �¼�
		public static String TYPE_DOWN = "01";

	}

	/**�豸īˮ����Ϣ�����ӿ�*/
	public static class OperatePadbookType {
		public PadbookaisleInstType padbookInfo;//������Ϣ
		public String operateType;//�������� sync ͬ�� add���� delɾ��

		public static final String SYNC = "sync";//ͬ��
		public static final String ADD = "add";//����
		public static final String DEL = "del";//ɾ��
	}

	/**�豸������Ϣ*/
	public static class EquParamInfoType {
		public String paramId;//�������ID
		public String equId;//�豸ID
		public String equCode;//�豸����
		public String paremName;//��������
		public String paremValue;//����ֵ
		public String createDate;//����ʱ��

	}

	/**�豸��Ϣ*/
	public static class EquInfoType {
		public String equId;//�豸ID
		public String equCode;//�豸����
		public String equSn;//�豸�忨SN
		public String equType;//�豸����00 ����� 01īˮƿ�� 02 ���ܼҾ�
		public String equName;//�豸����
		public String equDesc;//�豸����
		public String equIp;//�豸IP
		public String equGateway;//�豸����
		public String equ_submask;//	��������
		public String equ_dns;//��ѡ����������
		public String equ_dns2;//��������������
		public String equMac;//mac��ַ
		public String network_type;//�������Ͷ�̬IP:DYNAMIC��̬IP:STATIC
		public String versionCode;//�豸�汾��
		public String status;//00:����01:����	02������
		public String accSysIp;//����ϵͳIP
		public String accSysPort;//	����ϵͳ�˿�
		public String accSysId;//����ID
		public String createDate;//����ʱ��
		public String updateDate;//����ʱ��
		public String siteId;//����ID
		public String siteName;//��������
		public String provinceId;//ʡid
		public String provinceName;//ʡ��
		public String cityId;//��id
		public String cityName;//����
		public String areaId;//��id
		public String areaName;//����

	}

	//�����ṹ��
	public static class org_structure {
		public String siteId;//����ID
		public String siteName;//��������
		public String province_id;//ʡid
		public String province_name;//ʡ��
		public String city_id;//��id
		public String city_name;//����
		public String area_id;//��id
		public String area_name;//����
	}

	public static class Update {
		public ErrorType error;
		public String verNo; //�汾��
		public String verDesc;//�汾�ļ���
		public String downloadUrl;//����·��
		public String md5Check;
		public String subject;//��������
		public List<ArtifactVerType> artifactList;//����б�
	}

	//ϵͳ�����Ϣ
	public static class ArtifactVerType {
		public String artifactVerId; //ϵͳ�汾Id
		public String sysCode; //�������
		public String artifactVerCode; //���°汾��
		public String artifactVerInfo; //������Ϣ(����)
		public String artifaceDownloadPath;//���°�����·��
		public String md5Check; //md5У��
		public String artifactType; //���ͣ�01��������02��������
		public String oldArtifactCode; //�ɰ汾��
		public String filename; //�ļ���
	}

	/**�軹�б���Ϣ*/
	public static class PadbookBrType {
		public String recordId;//��¼���
		public String userId;//�û�ID
		public String userName;//�û�����
		public String areaId;//����Id
		public String areaName;//��������
		public String siteId;//����Id
		public String siteName;//��������
		public String equId;//�豸ID
		public String equName;//�豸����
		public String padbookId;//īˮƿID
		public String padbookName;//īˮƿ����
		public String createDate;//����ʱ��
		public String state;//1-�����2-�ѻ�
		public String bookState;//īˮƿ״̬  0,δ֪, 1,������2.����
		public String comment;//��ע

	}

	/**Ѻ�𶩵���*/
	public static class Padbook_order_deposit {
		public String userId;//�û�ID
		public String depositOrder;//Ѻ����ˮ
		public String orderType;//��������,1-��ֵѺ��2-�˿�
		public String creatTime;//����ʱ��
		public String orderStatus;//����״̬10000��δ֧��50000�����20000��֧���ɹ�20001��֧��ʧ��40000���˿�ɹ�40001���˿�ʧ��50001������99999���쳣
		public String payType;//֧����ʽ��01��΢��֧��02��֧����֧��
		public String orderAmount;//�����ܽ��
	}

	/**��ά����Ϣ*/
	public static class QrCodeType {
		public String qrCodeType;//��ά������	00��֧����Ϣ
		public String qrCodeInfo;//��ά��url
		public String qrCodeDesc;//��ά������
		public String depositOrder;//Ѻ����ˮ
	}

	/**�û���Ϣ*/
	public static class PadbookUserType {
		public String userId;//�û�����
		public String userName;//�û���
		public String realName;//��ʵ����
		public String password;//����
		public String userType;//�û�����
		public String sex;//�Ա�
		public String phone;//�绰
		public String email;//����
		public String certCode;//���֤��
		public String address;//��ϵ��ַ
		public String status;//�ʺ�״̬  ������00  ���ᣨδ��Ѻ�𣩣�01  ��ʧ��02  ע����03
		public String siteId;//����ID
		public String areaId;//����ID
		public String openId;//openId
		public String openType;//Ѻ��֧������1-΢��,2-֧������3-����
		public String deposit;//Ѻ����
		public String createTime;//����ʱ��
		public String updateTime;//����ʱ��
		public String pwdExpiredTime;//����ʧЧʱ��
		public String roleId;//��ɫID
		public String cardNum;//����

	}

	/**�豸����īˮ����Ϣ*/
	public static class PadbookaisleInstType {
		public String aisleId;//����id
		public String cabinetId;//����id
		public String equtype;//�豸����
		public String aislecode;//��������
		public String cabinetCode;//�������
		public String capacityNum;//��������
		public String storageNum;//�������
		public String remark;//��ע
		public String aislestatus;//����״̬		00������		01������02������
		public String rowIndex;//������
		public String colIndex;//������
		public String createDate;//����ʱ��
		public String updateDate;//����ʱ��
		public String padbookCode;//īˮ������
		public String padbookName;//	īˮ������
		public String status;//īˮ��״̬

	}

	/**�豸������Ϣ*/
	public static class CabinetInit {
		public String cabinetId;//����Id
		public String cabinetCode;//�������
		public String status;//״̬��00 ɾ��
	}

	/**��ҳ��Ϣ*/
	public static class PageInfo {
		public String page;//��ǰҳ��
		public String pageCount;//ÿҳ��¼��
		public String total;//�ܼ�¼��
	}

	/**�軹īˮƿ��Ϣ*/
	public static class BrInfo {
		public String equ_id;//�豸id
		public String equ_code;//�豸����
		public String cabinet_id;//����id
		public String aisle_id;//	����id  
		public String user_id;//�û�id
		public String cert_code;//���֤��
		public String site_id;//����id
		public String area_id;//����id
		public String padbook_id;//īˮ��id
		public String padbook_code;//īˮ��id
		public String state;//	״̬1-�����2-�ѻ�
		public String comment;//��ע
	}

	/*	  1001	���������ղ���
	1002	�������޿�
	1003	��������ʧ��
	1999	 ������δ֪����
	2001	 ���֤��������ʧ��
	2999	 ���֤������δ֪����
	3001	 �������ư�����ʧ��
	3002	 �������쳣
	3003	 ��������쳣
	3004	 ��������쳣
	3999	 ���ư�δ֪�쳣
	4001	 īˮ������ʧ��
	4002	īˮ������쳣
	4003	īˮ����
	4999	īˮ��δ֪�쳣*/
	/**�澯��Ϣ��*/
	public static class AlarmInfoType {
		public String alarmId;//�澯id
		public String alarmType;//�澯����
		public String alarmLevel;//�澯�ȼ�	00:�����澯	01:��Ҫ�澯02:��Ҫ�澯03:��ʾ�澯
		public String isDeal;//�澯�Ƿ���00δ����01�Ѵ���
		public String alarmDate;//�澯ʱ��
		public String alarmDesc;//�澯����
	}

	/**������Ϣ��*/
	public static class ErrorType {//id = err;json����ʧ��
		public String id; //0000�ɹ�  9999ʧ��
		public String message; //����
	}

	/**������Դ��Ϣ*/
	public static class ScreenResourceType {
		public String type;//��Դ���� ͼƬ��img
		public String url;//��ԴURL
	}

	//----���ǽӿ�----------------------------------
	/**�������� */
	public static class CX_Region {
		public String id;//id
		public String level;//�㼶
		public String name;//����
		public String parent_id;//��id
	}

	/**����ͼ��� */
	public static class CX_Lib {
		public String address;//
		public String area_id;//
		public String city_id;//
		public String created_at;//
		public String creater_id;//
		public String id;//
		public String invite_code;//
		public String is_delete;//
		public String logo;//
		public String name;//
		public String phone;//
		public String province_id;//
		public String study_url;//http://dev.lib.chaoxingbook.com/chaoxingstudy/index?libraryId=bbcf4ddced5c433f0544ec019fd15025",
		public String updated_at;//2018-07-18 11:55:28",
		public String wechat_url;//http://dev.lib.chaoxingbook.com/wechat/index?libraryId=bbcf4ddced5c433f0544ec019fd15025"
	}
	/**�������� */
	public static class CX_SCREEN {
		public String pic;//ͼƬurl
	 
	}
}
