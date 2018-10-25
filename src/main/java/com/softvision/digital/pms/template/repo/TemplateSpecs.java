package com.softvision.digital.pms.template.repo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.softvision.digital.pms.template.entity.Template;

public class TemplateSpecs {

	private TemplateSpecs() {}

	public static Specification<Template> searchInName(String searchString) {
		return ((Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
			builder.like(builder.lower(root.get("name")), "%" + searchString.toLowerCase() + "%")
		);
	}

}
