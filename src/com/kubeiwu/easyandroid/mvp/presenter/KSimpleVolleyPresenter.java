package com.kubeiwu.easyandroid.mvp.presenter;

import com.kubeiwu.easyandroid.mvp.kabstract.KPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleView;

public class KSimpleVolleyPresenter<T> extends KPresenter<ISimpleView<T>, T>{

	@Override
	protected void onDeliverResult(T results) {
		
	}

}
