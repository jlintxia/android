package com.test.testtcp;

import java.util.List;

import com.test.testtcp.ThemeDataModle_Ink.ArtifactVerType;

public class ThemeDataModle_Ink {
	/**场所信息信息 */
	public static class SiteType {
		public String siteId;//场所ID
		public String siteName;//场所名称
		public String siteCode;//场所编码
		public String provinceId;//省区域ID
		public String cityId;//市区域ID
		public String countyId;//(区/县)区域ID
		public String status;//状态
		public String createTime;//创建时间

	}

	/**区域信息 */
	public static class RegionType {
		public String areaId;//区域ID
		public String areaCode;//区域编码
		public String parentCode;//上级区域编码
		public String areaName;//区域名称
		public String pingyin;//拼音名称
	}

	/**借还墨水瓶数据提交接口使用*/
	public static class SubBorrowReturn {
		public String userName;//用户名
		public String certCode;//身份证号
		public String areaId;//区域Id
		public String areaCode;//区域编码
		public String areaName;//区域名称
		public String siteId;//场所Id
		public String siteCode;//	场所编码
		public String siteName;//场所名称
		public String equCode;//设备编码
		public String equName;//设备名称
		public List<PadbookType> padbookInfoList;//墨水屏信息

	}

	/**墨水屏信息*/
	public static class PadbookType {
		public String padbookId;//墨水屏id
		public String padbookCode;//墨水屏编码
		public String padbookName;//墨水屏名称
		public String padbookAuthor;//墨水屏厂家
		public String padbookType;//墨水屏类型
		public String padbookMac;//墨水屏MAC
		public String padbookVersion;//墨水屏版本
		public String userScope;//使用范围:1-学校,2-C端用户
		public String status;//状态 0-上架,1-下架,2-借出
		public String areaId;//区域Id
		public String areaName;//区域名称
		public String siteId;//场所Id
		public String siteName;//场所名称
		public String createDate;//入库时间
		public String updateDate;//更新时间

		public static String TYPE_UP = "00";//00 上架 01 下架
		public static String TYPE_DOWN = "01";

	}

	/**设备墨水屏信息操作接口*/
	public static class OperatePadbookType {
		public PadbookaisleInstType padbookInfo;//货道信息
		public String operateType;//操作类型 sync 同步 add新增 del删除

		public static final String SYNC = "sync";//同步
		public static final String ADD = "add";//新增
		public static final String DEL = "del";//删除
	}

	/**设备参数信息*/
	public static class EquParamInfoType {
		public String paramId;//参数编号ID
		public String equId;//设备ID
		public String equCode;//设备编码
		public String paremName;//参数名称
		public String paremValue;//参数值
		public String createDate;//创建时间

	}

	/**设备信息*/
	public static class EquInfoType {
		public String equId;//设备ID
		public String equCode;//设备编码
		public String equSn;//设备板卡SN
		public String equType;//设备类型00 借书柜 01墨水瓶柜 02 智能家居
		public String equName;//设备名称
		public String equDesc;//设备描述
		public String equIp;//设备IP
		public String equGateway;//设备网关
		public String equ_submask;//	子网掩码
		public String equ_dns;//首选域名服务器
		public String equ_dns2;//备用域名服务器
		public String equMac;//mac地址
		public String network_type;//网络类型动态IP:DYNAMIC静态IP:STATIC
		public String versionCode;//设备版本号
		public String status;//00:离线01:在线	02：故障
		public String accSysIp;//接入系统IP
		public String accSysPort;//	接入系统端口
		public String accSysId;//接入ID
		public String createDate;//创建时间
		public String updateDate;//更新时间
		public String siteId;//场所ID
		public String siteName;//场所名称
		public String provinceId;//省id
		public String provinceName;//省名
		public String cityId;//市id
		public String cityName;//市名
		public String areaId;//区id
		public String areaName;//区名

	}

	//机构结构体
	public static class org_structure {
		public String siteId;//场所ID
		public String siteName;//场所名称
		public String province_id;//省id
		public String province_name;//省名
		public String city_id;//市id
		public String city_name;//市名
		public String area_id;//区id
		public String area_name;//区名
	}

	public static class Update {
		public ErrorType error;
		public String verNo; //版本号
		public String verDesc;//版本文件名
		public String downloadUrl;//下载路径
		public String md5Check;
		public String subject;//升级类型
		public List<ArtifactVerType> artifactList;//组件列表
	}

	//系统组件信息
	public static class ArtifactVerType {
		public String artifactVerId; //系统版本Id
		public String sysCode; //组件编码
		public String artifactVerCode; //最新版本号
		public String artifactVerInfo; //更新信息(包名)
		public String artifaceDownloadPath;//更新包下载路径
		public String md5Check; //md5校验
		public String artifactType; //类型，01：补丁，02：完整包
		public String oldArtifactCode; //旧版本号
		public String filename; //文件名
	}

