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

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.common.constants.EmailConstant;
import com.softvision.ipm.pms.common.exception.EmailException;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeRepository;

@Repository
public class EmailRepository {

	@Autowired Environment environment;

	@Autowired private Session session;

	@Autowired private EmailTemplateRepository emailTemplateEngine;
	
	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;
	
	@Autowired private AppraisalPhaseDataRepository appraisalPhaseDataRepository;
	
	@Autowired private EmployeeRepository employeeRepository;
	
	public void sendGenericMail(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager,String emailType) throws EmailException {
		try {
			
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			
			String templateFile ="";
			String subject = "";
			String ccEmail = null;
			String emailFrom = "";
			String emailTo = "";
			String empName="";
			String managerName="";
			
			EmailConstant enumObj = EmailConstant.valueOf(emailType);

			if(enumObj != null && enumObj == EmailConstant.KICK_OFF){
				
				 templateFile = environment.getRequiredProperty("app.email.template.kickoff.file");
				 emailFrom = environment.getRequiredProperty("app.email.template.kickoff.from");
				 emailTo = environment.getRequiredProperty("app.email.template.kickoff.to");
				 subject = environment.getRequiredProperty("app.email.template.kickoff.subject");
				 subject = MessageFormat.format(subject, appraisalCycle.getName());
				
			}else if(enumObj != null && enumObj == EmailConstant.EMPLOYEE_ENABLE){
				 subject = environment.getRequiredProperty("app.email.template.enable.subject");
				 templateFile = environment.getRequiredProperty("app.email.template.enable.file");
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
					
			}else if(enumObj != null && enumObj == EmailConstant.EMPLOYEE_SUBMITED){
				 templateFile = environment.getRequiredProperty("app.email.template.employeesubmit.file");
				 subject = environment.getRequiredProperty("app.email.template.employeesubmit.subject");
				 ccEmail = environment.getRequiredProperty("app.email.template.employeesubmit.cc");
				 emailFrom = employee.getLoginId()+domain;
				 emailTo = manager.getLoginId()+domain;
				 empName = employee.getFirstName();
				 managerName = manager.getFirstName();
				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
				
			}else if(enumObj != null && enumObj == EmailConstant.MANAGER_REMAINDER){
				 templateFile = environment.getRequiredProperty("app.email.template.mangerreview.file");
				 subject = environment.getRequiredProperty("app.email.template.mangerreview.subject");
				 ccEmail = environment.getRequiredProperty("app.email.template.mangerreview.cc");
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
				 empName = employee.getFirstName();
				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
				
			}else if(enumObj != null && enumObj == EmailConstant.MANAGER_REVIEW){
				
				 templateFile = environment.getRequiredProperty("app.email.template.mangerreview.file");
				 subject = environment.getRequiredProperty("app.email.template.mangerreview.subject");
				 ccEmail = environment.getRequiredProperty("app.email.template.mangerreview.cc");
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
				 empName = employee.getFirstName();
				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
				 
			}else if(enumObj != null && enumObj == EmailConstant.MANAGER_FROZEN){
				
				 templateFile = environment.getRequiredProperty("app.email.template.mangerfrozen.file");
				 subject = environment.getRequiredProperty("app.email.template.mangerfrozen.subject");
				 ccEmail = environment.getRequiredProperty("app.email.template.mangerfrozen.cc");
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
				 empName = employee.getFirstName();
				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			}
			
		
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);
			emailData.put("cycle", appraisalCycle.getName());
			
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			sendEmail(emailFrom, emailTo,ccEmail, subject, reportContent);
			
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	public void sendManagerReviewFrozen(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			String templateFile = environment.getRequiredProperty("app.email.template.mangerfrozen.file");
			String subject = environment.getRequiredProperty("app.email.template.mangerfrozen.subject");
			String ccEmail = environment.getRequiredProperty("app.email.template.mangerfrozen.cc");
			String emailFrom = manager.getLoginId()+domain;
			String emailTo = employee.getLoginId()+domain;
			
			String empName = employee.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo,ccEmail, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	public void sendManagerReviewCompleted(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			String templateFile = environment.getRequiredProperty("app.email.template.mangerreview.file");
			String subject = environment.getRequiredProperty("app.email.template.mangerreview.subject");
			String ccEmail = environment.getRequiredProperty("app.email.template.mangerreview.cc");
			String emailFrom = manager.getLoginId()+domain;
			String emailTo = employee.getLoginId()+domain;
			
			String empName = employee.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo,ccEmail, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	public void sendEmployeeRemainder(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			String templateFile = environment.getRequiredProperty("app.email.template.employeeremainder.file");
			String subject = environment.getRequiredProperty("app.email.template.employeeremainder.subject");
			String ccEmail = environment.getRequiredProperty("app.email.template.employeeremainder.cc");
			String emailFrom = manager.getLoginId()+domain;
			String emailTo = employee.getLoginId()+domain;
			
			String empName = employee.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo,ccEmail, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
			
	
	public void sendApprasialEmployeeSubmitted(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			String domain=  environment.getRequiredProperty("app.email.domain");
			String templateFile = environment.getRequiredProperty("app.email.template.employeesubmit.file");
			String subject = environment.getRequiredProperty("app.email.template.employeesubmit.subject");
			String ccemal = environment.getRequiredProperty("app.email.template.employeesubmit.cc");
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
			sendEmail(emailFrom, emailTo, ccemal,subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}

	
	public void sendApprasialManagerAssign(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			String templateFile = environment.getRequiredProperty("app.email.template.enable.file");
			String domain=  environment.getRequiredProperty("app.email.domain");
			String emailFrom = manager.getLoginId()+domain;
			String emailTo = employee.getLoginId()+domain;
			String subject = environment.getRequiredProperty("app.email.template.enable.subject");
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			String empName = employee.getFirstName();
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}

	public void sendApprasialKickOff(AppraisalCycle appraisalCycle) throws EmailException {
		try {
			String templateFile = environment.getRequiredProperty("app.email.template.kickoff.file");
			String emailFrom = environment.getRequiredProperty("app.email.template.kickoff.from");
			String emailTo = environment.getRequiredProperty("app.email.template.kickoff.to");
			String subject = environment.getRequiredProperty("app.email.template.kickoff.subject");
			String image = environment.getRequiredProperty("app.email.image");
			String url = environment.getRequiredProperty("app.email.url");
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("cycle", appraisalCycle.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
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
	private void sendEmail(String from, String toAddresses, String ccAddresses, String subject, String content) throws MessagingException {
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		if(ccAddresses != null ){
		message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddresses));
		}
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

	public void sendEnableMail(int phaseId, int assignedBy, int employeeId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(assignedBy);
		
		try {
			sendApprasialManagerAssign(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void sendConcludeMail(int phaseId, int managerId, int employeeId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		try {
			sendManagerReviewFrozen(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void sendReviewCompleted(int phaseId, int managerId, int employeeId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		try {
			sendManagerReviewCompleted(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void sendEmployeeSubmitMail(int phaseId, int managerId, int employeeId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		try {
			sendApprasialEmployeeSubmitted(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
		
	}
}
