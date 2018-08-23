package com.softvision.digital.pms.goal.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.digital.pms.goal.entity.Goal;

public interface GoalDataRepository extends CrudRepository<Goal, Long> {

	List<Goal> findAll();

	Optional<Goal> findById(@Param("id") long id);

}