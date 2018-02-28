package com.softvision.ipm.pms.assessment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.assessment.assembler.EmployeeAssignAssembler;
import com.softvision.ipm.pms.assessment.entity.EmployeeAssign;
import com.softvision.ipm.pms.assessment.model.EmployeeAssignDto;
import com.softvision.ipm.pms.assessment.repo.EmployeeAssignRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;

@Service
public class EmployeeAssignService {

	@Autowired private EmployeeAssignRepository employeeAssignRepository;

	public List<EmployeeAssignDto> getEmployeeAssigns() {
		return EmployeeAssignAssembler.getAllEmployeeAssigns(employeeAssignRepository.findAll());
	}
	

	public EmployeeAssignDto getTemplate(long id) {
		return EmployeeAssignAssembler.getEmployeeAssignDto(employeeAssignRepository.findById(id));
	}

	
	public EmployeeAssignDto update(EmployeeAssignDto employeeAssignDto) throws ServiceException {
		try {
			if (employeeAssignDto == null) {
				throw new ServiceException("Employee Assignment information is not provided.");
			}
			System.out.println("##### templateDto=" + employeeAssignDto);
		//	ValidationUtil.validate(employeeAssignDto);
			
			EmployeeAssign template = EmployeeAssignAssembler.getEmployeeAssign(employeeAssignDto);
			System.out.println("##### EmployeeAssign=" + template);
			
			Long templateId = template.getId();
			if (templateId == 0) {
				employeeAssignRepository.save(template);
				System.out.println("completed");
			} else {
				// Update
				// Delete the header and details.
				//templateHeaderDataRepository.deleteByTemplateId(templateId);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
		return employeeAssignDto;
	}
//
//	public void delete(Long id) throws ServiceException {
//		try {
//			employeeAssignRepository.delete(id);
//		} catch (Exception exception) {
//			String message = ExceptionUtil.getExceptionMessage(exception);
//			throw new ServiceException(message, exception);
//		}
//	}

}
