package com.softvision.ipm.pms.template.service;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.mapper.EmployeeMapper;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.goal.service.GoalService;
import com.softvision.ipm.pms.template.constant.TemplateComparator;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.mapper.TemplateMapper;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;
import com.softvision.ipm.pms.template.repo.TemplateRepository;
import com.softvision.ipm.pms.template.repo.TemplateSpecs;

@Service
public class TemplateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);

	@Autowired private GoalService goalService;

	@Autowired private TemplateRepository templateRepository;

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

	@Autowired private EmployeeDataRepository employeeRepository;

	@Autowired private TemplateMapper templateMapper;

	@Autowired private EmployeeMapper employeeMapper;

	public List<TemplateDto> getTemplates() {
	    List<TemplateDto> templateDtoList = templateMapper.getTemplateDtoList(templateDataRepository.findAll());
	    if (templateDtoList != null && !templateDtoList.isEmpty()) {
	        Collections.sort(templateDtoList, TemplateComparator.BY_NAME);
	        updateTemplateDtoList(templateDtoList);
	    }
		return templateDtoList;
	}

	public TemplateDto getTemplate(long id) {
		TemplateDto templateDto = templateMapper.getTemplateDto(templateDataRepository.findById(id));
		updateTemplateDto(templateDto);
		return templateDto;
	}

	public TemplateDto getNewTemplate() {
		return templateMapper.getTemplateDto(goalService.getActiveGoals());
	}

	public TemplateDto copyTemplate(long id) throws ServiceException {
	    TemplateDto template = getTemplate(id);

	    if (template == null) {
	        throw new ServiceException("There is no such template");
	    }
	    template.setId(0);
	    //template.setName(template.getName() + " - Copy");
	    template.setName(null);

	    List<TemplateHeaderDto> headers = template.getHeaders();
	    headers.forEach(header -> {
	        header.setId(0);
	        header.getDetails().forEach(detail -> {
	            detail.setId(0);
	        });
	    });
        return template;
    }

	@Transactional
	public TemplateDto update(TemplateDto templateDto) throws ServiceException {
		try {
			LOGGER.info("update: START templateDto={}" + templateDto);
			if (templateDto == null) {
				throw new ServiceException("Template information is not provided.");
			}
			ValidationUtil.validate(templateDto);

			long templateId = templateDto.getId();
			if (templateId > 0 && isInUse(templateId)) {
				throw new ServiceException("Template is already in use. Cannot update now.");
			}
			List<TemplateHeaderDto> headers = templateDto.getHeaders();
			int totalWeightage=0;


			for (int index = 0; index < headers.size(); index++) {
				TemplateHeaderDto header = headers.get(index);
				int weightage = header.getWeightage();
				totalWeightage += weightage;

				List<TemplateDetailDto> details = header.getDetails();
				boolean containsApply = details.stream().anyMatch(o -> {
				    String apply = o.getApply();
				    return apply != null && apply.equalsIgnoreCase("Y");
				});
				// Weightage should not be zero if any apply='Y'.
				if (containsApply && weightage == 0) {
                    throw new ServiceException("There should not be any items applicable if the weightage is zero. A param is applied but weightage is zero for '" + header.getGoalName() + "'");
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
			Template template = templateMapper.getTemplate(templateDto);
			templateRepository.save(template);
			templateDto=templateMapper.getTemplateDto(template);
			LOGGER.info("update: END templateDto={}" + templateDto);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
		return templateDto;
	}

	public void delete(Long id) throws ServiceException {
		try {
			LOGGER.info("delete: START id={}" + id);
			if (id > 0 && isInUse(id)) {
				throw new ServiceException("Template is already in use. Cannot delete now.");
			}
			Template template = templateDataRepository.findById(id);
			templateDataRepository.delete(template);
			LOGGER.info("delete: END id={}" + id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public List<TemplateDto> searchName(String searchString) {
		List<TemplateDto> templateDtoList = null;
		if (searchString != null) {
			templateDtoList = templateMapper.getTemplateDtoList(templateDataRepository.findAll(TemplateSpecs.searchInName(searchString)));
			updateTemplateDtoList(templateDtoList);
		}
		return templateDtoList;
	}

	public boolean isInUse(long templateId) throws ServiceException {
		try {
			PhaseAssignment phaseAssignement = phaseAssignmentDataRepository.findFirstByTemplateId(templateId);
			if (phaseAssignement != null) {
				return true;
			}
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
		return false;
	}

	private void updateTemplateDtoList(List<TemplateDto> templateDtoList) {
		if (templateDtoList != null && !templateDtoList.isEmpty()) {
		    templateDtoList.forEach(o -> updateTemplateDto(o));
		}
	}

	private void updateTemplateDto(TemplateDto templateDto) {
		if (templateDto != null) {
			Employee employee = employeeRepository.findByEmployeeId(templateDto.getUpdatedBy().getEmployeeId());
			templateDto.setUpdatedBy(employeeMapper.getEmployeeDto(employee));
		}
	}

}
