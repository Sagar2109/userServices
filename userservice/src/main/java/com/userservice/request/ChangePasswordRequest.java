package com.userservice.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class ChangePasswordRequest {
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email Not In Format")
	@Pattern(regexp = "^(?!\\s*$).+")
	private String email;

	@NotBlank(message = "Old Password is mandatory")
	@Length(min = 5, message = "Password must be of Minimum 5 character")
	private String oldPassword;

	@NotBlank(message = "New Password is mandatory")
	@Length(min = 5, message = "Password must be of Minimum 5 character")
	private String newPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
