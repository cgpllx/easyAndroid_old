package com.kubeiwu.httphelper.text;

import com.kubeiwu.httphelper.retrofit.KRetrofitApiFactory;

public class ApiPrivider {
	private static final Api API = KRetrofitApiFactory.getInstance().getApi(
			Api.class, "http://xp.qfang.com");

	public static Api getInstance() {
		return API;
	}
	
}
