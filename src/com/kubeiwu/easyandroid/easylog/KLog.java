package com.kubeiwu.easyandroid.easylog;

import android.util.Log;

public class KLog {
	public static boolean isDebug = true;
	
	private static final String TAG = "KLog";

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}
	public static void e(String tag, String text) {
		if(isDebug){
			Log.e(tag,text);
		}
	}

	public static void d(String tag, String text) {
		if(isDebug){
			Log.d(tag,text);
		}
	}

	public static void w(String tag, String text) {
		if(isDebug){
			Log.w(tag,text);
		}
	}

	public static void i(String tag, String text) {
		if(isDebug){
			Log.i(tag,text);
		}
	}

	public static void v(String tag, String text) {
		if(isDebug){
			Log.v(tag,text);
		}
	}
}
