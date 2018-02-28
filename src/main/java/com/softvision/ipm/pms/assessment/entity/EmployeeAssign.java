package com.softvision.ipm.pms.assessment.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.template.entity.Template;

import lombok.Data;

@Data
@Entity(name="emp_assign")
public class EmployeeAssign implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="emp_assign_id_seq",sequenceName="emp_assign_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="emp_assign_id_seq")
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.DETACH, orphanRemoval=true)
	@JoinColumn(name = "employee_number", nullable = false)
	private Employee employee;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.DETACH, orphanRemoval=true)
	@JoinColumn(name = "appraisal_cycle_id", nullable = false)
	private AppraisalCycle cycle;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.DETACH, orphanRemoval=true)
	@JoinColumn(name = "appraisal_phase_id", nullable = false)
	private AppraisalPhase phases;
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.DETACH, orphanRemoval=true)
	@JoinColumn(name = "template_id", nullable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Template template;
	
	private String assignedBy;

	private Date assignedAt;

	@Override
	public String toString() {
		return "EmployeeAssign [id=" + id + ", employee=" + employee + ", cycle=" + cycle + ", phases=" + phases
				+ ", template=" + template + ", assignedBy=" + assignedBy + ", assignedAt=" + assignedAt + "]";
	}

}
