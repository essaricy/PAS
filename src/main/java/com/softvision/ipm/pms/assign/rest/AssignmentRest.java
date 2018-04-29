package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.service.AssignmentService;
import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;

@RestController
@RequestMapping(value = "assignment", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssignmentRest {

	@Autowired private AssignmentService assignmentService;

	@PreAuthorize(AuthorizeConstant.IS_MANAGER)
	@RequestMapping(value = "save/bulk", method = RequestMethod.POST)
	public Result saveBulk(@RequestBody(required = true) @NotNull @NotEmpty BulkAssignmentDto bulkAssignmentDto) {
		Result saveResult = new Result();
		try {
			bulkAssignmentDto.setAssignedBy(RestUtil.getLoggedInEmployeeId());
			List<Result> results = assignmentService.bulkAssign(bulkAssignmentDto);
			saveResult.setCode(Result.SUCCESS);
			if (results != null && !results.isEmpty()) {
				long failedCount = results.stream().filter(o -> Result.FAILURE.equals(o.getCode())).count();
				if (failedCount == 0) {
					saveResult.setMessage("All asignments are successfully assigned for the selected employees");
				} else if (failedCount == results.size()) {
					saveResult.setMessage("All asignments are failed. Look for the individual status for reason");
				} else {
					saveResult.setMessage(
							"Some of the assignments are failed. Look for the individual status for reason");
				}
			}
			saveResult.setContent(results);
		} catch (Exception exception) {
			saveResult.setCode(Result.FAILURE);
			saveResult.setMessage(exception.getMessage());
		}
		return saveResult;
	}

}
