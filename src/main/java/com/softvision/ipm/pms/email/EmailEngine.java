package com.softvision.ipm.pms.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.common.constants.EmailConstants;
import com.softvision.ipm.pms.common.exception.EmailException;

import java.text.MessageFormat;
import java.util.Map;

import javax.mail.MessagingException;

@Component
public class EmailEngine {

	@Autowired SPIEmailUtil spiEmailUtil;

	private static final String EMAIL_PROFILE_SV_DEV = "SV_DEV";

	private static final String EMAIL_PROFILE_SV_PRD = "SV_PRD";
	
	private static final String EMAIL_PROFILE = EMAIL_PROFILE_SV_DEV;
	
	
	private static String FROM_EMAIL;

	private static String[] TO_EMAIL_ADDRESS;;

	static {
	    if (EMAIL_PROFILE.equals(EMAIL_PROFILE_SV_DEV)) {
            FROM_EMAIL = "rohith.ramesh@softvision.com";
            TO_EMAIL_ADDRESS = new String[] { FROM_EMAIL };
        }  else if (EMAIL_PROFILE.equals(EMAIL_PROFILE_SV_PRD)) {
            FROM_EMAIL = "rohith.ramesh@softvision.com";
            TO_EMAIL_ADDRESS = new String[] { FROM_EMAIL };
        }
	}

	@Autowired
	private EmailTemplateEngine emailTemplateEngine;
	
	public void sendApprasialKickOfMail(Map<String, Object> emailData)
			throws EmailException {
		try {
			String reportContent = emailTemplateEngine.getApprasialKickOfMail(emailData);
			String subject =EmailConstants.EMAIL_KICKOFF_SUBJECT + emailData.get("cycle");
			sendEmail(FROM_EMAIL, TO_EMAIL_ADDRESS, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	
	public void sendEmployeeAlert(Map<String, Object> emailData)
			throws EmailException {
		try {
			String reportContent = emailTemplateEngine.getEmployeeAlert(emailData);
			String subject =EmailConstants.EMPLOYEE_ALERT_SUBJECT + emailData.get("employeeName");
			sendEmail(FROM_EMAIL, TO_EMAIL_ADDRESS, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	public void sendManagerAlert(Map<String, Object> emailData)
			throws EmailException {
		try {
			String reportContent = emailTemplateEngine.getEmployeeAlert(emailData);
			String subject =EmailConstants.EMPLOYEE_ALERT_SUBJECT + emailData.get("managerName");
			sendEmail(FROM_EMAIL, TO_EMAIL_ADDRESS, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	 private void sendEmail(String from, String[] toAddresses,
	            String subject, String content) throws MessagingException {
	            spiEmailUtil.sendEmail(from, toAddresses, subject, content);
	    }
}
