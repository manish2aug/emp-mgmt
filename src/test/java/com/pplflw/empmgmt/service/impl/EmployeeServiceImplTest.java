package com.pplflw.empmgmt.service.impl;

import com.pplflw.empmgmt.ApplicationConstants;
import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.domain.EmployeeState;
import com.pplflw.empmgmt.repository.EmployeeRepository;
import com.pplflw.empmgmt.representation.EmployeeReadRepresentation;
import com.pplflw.empmgmt.representation.EmployeeWriteRepresentation;
import com.pplflw.empmgmt.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
class EmployeeServiceImplTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl(null, null);
        }
    }

    @Autowired
    private EmployeeService service;

    @MockBean
    private EmployeeRepository repository;

    @MockBean
    private PersistStateMachineHandler persistStateMachineHandler;
    Employee foo = Employee.builder().name("A").age(1).id(1L).state(EmployeeState.ADDED).build();

    @BeforeEach
    void setUp() {

    }

    @Test
    void testFindAllEmployees() {
        Mockito.when(repository.findAll()).thenReturn(Collections.singleton(foo));
        List<EmployeeReadRepresentation> allEmployees = service.findAllEmployees();

        Assertions.assertNotNull(allEmployees);
        Assertions.assertEquals(1, allEmployees.size());
        EmployeeReadRepresentation employeeReadRepresentation = allEmployees.get(0);
        Assertions.assertEquals(1, employeeReadRepresentation.getAge());
        Assertions.assertEquals(1, employeeReadRepresentation.getId());
        Assertions.assertEquals("A", employeeReadRepresentation.getName());
        Assertions.assertEquals("ADDED", employeeReadRepresentation.getState());

    }

    @Test
    void testFindEmployeeById() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(foo));
        EmployeeReadRepresentation employeeReadRepresentation = service.findEmployeeById(1L);

        Assertions.assertNotNull(employeeReadRepresentation);
        Assertions.assertEquals(1, employeeReadRepresentation.getAge());
        Assertions.assertEquals(1, employeeReadRepresentation.getId());
        Assertions.assertEquals("A", employeeReadRepresentation.getName());
        Assertions.assertEquals("ADDED", employeeReadRepresentation.getState());

    }

    @Test
    void testAddEmployee() {
        EmployeeWriteRepresentation writeRepresentation = EmployeeWriteRepresentation.builder().name("B").age(2).build();
        Employee employee = writeRepresentation.toEmployee();
        employee.setId(2L);
        Mockito.when(repository.save(Mockito.any(Employee.class))).thenReturn(employee);
        EmployeeReadRepresentation employeeReadRepresentation = service.addEmployee(writeRepresentation);

        Assertions.assertNotNull(employeeReadRepresentation);
        Assertions.assertEquals(2, employeeReadRepresentation.getAge());
        Assertions.assertEquals(2, employeeReadRepresentation.getId());
        Assertions.assertEquals("B", employeeReadRepresentation.getName());
        Assertions.assertEquals("ADDED", employeeReadRepresentation.getState());
    }

    @Test
    void testSendEmployeeEvent() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(foo));
        Mockito.when(persistStateMachineHandler.handleEventWithState(Mockito.any(Message.class), Mockito.anyString())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> service.sendEmployeeEvent(1L, EmployeeEvent.VALIDATE));
    }

    @Test
    void testSendEmployeeEventEmployeeNotFound() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(persistStateMachineHandler.handleEventWithState(Mockito.any(Message.class), Mockito.anyString())).thenReturn(true);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> service.sendEmployeeEvent(1L, EmployeeEvent.VALIDATE));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatus());
        Assertions.assertEquals(String.format(ApplicationConstants.NO_EMPLOYEE_FOUND_WITH_ID, 1), responseStatusException.getReason());
    }

    @Test
    void testSendEmployeeEventInvalidState() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(foo));
        Mockito.when(persistStateMachineHandler.handleEventWithState(Mockito.any(Message.class), Mockito.anyString())).thenReturn(true);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> service.sendEmployeeEvent(1L, EmployeeEvent.ADD));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatus());
        Assertions.assertEquals(String.format(ApplicationConstants.INVALID_TRANSITION, "ADD"), responseStatusException.getReason());
    }

    @Test
    void testSendEmployeeEventStateTransitionFailure() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(foo));
        Mockito.when(persistStateMachineHandler.handleEventWithState(Mockito.any(Message.class), Mockito.anyString())).thenReturn(false);
        ResponseStatusException responseStatusException = Assertions.assertThrows(ResponseStatusException.class, () -> service.sendEmployeeEvent(1L, EmployeeEvent.VALIDATE));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatus());
        Assertions.assertEquals(ApplicationConstants.INVALID_OR_DISALLOWED_EVENT_SUPPLIED, responseStatusException.getReason());
    }

}