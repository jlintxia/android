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

	public static final long CONNECT_TIMEOUT = 15L;//连接超时
	public static final long READ_TIMEOUT = 30L;//读取超时
	public static final long WRITE_TIMEOUT = 30L;//写入超时

	private Context mContext;
	private String mMetyodType;//请求方式，目前只支持get和post
	private String mUrl;//接口
	private byte cmd;//命令类型
	private Map<String, String> mParamsMap;//键值对类型的参数，只有这一种情况下区分post和get。
	private String mJsonStr;//json类型的参数，post方式
	private File mFile;//文件的参数，post方式,只有一个文件
	private List<File> mfileList;//文件集合，这个集合对应一个key，即mfileKey
	private String mfileKey;//上传服务器的文件对应的key
	private Map<String, File> mfileMap;//文件集合，每个文件对应一个key
	private String mFileType;//文件类型的参数，与file同时存在
	private Map<String, String> mHeaderMap;//头参数
	private CallBackUtil mCallBack;//回调接口
	private OkHttpClient mOkHttpClient;//OKhttpClient对象
	private Request mOkHttpRequest;//请求对象
	private Request.Builder mRequestBuilder;//请求对象的构建者

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
	 * 创建OKhttpClient实例。
	 */
	/**
	 * 创建OKhttpClient实例。
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
		if (mFile != null || mfileList != null || mfileMap != null) {//先判断是否有文件，
			setFile();
		} else {
			if (OkhttpUtil.METHOD_GET.equals(mMetyodType)) {
				setGetParams();
			} else if (OkhttpUtil.METHOD_POST.equals(mMetyodType)) {
				mRequestBuilder.post(getRequestBody());
			} else if (OkhttpUtil.METHOD_PUT.equals(mMetyodType)) {
				mRequestBuilder.put(getRequestBody());//上传文件
			} else if (OkhttpUtil.METHOD_DELETE.equals(mMetyodType)) {
				mRequestBuilder.delete(getRequestBody());//删除某些资源
			}
		}
		mRequestBuilder.url(mUrl);
		if (mHeaderMap != null) {
			setHeader();
		}
		//mRequestBuilder.addHeader("Authorization","Bearer "+"token");可以把token添加到这儿
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
								return true;//忽略服务器证书验证
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
			if (mFile != null || mfileList != null || mfileMap != null) {//先判断是否有文件，
				setFile();
			} else {
				if (OkhttpUtil.METHOD_GET.equals(mMetyodType)) {
					setGetParams();
				} else if (OkhttpUtil.METHOD_POST.equals(mMetyodType)) {
					mRequestBuilder.post(getRequestBody());
				} else if (OkhttpUtil.METHOD_PUT.equals(mMetyodType)) {
					mRequestBuilder.put(getRequestBody());//上传文件
				} else if (OkhttpUtil.METHOD_DELETE.equals(mMetyodType)) {
					mRequestBuilder.delete(getRequestBody());//删除某些资源
				}
			}
			mRequestBuilder.url(mUrl);
			if (mHeaderMap != null) {
				setHeader();
			}
			//mRequestBuilder.addHeader("Authorization","Bearer "+"token");可以把token添加到这儿
			mOkHttpRequest = mRequestBuilder.build();
		}*/

	/**
	 * 得到body对象
	 */
	private RequestBody getRequestBody() {
		/**
		 * 首先判断mJsonStr是否为空，由于mJsonStr与mParamsMap不可能同时存在，所以先判断mJsonStr
		 */
		if (!TextUtils.isEmpty(mJsonStr)) {
			MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
			return RequestBody.create(JSON, mJsonStr);//json数据，
		}

		/**
		 * post,put,delete都需要body，但也都有body等于空的情况，此时也应该有body对象，但body中的内容为空
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
	 * get请求，只有键值对参数
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
	 * 设置上传文件
	 */
	private void setFile() {
		if (mFile != null) {//只有一个文件，且没有文件名
			if (mParamsMap == null) {
				setPostFile();
			} else {
				setPostParameAndFile();
			}
		} else if (mfileList != null) {//文件集合，只有一个文件名。所以这个也支持单个有文件名的文件
			setPostParameAndListFile();
		} else if (mfileMap != null) {//多个文件，每个文件对应一个文件名
			setPostParameAndMapFile();
		}

	}

	/**
	 * 只有一个文件，且提交服务器时不用指定键，没有参数
	 */
	private void setPostFile() {
		if (mFile != null && mFile.exists()) {
			MediaType fileType = MediaType.parse(mFileType);
			RequestBody body = RequestBody.create(fileType, mFile);//json数据，
			mRequestBuilder.post(new ProgressRequestBody(body, mCallBack));
		}
	}

	/**
	 * 只有一个文件，且提交服务器时不用指定键，带键值对参数
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
	 * 文件集合，可能带有键值对参数
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
	 * 文件Map，可能带有键值对参数
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
	 * 设置头参数
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
	 * 自定义RequestBody类，得到文件上传的进度
	 */
	private static class ProgressRequestBody extends RequestBody {
		//实际的待包装请求体
		private final RequestBody requestBody;
		//包装完成的BufferedSink
		private BufferedSink bufferedSink;
		private CallBackUtil callBack;

		ProgressRequestBody(RequestBody requestBody, CallBackUtil callBack) {
			this.requestBody = requestBody;
			this.callBack = callBack;
		}

		/** 重写调用实际的响应体的contentType*/
		@Override
		public MediaType contentType() {
			return requestBody.contentType();
		}

		/**重写调用实际的响应体的contentLength ，这个是文件的总字节数 */
		@Override
		public long contentLength() throws IOException {
			return requestBody.contentLength();
		}

		/** 重写进行写入*/
		@Override
		public void writeTo(BufferedSink sink) throws IOException {
			if (bufferedSink == null) {
				bufferedSink = Okio.buffer(sink(sink));
			}
			requestBody.writeTo(bufferedSink);
			//必须调用flush，否则最后一部分数据可能不会被写入
			bufferedSink.flush();
		}

		/** 写入，回调进度接口*/
		private Sink sink(BufferedSink sink) {
			return new ForwardingSink(sink) {
				//当前写入字节数
				long bytesWritten = 0L;
				//总字节长度，避免多次调用contentLength()方法
				long contentLength = 0L;

				@Override
				public void write(Buffer source, long byteCount) throws IOException {
					super.write(source, byteCount);//这个方法会循环调用，byteCount是每次调用上传的字节数。
					if (contentLength == 0) {
						//获得总字节长度
						contentLength = contentLength();
					}
					//增加当前写入的字节数
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
	 * 获得KeyStore. 
	 * @param keyStorePath 
	 *            密钥库路径 
	 * @param password 
	 *            密码 
	 * @return 密钥库 
	 * @throws Exception 
	 */
	public static KeyStore getKeyStore(Context context, String password, String keyStorePath) throws Exception {
		// 实例化密钥库  
		KeyStore ks = KeyStore.getInstance("PKCS12");
		// 获得密钥库文件流  
		InputStream in = context.getAssets().open(keyStorePath);
		// 加载密钥库  
		ks.load(in, password.toCharArray());
		// 关闭密钥库文件流  
		return ks;
	}

	/** 
	 * 获得SSLSocketFactory. 
	 * @param password 
	 *            密码 
	 * @param keyStorePath 
	 *            密钥库路径 
	 * @param trustStorePath 
	 *            信任库路径 
	 * @return SSLSocketFactory 
	 * @throws Exception 
	 */
	public SSLContext getSSLContext(Context context, String password, String keyStorePath, String trustStorePath)
			throws Exception {
		// 实例化密钥库  
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		// 获得密钥库  
		KeyStore keyStore = getKeyStore(context, password, keyStorePath);
		// 初始化密钥工厂  
		keyManagerFactory.init(keyStore, password.toCharArray());

		// 实例化SSL上下文  
		SSLContext ctx = SSLContext.getInstance("TLS");
		// 初始化SSL上下文  
		ctx.init(keyManagerFactory.getKeyManagers(),
				new TrustManager[] { geTrustManager(context, password, trustStorePath) }, null);
		// 获得SSLSocketFactory  
		return ctx;
	}

	public X509TrustManager geTrustManager(Context context, String password, String trustStorePath) {
		// 实例化信任库  
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			// 获得信任库  
			KeyStore trustStore = getKeyStore(context, password, trustStorePath);
			// 初始化信任库  

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
