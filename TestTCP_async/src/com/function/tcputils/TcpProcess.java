package com.function.tcputils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.function.tcputils.MySocket.MySocketCallback;
import com.function.tcputils.TcpProtocol.Encrypt;
import com.function.tool.Mutex;
import com.function.tool.MyEncrypt;
import com.test.testtcp.SystemManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class TcpProcess {

	private static final String TAG = "TcpProcess";

	private MySocket mySocket;
	private TcpCallback mCallback;
	private TcpProtocol mTcpProtocol;

	private String ip;
	private int port;

	public String equ_id = "890";

	private byte mainSystem = TcpProtocol.FormToType.SYSTEM_BOOK;//����ϵͳ Ĭ�Ͻ����

	//tcp����״̬
	private boolean isStart = false;
	//�Ƿ��Ȩ�ɹ���δ��Ȩ�޷�����
	private boolean isAuto = false;
	private Context mContext;

	private int heart = 60 * 1000;//����ʱ��
	private int timeout = 10 * 1000;//�첽�ȴ���ʱʱ��

	private Mutex mutex = new Mutex();

	//�첽�ȴ�����---------------------------------------
	public class Process {
		public AppData prot = null;
		public Timer timer = null;

		public Process(AppData prot, Timer timer) {
			this.prot = prot;
			this.timer = timer;
		}
	}

	public TcpProcess(TcpCallback callback, Context context, byte mainSystem) {
		mCallback = callback;
		this.mContext = context;
		this.mainSystem = mainSystem;

	}

	//������У��ж��·�����������е�sn cmd ��ͬ��ΪӦ����һ������
	private ArrayList<Process> mReqProcList = new ArrayList<Process>();

	//---------------------------------------

	public void init() {
		mTcpProtocol = new TcpProtocol();
		mySocket = new MySocket(new MySocketCallback() {

			@Override
			public void send(byte cmd, boolean ret) {
				// ���ͽ���ص�
				if (!ret) {
					SystemManager.LOGD(TAG, "send ret = false == >" + cmd);
					switch (cmd) {
					// �豸������Ȩ���� ����ʧ�ܣ����·���
					case TcpProtocol.Cmd.PACKET_CMD_START:
						handler.removeMessages(HANDLER_START);
						handler.sendEmptyMessageDelayed(HANDLER_START, 1000);
						break;
					}
				} else {//��������������������������������ʱ�������� ����һֱ��
//					handler.removeMessages(HANDLER_HEART);
//					handler.sendEmptyMessageDelayed(HANDLER_HEART, heart);
				}
			}

			@Override
			public void read(List<AppData> appDatas) {
				if (appDatas != null && appDatas.size() > 0) {
					for (AppData appData : appDatas) {
						// �����յ��ı���
						analisys(appData);
					}
				}
			}

			@Override
			public void getState(int state) {
				// Socket״̬�ص�
				switch (state) {
				// Socket�����ɹ����豸������Ȩ����
				case MySocket.CONNECT_STATE_ON:
					SystemManager.LOGD(TAG, "Socket�����ɹ� ===<<");
					isStart = true;
					start();
					break;
				// Socket�Ͽ���ֹͣ��������
				case MySocket.CONNECT_STATE_OFF:
					SystemManager.LOGD(TAG, "Socket�Ͽ� ===<<");
					isStart = false;
					isAuto = false;
					handler.removeMessages(HANDLER_HEART);
					//�����첽�ȴ�����
					mutex.lock(0);
					for (Process proc : mReqProcList) {
						//--ɾ����ʱ����
						if (proc.timer != null) {
							proc.timer.cancel();
							proc.timer = null;
						}
						//--ִ��Ӧ��ص�
						if (mCallback != null) {
							String json = null;
							if (proc.prot.data != null) {
								try {
									json = new String(proc.prot.data, TcpProtocol.charset);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							}
							mCallback.answer_async(false, proc.prot.cmd, json);
						}
						//--ɾ���ڵ�
						mReqProcList.remove(proc);
						proc = null;
					}
					mutex.unlock();
					mCallback.tcpOff();
					break;
				// Socket������
				case MySocket.CONNECT_STATE_CONNECT:
					SystemManager.LOGD(TAG, "Socket������ ===<<");
					break;
				// Socket��ʱ
				case MySocket.CONNECT_READ_TIMEOUT:
					SystemManager.LOGD(TAG, "Socket��ʱ ===<<");
					break;
				}
			}

		}, mContext);
	}

	/**���Ĵ���*/
	private void analisys(AppData pack) {
		if (pack != null) {
			SystemManager.LOGI(TAG, pack.toString());
			//�ӽ���ƽ̨����������
			//ֻ���������īˮ�������ı���
			if (pack.from == TcpProtocol.FormToType.SYSTEM_ACCESS) {
				switch (pack.cmd) {
				//startӦ��
				case TcpProtocol.Cmd.PACKET_CMD_START:
					if (pack.len != 0) {
						System.arraycopy(pack.data, 0, mTcpProtocol.PacketKey, 0, pack.len);
						//��Ȩ
						mCallback.connectResult(pack.cmd, true);
						auth();
					} else {
						mCallback.connectResult(pack.cmd, false);
						start();
					}
					break;
				//��ȨӦ��
				case TcpProtocol.Cmd.PACKET_CMD_AUTH:
					if (pack.len != 0) {

						byte[] data = new byte[8];

						try {
							switch (Encrypt.ENCRYPT_TYPE_DES) {
							case TcpProtocol.Encrypt.ENCRYPT_TYPE_3DES:
								data = MyEncrypt.des3UnEncrypt(pack.data, mTcpProtocol.MyKey);
								break;
							case TcpProtocol.Encrypt.ENCRYPT_TYPE_AES:
								break;
							case TcpProtocol.Encrypt.ENCRYPT_TYPE_DES:
								data = MyEncrypt.desUnEncrypt(pack.data, mTcpProtocol.MyKey);
								break;
							case TcpProtocol.Encrypt.ENCRYPT_TYPE_NO:
								System.arraycopy(pack.data, 0, data, 0, pack.len);
								break;
							case TcpProtocol.Encrypt.ENCRYPT_TYPE_RSA:
								break;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (!mTcpProtocol.checkByte(data, TcpProtocol.FinalBao, TcpProtocol.FinalBao.length)) {
							Log.i(TAG, "CHECK err");
							mCallback.connectResult(pack.cmd, false);
						} else {
							SystemManager.LOGI(TAG, "��Ȩ�ɹ�");
							mCallback.connectResult(pack.cmd, true);
							isAuto = true;
							//��Ȩ�ɹ� ��������
							handler.sendEmptyMessage(HANDLER_HEART);
						}
					} else {
						SystemManager.LOGI(TAG, "para.datalen = 0");
						mCallback.connectResult(pack.cmd, false);
					}
					break;
				//����
				case TcpProtocol.Cmd.PACKET_CMD_END:
					break;
				//����
				case TcpProtocol.Cmd.PACKET_CMD_HEART:
					handler.removeMessages(HANDLER_RE_START_1);//ȡ��������ʱ
					handler.sendEmptyMessageDelayed(HANDLER_HEART, heart);
					break;
				//�����ϼ�������Ĭ��ҵ������ //�첽���ܲ���Ҫ����
				default:
					SystemManager.LOGD(TAG, "=====  ƽ̨ �·�=====" + pack.cmd);
					//---------�ж����첽Ӧ�����·�����
					boolean isRequest = false;
					mutex.lock(0);
					for (Process proc : mReqProcList) {
						//--ƥ���豸ID(���ͷ����ڽ��շ�)����ˮ�š�����
						if (proc.prot.src_id.equals(pack.dst_id) && proc.prot.cmd == pack.cmd) {
							//--ƥ����ˮ�ţ���У��sn��
							/*		int i = 0;
									for (i = 0; i < proc.prot.sn.length; i++) {
										if (proc.prot.sn[i] != pack.sn[i]) {
											break;
										}
									}
									if (i != proc.prot.sn.length) {
										break;
									}*/
							//--ɾ����ʱ����
							if (proc.timer != null) {
								proc.timer.cancel();
								proc.timer = null;
							}
							//--ִ��Ӧ��ص�
							if (mCallback != null) {
								String json = null;
								if (pack.data != null) {
									try {
										json = new String(pack.data, TcpProtocol.charset);
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}
								}
								mCallback.answer_async(true, pack.cmd, json);
							}
							//--ɾ���ڵ�
							mReqProcList.remove(proc);
							proc = null;
							isRequest = true;
							break;
						}
					}
					mutex.unlock();
					//ƽ̨�·�������
					if (isRequest == false) {
						String json = null;
						if (pack.data != null) {
							try {
								json = new String(pack.data, TcpProtocol.charset);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						mCallback.request(pack.cmd, json);
					}

					break;
				}
			}

		}
	}

	/**���Ϳ�ʼ���� *///�������뷢�ͷ���̶�����ϵͳ�ⲿ�ṩ   �����޼��ܼ��ܷ�ʽ
	public void start() {
		SystemManager.LOGD(TAG, "===== �����Ự =====");
		AppData appData = new AppData();
		appData.from = mainSystem;
		appData.to = TcpProtocol.FormToType.SYSTEM_ACCESS;
		appData.src_id = equ_id;
		appData.dst_id = mTcpProtocol.serverID;
		appData.sn = TcpProtocol.getSn();
		appData.cmd = TcpProtocol.Cmd.PACKET_CMD_START;
		appData.len = 0;
		appData.data = null;
		byte[] send;
		try {
			send = new byte[9 + 15 + appData.len + appData.src_id.getBytes(TcpProtocol.charset).length
					+ appData.dst_id.getBytes(TcpProtocol.charset).length];
			int sendlen = 0;
			sendlen = mTcpProtocol.request(appData, send);
			mySocket.send(appData.cmd, send, sendlen);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// ��������
	private void heart() {
		SystemManager.LOGD(TAG, "=====��������=====");
		AppData appData = new AppData();
		appData.from = mainSystem;
		appData.to = TcpProtocol.FormToType.SYSTEM_ACCESS;
		appData.src_id = equ_id;
		appData.dst_id = mTcpProtocol.serverID;
		appData.sn = TcpProtocol.getSn();
		appData.cmd = TcpProtocol.Cmd.PACKET_CMD_HEART;
		appData.len = 0;
		appData.data = null;
		byte[] send;
		try {
			send = new byte[9 + 15 + appData.len + appData.src_id.getBytes(TcpProtocol.charset).length
					+ appData.dst_id.getBytes(TcpProtocol.charset).length];
			int sendlen = 0;
			sendlen = mTcpProtocol.request(appData, send);
			mySocket.send(appData.cmd, send, sendlen);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//��Ȩ
	private void auth() {
		SystemManager.LOGD(TAG, "===== �����Ȩ =====");
		AppData appData = new AppData();
		appData.from = mainSystem;
		appData.to = TcpProtocol.FormToType.SYSTEM_ACCESS;
		appData.src_id = equ_id;
		appData.dst_id = mTcpProtocol.serverID;//������������Э��������
		appData.sn = TcpProtocol.getSn();
		appData.cmd = TcpProtocol.Cmd.PACKET_CMD_AUTH;
		mTcpProtocol.MyKey = mTcpProtocol.getrandom(false, 8).getBytes();//�豸������8�ֽ������
		byte[] token = new byte[8];
		switch (Encrypt.ENCRYPT_TYPE_DES) {//����
		case Encrypt.ENCRYPT_TYPE_3DES: {
			try {
				token = MyEncrypt.des3Encrypt(mTcpProtocol.FinalBao, mTcpProtocol.PacketKey);
				if (token == null) {
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case Encrypt.ENCRYPT_TYPE_DES: {
			try {
				token = MyEncrypt.desEncrypt(mTcpProtocol.FinalBao, mTcpProtocol.PacketKey);
				if (token == null) {
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case Encrypt.ENCRYPT_TYPE_AES:
			break;
		case Encrypt.ENCRYPT_TYPE_RSA:
			break;
		}

		//����
		byte[] data = new byte[16];
		System.arraycopy(token, 0, data, 0, token.length);
		System.arraycopy(mTcpProtocol.MyKey, 0, data, token.length, mTcpProtocol.MyKey.length);
		appData.len = data.length;
		appData.data = data;

		try {
			byte[] send = new byte[9 + 15 + appData.len + appData.src_id.getBytes(TcpProtocol.charset).length
					+ appData.dst_id.getBytes(TcpProtocol.charset).length];

			int sendlen = 0;
			sendlen = mTcpProtocol.request(appData, send);
			mySocket.send(appData.cmd, send, sendlen);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// �豸����Ͽ�����
	private void end() {
		SystemManager.LOGD(TAG, "===== �Ͽ����� =====");
		AppData appData = new AppData();
		appData.from = mainSystem;
		appData.to = TcpProtocol.FormToType.SYSTEM_ACCESS;

		appData.src_id = equ_id;
		appData.dst_id = mTcpProtocol.serverID;
		appData.sn = TcpProtocol.getSn();
		appData.cmd = TcpProtocol.Cmd.PACKET_CMD_END;
		appData.len = 0;
		appData.data = null;
		byte[] send;
		try {
			send = new byte[9 + 15 + appData.len + appData.src_id.getBytes(TcpProtocol.charset).length
					+ appData.dst_id.getBytes(TcpProtocol.charset).length];

			int sendlen = 0;
			sendlen = mTcpProtocol.request(appData, send);
			mySocket.send(appData.cmd, send, sendlen);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//�ṩ��㣬����json����
	public void send(byte cmd, String json) {
		if (isAuto) {
			SystemManager.LOGD(TAG, "===== �������� =====cmd = " + cmd + " json " + json);
			AppData appData = new AppData();
			appData.from = mainSystem;
			appData.to = TcpProtocol.FormToType.SYSTEM_ACCESS;
			appData.src_id = equ_id;
			appData.dst_id = mTcpProtocol.serverID;
			appData.sn = TcpProtocol.getSn();
			appData.cmd = cmd;

			if (json != null) {
				try {
					appData.data = json.getBytes(TcpProtocol.charset);
					appData.len = appData.data.length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				appData.len = 0;
				appData.data = null;
			}

			byte[] send;
			try {
				send = new byte[9 + 15 + appData.len + appData.src_id.getBytes(TcpProtocol.charset).length
						+ appData.dst_id.getBytes(TcpProtocol.charset).length];

				int sendlen = 0;
				sendlen = mTcpProtocol.request(appData, send);
				mySocket.send(appData.cmd, send, sendlen);

				//-------��������
				Timer timer = new Timer();
				Process proc = new Process(appData, timer);
				mutex.lock(0);
				mReqProcList.add(proc);
				mutex.unlock();
				proc.timer.schedule(new ReqTimerTask(proc), timeout);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			SystemManager.LOGD(TAG, "δͨ����Ȩ");
		}
	}

	public void startTcp(String ip, int port) {
		if (!isStart && !TextUtils.isEmpty(ip)) {
			isStart = true;
			this.ip = ip;
			this.port = port;
			mySocket.initTCP(ip, port, mTcpProtocol);
			mySocket.startTCP();
		}
	}

	public void stopTcp() {
		end();
		mySocket.stopTCP();
		isStart = false;
		isAuto = false;
	}

	public void reStart(String ip, int port) {
		if (!TextUtils.isEmpty(ip)) {
			stopTcp();
			this.ip = ip;
			this.port = port;
			handler.sendEmptyMessageDelayed(HANDLER_START_TCP, 15 * 1000);
		}
	}

	private static final int HANDLER_START_TCP = 0;//����tcp
	private static final int HANDLER_START = 1;//�����Ự
	private static final int HANDLER_HEART = 2;//��ʼ����
	private static final int HANDLER_AUTO = 3;//��ʼ��Ȩ
	private static final int HANDLER_RE_START_1 = 4;//���������Ự
	private static final int HANDLER_RE_START_2 = 5;//���������Ự

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_START_TCP:
				startTcp(ip, port);
				break;
			case HANDLER_START:
				start();
				break;
			case HANDLER_HEART:
				heart();
				handler.sendEmptyMessageDelayed(HANDLER_RE_START_1, heart);//һ������ʱ������������Ӧ����������ʱ ��analisys������ʱ���
				break;
			case HANDLER_AUTO:
				auth();
				break;
			case HANDLER_RE_START_1:
				mySocket.stopTCP();
				handler.sendEmptyMessageDelayed(HANDLER_RE_START_2, 10000);
				break;
			case HANDLER_RE_START_2:
				mySocket.startTCP();
				break;
			}
		};
	};

	public boolean isTcpStart() {
		return isStart;
	}

	public interface TcpCallback {

		//ƽ̨����ص�
		public abstract void request(byte cmd, String sjson);

		//����Ͳ�����ִ�н��
		public abstract void connectResult(byte cmd, boolean ret);

		//ƽ̨Ӧ��ص�
		public abstract void answer(byte cmd, String sjson);

		//ƽ̨�첽Ӧ��ص�
		public abstract void answer_async(boolean receive, byte cmd, String sjson);

		//tcp��·�Ͽ������յ��˻ص�����ҵ��ʧ��
		public abstract void tcpOff();
	}

	//����ʱ������ʱ����ʧ��
	private class ReqTimerTask extends TimerTask {
		Process proc = null;

		public ReqTimerTask(Process proc) {
			this.proc = proc;
		}

		@Override
		public void run() {

			if (proc != null) {
				if (mCallback != null) {
					mCallback.answer_async(false, proc.prot.cmd, null);
				}
				mutex.lock(0);
				mReqProcList.remove(proc);
				mutex.unlock();
			}

		}
	}
}
