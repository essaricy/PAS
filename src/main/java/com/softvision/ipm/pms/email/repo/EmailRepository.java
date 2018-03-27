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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.common.constants.EmailConstant;
import com.softvision.ipm.pms.email.entity.MailTemplate;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;

@Repository
public class EmailRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRepository.class);

	@Autowired Environment environment;

	@Autowired private Session session;

	@Autowired private EmailTemplateRepository emailTemplateEngine;
	
	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;
	
	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;
	
	@Autowired private EmployeeRepository employeeRepository;
	
	@Autowired private MailTemplateDataRepository mailDataRepository;
	
	@Value("${app.email.image}")
	private String image;
	
	@Value("${app.email.domain}")
	private String domain;
	
	@Value("${app.email.mode:on}")
	private String mode;
	
	@Value("${app.email.always-send-to:#{null}}")
	private String alwaysSendTo;

	private boolean turnOffEmails;

	@PostConstruct
	private void init() {
		turnOffEmails = mode != null && mode.trim().equalsIgnoreCase("off");
		alwaysSendTo = alwaysSendTo == null || alwaysSendTo.trim().length() == 0 ? null: alwaysSendTo;
	}

	public void sendApprasialKickOff(AppraisalCycle appraisalCycle) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.KICK_OFF);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.KICK_OFF.toString());
			String templateFile = mailTemplate.getFileName();
			String url = mailTemplate.getButtonUrl();
			String subject = mailTemplate.getSubject();
			String emailFrom = mailTemplate.getFromMail();
			String emailTo = mailTemplate.getToMail();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("cycle", appraisalCycle.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName());
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.KICK_OFF, exception);
		}
	}

	public void sendApprasialCycleConclude(AppraisalCycle appraisalCycle) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.CYCLE_CONCLUDE);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.CYCLE_CONCLUDE.toString());
			String templateFile = mailTemplate.getFileName();
			String emailFrom = mailTemplate.getFromMail();
			String emailTo = mailTemplate.getToMail();
			String subject = mailTemplate.getSubject();
			String url = mailTemplate.getButtonUrl();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("cycle", appraisalCycle.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, appraisalCycle.getName());
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.CYCLE_CONCLUDE, exception);
		}
	}

	private void sendApprasialEmployeeSubmitted(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.EMPLOYEE_SUBMITED);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMPLOYEE_SUBMITED.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String emailFrom = employee.getLoginId() + domain;
			String emailTo = manager.getLoginId() + domain;
			String empName = employee.getFirstName();
			String managerName = manager.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.EMPLOYEE_SUBMITED, exception);
		}
	}
	
	private void sendEmployeeAcceptenceMail(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.EMP_ACCEPTED);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMP_ACCEPTED.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String ccEmail = mailTemplate.getCcMail();
			String emailFrom = employee.getLoginId()+domain;
			String emailTo = manager.getLoginId()+domain;
			String empName = employee.getFirstName();
			String managerName = manager.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, ccEmail,subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.EMP_ACCEPTED, exception);
		}
	}
	
	private void sendEmployeeRejectionMail(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.EMP_REJECTED);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMP_REJECTED.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String ccEmail = mailTemplate.getCcMail();
			String emailFrom = employee.getLoginId()+domain;
			String emailTo = manager.getLoginId()+domain;
			String empName = employee.getFirstName();
			String managerName = manager.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, ccEmail,subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.EMP_REJECTED, exception);
		}
	}
	
	private void sendManagerReviewFrozen(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.MANAGER_FROZEN);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.MANAGER_FROZEN.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String emailFrom = manager.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;
			String ccEmail = emailFrom + mailTemplate.getCcMail();
			
			String empName = employee.getFirstName();
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo,ccEmail, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.MANAGER_FROZEN, exception);
		}
	}
	
	private void sendManagerReviewCompleted(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.MANAGER_REVIEW);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.MANAGER_REVIEW.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String empName = employee.getFirstName();
			String emailFrom = manager.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.MANAGER_REVIEW, exception);
		}
	}
	
	private void sendManagerToEmployeeRemainder(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.MGR_TO_EMP_REMAINDER);
			MailTemplate mailTemplate = mailDataRepository.findByName(EmailConstant.MGR_TO_EMP_REMAINDER.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String empName = employee.getFirstName();
			String emailFrom = manager.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.MGR_TO_EMP_REMAINDER, exception);
		}
	}
			
	private void sendHrToEmployeeRemainder(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase, Employee hr,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.HR_TO_EMP_REM);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.HR_TO_EMP_REM.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String empName = employee.getFirstName();

			String emailFrom = hr.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;
			String ccEmail = manager.getLoginId() + domain;

			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, ccEmail,subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.HR_TO_EMP_REM, exception);
		}
	}
	
	private void sendHrToManagerRemainderMail(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase, Employee hr,
			Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.HR_TO_MGR_REM);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.HR_TO_MGR_REM.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String managerName = manager.getFirstName();
			
			String emailFrom = hr.getLoginId() + domain;
			String emailTo = manager.getLoginId() + domain;
		
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("managerName", managerName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),managerName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.HR_TO_MGR_REM, exception);
		}
	}
	
	
	private void sendChangeManagerMail(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase, Employee fManager,
			Employee tManager, Employee employee) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.CHANGE_MGR);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.CHANGE_MGR.toString());
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String url = mailTemplate.getButtonUrl();
			String empName = employee.getFirstName();
			String fromManager = fManager.getFirstName();
			String toManager = tManager.getFirstName();
			String emailTo = employee.getLoginId() + domain;
			String emailFrom = fManager.getLoginId() + domain;
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
			LOGGER.error("Unable to email for " + EmailConstant.CHANGE_MGR, exception);
		}
	}
	
	private void sendApprasialManagerAssign(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee employee, Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.EMPLOYEE_ENABLE);
			MailTemplate mailTemplate = mailDataRepository.findByName(EmailConstant.EMPLOYEE_ENABLE.toString());
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String url = mailTemplate.getButtonUrl();
			String empName = employee.getFirstName();
			String emailFrom = manager.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.EMPLOYEE_ENABLE, exception);
		}
	}

	private void sendUdatedReviewMail(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase, Employee employee,
			Employee manager) {
		try {
			LOGGER.info("Sending email for "+ EmailConstant.UPDATE_REVIEW);
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.UPDATE_REVIEW.toString());
			String url = mailTemplate.getButtonUrl();
			String subject = mailTemplate.getSubject();
			String templateFile = mailTemplate.getFileName();
			String empName = employee.getFirstName();
			
			String emailFrom = manager.getLoginId() + domain;
			String emailTo = employee.getLoginId() + domain;
			String ccEmail = mailTemplate.getCcMail();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, ccEmail,subject, reportContent);
		} catch (Exception exception) {
			LOGGER.error("Unable to email for " + EmailConstant.UPDATE_REVIEW, exception);
		}
	}

	public void sendEnableMail(int phaseId, int assignedBy, int employeeId) {
		LOGGER.info("Sending email for sendEnableMail(" + phaseId + ", " + assignedBy + ", "+ employeeId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(assignedBy);
		sendApprasialManagerAssign(appraisalCycle, appraisalPhase, employee, manager);
	}
	
	public void sendEmployeeSubmitMail(int phaseId, int employeeId, int managerId) {
		LOGGER.info("Sending email for sendEmployeeSubmitMail(" + phaseId + ", " + employeeId + ", "+ managerId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		sendApprasialEmployeeSubmitted(appraisalCycle, appraisalPhase, employee, manager);
	}
	
	public void sendReviewCompleted(int phaseId, int managerId, int employeeId) {
		LOGGER.info("Sending email for sendReviewCompleted(" + phaseId + ", " + managerId + ", "+ employeeId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		sendManagerReviewCompleted(appraisalCycle, appraisalPhase, employee, manager);
	}
	

	public void sendEmployeeAcceptence(int phaseId, int employeeId,int managerId) {
		LOGGER.info("Sending email for sendEmployeeAcceptence(" + phaseId + ", " + employeeId + ", "+ managerId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		sendEmployeeAcceptenceMail(appraisalCycle, appraisalPhase, employee, manager);
	}
	
	public void sendEmployeeRejected(int phaseId, int employeeId,int managerId) {
		LOGGER.info("Sending email for sendEmployeeRejected(" + phaseId + ", " + employeeId + ", "+ managerId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		sendEmployeeRejectionMail(appraisalCycle, appraisalPhase, employee, manager);
	}
	
	
	public void sendUpdatedReviewMail(int phaseId, int assignedBy, int employeeId) {
		LOGGER.info("Sending email for sendUpdatedReviewMail(" + phaseId + ", " + assignedBy + ", "+ employeeId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(assignedBy);
		sendUdatedReviewMail(appraisalCycle, appraisalPhase, employee, manager);
	}
	
	public void sendChangeManager(int phaseId, int employeeId, int fromManager, int toManager) {
		LOGGER.info("Sending email for sendChangeManager(" + phaseId + ", " + employeeId + ", "+ fromManager + ", " + toManager + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee fManager=employeeRepository.findByEmployeeId(fromManager);
		Employee tManager=employeeRepository.findByEmployeeId(toManager);
		sendChangeManagerMail(appraisalCycle, appraisalPhase,fManager,tManager, employee);
	}

	public void sendManagerToEmployeeRemainder(int phaseId,int managerId, int employeeId) {
		LOGGER.info("Sending email for sendManagerToEmployeeRemainder(" + phaseId + ", " + managerId + ", "+ employeeId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		sendManagerToEmployeeRemainder(appraisalCycle, appraisalPhase, employee, manager);
	}
	
	
	public void sendHrToEmployeeRemainder(int phaseId,int hrId, int employeeId,int managerId) {
		LOGGER.info("Sending email for sendHrToEmployeeRemainder(" + phaseId + ", " + hrId + ", "+ employeeId + ", "+ managerId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		Employee hr=employeeRepository.findByEmployeeId(hrId);
		sendHrToEmployeeRemainder(appraisalCycle, appraisalPhase,hr, employee, manager);
	}
	
	
	public void sendHrToManagerRemainder(int phaseId,int hrId, int managerId) {
		LOGGER.info("Sending email for sendHrToManagerRemainder(" + phaseId + ", " + hrId + ", "+ managerId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		Employee hr=employeeRepository.findByEmployeeId(hrId);
		sendHrToManagerRemainderMail(appraisalCycle, appraisalPhase,hr,manager);
	}

	public void sendConcludeMail(int phaseId, int managerId, int employeeId) {
		LOGGER.info("Sending email for sendConcludeMail(" + phaseId + ", " + managerId + ", "+ employeeId + ")");
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		sendManagerReviewFrozen(appraisalCycle, appraisalPhase, employee, manager);
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
			// message.setText(content);
			message.setContent(content, "text/html");
			// Send message
			Transport.send(message);
			LOGGER.info("Email successfully sent to " + toAddresses + " with subject '" + subject + "'");
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
			// message.setText(content);
			message.setContent(content, "text/html");
			// Send message
			Transport.send(message);
			LOGGER.info("Email successfully sent2 to " + toAddresses + " with subject '" + subject + "'");
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

}
