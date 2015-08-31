package com.kubeiwu.easyandroid.text;

import com.kubeiwu.easyandroid.retrofit.KResult;

/**
 * 用来解析登录的返回结果
 * 
 * @author Administrator
 * @param <T>
 *
 * @param <T>
 * 
 */
public class JsonResult<T> implements KResult {

	private String code;
	private String desc;
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return "C0000".equals(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
