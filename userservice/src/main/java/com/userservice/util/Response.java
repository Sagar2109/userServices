package com.userservice.util;

import java.util.HashMap;
import java.util.Map;

public class Response {

	public static Map<String, Object> data(Integer code, String status, String mess, Object data) {

		Map<String, Object> response = new HashMap<>();

		response.put("code", code);
		response.put("status", status);
		response.put("message", mess);

		if (data != null)
			response.put("data", data);

		return response;

	}
}
