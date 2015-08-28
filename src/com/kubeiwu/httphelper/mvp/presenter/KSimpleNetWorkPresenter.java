package com.kubeiwu.httphelper.mvp.presenter;

import rx.Observable;

import com.kubeiwu.httphelper.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.httphelper.mvp.utils.RxUtils;
import com.kubeiwu.httphelper.mvp.view.ISimpleView;

public class KSimpleNetWorkPresenter<T> extends KRxJavaPresenter<ISimpleView<T>> {
	private KSubscriber<T> subscriber;

	@Override
	public void destroy() {
		RxUtils.unsubscribe(subscriber);
	}

	// observable.cache() //cache会回调多次，但是只会调用一次网络
	public void loadData(Observable<T> observable1) {

		RxUtils.unsubscribe(subscriber);
		subscriber = new KSubscriber<T>(iView);
		observable1.subscribe(subscriber);
	}
}