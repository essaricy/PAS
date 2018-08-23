package com.softvision.ipm.pms.assess.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;

@Data
@Entity(name="assess_header")
public class AssessHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="assess_header_id_seq",sequenceName="assess_header_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="assess_header_id_seq")
	private Long id;

	private long assignId;

	private int status;

	private Date assessDate;

	private int assessedBy;

	private int stage;

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "assess_header_id", nullable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<AssessDetail> assessDetails;

}
