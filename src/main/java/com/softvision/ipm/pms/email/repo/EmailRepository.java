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
import com.softvision.ipm.pms.email.entity.MailTemplate;
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
	
	@Autowired private MailTemplateDataRepository mailDataRepository;
	
	public void sendApprasialKickOff(AppraisalCycle appraisalCycle) throws EmailException {
		try {
			
			String image = environment.getRequiredProperty("app.email.image");
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.KICK_OFF.toString());
			String templateFile = mailTemplate.getFileName();
			String url = mailTemplate.getButtonUrl();
			String subject = mailTemplate.getSubject();
			
			String emailFrom = "";
			String emailTo = "";

			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = mailTemplate.getFromMail();
				 emailTo = mailTemplate.getToMail();
			}
			
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

	public void sendApprasialCycleConclude(AppraisalCycle appraisalCycle) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.CYCLE_CONCLUDE.toString());
			String templateFile = mailTemplate.getFileName();
			String emailFrom = "";
			String emailTo = "";
			String subject = mailTemplate.getSubject();
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = mailTemplate.getFromMail();
				 emailTo = mailTemplate.getToMail();
			}
			
			
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
	
	private void sendApprasialEmployeeSubmitted(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			String image = environment.getRequiredProperty("app.email.image");
			String domain=  environment.getRequiredProperty("app.email.domain");
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMPLOYEE_SUBMITED.toString());
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String emailFrom = "";
			String emailTo = "";
			String empName = employee.getFirstName();
			String managerName = manager.getFirstName();
			

			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = employee.getLoginId()+domain;
				 emailTo = manager.getLoginId()+domain;
			}
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			emailData.put("managerName", managerName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	
	private void sendEmployeeAcceptenceMail(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMP_ACCEPTED.toString());
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String domain=  environment.getRequiredProperty("app.email.domain");
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String ccemal = "";
			String emailFrom = "";
			String emailTo = "";
			String empName = employee.getFirstName();
			String managerName = manager.getFirstName();
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
				 ccemal=testMail;
			}else{
				 emailFrom = employee.getLoginId()+domain;
				 emailTo = manager.getLoginId()+domain;
				  ccemal = mailTemplate.getCcMail();
			}
			
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
	
	private void sendEmployeeRejectionMail(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMP_REJECTED.toString());
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String domain=  environment.getRequiredProperty("app.email.domain");
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String ccemal = "";
			String emailFrom = "";
			String emailTo = "";
			String empName = employee.getFirstName();
			String managerName = manager.getFirstName();
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
				 ccemal=testMail;
			}else{
				 ccemal = mailTemplate.getCcMail();
				 emailFrom = employee.getLoginId()+domain;
				 emailTo = manager.getLoginId()+domain;
			}
			
			
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
	
	public void sendGenericMail(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager,String emailType) throws EmailException {
//		try {
//			
//		
//			String domain=  environment.getRequiredProperty("app.email.domain");
//			String image = environment.getRequiredProperty("app.email.image");
//			String url = environment.getRequiredProperty("app.email.url");
//			
//			String templateFile ="";
//			String subject = "";
//			String ccEmail = null;
//			String emailFrom = "";
//			String emailTo = "";
//			String empName="";
//			String managerName="";
//			
//			EmailConstant enumObj = EmailConstant.valueOf(emailType);
//
//			if(enumObj != null && enumObj == EmailConstant.KICK_OFF){
//				
//				 templateFile = environment.getRequiredProperty("app.email.template.kickoff.file");
//				 emailFrom = environment.getRequiredProperty("app.email.template.kickoff.from");
//				 emailTo = environment.getRequiredProperty("app.email.template.kickoff.to");
//				 subject = environment.getRequiredProperty("app.email.template.kickoff.subject");
//				 subject = MessageFormat.format(subject, appraisalCycle.getName());
//				
//			}else if(enumObj != null && enumObj == EmailConstant.EMPLOYEE_ENABLE){
//				 subject = environment.getRequiredProperty("app.email.template.enable.subject");
//				 templateFile = environment.getRequiredProperty("app.email.template.enable.file");
//				 emailFrom = manager.getLoginId()+domain;
//				 emailTo = employee.getLoginId()+domain;
//				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
//					
//			}else if(enumObj != null && enumObj == EmailConstant.EMPLOYEE_SUBMITED){
//				 templateFile = environment.getRequiredProperty("app.email.template.employeesubmit.file");
//				 subject = environment.getRequiredProperty("app.email.template.employeesubmit.subject");
//				 ccEmail = environment.getRequiredProperty("app.email.template.employeesubmit.cc");
//				 emailFrom = employee.getLoginId()+domain;
//				 emailTo = manager.getLoginId()+domain;
//				 empName = employee.getFirstName();
//				 managerName = manager.getFirstName();
//				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
//				
//			}else if(enumObj != null && enumObj == EmailConstant.MANAGER_REMAINDER){
//				 templateFile = environment.getRequiredProperty("app.email.template.mangerreview.file");
//				 subject = environment.getRequiredProperty("app.email.template.mangerreview.subject");
//				 ccEmail = environment.getRequiredProperty("app.email.template.mangerreview.cc");
//				 emailFrom = manager.getLoginId()+domain;
//				 emailTo = employee.getLoginId()+domain;
//				 empName = employee.getFirstName();
//				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
//				
//			}else if(enumObj != null && enumObj == EmailConstant.MANAGER_REVIEW){
//				
//				 templateFile = environment.getRequiredProperty("app.email.template.mangerreview.file");
//				 subject = environment.getRequiredProperty("app.email.template.mangerreview.subject");
//				 ccEmail = environment.getRequiredProperty("app.email.template.mangerreview.cc");
//				 emailFrom = manager.getLoginId()+domain;
//				 emailTo = employee.getLoginId()+domain;
//				 empName = employee.getFirstName();
//				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
//				 
//			}else if(enumObj != null && enumObj == EmailConstant.MANAGER_FROZEN){
//				
//				 templateFile = environment.getRequiredProperty("app.email.template.mangerfrozen.file");
//				 subject = environment.getRequiredProperty("app.email.template.mangerfrozen.subject");
//				 ccEmail = environment.getRequiredProperty("app.email.template.mangerfrozen.cc");
//				 emailFrom = manager.getLoginId()+domain;
//				 emailTo = employee.getLoginId()+domain;
//				 empName = employee.getFirstName();
//				 subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
//			}
//			
//		
//			Map<String, Object> emailData = new HashMap<>();
//			emailData.put("phase", appraisalPhase.getName());
//			emailData.put("imagePath", image);
//			emailData.put("url", url);
//			emailData.put("empName", empName);
//			emailData.put("managerName", managerName);
//			emailData.put("cycle", appraisalCycle.getName());
//			
//			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
//			sendEmail(emailFrom, emailTo,ccEmail, subject, reportContent);
//			
//		} catch (MessagingException messagingException) {
//			throw new EmailException(messagingException.getMessage(), messagingException);
//		}
	}
	
	private void sendManagerReviewFrozen(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.MANAGER_FROZEN.toString());
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();

			String emailFrom = "";
			String emailTo = "";
			String ccEmail ="";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
				 ccEmail=testMail;
			}else{
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
			// need discussion	
				 ccEmail = emailFrom +mailTemplate.getCcMail();
			}
			
			

			System.out.println(" from:"+ emailFrom+" to "+ emailTo+" ->" +ccEmail);
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
	
	private void sendManagerReviewCompleted(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.MANAGER_REVIEW.toString());
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String empName = employee.getFirstName();
			
			String emailFrom = "";
			String emailTo = "";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
			}
			
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
	
	private void sendManagerToEmployeeRemainder(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.MGR_TO_EMP_REMAINDER.toString());
			
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String empName = employee.getFirstName();
			
			String emailFrom = "";
			String emailTo = "";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
			}
			
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
			
	private void sendHrToEmployeeRemainder(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee hr,Employee employee,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.HR_TO_EMP_REM.toString());
			// to do
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String empName = employee.getFirstName();
			
			String ccmail = "";
			String emailFrom = "";
			String emailTo = "";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
				 ccmail=testMail;
			}else{
				 emailFrom = hr.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
				 ccmail = manager.getLoginId()+domain;
			}
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, ccmail,subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	private void sendHrToManagerRemainderMail(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee hr,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.HR_TO_MGR_REM.toString());
			
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String managerName = manager.getFirstName();
			
			String emailFrom = "";
			String emailTo = "";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = hr.getLoginId()+domain;
				 emailTo = manager.getLoginId()+domain;
			}
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("managerName", managerName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),managerName});
			sendEmail(emailFrom, emailTo, subject, reportContent);
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	
	private void sendChangeManagerMail(AppraisalCycle appraisalCycle, AppraisalPhase appraisalPhase,
			Employee fManager, Employee tManager, Employee employee) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.CHANGE_MGR.toString());
		
			String image = environment.getRequiredProperty("app.email.image");
			String domain = environment.getRequiredProperty("app.email.domain");
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String url = mailTemplate.getButtonUrl();
			String empName = employee.getFirstName();
			String fromManager = fManager.getFirstName();
			String toManager = tManager.getFirstName();
			
			String emailcc = "";
			String emailFrom = "";
			String emailTo = "";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
				 emailcc=testMail;
			}else{
				 emailTo = employee.getLoginId() + domain;
				 emailFrom =  fManager.getLoginId() + domain;;
				 emailcc = tManager.getLoginId()+domain;
			}
			

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
		} catch (MessagingException messagingException) {
			throw new EmailException(messagingException.getMessage(), messagingException);
		}
	}
	
	private void sendApprasialManagerAssign(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			String image = environment.getRequiredProperty("app.email.image");
			String domain=  environment.getRequiredProperty("app.email.domain");
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.EMPLOYEE_ENABLE.toString());
			String templateFile = mailTemplate.getFileName();
			String subject = mailTemplate.getSubject();
			String url = mailTemplate.getButtonUrl();
			String empName = employee.getFirstName();
			
			String emailFrom = "";
			String emailTo = "";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
			}else{
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
			}
			
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

	private void sendUdatedReviewMail(AppraisalCycle appraisalCycle,AppraisalPhase appraisalPhase,Employee employee,Employee manager) throws EmailException {
		try {
			
			MailTemplate mailTemplate =mailDataRepository.findByName(EmailConstant.UPDATE_REVIEW.toString());
			String domain=  environment.getRequiredProperty("app.email.domain");
			String image = environment.getRequiredProperty("app.email.image");
			String url = mailTemplate.getButtonUrl();
			String subject = mailTemplate.getSubject();
			String templateFile = mailTemplate.getFileName();
			String empName = employee.getFirstName();
			
			String emailFrom = "";
			String emailTo = "";
			String ccEmail="";
			
			String enableTestMail = environment.getRequiredProperty("app.email.test");
			String testMail = environment.getRequiredProperty("app.email.emailid");
			
			if(enableTestMail.equalsIgnoreCase("true")){
				 emailFrom = testMail;
				 emailTo = testMail;
				 ccEmail=testMail;
			}else{
				 emailFrom = manager.getLoginId()+domain;
				 emailTo = employee.getLoginId()+domain;
				 ccEmail = mailTemplate.getCcMail();
			}
			
			Map<String, Object> emailData = new HashMap<>();
			emailData.put("phase", appraisalPhase.getName());
			emailData.put("imagePath", image);
			emailData.put("url", url);
			emailData.put("empName", empName);
			String reportContent = emailTemplateEngine.getGeneratedContent(templateFile, emailData);
			subject = MessageFormat.format(subject, new Object[] {appraisalCycle.getName(),appraisalPhase.getName(),empName});
			sendEmail(emailFrom, emailTo, ccEmail,subject, reportContent);
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
			
			if (ccAddresses.indexOf(",") != -1) {
				String[] split = ccAddresses.split(",");
				for (String ccAddress : split) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddress));
				}
			} else {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddresses));
			}

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
	
	public void sendEmployeeSubmitMail(int phaseId,int employeeId,int managerId) {
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
	

	public void sendEmployeeAcceptence(int phaseId, int employeeId,int managerId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		try {
			sendEmployeeAcceptenceMail(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmployeeRejected(int phaseId, int employeeId,int managerId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		try {
			sendEmployeeRejectionMail(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendUdatedReviewMail(int phaseId, int assignedBy, int employeeId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(assignedBy);
		
		try {
			sendUdatedReviewMail(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	public void sendChangeManager(int phaseId, int employeeId,int fromManager,int toManager) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
	
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee fManager=employeeRepository.findByEmployeeId(fromManager);
		Employee tManager=employeeRepository.findByEmployeeId(toManager);
		
		
		try {
			sendChangeManagerMail(appraisalCycle, appraisalPhase,fManager,tManager, employee);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	
	

	public void sendManagerToEmployeeRemainder(int phaseId,int managerId, int employeeId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		
		try {
			sendManagerToEmployeeRemainder(appraisalCycle, appraisalPhase, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendHrToEmployeeRemainder(int phaseId,int hrId, int employeeId,int managerId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee employee=employeeRepository.findByEmployeeId(employeeId);
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		Employee hr=employeeRepository.findByEmployeeId(hrId);
		
		try {
			sendHrToEmployeeRemainder(appraisalCycle, appraisalPhase,hr, employee, manager);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendHrToManagerRemainder(int phaseId,int hrId, int managerId) {
		AppraisalPhase appraisalPhase= appraisalPhaseDataRepository.findById(phaseId);
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		Employee manager=employeeRepository.findByEmployeeId(managerId);
		Employee hr=employeeRepository.findByEmployeeId(hrId);
		
		try {
			sendHrToManagerRemainderMail(appraisalCycle, appraisalPhase,hr,manager);
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

}
