package com.kubeiwu.easyandroid.mvp.presenter;

import rx.Observable;
import android.os.Bundle;

import com.kubeiwu.easyandroid.mvp.kabstract.KRxJavaPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleNetWorkView;

public class EasyRxWorkPresenter<T> extends KRxJavaPresenter<ISimpleNetWorkView<T>, T> {

	@Override
	public Observable<T> creatObservable(Bundle bundle) {
		return getView().onCreatObservable(getPresenterId(), bundle);
	}
	
}
