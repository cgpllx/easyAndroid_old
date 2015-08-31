package com.kubeiwu.easyandroid.mvp.presenter;

import com.kubeiwu.commontool.khttp.Response;
import com.kubeiwu.commontool.khttp.exception.VolleyError;
import com.kubeiwu.easyandroid.mvp.kabstract.KPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public class KSimpleVolleyPresenter<T> extends KPresenter<ISimpleView<T>, T> {

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
