package com.kubeiwu.easyandroid.mvp.utils;

import rx.Subscriber;

public class RxUtils {
	public static void unsubscribe(Subscriber<?> subscriber) {
		if (subscriber != null && !subscriber.isUnsubscribed()) {
			subscriber.unsubscribe();
		}
	}
}
