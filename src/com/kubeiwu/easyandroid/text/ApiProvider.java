package com.kubeiwu.easyandroid.text;

import com.kubeiwu.easyandroid.EasyHttpApiFactory;

public class ApiProvider {
	private static final Api API = EasyHttpApiFactory.getInstance().getApi(
			Api.class, "http://xf.qfang.com");

	public static Api getInstance() {
		return API;
	}
	
}
