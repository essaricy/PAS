package com.softvision.ipm.pms.appraisal.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.common.mapper.CustomModelMapper;

public class AppraisalMapper {

    private static ModelMapper mapper = new CustomModelMapper();

	public static List<AppraisalCycleDto> getCycleList(List<AppraisalCycle> all) {
	    return mapper.map(all, new TypeToken<List<AppraisalCycleDto>>() {}.getType());
	}

	public static AppraisalCycleDto getCycle(AppraisalCycle object) {
	    return mapper.map(object, AppraisalCycleDto.class);
	}

	public static List<AppraisalPhaseDto> getPhaseList(List<AppraisalPhase> all) {
	    return mapper.map(all, new TypeToken<List<AppraisalPhaseDto>>() {}.getType());
	}

	public static AppraisalPhaseDto getPhase(AppraisalPhase object) {
	    return mapper.map(object, AppraisalPhaseDto.class);
	}

    public static List<AppraisalCycle> getCycles(List<AppraisalCycleDto> all) {
        return mapper.map(all, new TypeToken<List<AppraisalCycle>>() {}.getType());
    }

	public static AppraisalCycle getCycle(AppraisalCycleDto dto) {
	    return mapper.map(dto, AppraisalCycle.class);
	}

	public static List<AppraisalPhase> getPhases(List<AppraisalPhaseDto> phases) {
	    return mapper.map(phases, new TypeToken<List<AppraisalPhase>>() {}.getType());
	}

	public static AppraisalPhase getPhase(AppraisalPhaseDto dto) {
	    return mapper.map(dto, AppraisalPhase.class);
	}

}
