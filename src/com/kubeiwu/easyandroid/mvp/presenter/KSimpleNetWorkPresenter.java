package com.kubeiwu.easyandroid.mvp.presenter;

import rx.Observable;

import com.kubeiwu.easyandroid.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.easyandroid.mvp.utils.RxUtils;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public class KSimpleNetWorkPresenter<T> extends KRxJavaPresenter<ISimpleView<T>, T> {


	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
	public void loadData(Observable<T> observable) {
		onCancel();//先取消之前的事件
		subscriber = new KSubscriber<T>(iView);
		observable.subscribe(subscriber);
	}


}