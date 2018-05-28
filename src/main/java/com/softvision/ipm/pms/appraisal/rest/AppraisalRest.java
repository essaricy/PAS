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
import com.softvision.ipm.pms.common.util.ResultUtil;

@RestController
@RequestMapping(value="api/appraisal", produces=MediaType.APPLICATION_JSON_VALUE)
public class AppraisalRest {

	@Autowired private AppraisalService appraisalService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public @ResponseBody List<AppraisalCycleDto> getCycles() {
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		return cycles;
    }

	@RequestMapping(value="list/{id}", method=RequestMethod.GET)
    public @ResponseBody AppraisalCycleDto getCycle(@PathVariable(required=true) @NotNull Integer id) {
		return appraisalService.getCycle(id);
    }

	@RequestMapping(value="get/active", method=RequestMethod.GET)
    public @ResponseBody AppraisalCycleDto getActiveCycle() {
		return appraisalService.getActiveCycle();
    }

	@RequestMapping(value="get/assignable", method=RequestMethod.GET)
    public @ResponseBody List<AppraisalCycleDto> getAssignableCycle() {
		return appraisalService.getAssignableCycle();
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="update", method=RequestMethod.POST)
    public Result update(@RequestBody(required=true) @NotNull AppraisalCycleDto dto) {
		try {
			return ResultUtil.getSuccess("Apprisal cycle has been updated successfully", appraisalService.update(dto));
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="ready/{id}", method=RequestMethod.PUT)
    public Result ready(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.READY);
			return ResultUtil.getSuccess("Apprisal cycle has been changed to " + AppraisalCycleStatus.READY + " status");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="activate/{id}", method=RequestMethod.PUT)
    public Result activate(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.ACTIVE);
			return ResultUtil.getSuccess("Apprisal cycle has been changed to " + AppraisalCycleStatus.ACTIVE + " status");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@RequestMapping(value="complete/{id}", method=RequestMethod.PUT)
    public Result complete(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.COMPLETE);
			return ResultUtil.getSuccess("Apprisal cycle has been changed to " + AppraisalCycleStatus.COMPLETE + " status");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="delete/{id}", method=RequestMethod.DELETE)
    public Result delete(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.delete(id);
			return ResultUtil.getSuccess("Apprisal cycle has been deleted succesfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
	}

}
