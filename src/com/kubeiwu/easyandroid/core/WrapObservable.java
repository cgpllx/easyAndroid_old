package com.kubeiwu.easyandroid.core;

import java.util.concurrent.Future;

import retrofit.Call;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * 被观察者，可以取消后台任务的执行
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class WrapObservable<T> {

	private Future<T> future;
	private Call<T> call;
	private Observable<T> observable;

	public void setCall(Call<T> call) {
		this.call = call;
	}

	public WrapObservable(Observable<T> observable) {
		this.observable = observable;
	}

	public void setFuture(Future<T> future) {
		this.future = future;
	}

	public Observable<T> getObservable() {
		return observable;
	}

	public void setObservable(Observable<T> observable) {
		this.observable = observable;
	}

	public void cancel() {
		if (future != null) {
			future.cancel(true);
		}
		if (call != null) {
			call.cancel();
		}
	}

	public Subscription subscribe(Observer<? super T> observer) {
		return observable.subscribe(observer);
	}

}
