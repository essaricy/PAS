package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;
import java.util.Date;

import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.goal.model.GoalDto;

import lombok.Data;

@Data
public class EmployeeAssessmentDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long assessmentId;

	private int assessType;

	private Date assessDate;

	private Employee assessedBy;

	private GoalDto goal;

	private double rating;

	private double score;

	private String comments;

}
