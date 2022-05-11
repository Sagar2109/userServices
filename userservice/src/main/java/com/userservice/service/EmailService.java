package com.userservice.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.userservice.request.EmailDetails;

public interface EmailService {

	public boolean sendSimpleMail(EmailDetails details) throws MessagingException, IOException;

}