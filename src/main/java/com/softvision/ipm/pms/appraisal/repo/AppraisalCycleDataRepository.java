package com.softvision.ipm.pms.appraisal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;

//@RepositoryRestResource(collectionResourceRel = "appr_cycles", path = "appr_cycles")
public interface AppraisalCycleDataRepository extends CrudRepository<AppraisalCycle, Long>{

	List<AppraisalCycle> findAll();

	AppraisalCycle findById(@Param("id") Long id);
	
	
	@Query("select count(*) from appr_cycle where status='ACTIVE' and id != :searchTerm")
    int  countOfActive(@Param("searchTerm") long searchTerm);

	AppraisalCycle findOneByStatus(String status);

}
