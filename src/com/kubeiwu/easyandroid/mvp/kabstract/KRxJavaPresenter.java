package com.kubeiwu.easyandroid.mvp.kabstract;

import rx.Observable;
import rx.Subscriber;

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

	public abstract Observable<T> creatObservable();

	public class KSubscriber extends Subscriber<T> {
		Controller<T> mController;

		public KSubscriber(Controller<T> controller) {
			this.mController = controller;
		}

		@Override
		public void onStart() {
			super.onStart();
			this.mController.showLoading();
		}

		@Override
		public void onNext(T s) {
			// deliverResult(s);
			this.mController.deliverResult(s);
		}

		@Override
		public void onCompleted() {
			this.mController.hideLoading();
		}

		@Override
		public void onError(Throwable e) {
			this.mController.handleError("服务器或网络异常");
			this.mController.hideLoading();
		}
	}

	@Override
	public void showLoading() {
		iView.showLoading(presenterId);
	}

	@Override
	public void hideLoading() {
		iView.hideLoading(presenterId);
	}

	@Override
	public void handleError(String errorDesc) {
		iView.handleError(presenterId, errorDesc);
	}

	@Override
	public void deliverResult(T results) {
		iView.deliverResult(presenterId, results);
	}
}