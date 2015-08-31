package com.kubeiwu.easyandroid.text;

import com.kubeiwu.easyandroid.retrofit.KRetrofitApiFactory;

public class ApiPrivider {
	private static final Api API = KRetrofitApiFactory.getInstance().getApi(
			Api.class, "http://xp.qfang.com");

	public static Api getInstance() {
		return API;
	}
	
}
