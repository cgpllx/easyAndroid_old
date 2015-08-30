package com.kubeiwu.httphelper.mvp.view;

import com.kubeiwu.httphelper.mvp.PresenterLoader;

public interface ISimpleThreadView<T> extends ISimpleView<T> {

	PresenterLoader<T> onCreatPresenterLoader(int loaderId);
}
