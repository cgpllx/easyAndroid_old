package com.kubeiwu.httphelper.mvp.kabstract;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.kubeiwu.httphelper.mvp.view.IView;

public abstract class KPresenter<V extends IView> {
	protected V iView;

	public void setView(V view) {
		this.iView = view;
		initDeliverResultType(view);
	}

	public void initialize() {
	}

	public void resume() {
	}

	public void pause() {
	}

	public void destroy() {
	}

	protected Type mType;

	public final int presenterId = hashCode();

	public int getPresenterId() {
		return presenterId;
	}

	void initDeliverResultType(V iView) {
		Method[] methods = iView.getClass().getMethods();
		for (Method method : methods) {
			if ("deliverResult".equals(method.getName())) {
				mType = method.getGenericParameterTypes()[1];
				return;
			}
		}
	}
}