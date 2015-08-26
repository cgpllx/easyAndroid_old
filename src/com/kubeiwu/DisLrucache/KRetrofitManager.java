package com.kubeiwu.DisLrucache;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import android.content.Context;

import com.google.gson.Gson;
import com.kubeiwu.DisLrucache.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

public class KRetrofitManager {
	public static void init(Context context) {
		OkHttpClient client = new OkHttpClient();
		client.setCookieHandler(new CookieManager(new PersistentCookieStore(
				context), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
		int cacheSize = 10 * 1024 * 1024; // 10 MiB
		client.setCache(new Cache(Utils.getDiskCacheDir(context,
				"okhttpdefault"), cacheSize));

		KOkClient kOkClient = new KOkClient(client);
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint("http://www.baidu.com")
				.setConverter(new GsonConverter(new Gson()))
				.setClient(kOkClient).build();
		// restAdapter.
		// Create an instance of our GitHub API interface.
		// Api github = restAdapter.create(Api.class);
	}

	public RestAdapter restAdapter;
	private KRetrofitManager kRetrofitManager;
	
	public KRetrofitManager getInstance(){
		if(kRetrofitManager==null){
			kRetrofitManager=new KRetrofitManager();
		}
		return kRetrofitManager;
	}
	
	public <T> T getAPI(Class<T> clazz) {
		T api = restAdapter.create(clazz);
		return api;
	}
}
