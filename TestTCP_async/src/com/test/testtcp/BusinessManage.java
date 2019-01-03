package com.test.testtcp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.function.tcputils.TcpProcess;
import com.function.tool.Mutex;
import com.guozheng.okhttputils.HttpProcess;
import com.test.testtcp.BusinessProcess.BusinessProcessCallBack;
import com.test.testtcp.ThemeDataModle_Ink.CX_SCREEN;
import com.test.testtcp.ThemeDataModle_Ink.ErrorType;
import com.test.testtcp.ThemeDataModle_Ink.PageInfo;

import android.content.Context;
import android.util.Log;

/**平台业务通讯管理 (tcp http 获得json数据，传入此类分析处理是存入数据库还是回调上层)*/
public class BusinessManage implements BusinessProcessCallBack {
	private static final String TAG = "BusinessManage";

	private List<OnBusinessListener> onBusinessListeners = new ArrayList<OnBusinessListener>();
	private Mutex mutex = new Mutex();
	private ExecutorService businessExecutorService;

	private Ink_BusinessProcess mBusinessProcess;//提供外层调用发送打包接口

	public BusinessManage(Context mContext, TcpProcess mTcpProcess, HttpProcess mHttpProcess) {
		mBusinessProcess = new Ink_BusinessProcess(mTcpProcess, mHttpProcess, mContext, this);
		businessExecutorService = Executors.newScheduledThreadPool(6);
	}

	/**业务请求回调*/
	public interface OnBusinessListener {

		//业务回调
		public void sendCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object);

		//连接失败超时等
		public void connectFail();
	}

	/**设置监听列表*/
	public void setOnBusinessListener(OnBusinessListener onBusinessListener) {
		mutex.lock(0);
		onBusinessListeners.add(onBusinessListener);
		mutex.unlock();
	}

	/**移除某监听项*/
	public void removeOnBusinessListener(OnBusinessListener onBusinessListener) {
		mutex.lock(0);
		onBusinessListeners.remove(onBusinessListener);
		mutex.unlock();
	}

	/**回调解包信息*/
	private void sendCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object) {
		mutex.lock(0);
		for (int i = 0; i < onBusinessListeners.size(); i++) {
			onBusinessListeners.get(i).sendCallback(cmd, errorType, pageInfo, object);
		}
		mutex.unlock();
	}

	//提供外层=====================================================
	/**连接失败回调，接收该回调所有请求都失败*/
	public void sendConnectFail() {
		mutex.lock(0);
		for (int i = 0; i < onBusinessListeners.size(); i++) {
			onBusinessListeners.get(i).connectFail();
		}
		mutex.unlock();
	}

	int i = 0;

	@Override
	public void analysisCallback(final byte cmd, final ErrorType errorType, final PageInfo pageInfo,
			final Object object) {

		businessExecutorService.submit(new Runnable() {
			@Override
			public void run() {
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
				//获取屏保资源接口*/
				case BusinessCmd.PACKET_CMD_SYSN_SCREEN:
					final List<CX_SCREEN> cabinetInitList = (List<CX_SCREEN>) object;
				/*	for (i = 0; i < cabinetInitList.size(); i++) {
						Log.e(TAG, "cabinetInitList.get(i).pic  " + cabinetInitList.get(i).pic);
						SystemManager.LOGI(TAG, "URL = " + screenlist.get(i).url + " ");
						String[] imgPath = screenlist.get(i).url.split("/");
						Download download = new Download();
						int fileSizes = download.getDownloadFileSize(screenlist.get(i).url);
						int writeSize = download.downloadFile(screenlist.get(i).url,
								ThemeDataType.Path.ImagePath + imgPath[imgPath.length - 1], 0, fileSizes);
						boolean isFinish = true;
						int a = 0;//计数
						while (isFinish) {
							if (writeSize == fileSizes) {
								isFinish = false;
							} else {
								writeSize = download.downloadFile(screenlist.get(i).url,
										ThemeDataType.Path.ImagePath + imgPath[imgPath.length - 1], 0,
										fileSizes);
								a++;
								if (a == 3) {
									isFinish = false;
								}
							}
						}
						download = null;
					}
*/
					/*if (cabinetInitList != null && cabinetInitList.size() > 0) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								for (int i = 0; i < cabinetInitList.size(); i++) {
									Log.e(TAG, "cabinetInitList.get(i).pic  " + cabinetInitList.get(i).pic);
									mBusinessProcess.downScreen(cabinetInitList.get(i).pic);
								}
							}
						}).start();
					}*/
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public void connectFail() {
		sendConnectFail();
	}

	public Ink_BusinessProcess getBusinessProcess() {
		return mBusinessProcess;
	}

}
