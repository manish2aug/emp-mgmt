package com.pplflw.empmgmt.domain;

import lombok.Getter;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum EmployeeEvent {
    ADD(EmployeeState.ADDED), VALIDATE(EmployeeState.IN_CHECK), APPROVE(EmployeeState.APPROVED), ACTIVATE(EmployeeState.APPROVED);

    private final EmployeeState state;

    EmployeeEvent(EmployeeState state) {
        this.state = state;
    }
}
