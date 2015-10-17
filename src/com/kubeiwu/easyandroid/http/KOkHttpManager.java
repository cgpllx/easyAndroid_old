package com.kubeiwu.easyandroid.http;

import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import retrofit.Call;
import retrofit.Converter;
import retrofit.RxJavaCallAdapterFactory.SimpleCallAdapter;
import rx.Observable;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.kubeiwu.easyandroid.cache.Utils;
import com.kubeiwu.easyandroid.cache.volleycache.DiskBasedCache;
import com.kubeiwu.easyandroid.core.WrapObservable;
import com.kubeiwu.easyandroid.kretrofit.KGsonConverter;
import com.kubeiwu.easyandroid.kretrofit.KGsonConverterFactory;
import com.kubeiwu.easyandroid.manager.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class KOkHttpManager {
	private static KOkHttpManager mInstance = new KOkHttpManager();
	private OkHttpClient mOkHttpClient;
	public Gson mGson = new Gson();
	public static final String UNIQUENAME = "okhttpcache";

	private KOkHttpManager() {
		mOkHttpClient = new OkHttpClient();
		// cookie enabled
		mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	public static KOkHttpManager getInstance() {
		return mInstance;
	}

	DiskBasedCache cache;

	public void init(Context context) {
		cache = new DiskBasedCache(Utils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));// retrofit缓存
		cache.initialize();

		// client = new OkHttpClient();
		mOkHttpClient.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
		mOkHttpClient.setFollowRedirects(true);
		mOkHttpClient.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
		/**
		 * cookies
		 */
		mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
		mOkHttpClient.setCache(new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), UNIQUENAME), 10 * 1024 * 1024));// OkHttpClient缓存
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
	public <T> WrapObservable<T> executeHttpRequestToObservable(Request request, Type type) {
		
		Converter responseConverter = KGsonConverterFactory.create(mGson, cache).get(type);
		Call<T> call = new EAOkHttpCall<T>(mOkHttpClient, responseConverter, request);
		SimpleCallAdapter<T> simpleCallAdapter = new SimpleCallAdapter<T>(type);

		Observable<T> observable = simpleCallAdapter.adapt(call);
		
		WrapObservable<T> wrapObservable = new WrapObservable<T>(observable);
		wrapObservable.setCall(call);
		return wrapObservable;
	}
}
