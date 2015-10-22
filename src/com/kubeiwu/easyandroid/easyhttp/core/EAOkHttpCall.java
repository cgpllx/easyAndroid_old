package com.kubeiwu.easyandroid.easyhttp.core;

import retrofit.Converter;
import retrofit.KOkHttpCall;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class EAOkHttpCall<T> extends KOkHttpCall<T> {
	private Request request;

	public EAOkHttpCall(OkHttpClient client, Converter<T> responseConverter, Request request) {
		super(client, null, responseConverter, null);
		this.request = request;
	}

	@Override
	public Request createRequest() {
		return request;
	}

}
