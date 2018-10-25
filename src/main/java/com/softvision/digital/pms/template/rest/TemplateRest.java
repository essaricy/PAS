package com.softvision.digital.pms.template.rest;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.RestUtil;
import com.softvision.digital.pms.common.util.ResultUtil;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.template.model.TemplateDto;
import com.softvision.digital.pms.template.service.TemplateService;

@RestController
@RequestMapping(value="api/template", produces=MediaType.APPLICATION_JSON_VALUE)
public class TemplateRest {

	@Autowired private TemplateService templateService;

	@GetMapping(value="list")
    public @ResponseBody List<TemplateDto> geTemplates() {
		return templateService.getTemplates();
    }

	@GetMapping(value="list/{id}")
    public @ResponseBody TemplateDto getTemplate(@PathVariable(required=true) @NotNull long id) {
		return templateService.getTemplate(id);
    }

	@GetMapping(value="new")
    public @ResponseBody TemplateDto getNewTemplate() {
		return templateService.getNewTemplate();
    }

	@GetMapping(value="copy/{id}")
    public @ResponseBody TemplateDto copyTemplate(@PathVariable(required = true) @NotNull long id)
            throws ServiceException {
        return templateService.copyTemplate(id);
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER_OR_ADMIN)
	@PostMapping(value="save")
    public Result save(@RequestBody(required=true) @NotNull TemplateDto template) {
		try {
			// set updatedBy & updatedAt
			int loggedInEmployeeId = RestUtil.getLoggedInEmployeeId();
			EmployeeDto updatedBy = new EmployeeDto();
			updatedBy.setEmployeeId(loggedInEmployeeId);
			template.setUpdatedBy(updatedBy);
			template.setUpdatedAt(new Date());
			TemplateDto updated = templateService.update(template);
			return ResultUtil.getSuccess("Template has been saved successfully", updated);
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@GetMapping(value="search/byName/{searchString}")
	public @ResponseBody List<TemplateDto> search(
    		@PathVariable(value="searchString", required=true) String searchString) {
		return templateService.searchName(searchString);
    }

	@PreAuthorize(AuthorizeConstant.IS_MANAGER_OR_ADMIN)
	@DeleteMapping(value="delete/{id}")
    public @ResponseBody Result delete(@PathVariable(required=true) @NotNull @Min(1) long id) {
		try {
			templateService.delete(id);
			return ResultUtil.getSuccess("Template has been deleted successfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

}
