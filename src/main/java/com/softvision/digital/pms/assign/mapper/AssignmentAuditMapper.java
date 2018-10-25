package com.softvision.digital.pms.assign.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.assign.entity.AssignmentAudit;
import com.softvision.digital.pms.assign.model.AssignmentAuditDto;

@Component
public class AssignmentAuditMapper {

    @Autowired ModelMapper mapper;

    public AssignmentAuditDto get(AssignmentAudit assignmentAudit) {
        return mapper.map(assignmentAudit, AssignmentAuditDto.class);
    }

    public List<AssignmentAuditDto> getList(List<AssignmentAudit> all) {
		return mapper.map(all, new TypeToken<List<AssignmentAuditDto>>() {}.getType());
	}

}
