package com.pplflw.empmgmt.representation;

import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
class EmployeeReadRepresentationTest {

    @Test
    void testReadRepresentationConversion() {
        Employee employee =
                Employee.builder()
                        .age(1).name("A").state(EmployeeState.ADDED).id(1L).build();
        EmployeeReadRepresentation employeeReadRepresentation = new EmployeeReadRepresentation(employee);

        assertEquals(EmployeeState.ADDED.name(), employeeReadRepresentation.getState(), "State should be same as in object");
        assertEquals(1, employeeReadRepresentation.getAge(), "Age should be same as in object");
        assertEquals("A", employeeReadRepresentation.getName(), "Name should be same as in object");
        assertEquals(1, employeeReadRepresentation.getId(), "Id should be same as in object");
    }

}