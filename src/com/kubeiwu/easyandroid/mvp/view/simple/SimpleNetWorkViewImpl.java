package com.kubeiwu.easyandroid.mvp.view.simple;

import com.kubeiwu.easyandroid.mvp.view.ISimpleNetWorkView;

public abstract class SimpleNetWorkViewImpl<T> implements ISimpleNetWorkView<T> {

	@Override
	public void onStart(int presenterId) {

	}

	@Override
	public void onCompleted(int presenterId) {

	}

	@Override
	public void onError(int presenterId, String errorDesc) {

	}

	@Override
	public void deliverResult(int presenterId, T results) {

	}

}
