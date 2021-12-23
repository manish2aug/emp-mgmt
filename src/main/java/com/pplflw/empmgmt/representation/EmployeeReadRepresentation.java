package com.pplflw.empmgmt.representation;

import com.pplflw.empmgmt.domain.Employee;
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
public class EmployeeReadRepresentation {
    private long id;
    private String name;
    private int age;
    private String state;

    public EmployeeReadRepresentation(Employee employee) {
        if (employee != null) {
            this.id = employee.getId();
            this.name = employee.getName();
            this.age = employee.getAge();
            this.state = employee.getState().name();
        }
    }
}
