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
package com.kubeiwu.httphelper.retrofit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kubeiwu.httphelper.cache.volley.Cache.Entry;
import com.kubeiwu.httphelper.cache.volley.DiskBasedCache;

/**
 * A {@link Converter} which uses GSON for serialization and deserialization of
 * entities.
 *
 * @author Jake Wharton (jw@squareup.com)
 */
public class KGsonConverter implements Converter {
	private final Gson gson;
	private final DiskBasedCache kOkhttpCache;
	private String charset;

	/**
	 * Create an instance using the supplied {@link Gson} object for conversion.
	 * Encoding to JSON and decoding from JSON (when no charset is specified by
	 * a header) will use UTF-8.
	 */
	public KGsonConverter(Gson gson, DiskBasedCache kOkhttpCache) {
		this(gson, kOkhttpCache, "UTF-8");
	}

	/**
	 * Create an instance using the supplied {@link Gson} object for conversion.
	 * Encoding to JSON and decoding from JSON (when no charset is specified by
	 * a header) will use the specified charset.
	 */
	public KGsonConverter(Gson gson, DiskBasedCache kOkhttpCache, String charset) {
		this.gson = gson;
		this.charset = charset;
		this.kOkhttpCache = kOkhttpCache;
	}

	@Override
	public Object fromBody(TypedInput body, Type type)
			throws ConversionException {
		String charset = this.charset;
		if (body.mimeType() != null) {
			charset = MimeUtil.parseCharset(body.mimeType(), charset);
		}
		InputStreamReader isr = null;

		try {
			isr = new InputStreamReader(body.in(), charset);
			KResult j = gson.fromJson(isr, type);
			if (j.isSuccess()) {
				System.out.println("成功");
				// kOkhttpCache.put(url, body);
				// 这里开始缓存
			}
			return j;
		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (JsonParseException e) {
			throw new ConversionException(e);
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public Object fromBody1(String url, TypedInput body, Type type)
			throws ConversionException {
		String charset = this.charset;
		if (body.mimeType() != null) {
			charset = MimeUtil.parseCharset(body.mimeType(), charset);
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(body.in(), charset);
			KResult result = gson.fromJson(isr, type);
			if (result != null && result.isSuccess()) {// 保存
				System.out.println("保存开始");
				Entry entry = new Entry();
				entry.data = gson.toJson(result).getBytes(charset);
				// entry.responseHeaders = null;
				entry.mimeType = body.mimeType();
				kOkhttpCache.put(url, entry);
				System.out.println("完成");
			}
			return result;
		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (JsonParseException e) {
			throw new ConversionException(e);
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	public Object fromBody(Response response, Type type)
			throws ConversionException {
		String charset = this.charset;
		 
		TypedInput body = response.getBody();
		String url = response.getUrl();
		List<Header> headers = response.getHeaders();
		
		Map<String, String> headerMap = convertToMap(headers);

		if (body.mimeType() != null) {
			charset = MimeUtil.parseCharset(body.mimeType(), charset);
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(body.in(), charset);
			KResult result = null;
			try {
				result = gson.fromJson(isr, type);
			} catch (JsonParseException e) {
				e.printStackTrace();
				//这里可以执行没有登陆的判断
			}
			
			if (result != null && result.isSuccess()) {// 保存
				Entry entry = new Entry();
				entry.data = gson.toJson(result).getBytes(charset);
				entry.responseHeaders = headerMap;
				entry.mimeType = body.mimeType();
				kOkhttpCache.put(url, entry);
			}
			
			return result;
		} catch (IOException e) {
			throw new ConversionException(e);
		} catch (JsonParseException e) {
			throw new ConversionException(e);
		} finally {
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	private Map<String, String> convertToMap(List<Header> headers) {

		if (headers != null && headers.size() > 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			for (Header header : headers) {
				map.put(header.getName(), header.getValue());
			}
			return map;
		}
		return Collections.emptyMap();
	}

	@Override
	public TypedOutput toBody(Object object) {
		try {
			return new JsonTypedOutput(gson.toJson(object).getBytes(charset),
					charset);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}

	private static class JsonTypedOutput implements TypedOutput {
		private final byte[] jsonBytes;
		private final String mimeType;

		JsonTypedOutput(byte[] jsonBytes, String encode) {
			this.jsonBytes = jsonBytes;
			this.mimeType = "application/json; charset=" + encode;
		}

		@Override
		public String fileName() {
			return null;
		}

		@Override
		public String mimeType() {
			return mimeType;
		}

		@Override
		public long length() {
			return jsonBytes.length;
		}

		@Override
		public void writeTo(OutputStream out) throws IOException {
			out.write(jsonBytes);
		}
	}
}
