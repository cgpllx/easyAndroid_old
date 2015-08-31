package com.kubeiwu.easyandroid.mvp.presenter;

import rx.Observable;

import com.kubeiwu.easyandroid.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleNetWorkView;

public class KSimpleNetWorkPresenter<T> extends KRxJavaPresenter<ISimpleNetWorkView<T>, T> {

	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
	public void loadData() {
		Observable<T> observable = creatObservable();
		if (observable == null) {
			throw new IllegalArgumentException("please Override onCreatObservable method, And can not be null，");
		}
		onCancel();// 先取消之前的事件
		subscriber = new KSubscriber(this);
		observable.subscribe(subscriber);
	}

	@Override
	public Observable<T> creatObservable() {
		return iView.onCreatObservable();
	}

}