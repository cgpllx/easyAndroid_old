package com.kubeiwu.httphelper.retrofit;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import retrofit.KRestAdapter;
import android.content.Context;

import com.google.gson.Gson;
import com.kubeiwu.httphelper.cache.KOkhttpCache;
import com.kubeiwu.httphelper.cache.Utils;
import com.kubeiwu.httphelper.manager.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

public class KRetrofitApiFactory {
	public static final String UNIQUENAME = "okhttpdefault";
	private final static KRetrofitApiFactory kRetrofitManager = new KRetrofitApiFactory();

	private KOkClient kclient;
	private KGsonConverter kGsonConverter;

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
		KOkhttpCache kOkhttpCache = new KOkhttpCache(context.getApplicationContext(), 1);

		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
		client.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
		client.setCookieHandler(new CookieManager(new PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
		client.setCache(new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), UNIQUENAME), 10 * 1024 * 1024));

		kclient = new KOkClient(kOkhttpCache, client);
		kGsonConverter=new KGsonConverter(new Gson(), kOkhttpCache);
	}

	/**
	 * 取得api
	 * 
	 * @param clazz
	 * @param endpoint
	 * @return
	 */
	public <T> T getApi(Class<T> clazz, String endpoint) {
		KRestAdapter restAdapter = new KRestAdapter.Builder()//
				.setEndpoint(endpoint)//
				.setConverter(kGsonConverter)//
				.setClient(kclient)//
				.build();
		T api = restAdapter.create(clazz);
		return api;
	}
}
