package com.function.tcputils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.function.tcputils.TcpProtocol.Encrypt;
import com.function.tool.MyEncrypt;
import com.test.testtcp.SystemManager;

import android.content.Context;
import android.util.Log;

public class MySocket {
	private static final String TAG = "MySocket";

	public static final int CONNECT_STATE_ON = 0; // ���ӳɹ�
	public static final int CONNECT_STATE_OFF = 1; // �Ͽ�
	public static final int CONNECT_STATE_CONNECT = 2; // ��������
	public static final int CONNECT_READ_TIMEOUT = 3; // ��ʱ

	private String mIP;
	private int mPort;
	private int connectTimeOut = 10 * 1000;//���ӳ�ʱ
	private int readTimeOut = 20 * 1000;//��ȡ��ʱ

	private TcpProtocol tcpProtocol = null;
	private Context mContext;

	private Socket mSocket = null;

	private MySocketCallback mCallback;
	private int mThreadStop;
	private Thread TCPThread;

	private long startTime = 0;//��ʼ���ʱ��
	private int decodeState = 0;//�������
	private int appIndex = 0;//app��λ��
	private TcpDate tcpDate;
	private AppData appData;
	private List<AppData> appDatas = new ArrayList<AppData>();

	public interface MySocketCallback {

		public abstract void read(List<AppData> appDatas);

		public abstract void getState(int state);

		public abstract void send(byte cmd, boolean ret);
	}

	public MySocket(MySocketCallback callback, Context context) {
		mCallback = callback;
		mContext = context;
	}

	public void initTCP(String ip, int port, TcpProtocol tcpProtocol) {
		this.mIP = ip;
		this.mPort = port;
		this.tcpProtocol = tcpProtocol;
	}

