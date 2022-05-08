package com.userservice.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class UserDTO {

	private String id;
	@NotBlank
	private String name;
	@NotBlank(message = "Email is mandatory")
//	@Schema(title = "Email")
	@Email(message = "Email Not In Format")
	@Pattern(regexp = "^(?!\\s*$).+", message = "Email Not Valid")
	private String email;
	@NotBlank(message = "Password is mandatory")
	@Length(min = 5, message = "Password must be of Minimum 5 character")
	private String password;
	private String contactNo;
	private boolean suspended = false;
	private Date createdAt = new Date();
	private Date modifiedAt = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
