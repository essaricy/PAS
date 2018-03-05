package com.softvision.ipm.pms.goal.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.goal.entity.Goal;

public interface GoalDataRepository extends CrudRepository<Goal, Long>{

	List<Goal> findAll();

	Goal findById(@Param("id") long id);

}
