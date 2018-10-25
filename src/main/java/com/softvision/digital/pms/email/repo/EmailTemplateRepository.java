package com.softvision.digital.pms.email.repo;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Repository
public class EmailTemplateRepository {

    @Autowired private Configuration configuration;

	public String getGeneratedContent(String templateName, Map<String, Object> emailData) throws IOException, TemplateException {
		// Freemarker configuration object
		try (Writer writer = new StringWriter()) {
			// Load template from source folder
			Template template = configuration.getTemplate(templateName.trim());
			template.process(emailData, writer);
			return writer.toString();
		}
	}
}