package com.guozheng.okhttputils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by fighting on 2017/4/7.
 */

class RequestUtil {

	public static final long CONNECT_TIMEOUT = 15L;//���ӳ�ʱ
	public static final long READ_TIMEOUT = 30L;//��ȡ��ʱ
	public static final long WRITE_TIMEOUT = 30L;//д�볬ʱ

	private Context mContext;
	private String mMetyodType;//����ʽ��Ŀǰֻ֧��get��post
	private String mUrl;//�ӿ�
	private byte cmd;//��������
	private Map<String, String> mParamsMap;//��ֵ�����͵Ĳ�����ֻ����һ�����������post��get��
	private String mJsonStr;//json���͵Ĳ�����post��ʽ
	private File mFile;//�ļ��Ĳ�����post��ʽ,ֻ��һ���ļ�
	private List<File> mfileList;//�ļ����ϣ�������϶�Ӧһ��key����mfileKey
	private String mfileKey;//�ϴ����������ļ���Ӧ��key
	private Map<String, File> mfileMap;//�ļ����ϣ�ÿ���ļ���Ӧһ��key
	private String mFileType;//�ļ����͵Ĳ�������fileͬʱ����
	private Map<String, String> mHeaderMap;//ͷ����
	private CallBackUtil mCallBack;//�ص��ӿ�
	private OkHttpClient mOkHttpClient;//OKhttpClient����
	private Request mOkHttpRequest;//�������
	private Request.Builder mRequestBuilder;//�������Ĺ�����

	RequestUtil(byte cmd, Context context, String methodType, String url, Map<String, String> paramsMap,
			Map<String, String> headerMap, CallBackUtil callBack) {
		this(cmd, context, methodType, url, null, null, null, null, null, null, paramsMap, headerMap, callBack);
	}

	RequestUtil(byte cmd, Context context, String methodType, String url, String jsonStr, Map<String, String> headerMap,
			CallBackUtil callBack) {
		this(cmd, context, methodType, url, jsonStr, null, null, null, null, null, null, headerMap, callBack);
	}

	RequestUtil(byte cmd, Context context, String methodType, String url, Map<String, String> paramsMap, File file,
			String fileKey, String fileType, Map<String, String> headerMap, CallBackUtil callBack) {
		this(cmd, context, methodType, url, null, file, null, fileKey, null, fileType, paramsMap, headerMap, callBack);
	}

	RequestUtil(byte cmd, Context context, String methodType, String url, Map<String, String> paramsMap,
			List<File> fileList, String fileKey, String fileType, Map<String, String> headerMap,
			CallBackUtil callBack) {
		this(cmd, context, methodType, url, null, null, fileList, fileKey, null, fileType, paramsMap, headerMap,
				callBack);
	}

	RequestUtil(byte cmd, Context context, String methodType, String url, Map<String, String> paramsMap,
			Map<String, File> fileMap, String fileType, Map<String, String> headerMap, CallBackUtil callBack) {
		this(cmd, context, methodType, url, null, null, null, null, fileMap, fileType, paramsMap, headerMap, callBack);
	}

	private RequestUtil(byte cmd, Context context, String methodType, String url, String jsonStr, File file,
			List<File> fileList, String fileKey, Map<String, File> fileMap, String fileType,
			Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
		this.cmd = cmd;
		mContext = context;
		mMetyodType = methodType;
		mUrl = url;
		mJsonStr = jsonStr;
		mFile = file;
		mfileList = fileList;
		mfileKey = fileKey;
		mfileMap = fileMap;
		mFileType = fileType;
		mParamsMap = paramsMap;
		mHeaderMap = headerMap;
		mCallBack = callBack;
		getInstance();
	}

