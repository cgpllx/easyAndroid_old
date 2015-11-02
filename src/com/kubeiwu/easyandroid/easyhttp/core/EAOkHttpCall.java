package com.kubeiwu.easyandroid.easyhttp.core;

import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Converter;
import com.kubeiwu.easyandroid.easyhttp.core.retrofit.KOkHttpCall;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class EAOkHttpCall<T> extends KOkHttpCall<T> {
	private Request request;

	public EAOkHttpCall(OkHttpClient client, Converter<T> responseConverter, Request request) {
		super(client, responseConverter);
		this.request = request;
	}

	@Override
	public Request createRequest() {
		return request;
	}

}
