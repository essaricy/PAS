package com.softvision.digital.pms.goal.rest;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.digital.pms.common.constants.AuthorizeConstant;
import com.softvision.digital.pms.common.model.Result;
import com.softvision.digital.pms.common.util.ResultUtil;
import com.softvision.digital.pms.goal.model.GoalDto;
import com.softvision.digital.pms.goal.service.GoalService;

@RestController
@RequestMapping(value="api/goal", produces=MediaType.APPLICATION_JSON_VALUE)
public class GoalRest {

	@Autowired private GoalService goalService;

	@GetMapping(value="list")
    public @ResponseBody List<GoalDto> getGoals() {
		return goalService.getGoals();
    }

	@GetMapping(value="list/{id}")
    public @ResponseBody GoalDto getGoal(@PathVariable(required=true) @NotNull long id) {
		return goalService.getGoal(id);
    }

	@PreAuthorize(AuthorizeConstant.IS_ADMIN)
	@PostMapping(value="update")
    public Result update(@RequestBody(required=true) @NotNull GoalDto goal) {
		try {
			return ResultUtil.getSuccess("Goal template has been saved successfully", goalService.update(goal));
		} catch (Exception exception) {
			return ResultUtil.getFailure(exception);
		}
    }

}
