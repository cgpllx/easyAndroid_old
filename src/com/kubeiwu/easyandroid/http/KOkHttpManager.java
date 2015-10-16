package com.kubeiwu.easyandroid.http;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import android.content.Context;

import com.google.gson.Gson;
import com.kubeiwu.easyandroid.cache.Utils;
import com.kubeiwu.easyandroid.manager.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

public class KOkHttpManager {
	private static KOkHttpManager mInstance = new KOkHttpManager();
	private OkHttpClient client;
	private Gson mGson = new Gson();
	public static final String UNIQUENAME = "okhttpcache";

	private KOkHttpManager() {
		client = new OkHttpClient();
		// cookie enabled
		client.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	public static KOkHttpManager getInstance() {
		return mInstance;
	}

	public void init(Context context) {
		client = new OkHttpClient();
		client.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
		client.setFollowRedirects(true);
		client.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
		client.setCookieHandler(new CookieManager(new PersistentCookieStore(context.getApplicationContext()), CookiePolicy.ACCEPT_ORIGINAL_SERVER));
		client.setCache(new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), UNIQUENAME), 10 * 1024 * 1024));// OkHttpClient缓存
	}
}
