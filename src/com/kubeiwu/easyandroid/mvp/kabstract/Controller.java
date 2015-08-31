package com.kubeiwu.easyandroid.mvp.kabstract;

public interface Controller<T> {
	/**
     * @hide
     */
	void showLoading();
	/**
     * @hide
     */
	void hideLoading();
	/**
     * @hide
     */
	void handleError(String errorDesc);
	/**
     * @hide
     */
	void deliverResult(final T results);
}
