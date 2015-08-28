package com.kubeiwu.httphelper.mvp.presenter;

import rx.Observable;
import rx.Subscriber;

import com.kubeiwu.httphelper.mvp.kabstract.KPresenter;
import com.kubeiwu.httphelper.mvp.utils.RxUtils;
import com.kubeiwu.httphelper.mvp.view.ISimpleView;

public class KSimpleNetWorkPresenter<T> extends KPresenter<ISimpleView<T>> {
	private KSubscriber subscriber;

	@Override
	public void destroy() {
		RxUtils.unsubscribe(subscriber);
	}

	// observable.cache() //cache会回调多次，但是只会调用一次网络
	public void loadData(Observable<T> observable1) {

		RxUtils.unsubscribe(subscriber);
		subscriber = new KSubscriber();
		observable1.subscribe(subscriber);
	}

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