package com.softvision.ipm.pms.assign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.assign.model.BulkAssignmentDto;
import com.softvision.ipm.pms.assign.service.AssignmentService;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.model.Result;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.mapper.EmployeeMapper;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;
import static org.mockito.Matchers.anyObject;

public class BulkAssignmentTest {

    @InjectMocks private AssignmentService assignmentService;

    @Mock private AppraisalCycleDataRepository appraisalCycleDataRepository;

    @Mock private AppraisalPhaseDataRepository appraisalPhaseDataRepository;

    @Mock private TemplateDataRepository templateDataRepository;

    @Mock private EmployeeDataRepository employeeDataRepository;

    @Mock private EmployeeMapper employeeMapper;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule(); 

    @Rule public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void shouldThrowServiceException_WithMessage_NoAssignmentsProvided() throws ServiceException {
        expectedEx.expect(ServiceException.class);
        expectedEx.expectMessage("No Assignments are provided");

        assignmentService.bulkAssign(null);
    }

    @Test
    public void shouldThrowValidationException_WithMessage_Invalid_assignedBy() throws ServiceException {
        expectedEx.expect(ValidationException.class);
        //expectedEx.expectMessage("Invalid employee Id for assignedBy");

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        assignmentService.bulkAssign(bulkAssignmentDto);
    }

    @Test
    public void shouldThrowValidationException_WithMessage_NoEmployees() throws ServiceException {
        expectedEx.expect(ValidationException.class);
        expectedEx.expectMessage("At least one employeeId must be provided");

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        assignmentService.bulkAssign(bulkAssignmentDto);
    }

    @Test
    public void shouldThrowServiceException_WithMessage_NoCycle() throws ServiceException {
        expectedEx.expect(ServiceException.class);
        expectedEx.expectMessage("There is no such cycle with the given id");

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(0);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        assignmentService.bulkAssign(bulkAssignmentDto);
    }

    @Test
    public void shouldThrowServiceException_WithMessage_NoPhase() throws ServiceException {
        Mockito.when(appraisalCycleDataRepository.findById(10)).thenReturn(getMockCycle());

        expectedEx.expect(ServiceException.class);
        expectedEx.expectMessage("There is no such phase with the given id");

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        bulkAssignmentDto.setCycleId(10);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(0);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        assignmentService.bulkAssign(bulkAssignmentDto);
    }

    @Test
    public void shouldThrowServiceException_WithMessage_READYorACTIVE() throws ServiceException {
        Mockito.when(appraisalCycleDataRepository.findById(10)).thenReturn(getMockCycle());
        Mockito.when(appraisalPhaseDataRepository.findById(20)).thenReturn(getMockPhase());

        expectedEx.expect(ServiceException.class);
        expectedEx.expectMessage("You can assign employees to a cycle when its either READY or ACTIVE");

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        bulkAssignmentDto.setCycleId(10);
        bulkAssignmentDto.setPhaseId(20);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(0);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        assignmentService.bulkAssign(bulkAssignmentDto);
    }

    @Test
    public void shouldThrowServiceException_WithMessage_noTemplate() throws ServiceException {
        AppraisalCycle mockCycle = getMockCycle();
        mockCycle.setStatus(AppraisalCycleStatus.ACTIVE.toString());
        Mockito.when(appraisalCycleDataRepository.findById(10)).thenReturn(mockCycle);
        Mockito.when(appraisalPhaseDataRepository.findById(20)).thenReturn(getMockPhase());

        expectedEx.expect(ServiceException.class);
        expectedEx.expectMessage("There is no such template with the given id");

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        bulkAssignmentDto.setCycleId(10);
        bulkAssignmentDto.setPhaseId(20);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(0);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        assignmentService.bulkAssign(bulkAssignmentDto);
    }

