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
import java.io.Writer;

import okio.Buffer;
import retrofit.Converter;
import retrofit.Result;

import com.google.gson.TypeAdapter;
import com.kubeiwu.easyandroid.cache.volleycache.Cache;
import com.kubeiwu.easyandroid.cache.volleycache.Cache.Entry;
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

	KGsonConverter(TypeAdapter<T> adapter, Cache cache) {
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
			System.out.println("request.cacheControl()=="+request.cacheControl());
			System.out.println("request.maxAgeSeconds()=="+request.cacheControl().maxAgeSeconds());
			System.out.println("request.maxStaleSeconds()=="+request.cacheControl().maxStaleSeconds());
			System.out.println("request.minFreshSeconds()=="+request.cacheControl().minFreshSeconds());
			System.out.println("request.sMaxAgeSeconds()=="+request.cacheControl().sMaxAgeSeconds());
			System.out.println("request.isPrivate()=="+request.cacheControl().isPrivate());
			System.out.println("request.isPublic()=="+request.cacheControl().isPublic());
			System.out.println("request.mustRevalidate()=="+request.cacheControl().mustRevalidate());
			System.out.println("request.noCache()=="+request.cacheControl().noCache());
			System.out.println("request.noStore()=="+request.cacheControl().noStore());
			System.out.println("request.noTransform()=="+request.cacheControl().noTransform());
			System.out.println("request.onlyIfCached()=="+request.cacheControl().onlyIfCached());
 
//			if(){
//				
//			}
			
			if (t instanceof KResult) {
				System.out.println("11111111111");
				KResult kResult = (KResult) t;
				if (kResult != null && kResult.isSuccess()) {
					System.out.println("22222222222222");
					Entry entry = new Entry();
//					request.cacheControl().
					System.out.println("request.cacheControl()=="+request.cacheControl());

					entry.data = string.getBytes(UTF8);
					entry.mimeType = value.contentType().toString();
					cache.put(request.urlString(), entry);
				}
			}
			return t;
		} finally {
			closeQuietly(reader);
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
