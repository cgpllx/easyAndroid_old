package com.kubeiwu.httphelper.mvp.kabstract;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.kubeiwu.httphelper.mvp.view.ISimpleLoaderView;

public abstract class KLoaderPresenterAbstract<M, T> extends KPresenter<ISimpleLoaderView<M, T>> implements LoaderCallbacks<M> {

	public void loadData(LoaderManager loaderManager, Bundle bundle) {
		loaderManager.initLoader(presenterId, bundle, this);
	}

	public void restartLoadData(LoaderManager loaderManager, Bundle bundle) {
		loaderManager.restartLoader(presenterId, bundle, this);
	}

	@Override
	public Loader<M> onCreateLoader(int arg0, Bundle bundle) {
		iView.showLoading(presenterId);
		return iView.onCreateLoader(arg0, bundle);
	}

	@Override
	public void onLoadFinished(Loader<M> arg0, M arg1) {
		deliverResult(arg1);
		iView.hideLoading(presenterId);
	}

	protected abstract void deliverResult(M arg1);

	@Override
	public void onLoaderReset(Loader<M> arg0) {

	}

	@Override
	public void initialize() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void destroy() {
	}

	public void destroyData(LoaderManager loaderManager) {
		loaderManager.destroyLoader(presenterId);
	}
}
