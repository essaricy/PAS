package com.softvision.ipm.pms.template.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;

@Service
public class TemplateService {

	@Autowired
	private TemplateDataRepository templateDataRepository;

	public List<TemplateDto> getTemplates() {
		List<TemplateDto> templates = TemplateAssembler.getTemplates(templateDataRepository.findAll());
		for (TemplateDto template : templates) {
			//template.setHeaders(templateDataRepository.findByTemplateId(template.getId()));
		}
		return templates;
	}

	public TemplateDto getTemplate(Long id) {
		return TemplateAssembler.getTemplate(templateDataRepository.findById(id));
	}

	public Template update(Template template) throws ServiceException {
		try {
			if (template == null) {
				throw new ServiceException("Competency Assessment information is not provided.");
			}
			ValidationUtil.validate(template);
			return templateDataRepository.save(template);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void delete(Long id) throws ServiceException {
		try {
			templateDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
