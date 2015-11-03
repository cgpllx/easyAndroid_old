//package com.kubeiwu.easyandroid.mvp.presenter;
//
//import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadFactory;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Process;
//
//import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Call;
//import com.kubeiwu.easyandroid.mvp.PresenterLoader;
//import com.kubeiwu.easyandroid.mvp.kabstract.KPresenter;
//import com.kubeiwu.easyandroid.mvp.kabstract.KRxJavaPresenter;
//import com.kubeiwu.easyandroid.mvp.kabstract.KOKHttpPresenter.kCallback;
//import com.kubeiwu.easyandroid.mvp.view.ISimpleThreadView;
//import com.kubeiwu.easyandroid.mvp.view.ISimpleView;
//
//public class EasyThreadPresenter<ISimpleThreadView, T> extends KPresenter<V, T> {
//
//	protected Observable<T> getObservable(final Bundle bundle) {
//		return Observable.create(new Observable.OnSubscribe<T>() {
//
//			@Override
//			public void call(Subscriber<? super T> sub) {
//				if (!sub.isUnsubscribed()) {
//					PresenterLoader<T> presenterLoader = getView().onCreatPresenterLoader(getPresenterId(), bundle);
//					try {
//						T t = presenterLoader.loadInBackground();
//						sub.onNext(t);
//						sub.onCompleted();
//					} catch (Exception e) {
//						sub.onError(e);
//					}
//				}
//			}
//		}).subscribeOn(Schedulers.io())//
//				.observeOn(AndroidSchedulers.mainThread());
//	}
//
//	public abstract Call<T> createCall(Bundle bundle);
//
//	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
//	public void execute(Bundle bundle) {
//		cancel();// 先取消之前的事件
//		Call<T> originalCall = createCall(bundle);
//		if (originalCall == null) {
//			throw new IllegalArgumentException("please Override onCreatObservable method, And can not be null，");
//		}
//		call = originalCall.clone();
//
//		call.enqueue(new kCallback(mController));
//	 
//	}
//
//	public void execute() {
//		execute(null);
//	}
//
//	
//	
//	static final String THREAD_PREFIX = "EA-";
//	static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";
//
//	static Executor defaultHttpExecutor() {
//		return Executors.newCachedThreadPool(new ThreadFactory() {
//			@Override
//			public Thread newThread(final Runnable r) {
//				return new Thread(new Runnable() {
//					@Override
//					public void run() {
//						Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
//						r.run();
//					}
//				}, IDLE_THREAD_NAME);
//			}
//		});
//	}
//
//	public static final Executor cacheExecutor = defaultHttpExecutor();
//	public static final Executor cacheCallbackExecutor = new MainThreadExecutor();
//
//	static class MainThreadExecutor implements Executor {
//		private final Handler handler = new Handler(Looper.getMainLooper());
//
//		@Override
//		public void execute(Runnable r) {
//			handler.post(r);
//		}
//	}
//}