	public void startTCP() {
		mThreadStop = 0;
		TCPThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (mCallback != null) {
						mCallback.getState(CONNECT_STATE_OFF);
						mCallback.getState(CONNECT_STATE_CONNECT);
					}
					try {
						mSocket = SocketClientSever(mIP, mPort);
						if (mSocket != null) {
							if (mCallback != null) {
								mCallback.getState(CONNECT_STATE_ON);//���ӳɹ���������������
							}
							// ���������߳�
							recv();
							if (mSocket != null) {
								mSocket.close();
								mSocket = null;
							}
						}
						if (mThreadStop == 1) {
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		TCPThread.start();
	}

	//����=====================================================================
	/**���������߳�*/
	private void recv() {
		int readlen = 0;
		byte[] recv = new byte[1024];
		while (true) {
			if (mThreadStop == 1) {
				stopTCP();
				break;
			}
			readlen = SocketRead(recv);
			if (readlen > 0) {
				SystemManager.LogHex(TAG + " read", recv, readlen);
				decode(recv, 0, readlen);
				if (mCallback != null && appDatas.size() > 0) {
					mCallback.read(appDatas);
					appDatas.clear();
				}
			} else if (readlen < 0) {
				break;
			}
		}
	}

	//������ز�
	private void decode(byte[] buffer, int offer, int length) {
		for (int i = 0; i < length; i++) {
			if (startTime != 0) {
				// --��ֹ�ϰ������ó�ʱ�ж�
				if ((System.currentTimeMillis() - startTime) > 1000) {
					startTime = 0;
					decodeState = 0;
					return;
				}
			}
			switch (decodeState) {
			case 0://��ͷ
				if (buffer[i + offer] == TcpProtocol.PACKET_STX) {

					tcpDate = new TcpDate();
					tcpDate.stx = buffer[i + offer];
					decodeState++;
				}
				break;
			case 1://app��
				tcpDate.len = (int) (buffer[i + offer] & 0xff);
				decodeState++;
				break;
			case 2://app��
				tcpDate.len += (int) ((buffer[i + offer] & 0xff) << 8);
				SystemManager.LOGI(TAG, "tcpDate.len = " + tcpDate.len);
				if (tcpDate.len == 0) {
					decodeState++;
				} else {
					tcpDate.app = new byte[tcpDate.len];
					appIndex = 0;
					decodeState++;
				}
				break;
			case 3://����ģʽ
				tcpDate.enc = buffer[i + offer];
				tcpProtocol.EncryptType = tcpDate.enc;//ÿ��ͨ�������ü���ģʽ
				SystemManager.LOGI(TAG, "tcpDate.enc = " + tcpDate.enc);
				if (tcpDate.len == 0) {
					decodeState += 2;
				} else {
					decodeState++;
				}

				break;
			case 4:// ��app
				tcpDate.app[appIndex++] = buffer[i + offer];
				if (appIndex == tcpDate.len) {
					decodeState++;
				}
				break;
			case 5://У��ֵ
				tcpDate.chk = (int) (buffer[i + offer] & 0xff);
				decodeState++;
				break;
			case 6:
				tcpDate.chk += (int) ((buffer[i + offer] & 0xff) << 8);
				decodeState++;
				break;
			case 7:
				tcpDate.chk += (int) ((buffer[i + offer] & 0xff) << 16);
				decodeState++;
				break;
			case 8:
				tcpDate.chk += (int) ((buffer[i + offer] & 0xff) << 24);
				if (tcpDate.checkPacket()) {
					decodeState++;
				} else {
					SystemManager.LOGE(TAG, "tcpDate check error");
					decodeState = 0;
					tcpDate = null;
				}
				break;

			case 9://β��
				SystemManager.LOGE(TAG, "β��");
				if (buffer[i + offer] == TcpProtocol.PACKET_ETX) {
					tcpDate.etx = TcpProtocol.PACKET_ETX;
					decodeState++;
				} else {
					SystemManager.LOGE(TAG, "tcpDate ETX error");
					decodeState = 0;
					tcpDate = null;
				}
				break;
			}
			startTime = System.currentTimeMillis();
			if (decodeState != 10) {
				continue;
			}
			encApp();
			//==============���Э���================
			appData = new AppData();
			appData = tcpProtocol.unpackApp(tcpDate.app, tcpDate.len);

			appDatas.add(appData);
			decodeState = 0;
			startTime = 0;
		}
	}

	private void encApp() {
		byte[] apptmp = new byte[tcpDate.len];
		System.arraycopy(tcpDate.app, 0, apptmp, 0, tcpDate.len);
		//����app	
		try {
			switch (tcpDate.enc) {
			case Encrypt.ENCRYPT_TYPE_3DES:
				tcpDate.app = MyEncrypt.des3UnEncrypt(apptmp, tcpProtocol.PacketKey);
				break;
			case Encrypt.ENCRYPT_TYPE_AES:
				break;
			case Encrypt.ENCRYPT_TYPE_DES:
				tcpDate.app = MyEncrypt.desUnEncrypt(apptmp, tcpProtocol.PacketKey);
				break;
			case Encrypt.ENCRYPT_TYPE_NO:
				break;
			case Encrypt.ENCRYPT_TYPE_RSA:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int SocketRead(byte[] readbuf) {
		int readlen = 0;
		DataInputStream inputStream = null;
		if (mSocket == null) {
			return -1;
		}
		try {
			InputStream input = mSocket.getInputStream();
			inputStream = new DataInputStream(input);
		} catch (IOException e1) {
			e1.printStackTrace();
			return -1;
		}
		try {
			if (inputStream != null) {
				readlen = inputStream.read(readbuf);
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (e.getClass() == SocketTimeoutException.class) {
				if (mCallback != null)
					mCallback.getState(CONNECT_READ_TIMEOUT);
				readlen = -1;
			} else {
				e.printStackTrace();
				// --����
				if (mCallback != null)
					mCallback.getState(CONNECT_STATE_OFF);
				readlen = -1;
			}
		}
		if (mThreadStop == 1) {
			readlen = 0;
		}
		return readlen;
	}

	public void stopTCP() {
		mThreadStop = 1;
		try {
			if (mSocket != null) {
				mSocket.close();
				mSocket = null;
			}
			if (mCallback != null) {
				mCallback.getState(CONNECT_STATE_OFF);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//����==================================================================================
	public void send(final byte cmd, final byte[] sendbuf, final int sendlen) {
		//20180309 y �ж�mSslsocket�����ڲ������������߳�
		if (mSocket != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (SocketSend(sendbuf, sendlen)) {
						if (mCallback != null)
							mCallback.send(cmd, true);
					} else {
						if (mCallback != null)
							mCallback.send(cmd, false);
					}
				}
			}).start();
		}
	}

	private boolean SocketSend(byte[] sendbuf, int sendlen) {
		if (mSocket != null) {
			try {
				OutputStream output = mSocket.getOutputStream();
				DataOutputStream outputStream = new DataOutputStream(output);
				SystemManager.LogHex(TAG + " send", sendbuf, sendlen);
				outputStream.write(sendbuf, 0, sendlen);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	//����================================================================================
	private Socket SocketClientSever(String ip, int port) throws IOException {
		Socket socket = new Socket();
		SocketAddress socAddress = new InetSocketAddress(ip, port);
		socket.connect(socAddress, connectTimeOut);
		return socket;
	}
}
