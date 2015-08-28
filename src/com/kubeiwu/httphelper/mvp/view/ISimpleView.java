package com.kubeiwu.httphelper.mvp.view;

public interface ISimpleView<T> extends IView {
	void showLoading(int presenterId);

	void hideLoading(int presenterId);

	void handleError(int presenterId,String errorDesc);

	void deliverResult(int presenterId,final T results);

}
