package com.softvision.ipm.pms.appraisal.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;

@RestController
@RequestMapping(value="appraisal", produces=MediaType.APPLICATION_JSON_VALUE)
public class AppraisalRest {

	@Autowired
	private AppraisalService appraisalService;

	@RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody List<AppraisalCycleDto> getCycles() {
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		return cycles;
    }

	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
    public @ResponseBody AppraisalCycleDto getCycle(@PathVariable(required=true) @NotNull Integer id) {
		return appraisalService.getCycle(id);
    }

	@RequestMapping(value="/get/active", method=RequestMethod.GET)
    public @ResponseBody AppraisalCycleDto getActiveCycle() {
		return appraisalService.getActiveCycle();
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="update", method=RequestMethod.POST)
    public Result update(@RequestBody(required=true) @NotNull AppraisalCycleDto dto) {
		Result result = new Result();
		try {
			AppraisalCycleDto updated = appraisalService.update(dto);
			result.setCode(Result.SUCCESS);
			result.setContent(updated);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="/activate/{id}", method=RequestMethod.PUT)
    public Result activate(@PathVariable(required=true) @NotNull Integer id) {
		Result result = new Result();
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.ACTIVE);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@RequestMapping(value="/complete/{id}", method=RequestMethod.PUT)
    public Result complete(@PathVariable(required=true) @NotNull Integer id) {
		Result result = new Result();
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.COMPLETE);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
    public Result delete(@PathVariable(required=true) @NotNull Integer id) {
		Result result = new Result();
		try {
			appraisalService.delete(id);
			result.setCode(Result.SUCCESS);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }

}
