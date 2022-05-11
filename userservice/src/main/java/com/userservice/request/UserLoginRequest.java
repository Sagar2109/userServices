package com.userservice.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserLoginRequest {
	
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email Not In Format")
	@Pattern(regexp = "^(?!\\s*$).+")
	private String email;
	
	@NotBlank(message = "Password is mandatory")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	

}
