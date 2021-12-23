package com.pplflw.empmgmt.representation;

import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeState;
import lombok.*;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeWriteRepresentation {
    private String name;
    private int age;

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setName(this.name);
        employee.setAge(this.age);
        employee.setState(EmployeeState.ADDED);
        return employee;
    }

}
