/*
 * Copyright (C) 2012 Square, Inc.
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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import okio.Buffer;
import retrofit.Converter;

import com.google.gson.TypeAdapter;
import com.kubeiwu.easyandroid.cache.volleycache.Cache;
import com.kubeiwu.easyandroid.core.KResult;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;

public final class KGsonConverter<T> implements Converter<T> {
	private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
	public static final String UTF8 = "UTF-8";
	private final TypeAdapter<T> typeAdapter;
	private final Cache cache;

	public KGsonConverter(TypeAdapter<T> adapter, Cache cache) {
		this.typeAdapter = adapter;
		this.cache = cache;
	}

	public Cache getCache() {
		return cache;
	}

	@Override
	public T fromBody(ResponseBody body) throws IOException {
		Reader reader = body.charStream();
		try {
			return typeAdapter.fromJson(reader);
		} finally {
			closeQuietly(reader);
		}
	}

	public T fromBody(ResponseBody value, Request request) throws IOException {
		String string = value.string();
		System.out.println("网络请求到的字符串:" + string);
		Reader reader = new InputStreamReader((new ByteArrayInputStream(string.getBytes(UTF8))), Util.UTF_8);
		try {
			T t = typeAdapter.fromJson(reader);
			System.out.println("转换的最终对象：" + t);
			String mimeType = value.contentType().toString();
			parseCache(request, t, string, mimeType);
			return t;
		} finally {
			closeQuietly(reader);
		}
	}

	private void parseCache(Request request, T object, String string, String mimeType) throws UnsupportedEncodingException {
		com.squareup.okhttp.CacheControl cacheControl = request.cacheControl();
		if (cacheControl != null) {
			if (!cacheControl.noCache() && !cacheControl.noStore()) {
				if (object instanceof KResult) {
					KResult kResult = (KResult) object;
					if (kResult != null && kResult.isSuccess()) {
						long now = System.currentTimeMillis();
						long maxAge = cacheControl.maxAgeSeconds();
						long softExpire = now + maxAge * 1000;
						System.out.println("缓存时长:" + (softExpire - now) / 1000 + "秒");
						Cache.Entry entry = new Cache.Entry();
						entry.softTtl = softExpire;
						entry.ttl = entry.softTtl;
						// entry.serverDate = serverDate;
						// entry.responseHeaders = headers;
						entry.mimeType = mimeType;
						System.out.println("request.cacheControl()==" + request.cacheControl());
						entry.data = string.getBytes(UTF8);
						cache.put(request.urlString(), entry);
					}
				}
			}
		}
	}

	static void closeQuietly(Closeable closeable) {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (IOException ignored) {
		}
	}

	@Override
	public RequestBody toBody(T value) {
		Buffer buffer = new Buffer();
		Writer writer = new OutputStreamWriter(buffer.outputStream(), Util.UTF_8);
		try {
			typeAdapter.toJson(writer, value);
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e); // Writing to Buffer does no I/O.
		}
		return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
	}
}
