package com.userservice.util;

import org.springframework.validation.BindingResult;

public class Utils {

	public static String getFieldError(BindingResult bindingResult) {
		return bindingResult.getFieldError().getField() + " : " + bindingResult.getFieldError().getDefaultMessage();
	}
}
