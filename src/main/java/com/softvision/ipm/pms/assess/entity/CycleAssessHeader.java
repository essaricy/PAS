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
@Entity(name="cycle_assess_header")
public class CycleAssessHeader implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="cycle_assess_header_id_seq",sequenceName="cycle_assess_header_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cycle_assess_header_id_seq")
	private Long id;

	private long assignId;

	private int status;

	private Date assessDate;

	private int assessedBy;

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "assess_header_id", nullable = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<CycleAssessDetail> cycleAssessDetails;

	@Override
	public String toString() {
		return "CycleAssessHeader [id=" + id + ", assignId=" + assignId + ", status=" + status + ", assessDate="
				+ assessDate + ", assessedBy=" + assessedBy + ", cycleAssessDetails=" + cycleAssessDetails + "]";
	}

}
