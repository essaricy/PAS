package com.softvision.ipm.pms.appraisal.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;

public interface AppraisalCycleDataRepository extends CrudRepository<AppraisalCycle, Integer>{

	List<AppraisalCycle> findAllByOrderByStartDateDesc();

	AppraisalCycle findById(@Param("id") Integer id);

	@Query("select count(*) from appr_cycle where status='ACTIVE' and id != :id")
    int countOfOtherActiveCycles(@Param("id") int id);

	AppraisalCycle findOne(Specification<AppraisalCycle> spec);

	@Query(value="select * from appr_cycle "
		+ "where start_date >= (select start_date from appr_cycle where status='ACTIVE') "
		+ "and (status='ACTIVE' or status='READY') "
		+ "order by appr_cycle.name "
		+ "LIMIT 2 ", nativeQuery=true)
	List<AppraisalCycle> findAssignableCycles();

}
