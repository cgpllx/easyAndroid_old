package com.kubeiwu.easyandroid.mvp;

public interface PresenterLoader<T> {

	public T loadInBackground() throws Exception;
}
