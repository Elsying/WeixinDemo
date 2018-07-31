package com.yuansheng.resultful.core.common;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class JsonResult implements Serializable{


	private static final long serialVersionUID = 4582860114716259879L;
	private int code;
	private String message;

	public JsonResult() {
	}

	public static JsonResult getInstance(int code, String message) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.code = code;
		jsonResult.message = message;
		return jsonResult;
	}

	public static JsonResult getJsonResult() {
		JsonResult jsonResult = new JsonResult();
		jsonResult.code = 0;
		return jsonResult;
	}

	@Override
	public String toString() {
		return toJsonString();
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}

	public void setCodeAndMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	
}
