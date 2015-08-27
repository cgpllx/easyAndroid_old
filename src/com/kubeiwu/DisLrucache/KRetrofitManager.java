package com.kubeiwu.DisLrucache;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.RestAdapter;
import android.content.Context;

import com.google.gson.Gson;
import com.kubeiwu.DisLrucache.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

public class KRetrofitManager {
	private final KRetrofitManager kRetrofitManager = new KRetrofitManager();

	private KOkClient kOkClient;

	public KRetrofitManager getInstance() {
		return kRetrofitManager;
	}

	/**
	 * 初始化配置
	 * 
	 * @param context
	 */
	public void init(Context context) {
		OkHttpClient client = new OkHttpClient();
		client.setCookieHandler(new CookieManager(new PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
		client.setCache(new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), "okhttpdefault"), 10 * 1024 * 1024));

		kOkClient = new KOkClient(client);
	}

	/**
	 * 取得api
	 * 
	 * @param clazz
	 * @param endpoint
	 * @return
	 */
	public <T> T getAPI(Class<T> clazz, String endpoint) {
		RestAdapter restAdapter = new RestAdapter.Builder()//
				.setEndpoint(endpoint)//
				.setConverter(new KGsonConverter(new Gson()))//
				.setClient(kOkClient)//
				.build();
		T api = restAdapter.create(clazz);
		return api;
	}
}
