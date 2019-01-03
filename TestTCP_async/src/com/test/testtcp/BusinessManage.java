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

/**ƽ̨ҵ��ͨѶ���� (tcp http ���json���ݣ����������������Ǵ������ݿ⻹�ǻص��ϲ�)*/
public class BusinessManage implements BusinessProcessCallBack {
	private static final String TAG = "BusinessManage";

	private List<OnBusinessListener> onBusinessListeners = new ArrayList<OnBusinessListener>();
	private Mutex mutex = new Mutex();
	private ExecutorService businessExecutorService;

	private Ink_BusinessProcess mBusinessProcess;//�ṩ�����÷��ʹ���ӿ�

	public BusinessManage(Context mContext, TcpProcess mTcpProcess, HttpProcess mHttpProcess) {
		mBusinessProcess = new Ink_BusinessProcess(mTcpProcess, mHttpProcess, mContext, this);
		businessExecutorService = Executors.newScheduledThreadPool(6);
	}

	/**ҵ������ص�*/
	public interface OnBusinessListener {

		//ҵ��ص�
		public void sendCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object);

		//����ʧ�ܳ�ʱ��
		public void connectFail();
	}

	/**���ü����б�*/
	public void setOnBusinessListener(OnBusinessListener onBusinessListener) {
		mutex.lock(0);
		onBusinessListeners.add(onBusinessListener);
		mutex.unlock();
	}

	/**�Ƴ�ĳ������*/
	public void removeOnBusinessListener(OnBusinessListener onBusinessListener) {
		mutex.lock(0);
		onBusinessListeners.remove(onBusinessListener);
		mutex.unlock();
	}

	/**�ص������Ϣ*/
	private void sendCallback(byte cmd, ErrorType errorType, PageInfo pageInfo, Object object) {
		mutex.lock(0);
		for (int i = 0; i < onBusinessListeners.size(); i++) {
			onBusinessListeners.get(i).sendCallback(cmd, errorType, pageInfo, object);
		}
		mutex.unlock();
	}

	//�ṩ���=====================================================
	/**����ʧ�ܻص������ոûص���������ʧ��*/
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
				//��ȡ������Դ�ӿ�*/
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
						int a = 0;//����
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
