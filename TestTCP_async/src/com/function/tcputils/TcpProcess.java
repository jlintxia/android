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

	private byte mainSystem = TcpProtocol.FormToType.SYSTEM_BOOK;//本机系统 默认借书柜

	//tcp连接状态
	private boolean isStart = false;
	//是否鉴权成功，未鉴权无法发送
	private boolean isAuto = false;
	private Context mContext;

	private int heart = 60 * 1000;//心跳时间
	private int timeout = 10 * 1000;//异步等待超时时间

	private Mutex mutex = new Mutex();

	//异步等待队列---------------------------------------
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

	//请求队列，判断下发命令与队列中的sn cmd 相同即为应答上一条请求
	private ArrayList<Process> mReqProcList = new ArrayList<Process>();

	//---------------------------------------

	public void init() {
		mTcpProtocol = new TcpProtocol();
		mySocket = new MySocket(new MySocketCallback() {

			@Override
			public void send(byte cmd, boolean ret) {
				// 发送结果回调
				if (!ret) {
					SystemManager.LOGD(TAG, "send ret = false == >" + cmd);
					switch (cmd) {
					// 设备请求授权连接 发送失败，重新发送
					case TcpProtocol.Cmd.PACKET_CMD_START:
						handler.removeMessages(HANDLER_START);
						handler.sendEmptyMessageDelayed(HANDLER_START, 1000);
						break;
					}
				} else {//发送所有命令都会重置心跳，当无命令发送时心跳正常 心跳一直发
//					handler.removeMessages(HANDLER_HEART);
//					handler.sendEmptyMessageDelayed(HANDLER_HEART, heart);
				}
			}

			@Override
			public void read(List<AppData> appDatas) {
				if (appDatas != null && appDatas.size() > 0) {
					for (AppData appData : appDatas) {
						// 解析收到的报文
						analisys(appData);
					}
				}
			}

			@Override
			public void getState(int state) {
				// Socket状态回调
				switch (state) {
				// Socket接连成功，设备请求授权连接
				case MySocket.CONNECT_STATE_ON:
					SystemManager.LOGD(TAG, "Socket接连成功 ===<<");
					isStart = true;
					start();
					break;
				// Socket断开，停止发送心跳
				case MySocket.CONNECT_STATE_OFF:
					SystemManager.LOGD(TAG, "Socket断开 ===<<");
					isStart = false;
					isAuto = false;
					handler.removeMessages(HANDLER_HEART);
					//清理异步等待队列
					mutex.lock(0);
					for (Process proc : mReqProcList) {
						//--删除超时计数
						if (proc.timer != null) {
							proc.timer.cancel();
							proc.timer = null;
						}
						//--执行应答回调
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
						//--删除节点
						mReqProcList.remove(proc);
						proc = null;
					}
					mutex.unlock();
					mCallback.tcpOff();
					break;
				// Socket连接中
				case MySocket.CONNECT_STATE_CONNECT:
					SystemManager.LOGD(TAG, "Socket连接中 ===<<");
					break;
				// Socket超时
				case MySocket.CONNECT_READ_TIMEOUT:
					SystemManager.LOGD(TAG, "Socket超时 ===<<");
					break;
				}
			}

		}, mContext);
	}

	/**报文处理*/
	private void analisys(AppData pack) {
		if (pack != null) {
			SystemManager.LOGI(TAG, pack.toString());
			//从接入平台发来的数据
			//只解析借书柜，墨水屏柜发来的报文
			if (pack.from == TcpProtocol.FormToType.SYSTEM_ACCESS) {
				switch (pack.cmd) {
				//start应答
				case TcpProtocol.Cmd.PACKET_CMD_START:
					if (pack.len != 0) {
						System.arraycopy(pack.data, 0, mTcpProtocol.PacketKey, 0, pack.len);
						//鉴权
						mCallback.connectResult(pack.cmd, true);
						auth();
					} else {
						mCallback.connectResult(pack.cmd, false);
						start();
					}
					break;
				//鉴权应答
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
							SystemManager.LOGI(TAG, "鉴权成功");
							mCallback.connectResult(pack.cmd, true);
							isAuto = true;
							//鉴权成功 开启心跳
							handler.sendEmptyMessage(HANDLER_HEART);
						}
					} else {
						SystemManager.LOGI(TAG, "para.datalen = 0");
						mCallback.connectResult(pack.cmd, false);
					}
					break;
				//结束
				case TcpProtocol.Cmd.PACKET_CMD_END:
					break;
				//心跳
				case TcpProtocol.Cmd.PACKET_CMD_HEART:
					handler.removeMessages(HANDLER_RE_START_1);//取消心跳超时
					handler.sendEmptyMessageDelayed(HANDLER_HEART, heart);
					break;
				//非以上几条命令默认业务命令 //异步可能不需要这里
				default:
					SystemManager.LOGD(TAG, "=====  平台 下发=====" + pack.cmd);
					//---------判断是异步应答还是下发命令
					boolean isRequest = false;
					mutex.lock(0);
					for (Process proc : mReqProcList) {
						//--匹配设备ID(发送方等于接收方)、流水号、命令
						if (proc.prot.src_id.equals(pack.dst_id) && proc.prot.cmd == pack.cmd) {
							//--匹配流水号（不校验sn）
							/*		int i = 0;
									for (i = 0; i < proc.prot.sn.length; i++) {
										if (proc.prot.sn[i] != pack.sn[i]) {
											break;
										}
									}
									if (i != proc.prot.sn.length) {
										break;
									}*/
							//--删除超时计数
							if (proc.timer != null) {
								proc.timer.cancel();
								proc.timer = null;
							}
							//--执行应答回调
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
							//--删除节点
							mReqProcList.remove(proc);
							proc = null;
							isRequest = true;
							break;
						}
					}
					mutex.unlock();
					//平台下发的命令
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

	/**发送开始命令 *///发送者与发送方向固定，由系统外部提供   启动无加密加密方式
	public void start() {
		SystemManager.LOGD(TAG, "===== 启动会话 =====");
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

	// 发送心跳
	private void heart() {
		SystemManager.LOGD(TAG, "=====发送心跳=====");
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

	//鉴权
	private void auth() {
		SystemManager.LOGD(TAG, "===== 请求鉴权 =====");
		AppData appData = new AppData();
		appData.from = mainSystem;
		appData.to = TcpProtocol.FormToType.SYSTEM_ACCESS;
		appData.src_id = equ_id;
		appData.dst_id = mTcpProtocol.serverID;//接收启动会解包协议层后填入
		appData.sn = TcpProtocol.getSn();
		appData.cmd = TcpProtocol.Cmd.PACKET_CMD_AUTH;
		mTcpProtocol.MyKey = mTcpProtocol.getrandom(false, 8).getBytes();//设备产生的8字节随机数
		byte[] token = new byte[8];
		switch (Encrypt.ENCRYPT_TYPE_DES) {//加密
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

		//令牌
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

	// 设备请求断开连接
	private void end() {
		SystemManager.LOGD(TAG, "===== 断开连接 =====");
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

	//提供外层，发送json请求
	public void send(byte cmd, String json) {
		if (isAuto) {
			SystemManager.LOGD(TAG, "===== 发送请求 =====cmd = " + cmd + " json " + json);
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

				//-------开启队列
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
			SystemManager.LOGD(TAG, "未通过鉴权");
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

	private static final int HANDLER_START_TCP = 0;//连接tcp
	private static final int HANDLER_START = 1;//启动会话
	private static final int HANDLER_HEART = 2;//开始心跳
	private static final int HANDLER_AUTO = 3;//开始鉴权
	private static final int HANDLER_RE_START_1 = 4;//重新启动会话
	private static final int HANDLER_RE_START_2 = 5;//重新启动会话

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
				handler.sendEmptyMessageDelayed(HANDLER_RE_START_1, heart);//一个心跳时间间隔内无心跳应答，则心跳超时 在analisys中清理超时标记
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

		//平台请求回调
		public abstract void request(byte cmd, String sjson);

		//命令发送步骤与执行结果
		public abstract void connectResult(byte cmd, boolean ret);

		//平台应答回调
		public abstract void answer(byte cmd, String sjson);

		//平台异步应答回调
		public abstract void answer_async(boolean receive, byte cmd, String sjson);

		//tcp链路断开，接收到此回调所有业务失败
		public abstract void tcpOff();
	}

	//请求定时器，超时请求失败
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
