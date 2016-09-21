package com.spider.bean;

import org.apache.commons.lang.StringUtils;

public class ResultCode<T> {

	private String code; // 错误编码
	private String msg; // 文本信息
	private T data; // 数据
	private Exception e; // 抛出错误

	public ResultCode() {
	}

	public ResultCode(String code, String msg) {
		this(code, msg, null);
	}

	public ResultCode(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public boolean isSucces() {
		return StringUtils.equals(code, StatusContent.CODE_SUCCESS);
	}

}
