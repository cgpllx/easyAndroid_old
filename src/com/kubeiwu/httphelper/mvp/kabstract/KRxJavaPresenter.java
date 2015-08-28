package com.kubeiwu.httphelper.mvp.kabstract;

import rx.Subscriber;

import com.kubeiwu.httphelper.mvp.view.ISimpleView;
import com.kubeiwu.httphelper.mvp.view.IView;

public abstract class KRxJavaPresenter<V extends IView> extends KPresenter<V> {

	public class KSubscriber<D> extends Subscriber<D> {
		ISimpleView<D> mIView;

		public KSubscriber(ISimpleView<D> iView) {
			this.mIView = iView;
		}

		@Override
		public void onStart() {
			super.onStart();
			mIView.showLoading(presenterId);
		}

		@Override
		public void onNext(D s) {
			mIView.deliverResult(presenterId, s);
		}

		@Override
		public void onCompleted() {
			mIView.hideLoading(presenterId);
		}

		@Override
		public void onError(Throwable e) {
			mIView.handleError(presenterId, "服务器或网络异常");
			mIView.hideLoading(presenterId);
		}
	}
}