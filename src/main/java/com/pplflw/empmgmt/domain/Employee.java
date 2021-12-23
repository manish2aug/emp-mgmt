package com.pplflw.empmgmt.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private EmployeeState state;
    private String name;
    private int age;
}
