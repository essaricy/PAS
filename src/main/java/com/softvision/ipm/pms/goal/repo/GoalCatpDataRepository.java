package com.softvision.ipm.pms.goal.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.softvision.ipm.pms.goal.entity.GoalCatp;

@RepositoryRestResource(collectionResourceRel = "goal_catps", path = "goal_catps")
public interface GoalCatpDataRepository extends CrudRepository<GoalCatp, Long>{

	List<GoalCatp> findById(@Param("id") Long id);

}
