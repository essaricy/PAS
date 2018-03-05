package com.softvision.ipm.pms.assess.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.assess.entity.EmployeePhaseAssessment;

@Repository
public interface AssessmentPhaseDataRepository extends CrudRepository<EmployeePhaseAssessment, Long> {

	EmployeePhaseAssessment findById(Long id);

	List<EmployeePhaseAssessment> findByAssignIdOrderByAssessTypeAsc(Long assignmentId);

}
