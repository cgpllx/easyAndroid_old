package com.kubeiwu.easyandroid.mvp.kabstract;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KPresenter<V extends ISimpleView<T>, T> implements Presenter<V> {
	protected V iView;
	protected final Controller<T> mController = new Controller<T>() {

		@Override
		public void showLoading() {
			onShowLoading();
		}

		@Override
		public void hideLoading() {
			onHideLoading();
		}

		@Override
		public void handleError(String errorDesc) {
			onHandleError(errorDesc);
		}

		@Override
		public void deliverResult(T results) {
			onDeliverResult(results);
		}
	};

	public void setView(V view) {
		this.iView = view;
		initDeliverResultType(view);
	}

	public final void initialize() {
		onInitialize();
	}

	public final void resume() {
		onResume();
	}

	public final void pause() {
		onPause();
	}

	public final void destroy() {
		onDestroy();
	}

	public void cancel() {
		onCancel();
	}

	protected void onCancel() {

	}

	protected void onPause() {

	}

	protected void onInitialize() {

	}

	protected void onResume() {

	}

	protected void onDestroy() {

	}

	protected void onShowLoading() {
	}

	protected void onHideLoading() {
	}

	protected void onHandleError(String errorDesc) {
	}

	protected abstract void onDeliverResult(final T results);

	protected Type mType;

	public final int presenterId = hashCode();

	public int getPresenterId() {
		return presenterId;
	}

	private void initDeliverResultType(V iView) {
		Method[] methods = iView.getClass().getMethods();
		for (Method method : methods) {
			if ("deliverResult".equals(method.getName())) {
				mType = method.getGenericParameterTypes()[1];
				return;
			}
		}
	}
}