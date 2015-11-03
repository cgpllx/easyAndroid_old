package com.kubeiwu.easyandroid.mvp.presenter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.kubeiwu.easyandroid.mvp.PresenterLoader;
import com.kubeiwu.easyandroid.mvp.kabstract.KPresenter;
import com.kubeiwu.easyandroid.mvp.utils.EAThreadRunnable;
import com.kubeiwu.easyandroid.mvp.view.ISimpleThreadView;

public class KThreadPresenter<T> extends KPresenter<ISimpleThreadView<T>, T> {
	protected EAThreadRunnable<T> eaThreadRunnable;

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
		if (eaThreadRunnable != null) {
			eaThreadRunnable.cancel();
		}
	}

	public void execute(final Bundle bundle) {
		cancel();// 先取消之前的事件

		eaThreadRunnable = new EAThreadRunnable<T>(mController, mainExecutor) {
			@Override
			public PresenterLoader<T> creatPresenterLoader() {
				return getView().onCreatPresenterLoader(getPresenterId(), bundle);
			}
		};
		ioExecutor.execute(eaThreadRunnable);
	}

	public void execute() {
		execute(null);
	}

	static final String THREAD_PREFIX = "EA-";
	static final String IDLE_THREAD_NAME = THREAD_PREFIX + "Idle";

	static Executor ioExecutor() {
		return Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				return new Thread(new Runnable() {
					@Override
					public void run() {
						Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
						r.run();
					}
				}, IDLE_THREAD_NAME);
			}
		});
	}

	public static final Executor ioExecutor = ioExecutor();
	public static final Executor mainExecutor = new MainThreadExecutor();

	static class MainThreadExecutor implements Executor {
		private final Handler handler = new Handler(Looper.getMainLooper());

		@Override
		public void execute(Runnable r) {
			handler.post(r);
		}
	}
}