    @Test
    public void shouldReturn_WithMessage_InvalidEmployeeID() throws ServiceException {
        AppraisalCycle mockCycle = getMockCycle();
        mockCycle.setStatus(AppraisalCycleStatus.ACTIVE.toString());
        Mockito.when(appraisalCycleDataRepository.findById(10)).thenReturn(mockCycle);
        Mockito.when(appraisalPhaseDataRepository.findById(20)).thenReturn(getMockPhase());
        Mockito.when(templateDataRepository.findById(30L)).thenReturn(getMockTemplate());

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        bulkAssignmentDto.setCycleId(10);
        bulkAssignmentDto.setPhaseId(20);
        bulkAssignmentDto.setTemplateId(30L);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(0);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        List<Result> results = assignmentService.bulkAssign(bulkAssignmentDto);
        assertNotNull(results);
        assertEquals(1, results.size());
        Result result = results.get(0);
        assertNotNull(result);
        assertEquals(Result.FAILURE, result.getCode());
        assertEquals("Invalid Employee ID", result.getMessage());
    }

    @Test
    public void shouldReturn_WithMessage_EmployeeDoesNotExist() throws ServiceException {
        AppraisalCycle mockCycle = getMockCycle();
        mockCycle.setStatus(AppraisalCycleStatus.ACTIVE.toString());
        Mockito.when(appraisalCycleDataRepository.findById(10)).thenReturn(mockCycle);
        Mockito.when(appraisalPhaseDataRepository.findById(20)).thenReturn(getMockPhase());
        Mockito.when(templateDataRepository.findById(30L)).thenReturn(getMockTemplate());

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        bulkAssignmentDto.setCycleId(10);
        bulkAssignmentDto.setPhaseId(20);
        bulkAssignmentDto.setTemplateId(30L);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(9999);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        List<Result> results = assignmentService.bulkAssign(bulkAssignmentDto);
        assertNotNull(results);
        assertEquals(1, results.size());
        Result result = results.get(0);
        assertNotNull(result);
        assertEquals(Result.FAILURE, result.getCode());
        assertEquals(true, result.getMessage().contains("Employee does not exist"));
    }

    @Test
    public void shouldReturn_WithMessage_Cannot_assign_an_appraisal_to_self() throws ServiceException {
        AppraisalCycle mockCycle = getMockCycle();
        mockCycle.setStatus(AppraisalCycleStatus.ACTIVE.toString());
        Mockito.when(appraisalCycleDataRepository.findById(10)).thenReturn(mockCycle);
        Mockito.when(appraisalPhaseDataRepository.findById(20)).thenReturn(getMockPhase());
        Mockito.when(templateDataRepository.findById(30L)).thenReturn(getMockTemplate());
        Mockito.when(employeeDataRepository.findByEmployeeId(9999)).thenReturn(getMockEmployee(9999));
        Mockito.when(employeeDataRepository.findByEmployeeId(1136)).thenReturn(getMockEmployee(1136));
        Mockito.when(employeeMapper.getEmployeeDto(anyObject())).thenReturn(getEmployeeDto());

        BulkAssignmentDto bulkAssignmentDto = new BulkAssignmentDto();
        bulkAssignmentDto.setAssignedBy(1136);
        bulkAssignmentDto.setCycleId(10);
        bulkAssignmentDto.setPhaseId(20);
        bulkAssignmentDto.setTemplateId(30L);
        List<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(1136);
        bulkAssignmentDto.setEmployeeIds(employeeIds);
        List<Result> results = assignmentService.bulkAssign(bulkAssignmentDto);
        assertNotNull(results);
        assertEquals(1, results.size());
        Result result = results.get(0);
        assertNotNull(result);
        assertEquals(Result.FAILURE, result.getCode());
        assertEquals(true, result.getMessage().contains("Cannot assign an appraisal to self"));
    }

    private EmployeeDto getEmployeeDto() {
        EmployeeDto employee = new EmployeeDto();
        employee.setEmployeeId(1136);
        return employee;
    }

    private AppraisalCycle getMockCycle() {
        AppraisalCycle appraisalCycle = new AppraisalCycle();
        appraisalCycle.setId(10);
        appraisalCycle.setStatus(AppraisalCycleStatus.DRAFT.toString());
        return appraisalCycle;
    }

    private AppraisalPhase getMockPhase() {
        AppraisalPhase appraisalPhase = new AppraisalPhase();
        appraisalPhase.setId(20);
        return appraisalPhase;
    }

    private Template getMockTemplate() {
        Template template = new Template();
        template.setId(30L);
        return template;
    }

    private Employee getMockEmployee(int employeeId) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        return employee;
    }

}
