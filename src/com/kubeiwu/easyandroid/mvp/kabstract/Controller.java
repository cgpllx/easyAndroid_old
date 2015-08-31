package com.kubeiwu.easyandroid.mvp.kabstract;

public interface Controller<T> {
	void showLoading();

	void hideLoading();

	void handleError(String errorDesc);

	void deliverResult(final T results);
}
