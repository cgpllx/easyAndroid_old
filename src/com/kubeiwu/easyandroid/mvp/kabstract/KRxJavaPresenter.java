package com.kubeiwu.easyandroid.mvp.kabstract;

import rx.Subscriber;

import com.kubeiwu.easyandroid.mvp.utils.RxUtils;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KRxJavaPresenter<V extends ISimpleView<T>, T> extends KPresenter<V, T> {

	protected KSubscriber<T> subscriber;
	
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