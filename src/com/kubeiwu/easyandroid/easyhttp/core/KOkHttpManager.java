package com.kubeiwu.easyandroid.easyhttp.core;

import java.lang.reflect.Type;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import retrofit.Call;
import retrofit.Converter;
import retrofit.RxJavaCallAdapterFactory.SimpleCallAdapter;
import rx.Observable;

import com.google.gson.Gson;
import com.kubeiwu.easyandroid.cache.volleycache.DiskBasedCache;
import com.kubeiwu.easyandroid.config.EAConfiguration;
import com.kubeiwu.easyandroid.kretrofit.KGsonConverterFactory;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class KOkHttpManager {
	private static final KOkHttpManager mInstance = new KOkHttpManager();
	private OkHttpClient mOkHttpClient;
	public Gson mGson;

	private KOkHttpManager() {
	}

	public static KOkHttpManager getInstance() {
		return mInstance;
	}

	private DiskBasedCache cache;

	public void init(EAConfiguration config) {
		if (config == null) {
			new IllegalArgumentException("EAConfiguration config 不能为null");
		}
		cache = config.getCache();
		mGson = config.getGson();
		mOkHttpClient = config.getOkHttpClient();
		// cookie enabled
		mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	private OkHttpGetUtils okHttpGetUtils;
	private OkHttpPostUtils okHttpPostUtils;
	private OkHttpUpLoadUtil okHttpUpLoadUtil;
	private OkHttpDownloadUtils okHttpDownloadUtils;

	public OkHttpPostUtils getOkHttpPostUtils() {
		if (okHttpPostUtils == null) {
			synchronized (KOkHttpManager.class) {
				if (okHttpPostUtils == null) {
					okHttpPostUtils = new OkHttpPostUtils(mOkHttpClient);
				}
			}
		}
		return okHttpPostUtils;
	}

	public OkHttpUpLoadUtil getOkHttpUpLoadUtil() {
		if (okHttpUpLoadUtil == null) {
			synchronized (KOkHttpManager.class) {
				if (okHttpUpLoadUtil == null) {
					okHttpUpLoadUtil = new OkHttpUpLoadUtil(mOkHttpClient);
				}
			}
		}
		return okHttpUpLoadUtil;
	}

	public OkHttpDownloadUtils getOkHttpDownloadUtils() {
		if (okHttpDownloadUtils == null) {
			synchronized (KOkHttpManager.class) {
				if (okHttpDownloadUtils == null) {
					okHttpDownloadUtils = new OkHttpDownloadUtils(mOkHttpClient);
				}
			}
		}
		return okHttpDownloadUtils;
	}

	public OkHttpGetUtils getOkHttpGetUtils() {
		if (okHttpGetUtils == null) {
			synchronized (KOkHttpManager.class) {
				if (okHttpGetUtils == null) {
					okHttpGetUtils = new OkHttpGetUtils(mOkHttpClient);
				}
			}
		}
		return okHttpGetUtils;
	}

	public OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Observable<T> executeHttpRequestToObservable(Request request, Type type) {
	 
		Converter responseConverter = KGsonConverterFactory.create(mGson, cache).get(type);
		Call<T> call = new EAOkHttpCall<T>(mOkHttpClient, responseConverter, request);
		SimpleCallAdapter<T> simpleCallAdapter = new SimpleCallAdapter<T>(type);

		Observable<T> observable = simpleCallAdapter.adapt(call);

		return observable;
	}

	private void checkNull(Object cache2) {
		
	}
}
