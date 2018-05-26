package com.softvision.ipm.pms.goal.rest;

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

import com.softvision.ipm.pms.common.constants.AuthorizeConstant;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.service.GoalService;

@RestController
@RequestMapping(value="api/goal", produces=MediaType.APPLICATION_JSON_VALUE)
public class GoalRest {

	@Autowired private GoalService goalService;

	@RequestMapping(value="list", method=RequestMethod.GET)
    public @ResponseBody List<GoalDto> getGoals() {
		return goalService.getGoals();
    }

	@RequestMapping(value="list/{id}", method=RequestMethod.GET)
    public @ResponseBody GoalDto getGoal(@PathVariable(required=true) @NotNull long id) {
		return goalService.getGoal(id);
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@RequestMapping(value="update", method=RequestMethod.POST)
    public Result update(@RequestBody(required=true) @NotNull GoalDto goal) {
		Result result = new Result();
		try {
			GoalDto updated = goalService.update(goal);
			result.setCode(Result.SUCCESS);
			result.setContent(updated);
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
    }

}
