package com.kubeiwu.easyandroid.kretrofit;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import retrofit.KRetrofit;
import retrofit.RxJavaCallAdapterFactory;
import android.content.Context;

import com.google.gson.Gson;
import com.kubeiwu.easyandroid.cache.Utils;
import com.kubeiwu.easyandroid.cache.volleycache.DiskBasedCache;
import com.kubeiwu.easyandroid.manager.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

public class KRetrofitApiFactory {
	public static final String UNIQUENAME = "okhttpcache";
	private final static KRetrofitApiFactory kRetrofitManager = new KRetrofitApiFactory();

	private OkHttpClient client;
	KGsonConverterFactory kGsonConverterFactory;

	public static KRetrofitApiFactory getInstance() {
		return kRetrofitManager;
	}

	/**
	 * 初始化配置
	 * 
	 * @param context
	 * @throws IOException
	 */
	public void init(Context context) throws IOException {
		init(context, new Gson());
	}

	/**
	 * 初始化配置
	 * 
	 * @param context
	 * @throws IOException
	 */
	public void init(Context context, Gson gson) throws IOException {
		DiskBasedCache cache = new DiskBasedCache(Utils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));// retrofit缓存
		cache.initialize();

		client = new OkHttpClient();
		client.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
		client.setFollowRedirects(true);
		client.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
		client.setCookieHandler(new CookieManager(new PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
		client.setCache(new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), UNIQUENAME), 10 * 1024 * 1024));// OkHttpClient缓存

		kGsonConverterFactory = KGsonConverterFactory.create(gson, cache);
	}

	public OkHttpClient getOkHttpClient() {
		if (client == null) {
			throw new IllegalArgumentException("请先调用KRetrofitApiFactory的init方法");
		}
		return client;
	}

	/**
	 * 取得api
	 * 
	 * @param clazz
	 * @param endpoint
	 * @return
	 */
	public <T> T getApi(Class<T> clazz, String endpoint) {
		KRetrofit retrofit = new KRetrofit.Builder()//
				.client(client)//
				.baseUrl(endpoint)//
				.addConverterFactory(kGsonConverterFactory)//
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
				.build();

		return retrofit.create(clazz);
	}
}
