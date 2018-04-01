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
	List<AppraisalPhase> findInActiveNextPhases(@Param("id") Integer id);

	// TODO add condition and ( appr_cycle.status='ACTIVE'), select only active or ready versions 
	@Query(value="select * from appr_phase "
	        + "inner join appr_cycle "
	        + "on appr_cycle.id=appr_phase.cycle_id "
	        + "where appr_phase.start_date = ("
	        + "select date(end_date + (1 * interval '1 day')) next_day from appr_phase where id=:id)",
            nativeQuery = true)
    AppraisalPhase findNextPhase(@Param("id") Integer id);

}
