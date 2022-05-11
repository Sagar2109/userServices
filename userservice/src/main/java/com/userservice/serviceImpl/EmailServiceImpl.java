package com.userservice.serviceImpl;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.userservice.request.EmailDetails;
import com.userservice.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

/*	 @Autowired
	 private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	
	  @Override public boolean sendSimpleMail(EmailDetails details) {
	  
	  try {
	  
	  SimpleMailMessage mailMessage = new SimpleMailMessage();
	  
	  mailMessage.setFrom(sender); mailMessage.setTo(details.gettoEmail());
	  mailMessage.setText(details.getMsgBody());
	  mailMessage.setSubject(details.getSubject());
	  
	  javaMailSender.send(mailMessage);
	  
	  return true; 
	  } catch (Exception e) {
	  log.info("Exception Inside EmailServiceImpl in Api sendSimpleMail(...)"+e);
	  return false;
	  }
   }
*/
	 
	public boolean sendSimpleMail(EmailDetails details) throws MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("a1b2c3dhoraji@gmail.com", "aaaabbbb1234");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("a1b2c3dhoraji@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(details.gettoEmail()));
		msg.setSubject(details.getSubject());
		msg.setContent(details.getMsgBody(), "text/html");

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(details.getMsgBody(), "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		Transport.send(msg);

		log.info("Exception Inside EmailServiceImpl in Api sendSimpleMail(...)");
		return true;
	}

}
