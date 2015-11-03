package com.kubeiwu.easyandroid.mvp.kabstract;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Call;
import com.kubeiwu.easyandroid.mvp.PresenterLoader;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public abstract class KThreadPresenter<V extends ISimpleView<T>, T> extends KPresenter<V, T> {
	protected Call<T> call;

	@Override
	protected void onCancel() {
		super.onCancel();
		cancelRequest();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelRequest();
	}

	private void cancelRequest() {
		 if(call!=null){
			 call.cancel();
		 }
	}

	public abstract PresenterLoader<T> onCreatPresenterLoader(Bundle bundle);

	// observable.cache() //观察者 会回调多次，但是只会调用一次网络
	public void execute(Bundle bundle) {
		cancel();// 先取消之前的事件
		PresenterLoader<T> presenterLoader = onCreatPresenterLoader(bundle);
		if (presenterLoader == null) {
			throw new IllegalArgumentException("please Override onCreatPresenterLoader method, And can not be null，");
		}
		
 
	 
	}

	public void execute() {
		execute(null);
	}
	static final String THREAD_PREFIX = "EA-";
	static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";

	static Executor defaultHttpExecutor() {
		return Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				return new Thread(new Runnable() {
					@Override
					public void run() {
						Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
						r.run();
					}
				}, IDLE_THREAD_NAME);
			}
		});
	}

	public static final Executor ioExecutor = defaultHttpExecutor();
	public static final Executor mainExecutor = new MainThreadExecutor();

	static class MainThreadExecutor implements Executor {
		private final Handler handler = new Handler(Looper.getMainLooper());
		@Override
		public void execute(Runnable r) {
			handler.post(r);
		}
	}
}
