package com.softvision.ipm.pms.email.repo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.common.constants.EmailType;
import com.softvision.ipm.pms.email.entity.EMailTemplate;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class EmailRepository {

	@Autowired private Session session;

	@Autowired private EmailTemplateRepository emailTemplateEngine;
	
	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;
	
	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;
	
	@Autowired private EmployeeDataRepository employeeRepository;
	
	@Autowired private EmailTemplateDataRepository mailDataRepository;
	
	@Value("${app.email.image}")
	private String image;
	
	@Value("${app.email.domain}")
	private String domain;
	
	@Value("${app.email.mode}")
	private String mode;
	
	@Value("${app.email.always-send-to:#{null}}")
	private String alwaysSendTo;

	private boolean turnOffEmails;

	@PostConstruct
	private void init() {
		turnOffEmails = mode != null && mode.trim().equalsIgnoreCase("disable");
		alwaysSendTo = alwaysSendTo == null || alwaysSendTo.trim().length() == 0 ? null: alwaysSendTo;
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
			AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
			AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
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
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("fromManager", fromManager);
			emailData.put("toManager", toManager);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject,
					new Object[] { appraisalCycle.getName(), appraisalPhase.getName(), empName });
			sendEmail(emailFrom, emailTo, emailcc, subject, reportContent);
		} catch (Exception exception) {
			log.error("Unable to send email for {}, phaseId={}, assignedBy={}, employeeId={}, ERROR={}",
					EmailType.CHANGE_MANAGER, phaseId, fromManagerId, employeeId, exception);
		}
	}

	public void sendManagerToEmployeeReminder(int phaseId,int managerId, int employeeId) {
		sendManagerToEmployeeEmail(EmailType.SEND_REMINDER_MGR_TO_EMP, phaseId, managerId, employeeId);
	}
	
	public void sendHrToEmployeeReminder(int phaseId,int hrId, int employeeId,int managerId) {
		sendManagerToEmployeeEmail(EmailType.SEND_REMINDER_HR_TO_EMP, phaseId, managerId, employeeId);
	}
	
	public void sendHrToManagerReminder(int phaseId,int employeeId, int managerId) {
		sendEmployeeToManagerEmail(EmailType.SEND_REMINDER_HR_TO_MGR, phaseId, employeeId, managerId);
	}

	private void sendEmail(String from, String toAddresses, String subject, String content) throws MessagingException {
		if (!turnOffEmails) {
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

	private void addRecepients(MimeMessage message, String addresses, RecipientType type)
			throws AddressException, MessagingException {
		if (addresses != null) {
			if (addresses.indexOf(",") != -1) {
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
			EMailTemplate emailTemplate = mailDataRepository.findByName(emailType.toString());
			String templateFile = emailTemplate.getFileName();
			String url = emailTemplate.getButtonUrl();
			String subject = emailTemplate.getSubject();
			String emailFrom = emailTemplate.getFromMail();
			String emailTo = emailTemplate.getToMail();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("cycle", appraisalCycle.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
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
			AppraisalPhase appraisalPhase = appraisalPhaseDataRepository.findById(phaseId);
			AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
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
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);

			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject,
					new Object[] { appraisalCycle.getName(), appraisalPhase.getName(), empName });
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
			AppraisalPhase appraisalPhase = appraisalPhaseDataRepository.findById(phaseId);
			AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
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
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);

			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject,
					new Object[] { appraisalCycle.getName(), appraisalPhase.getName(), empName });
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			log.error("Unable to send email emailType={}, phaseId={}, employeeId={}, managerId={}, ERROR={}",
					emailType, phaseId, employeeId, managerId, exception);
		}
	}

}
