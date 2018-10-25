package com.softvision.digital.pms.assign.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.assign.entity.CycleAssignment;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.mapper.AssignmentMapper;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.digital.pms.web.config.MyModelMapper;

public class AssignMapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private AssignmentMapper assignmentMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

	@Test
    public void test_phaseNull() {
        assertNull(assignmentMapper.get((PhaseAssignment) null));
    }

    @Test
    public void test_cycleNull() {
        assertNull(assignmentMapper.get((CycleAssignment) null));
    }

    @Test
    public void test_phaseValid() {
        PhaseAssignment phaseAssignment = new PhaseAssignment();
        phaseAssignment.setAssignedAt(new Date());
        phaseAssignment.setAssignedBy(1136);
        phaseAssignment.setEmployeeId(2006);
        phaseAssignment.setId(13L);
        phaseAssignment.setPhaseId(34);
        phaseAssignment.setScore(4.2);
        phaseAssignment.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.getCode());
        phaseAssignment.setTemplateId(56L);

        EmployeePhaseAssignmentDto employeeAssignmentDto = assignmentMapper.get(phaseAssignment);
        assertNotNull(employeeAssignmentDto);
        assertEquals(phaseAssignment.getAssignedAt(), employeeAssignmentDto.getAssignedAt());
        assertEquals(phaseAssignment.getAssignedBy(), employeeAssignmentDto.getAssignedBy().getEmployeeId());
        assertEquals(phaseAssignment.getEmployeeId(), employeeAssignmentDto.getAssignedTo().getEmployeeId());
        assertEquals((long)phaseAssignment.getId(), (long)employeeAssignmentDto.getAssignmentId());
        assertEquals((Double)phaseAssignment.getScore(), (Double)employeeAssignmentDto.getScore());
        assertEquals(phaseAssignment.getStatus(), employeeAssignmentDto.getStatus());
    }

    @Test
    public void test_cycleValid() {
        CycleAssignment cycleAssignment = new CycleAssignment();
        cycleAssignment.setAssignedAt(new Date());
        cycleAssignment.setAssignedBy(1136);
        cycleAssignment.setCycleId(34);
        cycleAssignment.setEmployeeId(2006);
        cycleAssignment.setId(13L);
        cycleAssignment.setScore(4.2);
        cycleAssignment.setStatus(PhaseAssignmentStatus.SELF_APPRAISAL_PENDING.getCode());
        cycleAssignment.setSubmittedTo(2543);

        EmployeeAssignmentDto employeeAssignmentDto = assignmentMapper.get(cycleAssignment);
        assertNotNull(employeeAssignmentDto);
        assertEquals(cycleAssignment.getAssignedAt(), employeeAssignmentDto.getAssignedAt());
        assertEquals(cycleAssignment.getAssignedBy(), employeeAssignmentDto.getAssignedBy().getEmployeeId());
        assertEquals(cycleAssignment.getEmployeeId(), employeeAssignmentDto.getAssignedTo().getEmployeeId());
        assertEquals((long)cycleAssignment.getId(), (long)employeeAssignmentDto.getAssignmentId());
        assertEquals((Double)cycleAssignment.getScore(), (Double)employeeAssignmentDto.getScore());
        assertEquals(cycleAssignment.getStatus(), employeeAssignmentDto.getStatus());
    }

}
