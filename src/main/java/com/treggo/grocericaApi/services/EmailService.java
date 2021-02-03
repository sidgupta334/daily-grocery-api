/**
 * 
 */
package com.treggo.grocericaApi.services;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Value("${app.mailId}")
	private String emailId;
	
	@Value("${app.mailPassword}")
	private String emailPassword;
	
	Logger logger = LoggerFactory.getLogger(EmailService.class);

	
	public boolean sendOtpMessage(String to, String otp)
			throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication(emailId, emailPassword);
		      }
		   });
		
		Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress(emailId, false));
		   
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		   msg.setSubject("RECOVER CONNECT BAZAR ACCOUNT");
		   String content = "<h3> GREETINGS FROM CONNECT BAZAR !!! </h3>";
		   content += "<p> Don't worry if you have forgot your password, we can help you recover your account " + to.toString() + ", Enter below OTP: </p>";
		   content+= "<h4> <span style='text-align=center; color:#7d0e61'>" + otp + "</span> </h4>";
		   msg.setContent(content, "text/html");
		   try {
			   Transport.send(msg);
			   return true;
		   }
		   catch(Exception e) {
			   logger.error("Failed to run: " + e);
			   return false;
		   }

	}
	
	public boolean sendRegistrationOtpMessage(String to, String otp, String name)
			throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication(emailId, emailPassword);
		      }
		   });
		
		Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress(emailId, false));
		   
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		   msg.setSubject("WELCOME TO CONNECT BAZAR !!!");
		   String content = "<h3> GREETINGS FROM CONNECT BAZAR !!! </h3>";
		   content += "<p> Hi " + name + ",";
		   content += "<p> Thank you for choosing us for your digital grocery shopping experience. Enter below OTP to activate your account: </p>";
		   content += "<h3> <span style='text-align=center; color:#7d0e61'>" + otp + "</span> </h3>";
		   content += "<p> The OTP will be valid for 24 hours from registration </p>";
		   msg.setContent(content, "text/html");
		   try {
			   Transport.send(msg);
			   return true;
		   }
		   catch(Exception e) {
			   logger.error("Failed to run: " + e);
			   return false;
		   }

	}
}
