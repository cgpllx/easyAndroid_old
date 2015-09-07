package com.kubeiwu.easyandroid.mvp.utils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.kubeiwu.commontool.khttp.KRequestQueueManager;
import com.kubeiwu.commontool.khttp.Request.Method;
import com.kubeiwu.commontool.khttp.Request.RequestMode;
import com.kubeiwu.commontool.khttp.toolbox.notused.RequestFuture;
import com.kubeiwu.easyandroid.kretrofit.KResult;
import com.kubeiwu.easyandroid.kvolley.KGsonRequest;
import com.kubeiwu.easyandroid.mvp.exception.MvpException;

public class HttpUtils {

	public static <T> Observable<T> futureToObservable(final Future<T> future) {
		Observable<T> observable = Observable.from(future, Schedulers.io());
		return observable;
	}

	public static <T extends KResult> RequestFuture<T> voleyToFuture(int method, String url, Map<String, String> headers, Map<String, String> params, Type type, int requestMode) {
		RequestFuture<T> future = RequestFuture.newFuture();
		KGsonRequest<T> gsonRequest = new KGsonRequest<T>(method, url, headers, params, future, future);
		gsonRequest.setShouldAddCookiesToRequest(true);
		gsonRequest.setCache_Duration(1000 * 60 * 60 * 24 * 300l);// 300天
		gsonRequest.setResponseType(type);
		gsonRequest.setRequestMode(requestMode);// 请求缓存策越
		KRequestQueueManager.getRequestQueue().add(gsonRequest);
		return future;
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(final int method, final String url, final Map<String, String> headers, final Map<String, String> params, final Type type, final int requestMode) {
		Observable<T> observable = Observable.create(new OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				Future<T> future = voleyToFuture(method, url, headers, params, type, requestMode);
				try {
					T t = future.get(20, TimeUnit.SECONDS);
					if (t != null && t.isSuccess()) {
						subscriber.onNext(t);
						subscriber.onCompleted();
					} else {
						subscriber.onError(new MvpException(t != null ? t.getFailureDesc() : "服务器或网络异常"));
					}
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					e.printStackTrace();
					subscriber.onError(new MvpException("服务器或网络异常"));
				}
			}
		});

		return observable;
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(String url, Map<String, String> params, Type type, int requestMode) {
		return executeHttpRequestToObservable(Method.POST, url, null, params, type, requestMode);
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(String url, Type type, int requestMode) {
		return executeHttpRequestToObservable(Method.POST, url, null, null, type, requestMode);
	}

	public static <T extends KResult> Observable<T> executeHttpRequestToObservable(String url, Type type) {
		return executeHttpRequestToObservable(Method.GET, url, null, null, type, RequestMode.LOAD_NETWORK_ONLY);
	}

}
