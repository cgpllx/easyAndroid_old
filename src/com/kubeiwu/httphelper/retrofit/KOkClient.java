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
package com.kubeiwu.httphelper.retrofit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.BufferedSink;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.kubeiwu.httphelper.cache.KOkhttpCache;
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

	public KOkClient(KOkhttpCache kOkhttpCache, OkHttpClient client) throws IOException {
		if (client == null)
			throw new NullPointerException("client == null");
		this.client = client;
		this.kOkhttpCache = kOkhttpCache;

	}

	private final KOkhttpCache kOkhttpCache;// = new KOkhttpCache(context, 1);

	/**
	 * 请求策越
	 * 
	 * @author Administrator
	 *
	 */
	public interface RequestMode {
		String LOAD_DEFAULT = "LOAD_DEFAULT";// 默认不处理
		String LOAD_NETWORK_ONLY = "LOAD_NETWORK_ONLY";// 只从网络获取
		String LOAD_NETWORK_ELSE_CACHE = "LOAD_NETWORK_ELSE_CACHE";// 先从网络获取，网络没有取本地
		String LOAD_CACHE_ELSE_NETWORK = "LOAD_CACHE_ELSE_NETWORK";// 先从本地获取，本地没有取网络
	}

	@Override
	public Response execute(Request request) throws IOException {

		// com.squareup.okhttp.Response t = client.newCall(createRequest(request)).execute();

		// 请求模式只能通过herder传过来 又因为header中重写了equals方法，只要比较key和value就可以了
		// List<Header> lists=request.getHeaders();
		com.squareup.okhttp.Request okhttpRequest = createRequest(request);

		String headerValue = okhttpRequest.header("RequestMode");
		if (!TextUtils.isEmpty(headerValue)) {
			switch (headerValue) {
				case RequestMode.LOAD_NETWORK_ELSE_CACHE:
					Response response = null;
					try {
						response = parseResponse(client.newCall(okhttpRequest).execute());
						// response 不会为空，new出来的
					} catch (Exception e) {
						e.printStackTrace();
						TypedInput typedInput = kOkhttpCache.getAsTypedInput(request.getUrl());
						// -----------------------------------------------------这里应该是响应headers
						response = new Response(request.getUrl(), 200, "cache", request.getHeaders(), typedInput);
					}
					System.out.println("--网络--缓存");
					return response;
				case RequestMode.LOAD_CACHE_ELSE_NETWORK:
					byte[] bytes = kOkhttpCache.getAsBytes(request.getUrl());
					if (bytes != null) {
						TypedInput typedInput = new TypedByteArray("application/json;charset=UTF-8", kOkhttpCache.getAsBytes(request.getUrl()));
						return new Response(request.getUrl(), 200, "cache", request.getHeaders(), typedInput);
					} else {
						return parseResponse(client.newCall(createRequest(request)).execute());
					}
				case RequestMode.LOAD_DEFAULT:
				case RequestMode.LOAD_NETWORK_ONLY:
				default:
					return parseResponse(client.newCall(okhttpRequest).execute());
			}
		}

		return parseResponse(client.newCall(createRequest(request)).execute());
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
