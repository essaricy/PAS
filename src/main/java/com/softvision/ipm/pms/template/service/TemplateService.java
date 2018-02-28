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
import com.softvision.ipm.pms.template.repo.TemplateHeaderDataRepository;

@Service
public class TemplateService {

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private TemplateHeaderDataRepository templateHeaderDataRepository;

	public List<TemplateDto> getTemplates() {
		return TemplateAssembler.getTemplateDtoList(templateDataRepository.findAll());
	}

	public TemplateDto getTemplate(long id) {
		return TemplateAssembler.getTemplateDto(templateDataRepository.findById(id));
	}

	public TemplateDto update(TemplateDto templateDto) throws ServiceException {
		try {
			if (templateDto == null) {
				throw new ServiceException("Competency Assessment information is not provided.");
			}
			System.out.println("##### templateDto=" + templateDto);
			ValidationUtil.validate(templateDto);
			Template template = TemplateAssembler.getTemplate(templateDto);
			Template savedTemplate = templateDataRepository.save(template);
			Long templateId = template.getId();
			if (templateId == 0) {
				// Create
			} else {
				// Update
				// Delete the header and details.
				templateHeaderDataRepository.deleteByTemplateId(templateId);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
		return templateDto;
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
