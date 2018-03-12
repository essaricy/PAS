package com.softvision.ipm.pms.appraisal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;

public interface AppraisalPhaseDataRepository extends CrudRepository<AppraisalPhase, Integer>{

	List<AppraisalPhase> findAll();

	AppraisalPhase findById(@Param("id") Integer id);

	@Query("select ap from appr_phase ap "
			+ "where cycle_id=(select id from appr_cycle ac where status='ACTIVE') "
			+ "and startDate > (select startDate from appr_phase where id = :id)")
	List<AppraisalPhase> findNextPhases(@Param("id") Integer id);
}
