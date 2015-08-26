package com.kubeiwu.DisLrucache;

/**
 * 用来解析登录的返回结果
 * 
 * @author Administrator
 *
 * @param <T>
 * 
 */
public class JsonResult {

	private String code;
	private String desc;

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
