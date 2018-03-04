package com.softvision.ipm.pms.appraisal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;

public interface AppraisalCycleDataRepository extends CrudRepository<AppraisalCycle, Integer>{

	List<AppraisalCycle> findAll();

	List<AppraisalCycle> findAllByOrderByStartDateDesc();

	AppraisalCycle findById(@Param("id") Integer id);
	
	@Query("select count(*) from appr_cycle where status='ACTIVE' and id != :searchTerm")
    int  countOfActive(@Param("searchTerm") int searchTerm);

	AppraisalCycle findOneByStatus(String status);

}
