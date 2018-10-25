package com.softvision.digital.pms.appraisal.rest;

import java.text.MessageFormat;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.digital.pms.appraisal.service.AppraisalService;
import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.ResultUtil;

@RestController
@RequestMapping(value="api/appraisal", produces=MediaType.APPLICATION_JSON_VALUE)
public class AppraisalRest {

	private static final String APPRISAL_CYCLE_HAS_BEEN_CHANGED = "Apprisal cycle has been changed to {0} status";

	@Autowired private AppraisalService appraisalService;

	@GetMapping(value="list")
    public @ResponseBody List<AppraisalCycleDto> getCycles() {
		return appraisalService.getCycles();
    }

	@GetMapping(value="list/{id}")
    public @ResponseBody AppraisalCycleDto getCycle(@PathVariable(required=true) @NotNull Integer id) {
		return appraisalService.getCycle(id);
    }

	@GetMapping(value="get/active")
    public @ResponseBody AppraisalCycleDto getActiveCycle() {
		return appraisalService.getActiveCycle();
    }

	@GetMapping(value="get/assignable")
    public @ResponseBody List<AppraisalCycleDto> getAssignableCycle() {
		return appraisalService.getAssignableCycle();
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PostMapping(value="update")
    public Result update(@RequestBody(required=true) @NotNull AppraisalCycleDto dto) {
		try {
			return ResultUtil.getSuccess("Apprisal cycle has been updated successfully", appraisalService.update(dto));
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PutMapping(value="ready/{id}")
    public Result ready(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.READY);
			return ResultUtil.getSuccess(MessageFormat.format(APPRISAL_CYCLE_HAS_BEEN_CHANGED, AppraisalCycleStatus.READY));
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PutMapping(value="activate/{id}")
    public Result activate(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.ACTIVE);
			return ResultUtil.getSuccess(MessageFormat.format(APPRISAL_CYCLE_HAS_BEEN_CHANGED, AppraisalCycleStatus.ACTIVE));
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PutMapping(value="complete/{id}")
    public Result complete(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.changeStatus(id, AppraisalCycleStatus.COMPLETE);
			return ResultUtil.getSuccess(MessageFormat.format(APPRISAL_CYCLE_HAS_BEEN_CHANGED, AppraisalCycleStatus.COMPLETE));
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@DeleteMapping(value="delete/{id}")
    public Result delete(@PathVariable(required=true) @NotNull Integer id) {
		try {
			appraisalService.delete(id);
			return ResultUtil.getSuccess("Apprisal cycle has been deleted succesfully");
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
	}

}
