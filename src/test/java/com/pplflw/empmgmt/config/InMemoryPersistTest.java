package com.pplflw.empmgmt.config;

import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.domain.EmployeeState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest
public class InMemoryPersistTest {

    @Autowired
    private StateMachinePersister<EmployeeState, EmployeeEvent, UUID> persister;

    @Autowired
    private StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;

    @Test
    public void testInMemoryPersist() throws Exception {
        // Arrange
        StateMachine<EmployeeState, EmployeeEvent> firstStateMachine = stateMachineFactory.getStateMachine();
        firstStateMachine.sendEvent(EmployeeEvent.ADD);
        firstStateMachine.sendEvent(EmployeeEvent.VALIDATE);

        StateMachine<EmployeeState, EmployeeEvent> secondStateMachine = stateMachineFactory.getStateMachine();

        // Check Precondition
        Assertions.assertThat(firstStateMachine.getState().getId()).isEqualTo(EmployeeState.ADDED);
        Assertions.assertThat((boolean) firstStateMachine.getExtendedState().getVariables().get("deployed"))
                .isEqualTo(true);
        Assertions.assertThat(secondStateMachine.getState().getId()).isEqualTo(EmployeeState.IN_CHECK);
        Assertions.assertThat(secondStateMachine.getExtendedState().getVariables().get("deployed")).isNull();

        // Act
        persister.persist(firstStateMachine, firstStateMachine.getUuid());
        persister.restore(secondStateMachine, firstStateMachine.getUuid());

        // Asserts
        Assertions.assertThat(secondStateMachine.getState().getId()).isEqualTo(EmployeeState.APPROVED);
        Assertions.assertThat((boolean) secondStateMachine.getExtendedState().getVariables().get("deployed"))
                .isEqualTo(true);
    }
}