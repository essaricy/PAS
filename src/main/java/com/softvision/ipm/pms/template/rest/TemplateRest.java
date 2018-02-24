package com.softvision.ipm.pms.template.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.service.TemplateService;

@RestController
@RequestMapping(value="template", produces=MediaType.APPLICATION_JSON_VALUE)
public class TemplateRest {

	@Autowired private TemplateService templateService;

	@RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody List<Template> geTemplates() {
		return templateService.getTemplates();
    }

	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
    public @ResponseBody Template getTemplate(@PathVariable(required=true) @NotNull long id) {
		return templateService.getTemplate(id);
    }

	@RequestMapping(value="update", method=RequestMethod.POST)
    public Result update(@RequestBody(required=true) @NotNull Template template) {
		Result result = new Result();
		try {
			System.out.println("Template= " + template);
			Template updated = templateService.update(template);
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
