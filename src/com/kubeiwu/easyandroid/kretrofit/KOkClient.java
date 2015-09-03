/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kubeiwu.easyandroid.kretrofit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okio.BufferedSink;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import android.text.TextUtils;

import com.kubeiwu.easyandroid.cache.volleycache.DiskBasedCache;
import com.kubeiwu.easyandroid.cache.volleycache.Cache.Entry;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

/** Retrofit client that uses OkHttp for communication. */
public class KOkClient implements Client {
	private static OkHttpClient generateDefaultOkHttp() {
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
		client.setReadTimeout(20 * 1000, TimeUnit.MILLISECONDS);
		return client;
	}

	private final OkHttpClient client;

	// private KOkClient() {
	// this(generateDefaultOkHttp());
	// }

	public KOkClient(DiskBasedCache kOkhttpCache, OkHttpClient client) throws IOException {
		if (client == null)
			throw new NullPointerException("client == null");
		this.client = client;
		this.kOkhttpCache = kOkhttpCache;

	}

	private final DiskBasedCache kOkhttpCache;// = new KOkhttpCache(context, 1);

	/**
	 * 请求策越
	 * 
	 * @author Administrator
	 *
	 */
	interface RequestMode {
		String LOAD_DEFAULT = "LOAD_DEFAULT";// 默认不处理
		String LOAD_NETWORK_ONLY = "LOAD_NETWORK_ONLY";// 只从网络获取
		String LOAD_NETWORK_ELSE_CACHE = "LOAD_NETWORK_ELSE_CACHE";// 先从网络获取，网络没有取本地
		String LOAD_CACHE_ELSE_NETWORK = "LOAD_CACHE_ELSE_NETWORK";// 先从本地获取，本地没有取网络
	}

	public interface CacheMode {
		String LOAD_DEFAULT = "RequestMode: " + RequestMode.LOAD_DEFAULT;
		String LOAD_NETWORK_ONLY = "RequestMode: " + RequestMode.LOAD_NETWORK_ONLY;
		String LOAD_NETWORK_ELSE_CACHE = "RequestMode: " + RequestMode.LOAD_NETWORK_ELSE_CACHE;
		String LOAD_CACHE_ELSE_NETWORK = "RequestMode: " + RequestMode.LOAD_CACHE_ELSE_NETWORK;
	}

	@Override
	public Response execute(Request request) throws IOException {

		// 请求模式只能通过herder传过来 key为RequestMode

		com.squareup.okhttp.Request okhttpRequest = createRequest(request);

		String headerValue = okhttpRequest.header("RequestMode");
		if (!TextUtils.isEmpty(headerValue)) {
			switch (headerValue) {
				case RequestMode.LOAD_NETWORK_ELSE_CACHE:// 先网络然后再缓存
					try {
						return parseResponse(client.newCall(okhttpRequest).execute());
						// response 不会为空，new出来的
					} catch (Exception e) {
						Response response = execCacheRequest(request);
						if (response != null) {
							return response;
						} else {
							return new Response(request.getUrl(), 200, "Not in the cache", new ArrayList<Header>(), null);
						}
					}
				case RequestMode.LOAD_CACHE_ELSE_NETWORK:// 先缓存再网络

					Response response = execCacheRequest(request);
					if (response != null) {
						return response;
					}
					// 如果缓存没有就跳出，执行网络请求
				case RequestMode.LOAD_DEFAULT:
				case RequestMode.LOAD_NETWORK_ONLY:
				default:
					break;// 直接跳出
			}
		}

		return parseResponse(client.newCall(okhttpRequest).execute());
	}

	private Response execCacheRequest(Request request) {
		Entry entry = kOkhttpCache.get(request.getUrl());// 充缓存中获取entry
		if (entry != null && entry.data != null) {// 如果有数据就使用缓存
			TypedInput typedInput = new TypedByteArray(entry.mimeType, entry.data);

			return new Response(request.getUrl(), 200, "cache", convertToList(entry.responseHeaders), typedInput);
		}
		return null;
	}

	private List<Header> convertToList(Map<String, String> map) {
		List<Header> headers = new ArrayList<>();
		if (map != null && !map.isEmpty()) {
			for (String key : map.keySet()) {
				headers.add(new Header(key, map.get(key)));
			}
		}
		return headers;
	}

	static com.squareup.okhttp.Request createRequest(Request request) {
		com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder().url(request.getUrl()).method(request.getMethod(), createRequestBody(request.getBody()));

		List<Header> headers = request.getHeaders();
		for (int i = 0, size = headers.size(); i < size; i++) {
			Header header = headers.get(i);
			String value = header.getValue();
			if (value == null)
				value = "";
			builder.addHeader(header.getName(), value);
		}

		return builder.build();
	}

	static Response parseResponse(com.squareup.okhttp.Response response) throws IOException {
		return new Response(response.request().urlString(), response.code(), response.message(), createHeaders(response.headers()), createResponseBody(response.body()));
	}

	private static RequestBody createRequestBody(final TypedOutput body) {
		if (body == null) {
			return null;
		}
		final MediaType mediaType = MediaType.parse(body.mimeType());
		return new RequestBody() {
			@Override
			public MediaType contentType() {
				return mediaType;
			}

			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				body.writeTo(sink.outputStream());
			}

			@Override
			public long contentLength() {
				return body.length();
			}
		};
	}

	private static TypedInput createResponseBody(final ResponseBody body) throws IOException {
		if (body.contentLength() == 0) {
			return null;
		}
		return new TypedInput() {
			@Override
			public String mimeType() {
				MediaType mediaType = body.contentType();
				return mediaType == null ? null : mediaType.toString();
			}

			@Override
			public long length() {
				try {
					return body.contentLength();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}

			@Override
			public InputStream in() throws IOException {
				return body.byteStream();
			}
		};
	}

	private static List<Header> createHeaders(Headers headers) {
		int size = headers.size();
		List<Header> headerList = new ArrayList<Header>(size);
		for (int i = 0; i < size; i++) {
			headerList.add(new Header(headers.name(i), headers.value(i)));
		}
		return headerList;
	}
}
