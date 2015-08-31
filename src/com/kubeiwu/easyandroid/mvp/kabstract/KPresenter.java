package com.kubeiwu.easyandroid.mvp.kabstract;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KPresenter<V extends ISimpleView<T>, T> implements Presenter<V> {
	protected V iView;

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