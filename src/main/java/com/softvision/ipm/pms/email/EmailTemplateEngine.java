package com.softvision.ipm.pms.email;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.softvision.ipm.pms.common.constants.EmailConstants;
import com.softvision.ipm.pms.common.exception.EmailException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EmailTemplateEngine {

    private static final Logger LOGGER = Logger.getLogger(EmailTemplateEngine.class);

    @Autowired
    private Configuration configuration;

    @PostConstruct
    private void init() {
        // Freemarker configuration object
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), EmailConstants.TEMPLATE_PATH);
    }
   

    public String getApprasialKickOfMail(Map<String, Object> emailData)
            throws EmailException {
        // Build the data-model
        return getReport(EmailConstants.EMAIL_KICKOFF_TEMPLATE, emailData);
    }
    
    public String getEmployeeAlert(Map<String, Object> emailData)
            throws EmailException {
        // Build the data-model
        return getReport(EmailConstants.EMPLOYEE_ALERT_TEMPLATE, emailData);
    }

  
    public String getReport(String templateName, Map<String, Object> emailData)
            throws EmailException {
        // Freemarker configuration object
        Writer writer = null;
        try {
        	 // Load template from source folder
        	Template template = configuration.getTemplate(templateName);
             writer= new StringWriter();
             template.process(emailData, writer);
              return writer.toString() ;
        } catch (IOException ioException) {
            throw new EmailException(ioException.getMessage(), ioException);
        } catch (TemplateException templateException) {
            throw new EmailException(templateException.getMessage(), templateException);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}