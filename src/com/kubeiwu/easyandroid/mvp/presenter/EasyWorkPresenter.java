package com.kubeiwu.easyandroid.mvp.presenter;

import android.os.Bundle;

import com.kubeiwu.easyandroid.easyhttp.core.retrofit.Call;
import com.kubeiwu.easyandroid.mvp.kabstract.KOKHttpPresenter;
import com.kubeiwu.easyandroid.mvp.view.ISimpleCallView;

public class EasyWorkPresenter<T> extends KOKHttpPresenter<ISimpleCallView<T>, T> {

	@Override
	public Call<T> createCall(Bundle bundle) {
		return getView().onCreateCall(getPresenterId(), bundle);
	}

	
}
