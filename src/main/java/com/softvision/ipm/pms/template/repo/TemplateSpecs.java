package com.softvision.ipm.pms.template.repo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.softvision.ipm.pms.template.entity.Template;

public class TemplateSpecs {

	public static Specification<Template> searchInName(String searchString) {
		return new Specification<Template>() {
			public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				System.out.println("searchString=" + searchString);
				return builder.like(builder.lower(root.get("name")), "%" + searchString.toLowerCase() + "%");
			}
		};
	}

}
