package com.kubeiwu.easyandroid.mvp.presenter;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.kubeiwu.easyandroid.mvp.kabstract.KLoaderPresenterAbstract;
import com.kubeiwu.easyandroid.mvp.view.ISimpleLoaderView;
import com.kubeiwu.easyandroid.mvp.presenter.KSimpleLoaderPresenter.View;;

/**
 * loader线程的返回值
 * 
 * @author Administrator
 *
 * @param <T>
 * @param <D>
 */
public class KSimpleLoaderPresenter<T> extends KLoaderPresenterAbstract<View<T>, T> {
	@Override
	public void setView(View<T> view) {
		super.setView(view);

	}

	@Override
	public Loader<T> onCreateLoader(int arg0, Bundle bundle) {
		mController.showLoading();
		return iView.onCreateLoader(arg0, bundle);
	}

	public interface View<D> extends ISimpleLoaderView<D> {

	}
}
