package com.kubeiwu.httphelper.mvp;

public interface PresenterLoader<T> {

	public T loadInBackground() throws Exception;
}
