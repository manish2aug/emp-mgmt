package com.pplflw.empmgmt.service;

import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.representation.EmployeeReadRepresentation;
import com.pplflw.empmgmt.representation.EmployeeWriteRepresentation;

import java.util.List;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
public interface EmployeeService {

    /**
     * Saves an employee
     *
     * @param employee employee payload to save
     * @return returns a read representation of the saved employee
     */
    EmployeeReadRepresentation addEmployee(EmployeeWriteRepresentation employee);

    /**
     * Find an employee by it's unique ID
     *
     * @param id unique id of the employee
     * @return returns a read representation of the found employee
     */
    EmployeeReadRepresentation findEmployeeById(Long id);

    /**
     * Find all existing employee
     *
     * @return returns collection of read representations of the found employees
     */
    List<EmployeeReadRepresentation> findAllEmployees();

    /**
     * Process the state update event as per configured in state machine
     *
     * @param id    unique id of the employee
     * @param event the state change event to take place
     */
    void sendEmployeeEvent(Long id, EmployeeEvent event);
}
