package retrofit;

import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import com.kubeiwu.easyandroid.kretrofit.KResult;
import com.kubeiwu.easyandroid.mvp.exception.MvpException;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

/**
 * Utilities for supporting RxJava Observables.
 * <p>
 * RxJava might not be on the available to use. Check
 * {@link Platform#HAS_RX_JAVA} before calling.
 */
final class KRxSupport {
	/** A callback into {@link RestAdapter} to actually invoke the request. */
	interface Invoker {
		/**
		 * Invoke the request. The interceptor will be "tape" from the time of
		 * subscription.
		 */
		ResponseWrapper invoke(RequestInterceptor requestInterceptor);
	}

	private final Executor executor;
	private final ErrorHandler errorHandler;
	private final RequestInterceptor requestInterceptor;

	KRxSupport(Executor executor, ErrorHandler errorHandler, RequestInterceptor requestInterceptor) {
		this.executor = executor;
		this.errorHandler = errorHandler;
		this.requestInterceptor = requestInterceptor;
	}

	Observable<? super Object> createRequestObservable(final Invoker invoker) {
		return Observable.create(new Observable.OnSubscribe<Object>() {
			@Override
			public void call(Subscriber<? super Object> subscriber) {
				RequestInterceptorTape interceptorTape = new RequestInterceptorTape();
				requestInterceptor.intercept(interceptorTape);

				Runnable runnable = getRunnable(subscriber, invoker, interceptorTape);
				FutureTask<Void> task = new FutureTask<Void>(runnable, null);

				// Subscribe to the future task of the network call allowing
				// unsubscription.
				subscriber.add(Subscriptions.from(task));
				executor.execute(task);
			}
		});
	}

	private Runnable getRunnable(final Subscriber<? super Object> subscriber, final Invoker invoker, final RequestInterceptorTape interceptorTape) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					if (subscriber.isUnsubscribed()) {
						return;
					}
					ResponseWrapper wrapper = invoker.invoke(interceptorTape);
					Object object = wrapper.responseBody;
					if (object instanceof KResult) {
						KResult kResult = (KResult) object;
						if (kResult != null && kResult.isSuccess()) {
							subscriber.onNext(wrapper.responseBody);
							subscriber.onCompleted();
						} else {
							subscriber.onError(new MvpException(kResult != null ? kResult.getFailureDesc() : "服务器或网络异常"));
						}
					} else {
						subscriber.onNext(wrapper.responseBody);
						subscriber.onCompleted();
					}
				} catch (RetrofitError e) {
					subscriber.onError(errorHandler.handleError(e));
				}
			}
		};
	}
}