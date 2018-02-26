package com.softvision.ipm.pms.template.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.softvision.ipm.pms.common.validator.NotContainNull;

import lombok.Data;

@Data
/*@SqlResultSetMapping(
		name = "TemplateDataRepository.findAllHeaders",
		entities = @EntityResult(
				entityClass = TemplateHeaderDto.class,
				fields = {
						@FieldResult(name = "id", column = "id"),
						@FieldResult(name = "goalId", column = "goalId"),
						@FieldResult(name = "goalName", column = "goalName"),
						@FieldResult(name = "weightage", column = "weightage")
				}
		)
)
@NamedNativeQuery(
		name = "TemplateHeaderDto.findByTemplateId", 
		query = "select template_header.id as id, goal_ca.id as goalId, goal_ca.name as goalName, template_header.weightage as weightage "
				+ "from template_header "
				+ "inner join template on template.id=template_header.template_id "
				+ "inner join goal_ca on goal_ca.id=template_header.ca_id "
				+ "where template.id=:templateId",
		resultSetMapping = "TemplateDataRepository.findAllHeaders"
)
@Entity*/
public class TemplateHeaderDto implements Serializable {

	private static final long serialVersionUID = 1L;

	//@Id
	private long id;

	private long goalId;

	@NotEmpty(message="Provide a name for template header")
	@NotBlank(message="Provide a name for template header")
	private String goalName;

	private int weightage;

	@NotNull(message="Details are required. At least one detail should be provided")
	@NotEmpty(message="At least one detail must be provided")
	@NotContainNull(message="Details list cannot contain null")
	private List<TemplateDetailDto> details;

	@Override
	public String toString() {
		return "TemplateHeaderDto [id=" + id + ", goalId=" + goalId + ", goalName=" + goalName + ", weightage="
				+ weightage + ", details=" + details + "]";
	}

}
