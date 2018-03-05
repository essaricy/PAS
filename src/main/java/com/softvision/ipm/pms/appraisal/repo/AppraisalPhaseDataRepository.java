package com.softvision.ipm.pms.appraisal.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;

public interface AppraisalPhaseDataRepository extends CrudRepository<AppraisalPhase, Integer>{

	List<AppraisalPhase> findAll();

	AppraisalPhase findById(@Param("id") Integer id);

}
