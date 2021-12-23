package com.pplflw.empmgmt.representation;

import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
class EmployeeWriteRepresentationTest {

    @Test
    void toEmployee() {
        EmployeeWriteRepresentation representation =
                EmployeeWriteRepresentation.builder().name("A").age(1).build();
        Employee employee = representation.toEmployee();
        assertEquals(EmployeeState.ADDED,employee.getState(),"Initial state should be ADDED");
        assertEquals(1,employee.getAge(),"Age should be same as in representation");
        assertEquals("A",employee.getName(),"Name should be same as in representation");
        assertNull(employee.getId(),"Id should be null at this point");
    }
}