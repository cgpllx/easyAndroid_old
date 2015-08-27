package com.kubeiwu.httphelper;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.kubeiwu.httphelper.manager.cookiesmanager.PersistentCookieStore;
import com.squareup.okhttp.OkHttpClient;

public class Text {
	public static void main(String[] args) {
		OkHttpClient client = new OkHttpClient();
		client.setCookieHandler(new CookieHandler() {
			@Override
			public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

			}
			@Override
			public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
				return null;
			}
		});
		// CookieManager d = new CookieManager(store, cookiePolicy);
		CookieManager d = new CookieManager(new PersistentCookieStore(null), CookiePolicy.ACCEPT_ALL);
		client.setCookieHandler(d);
//		client.setFollowRedirects(followRedirects);
//		client.setAuthenticator(authenticator)
//		client.setRetryOnConnectionFailure(retryOnConnectionFailure);
//		client.set
	}
}
