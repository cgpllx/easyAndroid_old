package com.kubeiwu.easyandroid.text;

import com.kubeiwu.easyandroid.easycore.converter.KRetrofitApiFactory;

public class ApiProvider {
	private static final Api API = KRetrofitApiFactory.getInstance().getApi(
			Api.class, "http://xf.qfang.com");

	public static Api getInstance() {
		return API;
	}
	
}
