package com.softvision.ipm.pms.email.repo;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.common.exception.EmailException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Repository
public class EmailTemplateRepository {

    @Autowired private Configuration configuration;

    public String getGeneratedContent(String templateName, Map<String, Object> emailData)
			throws EmailException {
		// Freemarker configuration object
		Writer writer = null;
		try {
			// Load template from source folder
			Template template = configuration.getTemplate(templateName.trim());
			writer = new StringWriter();
			template.process(emailData, writer);
			return writer.toString();
		} catch (IOException ioException) {
			throw new EmailException(ioException.getMessage(), ioException);
		} catch (TemplateException templateException) {
			throw new EmailException(templateException.getMessage(), templateException);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
}