	/**
	 * ����OKhttpClientʵ����
	 */
	/**
	 * ����OKhttpClientʵ����
	 */
	private void getInstance() {
		try {
			mOkHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {
					Request request = chain.request().newBuilder().build();
					return chain.proceed(request);
				}

			}).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
					.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mRequestBuilder = new Request.Builder();
		if (mFile != null || mfileList != null || mfileMap != null) {//���ж��Ƿ����ļ���
			setFile();
		} else {
			if (OkhttpUtil.METHOD_GET.equals(mMetyodType)) {
				setGetParams();
			} else if (OkhttpUtil.METHOD_POST.equals(mMetyodType)) {
				mRequestBuilder.post(getRequestBody());
			} else if (OkhttpUtil.METHOD_PUT.equals(mMetyodType)) {
				mRequestBuilder.put(getRequestBody());//�ϴ��ļ�
			} else if (OkhttpUtil.METHOD_DELETE.equals(mMetyodType)) {
				mRequestBuilder.delete(getRequestBody());//ɾ��ĳЩ��Դ
			}
		}
		mRequestBuilder.url(mUrl);
		if (mHeaderMap != null) {
			setHeader();
		}
		//mRequestBuilder.addHeader("Authorization","Bearer "+"token");���԰�token��ӵ����
		mOkHttpRequest = mRequestBuilder.build();
	}

	//ssl
	/*	private void getInstance() {
			try {
				mOkHttpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
						.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
						.hostnameVerifier(new HostnameVerifier() {
							@Override
							public boolean verify(String hostname, SSLSession session) {
								return true;//���Է�����֤����֤
							}
						})
						.sslSocketFactory(
								getSSLContext(mContext, "eyun123", "saleClient.p12", "eyun.p12").getSocketFactory(),
								geTrustManager(mContext, "eyun123", "eyun.p12"))
						.build();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			mRequestBuilder = new Request.Builder();
			if (mFile != null || mfileList != null || mfileMap != null) {//���ж��Ƿ����ļ���
				setFile();
			} else {
				if (OkhttpUtil.METHOD_GET.equals(mMetyodType)) {
					setGetParams();
				} else if (OkhttpUtil.METHOD_POST.equals(mMetyodType)) {
					mRequestBuilder.post(getRequestBody());
				} else if (OkhttpUtil.METHOD_PUT.equals(mMetyodType)) {
					mRequestBuilder.put(getRequestBody());//�ϴ��ļ�
				} else if (OkhttpUtil.METHOD_DELETE.equals(mMetyodType)) {
					mRequestBuilder.delete(getRequestBody());//ɾ��ĳЩ��Դ
				}
			}
			mRequestBuilder.url(mUrl);
			if (mHeaderMap != null) {
				setHeader();
			}
			//mRequestBuilder.addHeader("Authorization","Bearer "+"token");���԰�token��ӵ����
			mOkHttpRequest = mRequestBuilder.build();
		}*/

	/**
	 * �õ�body����
	 */
	private RequestBody getRequestBody() {
		/**
		 * �����ж�mJsonStr�Ƿ�Ϊ�գ�����mJsonStr��mParamsMap������ͬʱ���ڣ��������ж�mJsonStr
		 */
		if (!TextUtils.isEmpty(mJsonStr)) {
			MediaType JSON = MediaType.parse("application/json; charset=utf-8");//��������Ϊjson��ʽ��
			return RequestBody.create(JSON, mJsonStr);//json���ݣ�
		}

		/**
		 * post,put,delete����Ҫbody����Ҳ����body���ڿյ��������ʱҲӦ����body���󣬵�body�е�����Ϊ��
		 */
		FormBody.Builder formBody = new FormBody.Builder();
		if (mParamsMap != null) {
			for (String key : mParamsMap.keySet()) {
				formBody.add(key, mParamsMap.get(key));
			}
		}
		return formBody.build();
	}

	/**
	 * get����ֻ�м�ֵ�Բ���
	 */
	private void setGetParams() {  
		if (mParamsMap != null) {
			mUrl = mUrl + "?";
			for (String key : mParamsMap.keySet()) {
				mUrl = mUrl + key + "=" + mParamsMap.get(key) + "&";
			}
			mUrl = mUrl.substring(0, mUrl.length() - 1);
		}
		Log.e("", "mUrl = " + mUrl);
	}

	/**
	 * �����ϴ��ļ�
	 */
	private void setFile() {
		if (mFile != null) {//ֻ��һ���ļ�����û���ļ���
			if (mParamsMap == null) {
				setPostFile();
			} else {
				setPostParameAndFile();
			}
		} else if (mfileList != null) {//�ļ����ϣ�ֻ��һ���ļ������������Ҳ֧�ֵ������ļ������ļ�
			setPostParameAndListFile();
		} else if (mfileMap != null) {//����ļ���ÿ���ļ���Ӧһ���ļ���
			setPostParameAndMapFile();
		}

	}

	/**
	 * ֻ��һ���ļ������ύ������ʱ����ָ������û�в���
	 */
	private void setPostFile() {
		if (mFile != null && mFile.exists()) {
			MediaType fileType = MediaType.parse(mFileType);
			RequestBody body = RequestBody.create(fileType, mFile);//json���ݣ�
			mRequestBuilder.post(new ProgressRequestBody(body, mCallBack));
		}
	}

	/**
	 * ֻ��һ���ļ������ύ������ʱ����ָ����������ֵ�Բ���
	 */
	private void setPostParameAndFile() {
		if (mParamsMap != null && mFile != null) {
			MultipartBody.Builder builder = new MultipartBody.Builder();
			builder.setType(MultipartBody.FORM);
			for (String key : mParamsMap.keySet()) {
				builder.addFormDataPart(key, mParamsMap.get(key));
			}
			builder.addFormDataPart(mfileKey, mFile.getName(), RequestBody.create(MediaType.parse(mFileType), mFile));
			mRequestBuilder.post(new ProgressRequestBody(builder.build(), mCallBack));
		}
	}

	/**
	 * �ļ����ϣ����ܴ��м�ֵ�Բ���
	 */
	private void setPostParameAndListFile() {
		if (mfileList != null) {
			MultipartBody.Builder builder = new MultipartBody.Builder();
			builder.setType(MultipartBody.FORM);
			if (mParamsMap != null) {
				for (String key : mParamsMap.keySet()) {
					builder.addFormDataPart(key, mParamsMap.get(key));
				}
			}
			for (File f : mfileList) {
				builder.addFormDataPart(mfileKey, f.getName(), RequestBody.create(MediaType.parse(mFileType), f));
			}
			mRequestBuilder.post(builder.build());
		}
	}

	/**
	 * �ļ�Map�����ܴ��м�ֵ�Բ���
	 */
	private void setPostParameAndMapFile() {
		if (mfileMap != null) {
			MultipartBody.Builder builder = new MultipartBody.Builder();
			builder.setType(MultipartBody.FORM);
			if (mParamsMap != null) {
				for (String key : mParamsMap.keySet()) {
					builder.addFormDataPart(key, mParamsMap.get(key));
				}
			}

			for (String key : mfileMap.keySet()) {
				builder.addFormDataPart(key, mfileMap.get(key).getName(),
						RequestBody.create(MediaType.parse(mFileType), mfileMap.get(key)));
			}
			mRequestBuilder.post(builder.build());
		}
	}

	/**
	 * ����ͷ����
	 */
	private void setHeader() {
		if (mHeaderMap != null) {
			for (String key : mHeaderMap.keySet()) {
				mRequestBuilder.addHeader(key, mHeaderMap.get(key));
			}
		}
	}

	void execute() {
		//call
		mOkHttpClient.newCall(mOkHttpRequest).enqueue(new Callback() {
			@Override
			public void onFailure(final Call call, final IOException e) {
				if (mCallBack != null) {
					mCallBack.onError(cmd, call, e);
				}
			}

			@Override
			public void onResponse(final Call call, final Response response) throws IOException {
				if (mCallBack != null) {
					mCallBack.onSeccess(cmd, call, response);
				}
			}

		});
	}

	/**
	 * �Զ���RequestBody�࣬�õ��ļ��ϴ��Ľ���
	 */
	private static class ProgressRequestBody extends RequestBody {
		//ʵ�ʵĴ���װ������
		private final RequestBody requestBody;
		//��װ��ɵ�BufferedSink
		private BufferedSink bufferedSink;
		private CallBackUtil callBack;

		ProgressRequestBody(RequestBody requestBody, CallBackUtil callBack) {
			this.requestBody = requestBody;
			this.callBack = callBack;
		}

		/** ��д����ʵ�ʵ���Ӧ���contentType*/
		@Override
		public MediaType contentType() {
			return requestBody.contentType();
		}

		/**��д����ʵ�ʵ���Ӧ���contentLength ��������ļ������ֽ��� */
		@Override
		public long contentLength() throws IOException {
			return requestBody.contentLength();
		}

		/** ��д����д��*/
		@Override
		public void writeTo(BufferedSink sink) throws IOException {
			if (bufferedSink == null) {
				bufferedSink = Okio.buffer(sink(sink));
			}
			requestBody.writeTo(bufferedSink);
			//�������flush���������һ�������ݿ��ܲ��ᱻд��
			bufferedSink.flush();
		}

		/** д�룬�ص����Ƚӿ�*/
		private Sink sink(BufferedSink sink) {
			return new ForwardingSink(sink) {
				//��ǰд���ֽ���
				long bytesWritten = 0L;
				//���ֽڳ��ȣ������ε���contentLength()����
				long contentLength = 0L;

				@Override
				public void write(Buffer source, long byteCount) throws IOException {
					super.write(source, byteCount);//���������ѭ�����ã�byteCount��ÿ�ε����ϴ����ֽ�����
					if (contentLength == 0) {
						//������ֽڳ���
						contentLength = contentLength();
					}
					//���ӵ�ǰд����ֽ���
					bytesWritten += byteCount;
					final float progress = bytesWritten * 1.0f / contentLength;
					CallBackUtil.mMainHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onProgress(progress, contentLength);
						}
					});
				}
			};
		}
	}

	//------------------------ssl-------------------------------- 

	/** 
	 * ���KeyStore. 
	 * @param keyStorePath 
	 *            ��Կ��·�� 
	 * @param password 
	 *            ���� 
	 * @return ��Կ�� 
	 * @throws Exception 
	 */
	public static KeyStore getKeyStore(Context context, String password, String keyStorePath) throws Exception {
		// ʵ������Կ��  
		KeyStore ks = KeyStore.getInstance("PKCS12");
		// �����Կ���ļ���  
		InputStream in = context.getAssets().open(keyStorePath);
		// ������Կ��  
		ks.load(in, password.toCharArray());
		// �ر���Կ���ļ���  
		return ks;
	}

	/** 
	 * ���SSLSocketFactory. 
	 * @param password 
	 *            ���� 
	 * @param keyStorePath 
	 *            ��Կ��·�� 
	 * @param trustStorePath 
	 *            ���ο�·�� 
	 * @return SSLSocketFactory 
	 * @throws Exception 
	 */
	public SSLContext getSSLContext(Context context, String password, String keyStorePath, String trustStorePath)
			throws Exception {
		// ʵ������Կ��  
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		// �����Կ��  
		KeyStore keyStore = getKeyStore(context, password, keyStorePath);
		// ��ʼ����Կ����  
		keyManagerFactory.init(keyStore, password.toCharArray());

		// ʵ����SSL������  
		SSLContext ctx = SSLContext.getInstance("TLS");
		// ��ʼ��SSL������  
		ctx.init(keyManagerFactory.getKeyManagers(),
				new TrustManager[] { geTrustManager(context, password, trustStorePath) }, null);
		// ���SSLSocketFactory  
		return ctx;
	}

	public X509TrustManager geTrustManager(Context context, String password, String trustStorePath) {
		// ʵ�������ο�  
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			// ������ο�  
			KeyStore trustStore = getKeyStore(context, password, trustStorePath);
			// ��ʼ�����ο�  

			trustManagerFactory.init(trustStore);

			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
				throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
			}
			X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
			return trustManager;
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
