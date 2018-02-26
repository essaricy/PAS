package com.softvision.ipm.pms.goal.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.goal.entity.Goal;

//@RepositoryRestResource(collectionResourceRel = "goal_cas", path = "goal_cas")
public interface GoalCaDataRepository extends CrudRepository<Goal, Long>{

	List<Goal> findAll();

	Goal findById(@Param("id") long id);

}
