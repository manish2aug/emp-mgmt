package com.pplflw.empmgmt.config;

import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.domain.EmployeeState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableStateMachine(name = "entityStateMachine")
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        Set<String> stringStates = new HashSet<>();
        EnumSet.allOf(EmployeeState.class).forEach(entity -> stringStates.add(entity.name()));
        states.withStates().initial(EmployeeState.ADDED.name()).end(EmployeeState.ACTIVE.name()).states(stringStates);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal().source(EmployeeState.ADDED.name()).target(EmployeeState.IN_CHECK.name()).event(EmployeeEvent.VALIDATE.name()).and().withExternal().source(EmployeeState.IN_CHECK.name()).target(EmployeeState.APPROVED.name()).event(EmployeeEvent.APPROVE.name()).and().withExternal().source(EmployeeState.APPROVED.name()).target(EmployeeState.ACTIVE.name()).event(EmployeeEvent.ACTIVATE.name());
    }
}
