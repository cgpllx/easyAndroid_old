package com.kubeiwu.easyandroid.easycore.converter;

import retrofit.KRetrofit;
import retrofit.RxJavaCallAdapterFactory;

import com.kubeiwu.easyandroid.config.EAConfiguration;
import com.squareup.okhttp.OkHttpClient;

public class KRetrofitApiFactory {
	private final static KRetrofitApiFactory kRetrofitManager = new KRetrofitApiFactory();

	private OkHttpClient client;
	KGsonConverterFactory kGsonConverterFactory;

	public static KRetrofitApiFactory getInstance() {
		return kRetrofitManager;
	}

	public void init(EAConfiguration config) {
		if (config == null) {
			new IllegalArgumentException("EAConfiguration config 不能为null");
		}
		client = config.getOkHttpClient();

		kGsonConverterFactory = KGsonConverterFactory.create(config.getGson(), config.getCache());
	}

	public OkHttpClient getOkHttpClient() {
		if (client == null) {
			throw new IllegalArgumentException("请先调用KRetrofitApiFactory的init方法");
		}
		return client;
	}

	/**
	 * 取得api
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
