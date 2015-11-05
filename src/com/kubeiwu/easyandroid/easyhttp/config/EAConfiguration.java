package com.kubeiwu.easyandroid.easyhttp.config;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.concurrent.TimeUnit;

import android.content.Context;

import com.google.gson.Gson;
import com.kubeiwu.easyandroid.cache.Utils;
import com.kubeiwu.easyandroid.cache.volleycache.DiskBasedCache;
import com.kubeiwu.easyandroid.easyhttp.cookiestore.PersistentCookieStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

public class EAConfiguration {
	private Gson mGson;
	private DiskBasedCache mCache;
	private CookieStore mCookieStore;
	private OkHttpClient mOkHttpClient;

	public Gson getGson() {
		return mGson;
	}

	public DiskBasedCache getCache() {
		return mCache;
	}

	public CookieStore getCookieStore() {
		return mCookieStore;
	}

	public OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}

	private EAConfiguration(Builder builder) {
		mGson = builder.gson;
		mCache = builder.valleyCache;
		mCookieStore = builder.cookieStore;
		mOkHttpClient = builder.okHttpClient;
	}

	public static class Builder {
		private Gson gson;
		private final DiskBasedCache valleyCache;
		private final Cache okHttpCache;
		private final CookieStore cookieStore;
		private OkHttpClient okHttpClient;

		public Builder setGson(Gson gson) {
			this.gson = gson;
			return this;
		}

		public Builder setOkHttpClient(OkHttpClient okHttpClient) {
			this.okHttpClient = okHttpClient;
			return this;
		}

		public Builder(Context context) {
			// 根据volley 缓存cache修改 ，不需要http协议就可保存
			valleyCache = new DiskBasedCache(Utils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));// retrofit缓存
			valleyCache.initialize();
			cookieStore = new PersistentCookieStore(context.getApplicationContext());
			okHttpCache = new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), "okhttpcache"), 10 * 1024 * 1024);
			okHttpClient.setCache(new Cache(Utils.getDiskCacheDir(context.getApplicationContext(), "okhttpcache"), 10 * 1024 * 1024));// OkHttpClient缓存
		}

		public EAConfiguration build() {
			if (gson == null) {
				gson = new Gson();
			}
			if (okHttpClient == null) {
				okHttpClient = new OkHttpClient();
				okHttpClient.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
				okHttpClient.setFollowRedirects(true);
				okHttpClient.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
				okHttpClient.setCookieHandler(new CookieManager(cookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
				okHttpClient.setCache(okHttpCache);// OkHttpClient缓存
			}
			return new EAConfiguration(this);
		}
	}

}
