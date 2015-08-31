package com.kubeiwu.easyandroid.mvp.view;

import rx.Observable;

public interface ISimpleNetWorkView<T> extends ISimpleView<T> {
	Observable<T> onCreatObservable();
}
