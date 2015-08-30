package com.kubeiwu.httphelper.mvp.kabstract;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.kubeiwu.httphelper.mvp.view.ISimpleLoaderView;

public abstract class KLoaderPresenterAbstract<T> extends KPresenter<ISimpleLoaderView<T>,T> implements LoaderCallbacks<T> {

	public void loadData(LoaderManager loaderManager, Bundle bundle) {
		loaderManager.initLoader(presenterId, bundle, this);
	}

	public void restartLoadData(LoaderManager loaderManager, Bundle bundle) {
		loaderManager.restartLoader(presenterId, bundle, this);
	}

	@Override
	public Loader<T> onCreateLoader(int arg0, Bundle bundle) {
		iView.showLoading(presenterId);
		return iView.onCreateLoader(arg0, bundle);
	}

	@Override
	public void onLoadFinished(Loader<T> arg0, T arg1) {
		deliverResult(arg1);
		iView.hideLoading(presenterId);
	}

	protected abstract void deliverResult(T arg1);

	@Override
	public void onLoaderReset(Loader<T> arg0) {

	}

	public void destroyData(LoaderManager loaderManager) {
		loaderManager.destroyLoader(presenterId);
	}

}
