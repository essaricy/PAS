package com.softvision.ipm.pms.email.repo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.common.exception.EmailException;

@Repository
public class EmailRepository {

	@Autowired Environment environment;

	@Autowired private Session session;

	@Autowired private EmailTemplateRepository emailTemplateEngine;

	public void sendApprasialKickOff(AppraisalCycle appraisalCycle) throws EmailException {
		try {
			String templateFile = environment.getRequiredProperty("app.email.template.kickoff.file");
			String emailFrom = environment.getRequiredProperty("app.email.template.kickoff.from");
			String emailTo = environment.getRequiredProperty("app.email.template.kickoff.to");
			String subject = environment.getRequiredProperty("app.email.template.kickoff.subject");

			Map<String, Object> emailData = new HashMap<>();
			emailData.put("AppraisalCycle", appraisalCycle);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName());
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}

	private void sendEmail(String from, String toAddresses, String subject, String content) throws MessagingException {
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		if (toAddresses.indexOf(",") != -1) {
			String[] split = toAddresses.split(",");
			for (String toAddress : split) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			}
		} else {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses));
		}
		// Set Subject: header field
		message.setSubject(subject);
		// Now set the actual message
		// message.setText(content);
		message.setContent(content, "text/html");
		// Send message
		Transport.send(message);
		System.out.println("Sent message successfully....");
	}
}
