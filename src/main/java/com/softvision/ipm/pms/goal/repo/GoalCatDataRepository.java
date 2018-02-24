package com.softvision.ipm.pms.goal.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.softvision.ipm.pms.goal.entity.GoalCat;

@RepositoryRestResource(collectionResourceRel = "goal_cats", path = "goal_cats")
public interface GoalCatDataRepository extends CrudRepository<GoalCat, Long>{

	List<GoalCat> findById(@Param("id") Long id);

	List<GoalCat> findByName(@Param("name") String name);

}
