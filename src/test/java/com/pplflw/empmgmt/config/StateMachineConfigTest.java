package com.pplflw.empmgmt.config;

import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.domain.EmployeeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<EmployeeState, EmployeeEvent> factory;

    @Test
    void test1(){
        StateMachine<EmployeeState, EmployeeEvent> sm = factory.getStateMachine(UUID.randomUUID());
        sm.start();
        System.out.println(sm.getState().toString());
    }
}