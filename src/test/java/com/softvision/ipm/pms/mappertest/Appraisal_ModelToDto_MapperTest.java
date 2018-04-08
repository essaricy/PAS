package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Appraisal_ModelToDto_MapperTest {

	@Autowired private AppraisalMapper appraisalMapper;

    @Test
    public void test_cycleNull() {
        assertNull(appraisalMapper.getCycle((AppraisalCycle) null));
    }

    @Test
    public void test_phaseNull() {
        assertNull(appraisalMapper.getPhase((AppraisalPhase) null));
    }

    @Test
    public void test_cycleValid() {
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
    public void test_phaseValid() {
        AppraisalPhase phase = new AppraisalPhase();
        phase.setId(10);
        phase.setName("Some Phase Name");
        phase.setStartDate(new Date());
        phase.setEndDate(new Date());

        AppraisalPhaseDto dto = appraisalMapper.getPhase(phase);
        assertPhase(phase, dto);
    }

    @Test
    public void test_cycleList() {
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
    public void test_phaseList() {
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