	/**借还列表信息*/
	public static class PadbookBrType {
		public String recordId;//记录编号
		public String userId;//用户ID
		public String userName;//用户名称
		public String areaId;//区域Id
		public String areaName;//区域名称
		public String siteId;//场所Id
		public String siteName;//场所名称
		public String equId;//设备ID
		public String equName;//设备名称
		public String padbookId;//墨水瓶ID
		public String padbookName;//墨水瓶名称
		public String createDate;//创建时间
		public String state;//1-借出，2-已还
		public String bookState;//墨水瓶状态  0,未知, 1,正常，2.破损
		public String comment;//备注

	}

	/**押金订单表*/
	public static class Padbook_order_deposit {
		public String userId;//用户ID
		public String depositOrder;//押金流水
		public String orderType;//操作类型,1-充值押金，2-退款
		public String creatTime;//创建时间
		public String orderStatus;//订单状态10000：未支付50000：完成20000：支付成功20001：支付失败40000：退款成功40001：退款失败50001：撤销99999：异常
		public String payType;//支付方式：01：微信支付02：支付宝支付
		public String orderAmount;//交易总金额
	}

	/**二维码信息*/
	public static class QrCodeType {
		public String qrCodeType;//二维码类型	00：支付信息
		public String qrCodeInfo;//二维码url
		public String qrCodeDesc;//二维码描述
		public String depositOrder;//押金流水
	}

	/**用户信息*/
	public static class PadbookUserType {
		public String userId;//用户主键
		public String userName;//用户名
		public String realName;//真实姓名
		public String password;//密码
		public String userType;//用户类型
		public String sex;//性别
		public String phone;//电话
		public String email;//邮箱
		public String certCode;//身份证号
		public String address;//联系地址
		public String status;//帐号状态  正常：00  冻结（未交押金）：01  挂失：02  注销：03
		public String siteId;//场所ID
		public String areaId;//区域ID
		public String openId;//openId
		public String openType;//押金支付类型1-微信,2-支付宝，3-其他
		public String deposit;//押金金额
		public String createTime;//创建时间
		public String updateTime;//更新时间
		public String pwdExpiredTime;//密码失效时间
		public String roleId;//角色ID
		public String cardNum;//卡号

	}

	/**设备货道墨水屏信息*/
	public static class PadbookaisleInstType {
		public String aisleId;//货道id
		public String cabinetId;//货柜id
		public String equtype;//设备类型
		public String aislecode;//货道编码
		public String cabinetCode;//货柜编码
		public String capacityNum;//货道容量
		public String storageNum;//库存数量
		public String remark;//备注
		public String aislestatus;//货道状态		00：正常		01：故障02：禁用
		public String rowIndex;//行索引
		public String colIndex;//列索引
		public String createDate;//创建时间
		public String updateDate;//更新时间
		public String padbookCode;//墨水屏编码
		public String padbookName;//	墨水屏名称
		public String status;//墨水屏状态

	}

	/**设备货柜信息*/
	public static class CabinetInit {
		public String cabinetId;//货柜Id
		public String cabinetCode;//货柜编码
		public String status;//状态：00 删除
	}

	/**分页信息*/
	public static class PageInfo {
		public String page;//当前页数
		public String pageCount;//每页记录数
		public String total;//总记录数
	}

	/**借还墨水瓶信息*/
	public static class BrInfo {
		public String equ_id;//设备id
		public String equ_code;//设备编码
		public String cabinet_id;//货柜id
		public String aisle_id;//	货道id  
		public String user_id;//用户id
		public String cert_code;//身份证号
		public String site_id;//场所id
		public String area_id;//区域id
		public String padbook_id;//墨水屏id
		public String padbook_code;//墨水屏id
		public String state;//	状态1-借出，2-已还
		public String comment;//备注
	}

	/*	  1001	发卡器回收仓满
	1002	发卡器无卡
	1003	发卡器打开失败
	1999	 发卡器未知故障
	2001	 身份证读卡器打开失败
	2999	 身份证读卡器未知错误
	3001	 借书柜控制板连接失败
	3002	 货道门异常
	3003	 货道检测异常
	3004	 货道检测异常
	3999	 控制板未知异常
	4001	 墨水屏连接失败
	4002	墨水屏充电异常
	4003	墨水屏损坏
	4999	墨水屏未知异常*/
	/**告警信息表*/
	public static class AlarmInfoType {
		public String alarmId;//告警id
		public String alarmType;//告警类型
		public String alarmLevel;//告警等级	00:紧急告警	01:重要告警02:次要告警03:提示告警
		public String isDeal;//告警是否处理00未处理01已处理
		public String alarmDate;//告警时间
		public String alarmDesc;//告警详情
	}

	/**错误信息类*/
	public static class ErrorType {//id = err;json解析失败
		public String id; //0000成功  9999失败
		public String message; //描述
	}

	/**屏保资源信息*/
	public static class ScreenResourceType {
		public String type;//资源类型 图片：img
		public String url;//资源URL
	}

	//----超星接口----------------------------------
	/**超星区域 */
	public static class CX_Region {
		public String id;//id
		public String level;//层级
		public String name;//名称
		public String parent_id;//父id
	}

	/**超星图书馆 */
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
	/**超星屏保 */
	public static class CX_SCREEN {
		public String pic;//图片url
	 
	}
}
