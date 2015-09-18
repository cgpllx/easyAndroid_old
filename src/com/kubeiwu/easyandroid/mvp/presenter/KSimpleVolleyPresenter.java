package com.kubeiwu.easyandroid.mvp.presenter;

import java.util.Map;

import com.kubeiwu.commontool.khttp.KRequestQueueManager;
import com.kubeiwu.commontool.khttp.Request.Method;
import com.kubeiwu.commontool.khttp.Request.RequestMode;
import com.kubeiwu.commontool.khttp.Response;
import com.kubeiwu.commontool.khttp.exception.VolleyError;
import com.kubeiwu.easyandroid.core.KResult;
import com.kubeiwu.easyandroid.kvolley.KGsonRequest;
import com.kubeiwu.easyandroid.mvp.kabstract.KPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public class KSimpleVolleyPresenter<T extends KResult> extends KPresenter<ISimpleView<T>, T> {
	public final String TAG = hashCode() + "";
	private KGsonRequest<T> gsonRequest;
	private int mRequestMode = RequestMode.LOAD_NETWORK_ONLY;

	@Override
	public void attachView(ISimpleView<T> view) {
		super.attachView(view);
		checkTypeCorrectness();// 只有KSimpleVolleyPresenter 需要检查类型
	}

	public synchronized void loadData(int method, String url, Map<String, String> headers, Map<String, String> params) {
		cancel();
		mController.start();
		gsonRequest = new KGsonRequest<T>(method, url, headers, params, listener, errorListener);
		gsonRequest.setShouldAddCookiesToRequest(true);
		gsonRequest.setCache_Duration(1000 * 60 * 60 * 24 * 300l);// 300天
		gsonRequest.setResponseType(getDeliverResultType());
		gsonRequest.setRequestMode(mRequestMode);// 请求缓存策越
		gsonRequest.setTag(TAG);
		KRequestQueueManager.getRequestQueue().add(gsonRequest);
	}

	public synchronized void loadData(int method, String url) {
		loadData(method, url, null, null);
	}

	public synchronized void loadData(String url) {
		loadData(Method.GET, url, null, null);
	}

	@Override
	protected void onCancel() {
		if (gsonRequest != null && !gsonRequest.isCanceled()) {
			gsonRequest.cancel();
		}
	}

	public synchronized void setRequestMode(int requestMode) {
		mRequestMode = requestMode;
	}

	@Override
	protected void onDestroy() {
		cancel();
	}

	/**
	 * 正确监听
	 */
	Response.Listener<T> listener = new Response.Listener<T>() {
		@Override
		public void onResponse(T t) {
			finishLoad();
			mController.deliverResult(t);
		}
	};

	private void finishLoad() {
		mController.completed();
	}

	/**
	 * 错误监听
	 */
	Response.ErrorListener errorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			mController.error("服务器或网络异常");
			finishLoad();
		}

	};

	public void checkTypeCorrectness() {
		if (getDeliverResultType() == null) {
			throw new IllegalArgumentException("请设置type 类型");
		}
	}
}
