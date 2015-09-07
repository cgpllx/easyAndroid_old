package com.kubeiwu.easyandroid.mvp.kabstract;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.os.Bundle;

import com.kubeiwu.easyandroid.mvp.exception.MvpException;
import com.kubeiwu.easyandroid.mvp.utils.RxUtils;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KRxJavaPresenter<V extends ISimpleView<T>, T> extends KPresenter<V, T> {

	protected KSubscriber subscriber;

	@Override
	protected void onCancel() {
		super.onCancel();
		unsubscribe();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unsubscribe();
	}

	private void unsubscribe() {
		RxUtils.unsubscribe(subscriber);
	}

	public abstract Observable<T> creatObservable(Bundle bundle);

	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
	public void loadData(Bundle bundle) {
		Observable<T> observable = creatObservable(bundle).subscribeOn(Schedulers.io())//
				.observeOn(AndroidSchedulers.mainThread());
		if (observable == null) {
			throw new IllegalArgumentException("please Override onCreatObservable method, And can not be null，");
		}
		cancel();// 先取消之前的事件
		subscriber = new KSubscriber(this.mController);
		observable.subscribe(subscriber);
	}

	public void loadData() {
		loadData(null);
	}

	public class KSubscriber extends Subscriber<T> {
		IController<T> mController;

		public KSubscriber(IController<T> controller) {
			this.mController = controller;
		}

		@Override
		public void onStart() {
			super.onStart();
			this.mController.start();
		}

		@Override
		public void onNext(T s) {
			this.mController.deliverResult(s);
		}

		@Override
		public void onCompleted() {
			this.mController.completed();
		}

		@Override
		public void onError(Throwable e) {
			if (e != null && e instanceof MvpException) {
				this.mController.error(e.getMessage());
			} else {
				this.mController.error("服务器或网络异常");
			}
			// this.mController.hideLoading();
		}
	}
}