package com.kubeiwu.httphelper.mvp.presenter;

import com.kubeiwu.httphelper.mvp.kabstract.KLoaderPresenterAbstract;

/**
 * loader线程的返回值
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class KSimpleLoaderPresenter<T> extends KLoaderPresenterAbstract<T> {

	@Override
	protected void deliverResult(T result) {
		iView.deliverResult(presenterId, result);
	}


}
