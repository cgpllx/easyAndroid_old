package com.kubeiwu.httphelper.mvp.presenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.kubeiwu.httphelper.mvp.PresenterLoader;
import com.kubeiwu.httphelper.mvp.kabstract.KPresenter;
import com.kubeiwu.httphelper.mvp.utils.RxUtils;
import com.kubeiwu.httphelper.mvp.view.ISimpleThreadView;

public class KSimpleThreadPresenter<T> extends KPresenter<ISimpleThreadView<T>> {

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
		subscriber = new KSubscriber();
		myObservable.subscribe(subscriber);
	}

	private KSubscriber subscriber;

	class KSubscriber extends Subscriber<T> {
		@Override
		public void onStart() {
			super.onStart();
			iView.showLoading(presenterId);
		}

		@Override
		public void onNext(T s) {
			iView.deliverResult(presenterId, s);
		}

		@Override
		public void onCompleted() {
			iView.hideLoading(presenterId);
		}

		@Override
		public void onError(Throwable e) {
			iView.handleError(presenterId, "服务器或网络异常");
			iView.hideLoading(presenterId);
		}
	}
}