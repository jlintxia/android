package com.guozheng.okhttputils;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * Created by fighting on 2017/4/7.
 */

public class OkhttpUtil {

	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_PUT = "PUT";
	public static final String METHOD_DELETE = "DELETE";

	public static final String FILE_TYPE_FILE = "file/*";
	public static final String FILE_TYPE_IMAGE = "image/*";
	public static final String FILE_TYPE_AUDIO = "audio/*";
	public static final String FILE_TYPE_VIDEO = "video/*";

	/**
	 * get����
	 * @param url��url
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpGet(byte cmd, Context context, String url, CallBackUtil callBack) {
		okHttpGet(cmd, context, url, null, null, callBack);
	}

	/**
	 * get���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpGet(byte cmd, Context context, String url, Map<String, String> paramsMap,
			CallBackUtil callBack) {
		okHttpGet(cmd, context, url, paramsMap, null, callBack);
	}

	/**
	 * get���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpGet(byte cmd, Context context, String url, Map<String, String> paramsMap,
			Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_GET, url, paramsMap, headerMap, callBack).execute();
	}

	/**
	 * post����
	 * @param url��url
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPost(byte cmd, Context context, String url, CallBackUtil callBack) {
		okHttpPost(cmd, context, url, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPost(byte cmd, Context context, String url, Map<String, String> paramsMap,
			CallBackUtil callBack) {
		okHttpPost(cmd, context, url, paramsMap, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPost(byte cmd, Context context, String url, Map<String, String> paramsMap,
			Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_POST, url, paramsMap, headerMap, callBack).execute();
	}

	/**
	 * post����
	 * @param url��url
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPut(byte cmd, Context context, String url, CallBackUtil callBack) {
		okHttpPut(cmd, context, url, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPut(byte cmd, Context context, String url, Map<String, String> paramsMap,
			CallBackUtil callBack) {
		okHttpPut(cmd, context, url, paramsMap, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPut(byte cmd, Context context, String url, Map<String, String> paramsMap,
			Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_PUT, url, paramsMap, headerMap, callBack).execute();
	}

	/**
	 * post����
	 * @param url��url
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpDelete(byte cmd, Context context, String url, CallBackUtil callBack) {
		okHttpDelete(cmd, context, url, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpDelete(byte cmd, Context context, String url, Map<String, String> paramsMap,
			CallBackUtil callBack) {
		okHttpDelete(cmd, context, url, paramsMap, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpDelete(byte cmd, Context context, String url, Map<String, String> paramsMap,
			Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_DELETE, url, paramsMap, headerMap, callBack).execute();
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param jsonStr��json��ʽ�ļ�ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPostJson(byte cmd, Context context, String url, String jsonStr, CallBackUtil callBack) {
		okHttpPostJson(cmd, context, url, jsonStr, null, callBack);
	}

	/**
	 * post���󣬿��Դ��ݲ���
	 * @param url��url
	 * @param jsonStr��json��ʽ�ļ�ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpPostJson(byte cmd, Context context, String url, String jsonStr,
			Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_POST, url, jsonStr, headerMap, callBack).execute();
	}

	/**
	 * post�����ϴ������ļ�
	 * @param url��url
	 * @param file��File����
	 * @param fileKey���ϴ�����ʱfile��Ӧ�ļ�
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳���������дonProgress�������õ��ϴ�����
	 */
	public static void okHttpUploadFile(byte cmd, Context context, String url, File file, String fileKey,
			String fileType, CallBackUtil callBack) {
		okHttpUploadFile(cmd, context, url, file, fileKey, fileType, null, callBack);
	}

	/**
	 * post�����ϴ������ļ�
	 * @param url��url
	 * @param file��File����
	 * @param fileKey���ϴ�����ʱfile��Ӧ�ļ�
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳���������дonProgress�������õ��ϴ�����
	 */
	public static void okHttpUploadFile(byte cmd, Context context, String url, File file, String fileKey,
			String fileType, Map<String, String> paramsMap, CallBackUtil callBack) {
		okHttpUploadFile(cmd, context, url, file, fileKey, fileType, paramsMap, null, callBack);
	}

