package com.kubeiwu.httphelper.mvp.presenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.kubeiwu.httphelper.mvp.PresenterLoader;
import com.kubeiwu.httphelper.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.httphelper.mvp.utils.RxUtils;
import com.kubeiwu.httphelper.mvp.view.ISimpleThreadView;

public class KSimpleThreadPresenter<T> extends KRxJavaPresenter<ISimpleThreadView<T>> {

	@Override
	public void destroy() {
		RxUtils.unsubscribe(subscriber);
	}

	private Observable<T> myObservable = Observable.create(new Observable.OnSubscribe<T>() {
		@Override
		public void call(Subscriber<? super T> sub) {
			if (!sub.isUnsubscribed()) {
				PresenterLoader<T> presenterLoader = iView.onCreatPresenterLoader(presenterId);
				try {
					T t = presenterLoader.loadInBackground();
					sub.onNext(t);
					sub.onCompleted();
				} catch (Exception e) {
					sub.onError(e);
					e.printStackTrace();
				}
			}
		}
	}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

	public void loadData() {
		RxUtils.unsubscribe(subscriber);
		subscriber = new KSubscriber<T>(iView);
		myObservable.subscribe(subscriber);
	}

	private KSubscriber<T> subscriber;

}