package com.kubeiwu.easyandroid.mvp.kabstract;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KPresenter<V extends ISimpleView<T>, T> implements Presenter<V> {
	private V iView;
	protected final IController<T> mController = new IController<T>() {

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

	public V getView() {
		return iView;
	}

	@Override
	public final void initialize() {
		onInitialize();
	}

	@Override
	public final void resume() {
		onResume();
	}

	@Override
	public final void pause() {
		onPause();
	}

	@Override
	public final void destroy() {
		onDestroy();
	}

	@Override
	public final void cancel() {
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

	private void onShowLoading() {
		iView.showLoading(presenterId);
	}

	private void onHideLoading() {
		iView.hideLoading(presenterId);
	}

	private void onHandleError(String errorDesc) {
		iView.handleError(presenterId, errorDesc);
	}

	private void onDeliverResult(final T results) {
		iView.deliverResult(presenterId, results);
	}

	protected Type mType;

	public final int presenterId = hashCode();

	@Override
	public int getPresenterId() {
		return presenterId;
	}

	private void initDeliverResultType(V iView) {
		
		// Method[] methods = iView.getClass().getMethods();
		// for (Method method : methods) {
		// if ("deliverResult".equals(method.getName())) {
		// mType = method.getGenericParameterTypes()[1];
		// return;
		// }
		// }
//		ParameterizedType parameterizedType = (ParameterizedType) iView.getClass().getGenericSuperclass();
//		if (parameterizedType != null) {
//			Type[] types = parameterizedType.getActualTypeArguments();
//			if (types != null && types.length > 0) {
//				mType = types[0];
//			}
//		}
		Type[] a = iView.getClass().getGenericInterfaces();
		// interface
		for (Type t : a) {
			if(t.toString().startsWith(ISimpleView.class.getName())){
				
			}
			Type[] types = ((ParameterizedType) t).getActualTypeArguments();
			if (types != null && types.length > 0) {
				mType = types[0];
			}
		}

		// System.out.println(dd.getActualTypeArguments()[0]);
	}
}