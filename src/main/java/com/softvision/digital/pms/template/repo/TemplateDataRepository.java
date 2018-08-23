package com.softvision.digital.pms.template.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.template.entity.Template;

public interface TemplateDataRepository extends CrudRepository<Template, Long> {

	List<Template> findAll();

	List<Template> findAllByOrderByUpdatedAtDesc();
	
	Optional<Template> findById(Long id);

	List<Template> findAll(Specification<Template> spec);

}
