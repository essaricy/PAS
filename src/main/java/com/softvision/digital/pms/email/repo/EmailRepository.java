package com.softvision.digital.pms.email.repo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.digital.pms.appraisal.repo.AppraisalRepository;
import com.softvision.digital.pms.common.beans.AppEmailConfig;
import com.softvision.digital.pms.common.constants.EmailType;
import com.softvision.digital.pms.email.entity.EMailTemplate;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.repo.EmployeeDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmailRepository {

	private static final String MANAGER_NAME = "managerName";

	private static final String TO_MANAGER = "toManager";

	private static final String FROM_MANAGER = "fromManager";

	private static final String EMP_NAME = "empName";

	private static final String URL = "url";

	private static final String IMAGE_PATH = "imagePath";

	private static final String PHASE = "phase";

	@Autowired private Session session;

	@Autowired private EmailTemplateRepository emailTemplateEngine;

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;
	
	@Autowired private EmployeeDataRepository employeeRepository;
	
	@Autowired private EmailTemplateDataRepository mailDataRepository;
	
	@Autowired private AppEmailConfig appEmailConfig;

	private boolean turnOffEmails;

	@PostConstruct
	private void init() {
		String mode = appEmailConfig.getMode();
		String alwaysSendTo = appEmailConfig.getAlwaysSendTo();
		turnOffEmails = mode != null && mode.trim().equalsIgnoreCase("disable");
		if (StringUtils.isBlank(alwaysSendTo)) {
			appEmailConfig.setAlwaysSendTo(null);
		}
	}

	public void sendApprasialKickOffEmail(AppraisalCycle appraisalCycle) {
		sendGlobalEmail(EmailType.APPRAISAL_CYCLE_KICK_OFF, appraisalCycle);
	}

	public void sendApprasialConcludeEmail(AppraisalCycle appraisalCycle) {
		sendGlobalEmail(EmailType.APPRAISAL_CYCLE_CONCLUDE, appraisalCycle);
	}

	public void sendEnableEmployeeAppraisalEmail(int phaseId, int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.ENABLE_EMPLOYEE_APPRAISAL, phaseId, managerId, employeeId);
	}
	
	public void sendEmployeeSubmittedEmail(int phaseId, int employeeId, int managerId) {
		sendEmployeeToManagerEmail(EmailType.EMPLOYEE_SUBMITED, phaseId, employeeId, managerId);
	}
	
	public void sendManagerReviewCompletedEmail(int phaseId, int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.MANAGER_REVIEW_COMPLETED, phaseId, managerId, employeeId);
	}

	public void sendRevertToSelfSubmissionEmail(int phaseId, int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.REVERT_TO_SELF_SUBMISSION, phaseId, managerId, employeeId);
	}

	public void sendEmployeeAgreedWithReviewEmail(int phaseId, int employeeId,int managerId) {
		sendEmployeeToManagerEmail(EmailType.AGREED_WITH_REVIEW, phaseId, employeeId, managerId);
	}
	
	public void sendEmployeeDisagreedWithReviewEmail(int phaseId, int employeeId,int managerId) {
		sendEmployeeToManagerEmail(EmailType.DISAGREED_WITH_REVIEW, phaseId, employeeId, managerId);
	}
	
	public void sendUpdatedReviewEmail(int phaseId, int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.MANAGER_REVIEW_UPDATED, phaseId, managerId, employeeId);
	}

	public void sendConcludeMail(int phaseId, int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.ASSIGNMENT_CONCLUDED, phaseId, managerId, employeeId);
	}

	public void sendChangeManagerEmail(int phaseId, int employeeId, int fromManagerId, int toManagerId) {
		log.info("Sending email for {}, phaseId={}, assignedBy={}, employeeId={}",
				EmailType.CHANGE_MANAGER, phaseId, fromManagerId, employeeId);
		try {
			String domain = appEmailConfig.getDomain();
			String image = appEmailConfig.getImage();

			AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
			AppraisalCycle appraisalCycle = appraisalRepository.getActiveCycle();
			Employee employee=employeeRepository.findByEmployeeId(employeeId);
			Employee manager=employeeRepository.findByEmployeeId(fromManagerId);
			Employee tManager=employeeRepository.findByEmployeeId(toManagerId);
			EMailTemplate emailTemplate = mailDataRepository.findByName(EmailType.CHANGE_MANAGER.toString());
			String templateFile = emailTemplate.getFileName();
			String subject = emailTemplate.getSubject();
			String url = emailTemplate.getButtonUrl();
			String empName = employee.getFirstName();
			String fromManager = manager.getFirstName();
			String toManager = tManager.getFirstName();
			String emailTo = employee.getLoginId() + domain;
			String emailFrom = manager.getLoginId() + domain;
			String emailcc = tManager.getLoginId() + domain;

			Map<String, Object> emailData = new HashMap<>();
			emailData.put(PHASE, appraisalPhase.getName());
			emailData.put(IMAGE_PATH, image);
			emailData.put(URL, url);
			emailData.put(EMP_NAME, empName);
			emailData.put(FROM_MANAGER, fromManager);
			emailData.put(TO_MANAGER, toManager);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName(), appraisalPhase.getName(), empName);
			sendEmail(emailFrom, emailTo, emailcc, subject, reportContent);
		} catch (Exception exception) {
			log.error("Unable to send email for {}, phaseId={}, assignedBy={}, employeeId={}, ERROR={}",
					EmailType.CHANGE_MANAGER, phaseId, fromManagerId, employeeId, exception);
		}
	}

	public void sendManagerToEmployeeReminder(int phaseId, int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.SEND_REMINDER_MGR_TO_EMP, phaseId, managerId, employeeId);
	}
	
	public void sendHrToEmployeeReminder(int phaseId, int employeeId, int managerId) {
		sendManagerToEmployeeEmail(EmailType.SEND_REMINDER_HR_TO_EMP, phaseId, managerId, employeeId);
	}
	
	public void sendHrToManagerReminder(int phaseId, int employeeId, int managerId) {
		sendEmployeeToManagerEmail(EmailType.SEND_REMINDER_HR_TO_MGR, phaseId, employeeId, managerId);
	}

	private void sendEmail(String from, String toAddresses, String subject, String content) throws MessagingException {
		if (!turnOffEmails) {
			String alwaysSendTo = appEmailConfig.getAlwaysSendTo();
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			if (alwaysSendTo == null) {
				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));
				// Set To: header field of the header.
				addRecepients(message, toAddresses, Message.RecipientType.TO);
			} else {
				message.setFrom(new InternetAddress(alwaysSendTo));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(alwaysSendTo));
			}
			// Set Subject: header field
			message.setSubject(subject);
			// Now set the actual message
			message.setContent(content, "text/html");
			// Send message
			Transport.send(message);
			log.info("Email successfully sent to " + toAddresses + " with subject '" + subject + "'");
		}
	}

	private void sendEmail(String from, String toAddresses, String ccAddresses, String subject, String content)
			throws MessagingException {
		if (!turnOffEmails) {
			String alwaysSendTo = appEmailConfig.getAlwaysSendTo();
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			if (alwaysSendTo == null) {
				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));
				// Set To: header field of the header.
				addRecepients(message, toAddresses, Message.RecipientType.TO);
				addRecepients(message, ccAddresses, Message.RecipientType.CC);
			} else {
				message.setFrom(new InternetAddress(alwaysSendTo));
				addRecepients(message, alwaysSendTo, Message.RecipientType.TO);
				addRecepients(message, alwaysSendTo, Message.RecipientType.CC);
			}
			// Set Subject: header field
			message.setSubject(subject);
			// Now set the actual message
			message.setContent(content, "text/html");
			// Send message
			Transport.send(message);
			log.info("Email successfully sent2 to " + toAddresses + " with subject '" + subject + "'");
		}
	}

	private void addRecepients(MimeMessage message, String addresses, RecipientType type) throws MessagingException {
		if (addresses != null) {
			if (addresses.indexOf(',') != -1) {
				String[] split = addresses.split(",");
				for (String toAddress : split) {
					message.addRecipient(type, new InternetAddress(toAddress));
				}
			} else {
				message.addRecipient(type, new InternetAddress(addresses));
			}
		}
	}

	private void sendGlobalEmail(EmailType emailType, AppraisalCycle appraisalCycle) {
		try {
			log.info("Sending email emailType={}", emailType);
			String image = appEmailConfig.getImage();
			EMailTemplate emailTemplate = mailDataRepository.findByName(emailType.toString());
			String templateFile = emailTemplate.getFileName();
			String url = emailTemplate.getButtonUrl();
			String subject = emailTemplate.getSubject();
			String emailFrom = emailTemplate.getFromMail();
			String emailTo = emailTemplate.getToMail();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("cycle", appraisalCycle.getName());
			emailData.put(IMAGE_PATH, image);
			emailData.put(URL, url);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName());
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			log.error("Unable to send email emailType={}, ERROR={}", emailType, exception);
		}
	}

	private void sendManagerToEmployeeEmail(EmailType emailType, int phaseId, int managerId, int employeeId) {
		log.info("Sending email emailType={}, phaseId={}, managerId={}, employeeId={}",
				emailType, phaseId, managerId, employeeId);
		try {
			String domain = appEmailConfig.getDomain();
			String image = appEmailConfig.getImage();

			AppraisalPhase appraisalPhase = appraisalPhaseDataRepository.findById(phaseId);
			AppraisalCycle appraisalCycle = appraisalRepository.getActiveCycle();
			Employee manager = employeeRepository.findByEmployeeId(managerId);
			Employee employee = employeeRepository.findByEmployeeId(employeeId);

			EMailTemplate emailTemplate =  mailDataRepository.findByName(emailType.toString());
			String templateFile = emailTemplate.getFileName();
			String subject = emailTemplate.getSubject();
			String url = emailTemplate.getButtonUrl();
			String emailFrom = manager.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;

			String managerName = manager.getFirstName();
			String empName = employee.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put(PHASE, appraisalPhase.getName());
			emailData.put(IMAGE_PATH, image);
			emailData.put(URL, url);
			emailData.put(EMP_NAME, empName);
			emailData.put(MANAGER_NAME, managerName);

			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName(), appraisalPhase.getName(), empName);
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			log.error("Unable to send email emailType={}, phaseId={}, managerId={}, employeeId={}, ERROR={}",
					emailType, phaseId, managerId, employeeId, exception);
		}
	}

	private void sendEmployeeToManagerEmail(EmailType emailType, int phaseId, int employeeId, int managerId) {
		log.info("Sending email emailType={}, phaseId={}, employeeId={}, managerId={}",
				emailType, phaseId, employeeId, managerId);
		try {
			String domain = appEmailConfig.getDomain();
			String image = appEmailConfig.getImage();

			AppraisalPhase appraisalPhase = appraisalPhaseDataRepository.findById(phaseId);
			AppraisalCycle appraisalCycle = appraisalRepository.getActiveCycle();
			Employee manager = employeeRepository.findByEmployeeId(managerId);
			Employee employee = employeeRepository.findByEmployeeId(employeeId);

			EMailTemplate emailTemplate =  mailDataRepository.findByName(emailType.toString());
			String templateFile = emailTemplate.getFileName();
			String subject = emailTemplate.getSubject();
			String url = emailTemplate.getButtonUrl();
			String emailFrom = employee.getLoginId() + domain;
			String emailTo = manager.getLoginId() + domain;

			String managerName = manager.getFirstName();
			String empName = employee.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put(PHASE, appraisalPhase.getName());
			emailData.put(IMAGE_PATH, image);
			emailData.put(URL, url);
			emailData.put(EMP_NAME, empName);
			emailData.put(MANAGER_NAME, managerName);

			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName(), appraisalPhase.getName(), empName);
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			log.error("Unable to send email emailType={}, phaseId={}, employeeId={}, managerId={}, ERROR={}",
					emailType, phaseId, employeeId, managerId, exception);
		}
	}

}
