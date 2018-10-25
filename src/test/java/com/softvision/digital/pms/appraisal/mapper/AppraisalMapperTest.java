package com.softvision.digital.pms.appraisal.mapper;

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

public class AppraisalMapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private AppraisalMapper appraisalMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void shouldReturn_Null_When_AppraisalCycleDto_isNull() {
        assertNull(appraisalMapper.getCycle((AppraisalCycleDto) null));
    }

    @Test
    public void shouldReturn_Null_When_AppraisalPhaseDto_isNull() {
        assertNull(appraisalMapper.getPhase((AppraisalPhaseDto) null));
    }

    @Test
    public void shouldReturn_AppraisalCycle_When_AppraisalCycleDto_isValid() {
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
    public void shouldReturn_AppraisalPhase_When_AppraisalPhaseDto_isValid() {
        AppraisalPhaseDto dto = new AppraisalPhaseDto();
        dto.setId(10);
        dto.setName("Some Phase Name");
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());

        AppraisalPhase phase = appraisalMapper.getPhase(dto);
        assertPhase(dto, phase);
    }

    @Test
    public void shouldReturn_AppraisalCycleList_When_AppraisalCycleDtoList_isValid() {
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
    public void shouldReturn_AppraisalPhaseList_When_AppraisalPhaseDtoList_isValid() {
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

    @Test
    public void shouldReturn_Null_When_AppraisalCycle_isNull() {
        assertNull(appraisalMapper.getCycle((AppraisalCycle) null));
    }

    @Test
    public void shouldReturn_Null_When_AppraisalPhase_isNull() {
        assertNull(appraisalMapper.getPhase((AppraisalPhase) null));
    }

    @Test
    public void shouldReturn_AppraisalCycleDto_When_AppraisalCycle_isValid() {
        AppraisalCycle cycle = new AppraisalCycle();
        cycle.setId(10);
        cycle.setName("Some Cycle Name");
        cycle.setStartDate(new Date());
        cycle.setEndDate(new Date());
        cycle.setStatus(AppraisalCycleStatus.ACTIVE.toString());

        AppraisalCycleDto dto = appraisalMapper.getCycle(cycle);
        assertCycle(cycle, dto);
    }

    @Test
    public void shouldReturn_AppraisalPhaseDto_When_AppraisalPhase_isValid() {
        AppraisalPhase phase = new AppraisalPhase();
        phase.setId(10);
        phase.setName("Some Phase Name");
        phase.setStartDate(new Date());
        phase.setEndDate(new Date());

        AppraisalPhaseDto dto = appraisalMapper.getPhase(phase);
        assertPhase(phase, dto);
    }

    @Test
    public void shouldReturn_AppraisalCycleDtoList_When_AppraisalCycleList_isValid() {
        int numberOfCycles = 10;
        List<AppraisalCycle> cycles = new ArrayList<>();
        for (int index = 0; index < numberOfCycles; index++) {
            AppraisalCycle cycle = new AppraisalCycle();
            cycle.setId(index);
            cycle.setName("Cycle Name " + index);
            cycle.setStartDate(new Date());
            cycle.setEndDate(new Date());
            cycle.setStatus(AppraisalCycleStatus.COMPLETE.toString());
            cycles.add(cycle);

            int numberOfPhases = 20;
            List<AppraisalPhase> phases = new ArrayList<>();
            for (int jindex = 0; jindex < numberOfPhases; jindex++) {
                AppraisalPhase phase = new AppraisalPhase();
                phase.setId(jindex);
                phase.setName("Some Phase Name " + jindex);
                phase.setStartDate(new Date());
                phase.setEndDate(new Date());
                phases.add(phase);
            }
            cycle.setPhases(phases);
        }

        List<AppraisalCycleDto> cycleDtoList = appraisalMapper.getCycleList(cycles);
        assertEquals(cycles.size(), cycleDtoList.size());

        for (int index = 0; index < cycles.size(); index++) {
            AppraisalCycleDto dto = cycleDtoList.get(index);
            AppraisalCycle cycle = cycles.get(index);
            assertCycle(cycle, dto);

            List<AppraisalPhase> phases = cycle.getPhases();
            List<AppraisalPhaseDto> phaseDtoList = dto.getPhases();
            assertEquals(phases.size(), phaseDtoList.size());
            
            for (int jindex = 0; jindex < phaseDtoList.size(); jindex++) {
                AppraisalPhaseDto phaseDto = phaseDtoList.get(index);
                AppraisalPhase phase = phases.get(index);
                assertPhase(phase, phaseDto);
            }
        }
    }

    @Test
    public void shouldReturn_AppraisalPhaseDtoList_When_AppraisalPhaseList_isValid() {
        int numberOfPhases = 20;
        List<AppraisalPhase> phases = new ArrayList<>();
        for (int index = 0; index < numberOfPhases; index++) {
            AppraisalPhase phase = new AppraisalPhase();
            phase.setId(index);
            phase.setName("Some Phase Name " + index);
            phase.setStartDate(new Date());
            phase.setEndDate(new Date());
            phases.add(phase);
        }

        List<AppraisalPhaseDto> phaseDtoList = appraisalMapper.getPhaseList(phases);
        assertEquals(phases.size(), phaseDtoList.size());

        for (int index = 0; index < phaseDtoList.size(); index++) {
            AppraisalPhaseDto phaseDto = phaseDtoList.get(index);
            AppraisalPhase phase = phases.get(index);
            assertPhase(phase, phaseDto);
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

    private void assertCycle(AppraisalCycle cycle, AppraisalCycleDto dto) {
        assertEquals((Integer)cycle.getId(), (Integer)dto.getId());
        assertEquals(cycle.getName(), dto.getName());
        assertEquals(cycle.getStartDate(), dto.getStartDate());
        assertEquals(cycle.getEndDate(), dto.getEndDate());
        assertEquals(cycle.getStatus(), dto.getStatus().toString());
    }

    private void assertPhase(AppraisalPhase phase, AppraisalPhaseDto dto) {
        assertEquals(phase.getId(), dto.getId());
        assertEquals(phase.getName(), dto.getName());
        assertEquals(phase.getStartDate(), dto.getStartDate());
        assertEquals(phase.getEndDate(), dto.getEndDate());
    }

}
