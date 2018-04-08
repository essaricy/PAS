package com.softvision.ipm.pms.appraisal.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;

@Component
public class AppraisalMapper {

	@Autowired private ModelMapper mapper;

	public List<AppraisalCycleDto> getCycleList(List<AppraisalCycle> all) {
	    return mapper.map(all, new TypeToken<List<AppraisalCycleDto>>() {}.getType());
	}

	public AppraisalCycleDto getCycle(AppraisalCycle object) {
	    return mapper.map(object, AppraisalCycleDto.class);
	}

	public List<AppraisalPhaseDto> getPhaseList(List<AppraisalPhase> all) {
	    return mapper.map(all, new TypeToken<List<AppraisalPhaseDto>>() {}.getType());
	}

	public AppraisalPhaseDto getPhase(AppraisalPhase object) {
	    return mapper.map(object, AppraisalPhaseDto.class);
	}

    public List<AppraisalCycle> getCycles(List<AppraisalCycleDto> all) {
        return mapper.map(all, new TypeToken<List<AppraisalCycle>>() {}.getType());
    }

	public AppraisalCycle getCycle(AppraisalCycleDto dto) {
	    return mapper.map(dto, AppraisalCycle.class);
	}

	public List<AppraisalPhase> getPhases(List<AppraisalPhaseDto> phases) {
	    return mapper.map(phases, new TypeToken<List<AppraisalPhase>>() {}.getType());
	}

	public AppraisalPhase getPhase(AppraisalPhaseDto dto) {
	    return mapper.map(dto, AppraisalPhase.class);
	}

}
