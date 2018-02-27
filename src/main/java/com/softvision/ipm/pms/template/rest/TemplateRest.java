package com.softvision.ipm.pms.template.rest;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.service.TemplateService;

@RestController
@RequestMapping(value="template", produces=MediaType.APPLICATION_JSON_VALUE)
public class TemplateRest {

	@Autowired private TemplateService templateService;

	@RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody List<TemplateDto> geTemplates() {
		return templateService.getTemplates();
    }

	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
    public @ResponseBody TemplateDto getTemplate(@PathVariable(required=true) @NotNull long id) {
		return templateService.getTemplate(id);
    }

	@RequestMapping(value="save", method=RequestMethod.POST)
    public Result save(@RequestBody(required=true) @NotNull TemplateDto template) {
		Result result = new Result();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// TODO set updatedBy & updatedAt
			template.setUpdatedBy(auth.getPrincipal().toString());
			template.setUpdatedAt(new Date());
			System.out.println("Template= " + template);
			TemplateDto updated = templateService.update(template);
			result.setCode(Result.SUCCESS);
			result.setContent(updated);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

}
