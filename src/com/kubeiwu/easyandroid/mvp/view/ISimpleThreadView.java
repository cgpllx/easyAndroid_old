package com.kubeiwu.easyandroid.mvp.view;

import com.kubeiwu.easyandroid.mvp.PresenterLoader;

public interface ISimpleThreadView<T> extends ISimpleView<T> {

	PresenterLoader<T> onCreatPresenterLoader(int loaderId);
}
