package com.softvision.ipm.pms.assess.rest;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.AssessmentService;


@RestController
@RequestMapping(value="assessment", produces=MediaType.APPLICATION_JSON_VALUE)
public class AssessmentRest {

	@Autowired private AssessmentService assessmentService;

	@RequestMapping(value="list/assign/{assignmentId}", method=RequestMethod.GET)
    public PhaseAssessmentDto getByAssignment(@PathVariable(required=true) @NotNull long assignmentId) {
		return assessmentService.getByAssignment(assignmentId);
    }

}
