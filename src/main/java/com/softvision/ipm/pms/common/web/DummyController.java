package com.softvision.ipm.pms.common.web;

import java.util.Date;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.model.Result;

@RestController
@RequestMapping("dummy")
public class DummyController {

	@RequestMapping("delay/{status}/{time}")
	public Result delayedResponse(@PathVariable String status, @PathVariable int time) {
		Result result = new Result();
		result.setCode(status);
		try {
			Thread.sleep(time * 1000);
			result.setContent("Dummy content generated on " + new Date());
		} catch (Exception exception) {
			result.setCode(Result.FAILURE);
			result.setMessage(exception.getMessage());
		}
		return result;
	}

}
