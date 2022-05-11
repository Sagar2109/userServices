package com.userservice.request;

public class EmailDetails {

	private String toEmail;
	private String msgBody;
	private String subject;

	public EmailDetails() {
		super();
	}

	public EmailDetails(String toEmail, String msgBody, String subject) {
		super();
		this.toEmail = toEmail;
		this.msgBody = msgBody;
		this.subject = subject;
	}

	public String gettoEmail() {
		return toEmail;
	}

	public void settoEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
