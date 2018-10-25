package com.softvision.digital.pms.assign.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.assign.entity.AssignmentAudit;

public interface AssignmentAuditRepository extends CrudRepository<AssignmentAudit, Long> {

	List<AssignmentAudit> findAll();

	@Query(value="select * from audit_phase_assign " + 
			" where phase_id in ( " + 
			" select id from appr_phase where cycle_id = (select id from appr_cycle where status='ACTIVE') " + 
			" ) order by created_ts desc", nativeQuery=true)
	List<AssignmentAudit> findAllInActive();

}
