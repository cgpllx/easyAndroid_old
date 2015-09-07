package com.kubeiwu.easyandroid.mvp.presenter;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.kubeiwu.easyandroid.mvp.kabstract.KLoaderPresenterAbstract;
import com.kubeiwu.easyandroid.mvp.view.ISimpleLoaderView;

/**
 * loader线程的返回值
 * 
 * @author Administrator
 *
 * @param <T>
 * @param <D>
 */
public class KSimpleLoaderPresenter<T> extends KLoaderPresenterAbstract<ISimpleLoaderView<T>, T> {

	@Override
	public Loader<T> onCreateLoader(int arg0, Bundle bundle) {
		mController.start();
		return getView().onCreateLoader(arg0, bundle);
	}

}
