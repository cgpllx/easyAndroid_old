package com.kubeiwu.easyandroid.mvp.presenter;

import java.util.Map;

import com.kubeiwu.commontool.khttp.KRequestQueueManager;
import com.kubeiwu.commontool.khttp.Request.Method;
import com.kubeiwu.commontool.khttp.Response;
import com.kubeiwu.commontool.khttp.exception.VolleyError;
import com.kubeiwu.commontool.khttp.krequestimpl.KGsonRequest;
import com.kubeiwu.easyandroid.mvp.kabstract.KPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public class KSimpleVolleyPresenter<T> extends KPresenter<ISimpleView<T>, T> {
	public static final String TAG = KSimpleVolleyPresenter.class.getSimpleName();
	KGsonRequest<T> gsonRequest;

	public synchronized void loadData(int method, String url, Map<String, String> headers, Map<String, String> params) {
		mController.showLoading();
		gsonRequest = new KGsonRequest<T>(method, url, headers, params, listener, errorListener);
		// gsonRequest.setShouldAddCookiesToRequest(true);
		gsonRequest.setCache_Duration(1000 * 60 * 60 * 24 * 300l);// 300天
//		 gsonRequest.setResponseType(mType);
		// gsonRequest.setRequestMode(mRequestMode);// 请求缓存策越
		// gsonRequest.setRequestMode(RequestMode.LOAD_CACHE_ELSE_NETWORK);
		gsonRequest.setTag(TAG);
		KRequestQueueManager.getRequestQueue().add(gsonRequest);
	}

	public synchronized void loadData(int method, String url) {
		loadData(method, url, null, null);
	}

	public synchronized void loadData(String url) {
		loadData(Method.GET, url, null, null);
	}

	protected void onCancel() {
		if (gsonRequest != null && !gsonRequest.isCanceled()) {
			gsonRequest.cancel();
		}
	}
	protected void onDestroy() {
		cancel();
	}

	/**
	 * 正确监听
	 */
	Response.Listener<T> listener = new Response.Listener<T>() {
		@Override
		public void onResponse(T t) {
			mController.deliverResult(t);
			finishLoad();
		}
	};

	private void finishLoad() {
		mController.hideLoading();
	}

	/**
	 * 错误监听
	 */
	Response.ErrorListener errorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			mController.handleError("服务器或网络异常");
			finishLoad();
		}

	};
}
