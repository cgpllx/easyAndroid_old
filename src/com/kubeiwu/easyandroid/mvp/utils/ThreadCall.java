package com.kubeiwu.easyandroid.mvp.utils;

import java.io.IOException;

import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Call;
import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Callback;
import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Response;
import com.kubeiwu.easyandroid.mvp.PresenterLoader;

public class ThreadCall<T> implements Call<T> {
	private PresenterLoader<T> loader;

	public ThreadCall(PresenterLoader<T> loader) {
		this.loader = loader;
	}

	@Override
	public Response<T> execute() throws IOException {
		return null;
	}

	@Override
	public void enqueue(Callback<T> callback) {

	}

	@Override
	public void cancel() {

	}

	@Override
	public Call<T> clone() {
		return null;
	}

	@Override
	public boolean isCancel() {
		return false;
	}

}
