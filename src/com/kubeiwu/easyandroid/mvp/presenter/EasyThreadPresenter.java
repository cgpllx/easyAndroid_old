package com.kubeiwu.easyandroid.mvp.presenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.kubeiwu.easyandroid.mvp.PresenterLoader;
import com.kubeiwu.easyandroid.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleThreadView;

public class EasyThreadPresenter<T> extends KRxJavaPresenter<ISimpleThreadView<T>, T> {
	private Handler mainHandler = new Handler(Looper.getMainLooper());

	@Override
	public Observable<T> creatObservable(Bundle bundle) {
		return getObservable(bundle);
	}

	protected Observable<T> getObservable(final Bundle bundle) {
		return Observable.create(new Observable.OnSubscribe<T>() {

			@Override
			public void call(Subscriber<? super T> sub) {
				if (!sub.isUnsubscribed()) {
					PresenterLoader<T> presenterLoader = getView().onCreatPresenterLoader(getPresenterId(), bundle);
					try {
						T t = presenterLoader.loadInBackground();
						sub.onNext(t);
						sub.onCompleted();
					} catch (Exception e) {
						sub.onError(e);
					}
				}
			}
		}).subscribeOn(Schedulers.io())//
				.observeOn(AndroidSchedulers.mainThread());
	}
}