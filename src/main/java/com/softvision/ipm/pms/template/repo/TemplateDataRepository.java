package com.softvision.ipm.pms.template.repo;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.template.entity.Template;

public interface TemplateDataRepository extends CrudRepository<Template, Long> {

	List<Template> findAll();

	Template findById(Long id);

	List<Template> findAll(Specification<Template> spec);

}
