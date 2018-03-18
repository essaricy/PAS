package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.service.AssignmentService;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;

@RestController
@RequestMapping(value="assignment", produces=MediaType.APPLICATION_JSON_VALUE)
public class AssignmentRest {

	@Autowired private AssignmentService assignmentService;

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value="save/bulk", method=RequestMethod.POST)
    public Result saveBulk(@RequestBody(required=true) @NotNull @NotEmpty BulkAssignmentDto bulkAssignmentDto) {
		Result result = new Result();
		try {
			bulkAssignmentDto.setAssignedBy(RestUtil.getLoggedInEmployeeId());
			List<Result> assignResult = assignmentService.bulkAssign(bulkAssignmentDto);
			result.setCode(Result.SUCCESS);
			result.setContent(assignResult);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
			result.setContent(exception);
		}
		return result;
    }
	
	@RequestMapping(value = "cycle/employee-status/{cycleId}", method = RequestMethod.GET)
	public List<EmployeeAssignmentDto> getEmpCycleAppraisalStatus(@PathVariable(required = true) @Min(1) int cycleId) {
		return assignmentService.getAllEmployeeAssignmentsforCycle(cycleId);
	}

	@RequestMapping(value = "phase/employee-status/{cycleId}/{phaseId}", method = RequestMethod.GET)
	public List<EmployeeAssignmentDto> getEmpCycleAppraisalStatus(@PathVariable(required = true) @Min(1) int cycleId,
			@PathVariable(required = true) @Min(1) int phaseId) {
		return assignmentService.getAllEmployeeAssignmentsforPhase(cycleId, phaseId);
	}

}
