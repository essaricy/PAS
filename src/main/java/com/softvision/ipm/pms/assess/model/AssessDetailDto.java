package com.softvision.ipm.pms.assess.model;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class AssessDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Min(0)
	private long id;

	@Min(0)
	private long templateHeaderId;

	@Min(value=0, message="Rating min value is {value}")
	@Max(value=5, message="Rating max value is {value}")
	private double rating;

	@NotBlank(message="Comments are mandatory")
	@Size(min=1, max=500, message="Comments must be between {min} and {max} characters")
	private String comments;

	@Min(value=0, message="Score min value is {value}")
	@Max(value=5, message="Score max value is {value}")
	private double score;

	@Override
	public String toString() {
		return "AssessDetail [id=" + id + ", templateHeaderId=" + templateHeaderId + ", rating=" + rating
				+ ", comments=" + comments + ", score=" + score + "]";
	}

}