	/**
	 * post�����ϴ������ļ�
	 * @param url��url
	 * @param file��File����
	 * @param fileKey���ϴ�����ʱfile��Ӧ�ļ�
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳���������дonProgress�������õ��ϴ�����
	 */
	public static void okHttpUploadFile(byte cmd, Context context, String url, File file, String fileKey,
			String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_POST, url, paramsMap, file, fileKey, fileType, headerMap, callBack)
				.execute();
	}

	/**
	 * post�����ϴ�����ļ�����list���ϵ���ʽ
	 * @param url��url
	 * @param fileList������Ԫ����File����
	 * @param fileKey���ϴ�����ʱfileList��Ӧ�ļ�
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpUploadListFile(byte cmd, Context context, String url, List<File> fileList, String fileKey,
			String fileType, CallBackUtil callBack) {
		okHttpUploadListFile(cmd, context, url, null, fileList, fileKey, fileType, callBack);
	}

	/**
	 * post�����ϴ�����ļ�����list���ϵ���ʽ
	 * @param url��url
	 * @param fileList������Ԫ����File����
	 * @param fileKey���ϴ�����ʱfileList��Ӧ�ļ�
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpUploadListFile(byte cmd, Context context, String url, Map<String, String> paramsMap,
			List<File> fileList, String fileKey, String fileType, CallBackUtil callBack) {
		okHttpUploadListFile(cmd, context, url, paramsMap, fileList, fileKey, fileType, null, callBack);
	}

	/**
	 * post�����ϴ�����ļ�����list���ϵ���ʽ
	 * @param url��url
	 * @param fileList������Ԫ����File����
	 * @param fileKey���ϴ�����ʱfileList��Ӧ�ļ�
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpUploadListFile(byte cmd, Context context, String url, Map<String, String> paramsMap,
			List<File> fileList, String fileKey, String fileType, Map<String, String> headerMap,
			CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_POST, url, paramsMap, fileList, fileKey, fileType, headerMap, callBack)
				.execute();
	}

	/**
	 * post�����ϴ�����ļ�����map���ϵ���ʽ
	 * @param url��url
	 * @param fileMap������key��File�����Ӧ�ļ�������value��File����
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpUploadMapFile(byte cmd, Context context, String url, Map<String, File> fileMap,
			String fileType, CallBackUtil callBack) {
		okHttpUploadMapFile(cmd, context, url, fileMap, fileType, null, callBack);
	}

	/**
	 * post�����ϴ�����ļ�����map���ϵ���ʽ
	 * @param url��url
	 * @param fileMap������key��File�����Ӧ�ļ�������value��File����
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpUploadMapFile(byte cmd, Context context, String url, Map<String, File> fileMap,
			String fileType, Map<String, String> paramsMap, CallBackUtil callBack) {
		okHttpUploadMapFile(cmd, context, url, fileMap, fileType, paramsMap, null, callBack);
	}

	/**
	 * post�����ϴ�����ļ�����map���ϵ���ʽ
	 * @param url��url
	 * @param fileMap������key��File�����Ӧ�ļ�������value��File����
	 * @param fileType��File���ͣ���image��video��audio��file
	 * @param paramsMap��map���ϣ���װ��ֵ�Բ���
	 * @param headerMap��map���ϣ���װ����ͷ��ֵ��
	 * @param callBack���ص��ӿڣ�onFailure����������ʧ��ʱ���ã�onResponse����������ɹ�����ã�������������ִ����UI�̡߳�
	 */
	public static void okHttpUploadMapFile(byte cmd, Context context, String url, Map<String, File> fileMap,
			String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
		new RequestUtil(cmd, context, METHOD_POST, url, paramsMap, fileMap, fileType, headerMap, callBack).execute();
	}

	/**
	 * �����ļ�,��������
	 */
	public static void okHttpDownloadFile(byte cmd, Context context, String url, CallBackUtil.CallBackFile callBack) {
		okHttpDownloadFile(cmd, context, url, null, callBack);
	}

	/**
	 * �����ļ�,������
	 */
	public static void okHttpDownloadFile(byte cmd, Context context, String url, Map<String, String> paramsMap,
			CallBackUtil.CallBackFile callBack) {
		okHttpGet(cmd, context, url, paramsMap, null, callBack);
	}

	/**
	 * ����ͼƬ
	 */
	public static void okHttpGetBitmap(byte cmd, Context context, String url, CallBackUtil.CallBackBitmap callBack) {
		okHttpGetBitmap(cmd, context, url, null, callBack);
	}

	/**
	 * ����ͼƬ��������
	 */
	public static void okHttpGetBitmap(byte cmd, Context context, String url, Map<String, String> paramsMap,
			CallBackUtil.CallBackBitmap callBack) {
		okHttpGet(cmd, context, url, paramsMap, null, callBack);
	}

}
