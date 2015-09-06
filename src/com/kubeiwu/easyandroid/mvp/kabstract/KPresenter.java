package com.kubeiwu.easyandroid.mvp.kabstract;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KPresenter<V extends ISimpleView<T>, T> implements Presenter<V> {
	// private V iView;
	private WeakReference<V> viewRef;
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

	public void attachView(V view) {
		this.viewRef = new WeakReference<V>(view);
		initDeliverResultType(view);
	}

	protected boolean isViewAttached() {
		return viewRef != null && viewRef.get() != null;
	}

	public V getView() {
		return viewRef == null ? null : viewRef.get();
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
		detachView();
	}

	public void detachView() {
		if (viewRef != null) {
			viewRef.clear();
			viewRef = null;
		}
	}

	private void onShowLoading() {
		if (isViewAttached())
			getView().showLoading(presenterId);

	}

	private void onHideLoading() {
		if (isViewAttached())
			getView().hideLoading(presenterId);
	}

	private void onHandleError(String errorDesc) {
		if (isViewAttached())
			getView().handleError(presenterId, errorDesc);
	}

	private void onDeliverResult(final T results) {
		if (isViewAttached())
			getView().deliverResult(presenterId, results);
	}

	private Type mType;

	public Type getDeliverResultType() {
		return mType;
	}

	public void setDeliverResultType(Type type) {
		this.mType = type;
	}

	private final int presenterId = hashCode();

	@Override
	public int getPresenterId() {
		return presenterId;
	}

	private void initDeliverResultType(V iView) {
		if (this.mType != null) {
			return;
		}
		Type[] interfacesTypes = iView.getClass().getGenericInterfaces();// 获取接口类型
		for (Type t : interfacesTypes) {
			if (t instanceof ParameterizedType) {// 判断接口的类型是否是ParameterizedType类型，因为只有泛型的接口才是ParameterizedType的类型
				ParameterizedType parameterizedType = (ParameterizedType) t;// 泛型类型都放在ParameterizedType中
				Type rawType = parameterizedType.getRawType();// 获取泛型真实类型
				if (rawType instanceof Class) {
					Class<?> clazz = (Class<?>) rawType;
					if (ISimpleView.class.isAssignableFrom(clazz)) {// class是否是ISimpleView.class的子类
						Type[] types = parameterizedType.getActualTypeArguments();// 获取当前接口所有的泛型类型
						if (types != null && types.length > 0) {
							Type guessType = types[0];// 取第一个
							// 这里还可以进行其他判断，这个guessType有可能是一个泛型
							this.mType = guessType;// 取到结果直接返回，
							return;
						}
					}
				}
			}
		}

	}
}