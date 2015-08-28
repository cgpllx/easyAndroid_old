package com.kubeiwu.httphelper.mvp.view;

import android.os.Bundle;
import android.support.v4.content.Loader;


public interface ISimpleLoaderView<M, T> extends ISimpleView<T> {

	public Loader<M> onCreateLoader(int arg0, Bundle bundle);

}
