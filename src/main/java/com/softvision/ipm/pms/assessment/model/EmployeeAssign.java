package com.softvision.ipm.pms.assessment.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity(name="emp_assign")
public class EmployeeAssign implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="emp_assign_id_seq",sequenceName="emp_assign_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="emp_assign_id_seq")
	@JsonFormat
	private Long id;

	@NotNull(message="Provide employee number for assignment")
	@JsonFormat
	private String employeeNumber;

	@NotNull(message="Provide appraisal period Id for assignment")
	@JsonFormat
	private Long appraisalPeriodId;

	@NotNull(message="Provide goal template parameter Id for assignment")
	@JsonFormat
	private Long goalCatpId;

	@NotNull(message="Provide assigned by employee number for assignment")
	@JsonFormat
	private String assignedBy;

}
