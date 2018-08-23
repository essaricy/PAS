package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.digital.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.digital.pms.web.config.MyModelMapper;

public class Appraisal_DtoToModel_MapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private AppraisalMapper appraisalMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void test_cycleNull() {
        assertNull(appraisalMapper.getCycle((AppraisalCycleDto) null));
    }

    @Test
    public void test_phaseNull() {
        assertNull(appraisalMapper.getPhase((AppraisalPhaseDto) null));
    }

    @Test
    public void test_cycleValid() {
        AppraisalCycleDto dto = new AppraisalCycleDto();
        dto.setId(10);
        dto.setName("Some Cycle Name");
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setStatus(AppraisalCycleStatus.ACTIVE);

        AppraisalCycle cycle = appraisalMapper.getCycle(dto);
        assertCycle(dto, cycle);
    }

    @Test
    public void test_phaseValid() {
        AppraisalPhaseDto dto = new AppraisalPhaseDto();
        dto.setId(10);
        dto.setName("Some Phase Name");
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());

        AppraisalPhase phase = appraisalMapper.getPhase(dto);
        assertPhase(dto, phase);
    }

    @Test
    public void test_cycleList() {
        int numberOfCycles = 10;
        List<AppraisalCycleDto> dtoList = new ArrayList<>();
        for (int index = 0; index < numberOfCycles; index++) {
            AppraisalCycleDto dto1 = new AppraisalCycleDto();
            dto1.setId(10);
            dto1.setName("Cycle Name " + index);
            dto1.setStartDate(new Date());
            dto1.setEndDate(new Date());
            dto1.setStatus(AppraisalCycleStatus.COMPLETE);
            dtoList.add(dto1);

            int numberOfPhases = 20;
            List<AppraisalPhaseDto> phaseDtoList = new ArrayList<>();
            for (int jindex = 0; jindex < numberOfPhases; jindex++) {
                AppraisalPhaseDto phaseDto = new AppraisalPhaseDto();
                phaseDto.setId(10);
                phaseDto.setName("Some Phase Name " + jindex);
                phaseDto.setStartDate(new Date());
                phaseDto.setEndDate(new Date());
                phaseDtoList.add(phaseDto);
            }
            dto1.setPhases(phaseDtoList);
        }

        List<AppraisalCycle> cycles = appraisalMapper.getCycles(dtoList);
        assertEquals(dtoList.size(), cycles.size());

        for (int index = 0; index < dtoList.size(); index++) {
            AppraisalCycle cycle = cycles.get(index);
            AppraisalCycleDto dto = dtoList.get(index);
            assertCycle(dto, cycle);

            List<AppraisalPhaseDto> phaseDtoList = dto.getPhases();
            List<AppraisalPhase> phases = cycle.getPhases();
            assertEquals(phaseDtoList.size(), phases.size());
            
            for (int jindex = 0; jindex < phases.size(); jindex++) {
                AppraisalPhase phase = phases.get(index);
                AppraisalPhaseDto phaseDto = phaseDtoList.get(index);
                assertPhase(phaseDto, phase);
            }
        }
    }

    @Test
    public void test_phaseList() {
        int numberOfPhases = 20;
        List<AppraisalPhaseDto> dtosList1 = new ArrayList<>();
        for (int jindex = 0; jindex < numberOfPhases; jindex++) {
            AppraisalPhaseDto dto = new AppraisalPhaseDto();
            dto.setId(10);
            dto.setName("Some Phase Name " + jindex);
            dto.setStartDate(new Date());
            dto.setEndDate(new Date());
            dtosList1.add(dto);
        }

        List<AppraisalPhase> phases = appraisalMapper.getPhases(dtosList1);
        assertEquals(dtosList1.size(), phases.size());

        for (int index = 0; index < phases.size(); index++) {
            AppraisalPhase phase = phases.get(index);
            AppraisalPhaseDto phaseDto = dtosList1.get(index);
            assertPhase(phaseDto, phase);
        }
    }

    private void assertCycle(AppraisalCycleDto dto, AppraisalCycle cycle) {
        assertEquals((Integer)dto.getId(), (Integer)cycle.getId());
        assertEquals(dto.getName(), cycle.getName());
        assertEquals(dto.getStartDate(), cycle.getStartDate());
        assertEquals(dto.getEndDate(), cycle.getEndDate());
        assertEquals(dto.getStatus().toString(), cycle.getStatus());
    }

    private void assertPhase(AppraisalPhaseDto dto, AppraisalPhase phase) {
        assertEquals(dto.getId(), phase.getId());
        assertEquals(dto.getName(), phase.getName());
        assertEquals(dto.getStartDate(), phase.getStartDate());
        assertEquals(dto.getEndDate(), phase.getEndDate());
    }

}
