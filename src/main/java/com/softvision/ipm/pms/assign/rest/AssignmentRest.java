package com.softvision.ipm.pms.assign.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.service.AssignmentService;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.common.util.RestUtil;
import com.softvision.ipm.pms.user.model.User;

@RestController
@RequestMapping(value="assignment", produces=MediaType.APPLICATION_JSON_VALUE)
public class AssignmentRest {

	@Autowired private AssignmentService assignmentService;

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

}
