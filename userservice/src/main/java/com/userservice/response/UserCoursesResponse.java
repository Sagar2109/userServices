package com.userservice.response;

import java.util.Date;
import java.util.List;

public class UserCoursesResponse {

	private String id;
	private String name;
	private String email;
	private String contactNo;
	private boolean suspended = false;
	private Date createdAt = new Date();
	private Date modifiedAt = new Date();
	private List<CoursesResponse> courses;

	public UserCoursesResponse() {
		super();

	}

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

	public List<CoursesResponse> getCourses() {
		return courses;
	}

	public void setCourses(List<CoursesResponse> courses) {
		this.courses = courses;
	}

}
