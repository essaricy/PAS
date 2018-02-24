package com.softvision.ipm.pms.goal.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.goal.entity.GoalCa;

//@RepositoryRestResource(collectionResourceRel = "goal_cas", path = "goal_cas")
public interface GoalCaDataRepository extends CrudRepository<GoalCa, Long>{

	List<GoalCa> findAll();

	GoalCa findById(@Param("id") long id);

}
