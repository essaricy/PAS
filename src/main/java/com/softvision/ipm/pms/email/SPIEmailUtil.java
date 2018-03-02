package com.softvision.ipm.pms.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class SPIEmailUtil {

	//private static final Properties PROPERTIES;

	
	@Value("${app.smtp.host}")
	private String smtpSvHost;
	
	public void sendEmail(String from, String[] toAddresses,
			String subject, String content) throws MessagingException {
		Properties PROPERTIES= new Properties();
		PROPERTIES.setProperty("mail.smtp.host", smtpSvHost);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(PROPERTIES);
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		for (String toAddress : toAddresses) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
		}
		// Set Subject: header field
		message.setSubject(subject);
		// Now set the actual message
		//message.setText(content);
		message.setContent(content, "text/html");
		// Send message
		Transport.send(message);
		System.out.println("Sent message successfully....");
	}

	public static void main(String[] args) throws MessagingException {
		new SPIEmailUtil().sendEmail("srikanth.kumar@softvision.com", new String[] {"rohith.ramesh@softvision.com"}, "subject", "content");
	}
}
