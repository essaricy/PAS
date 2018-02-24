package com.softvision.ipm.pms.appraisal.rest;

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

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.common.model.Result;

@RestController
@RequestMapping(value="appraisal", produces=MediaType.APPLICATION_JSON_VALUE)
public class AppraisalRest {

	@Autowired
	private AppraisalService appraisalService;

	@RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody List<AppraisalCycle> getCycles() {
		return appraisalService.getCycles();
    }

	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
    public @ResponseBody AppraisalCycle getCycle(@PathVariable(required=true) @NotNull long id) {
		return appraisalService.getCycle(id);
    }

	@RequestMapping(value="update", method=RequestMethod.POST)
    public Result update(@RequestBody(required=true) @NotNull AppraisalCycle appraisalCycle) {
		Result result = new Result();
		try {
			System.out.println("appraisalCycle= " + appraisalCycle);
			AppraisalCycle updated = appraisalService.update(appraisalCycle);
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
