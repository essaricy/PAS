package com.softvision.ipm.pms.template.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.goal.service.GoalService;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;
import com.softvision.ipm.pms.template.repo.TemplateRepository;
import com.softvision.ipm.pms.template.repo.TemplateSpecs;

@Service
public class TemplateService {

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private GoalService goalService;

	@Autowired private TemplateRepository templateRepository;

	public List<TemplateDto> getTemplates() {
		return TemplateAssembler.getTemplateDtoList(templateDataRepository.findAllByOrderByUpdatedAtDesc());
	}

	public TemplateDto getTemplate(long id) {
		return TemplateAssembler.getTemplateDto(templateDataRepository.findById(id));
	}

	public TemplateDto getNewTemplate() {
		return TemplateAssembler.getTemplateDto(goalService.getActiveGoals());
	}

	@Transactional
	public TemplateDto update(TemplateDto templateDto) throws ServiceException {
		try {
			if (templateDto == null) {
				throw new ServiceException("Competency Assessment information is not provided.");
			}
			ValidationUtil.validate(templateDto);

			List<TemplateHeaderDto> headers = templateDto.getHeaders();
			int totalWeightage=0;

			//for (TemplateHeaderDto header : headers) {
			for (int index = 0; index < headers.size(); index++) {
				TemplateHeaderDto header = headers.get(index);
				int weightage = header.getWeightage();
				totalWeightage += weightage;

				boolean containsApply=false;
				List<TemplateDetailDto> details = header.getDetails();
				for (TemplateDetailDto detail : details) {
					String apply = detail.getApply();
					if (apply != null && apply.equalsIgnoreCase("Y")) {
						containsApply=true;
						// Weightage should not be zero if any apply='Y'.
						if (weightage == 0) {
							throw new ServiceException("There should not be any items applicable if the weightage is zero. Param '" + detail.getParamName() + "' is applied but weightage is zero for '" + header.getGoalName() + "'");
						}
						//break;
					}
				}
				// Weightage should be zero if all apply='N'.
				if (!containsApply && weightage != 0) {
					throw new ServiceException("Weightage should be zero if there are no aparams applied");
				}
				if (weightage == 0) {
					// remove this
					headers.remove(index);
					index--;
				}
			}
			if (totalWeightage != 100) {
				throw new ValidationException("Total Weightage should accumulate to 100%. Its now " + totalWeightage + "%");
			}
			Template template = TemplateAssembler.getTemplate(templateDto);
			templateRepository.save(template);
			templateDto=TemplateAssembler.getTemplateDto(template);
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

	public List<TemplateDto> searchName(String searchString) {
		if (searchString != null) {
			return TemplateAssembler.getTemplateDtoList(templateDataRepository.findAll(TemplateSpecs.searchInName(searchString)));
		}
		return null;
	}

}
