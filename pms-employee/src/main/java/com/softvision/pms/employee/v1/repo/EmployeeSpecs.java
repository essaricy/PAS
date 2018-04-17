package com.softvision.pms.employee.v1.repo;

import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.softvision.pms.employee.v1.entity.Employee;

public class EmployeeSpecs {

	public static Specification<Employee> searchInName(String searchString) {
		return new Specification<Employee>() {
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		        if (Pattern.matches("\\d+", searchString)) {
		            return builder.and(
		            		builder.equal(builder.upper(root.get("active")), "Y"),
		            		builder.equal(root.get("employeeId"), Integer.valueOf(searchString)));
		        }
		        return builder.and(
	            		builder.equal(builder.upper(root.get("active")), "Y"),
	            		builder.or(
	            				builder.like(builder.lower(root.get("firstName")), searchString.toLowerCase() + "%"),
	    		                builder.like(builder.lower(root.get("lastName")), searchString.toLowerCase() + "%")
	    		        )
	            );
			}
		};
	}

	public static Specification<Employee> searchInDesignationOrBand(String searchString) {
		return new Specification<Employee>() {
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.or(
						builder.like(root.get("designation"), "%" + searchString + "%"),
						builder.like(root.get("band"), "%" + searchString + "%"));
			}
		};
	}

}
