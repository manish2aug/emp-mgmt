package com.pplflw.empmgmt.config;

import com.pplflw.empmgmt.ApplicationConstants;
import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeState;
import com.pplflw.empmgmt.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
public class StateChangeListener implements PersistStateChangeListener {

    @Autowired
    private EmployeeRepository entityRepository;

    @Override
    public void onPersist(State<String, String> state,
                          Message<String> message,
                          Transition<String, String> transition,
                          StateMachine<String, String> stateMachine) {
        System.out.println("calling onPersist");
        if (message != null && message.getHeaders().containsKey(ApplicationConstants.ENTITY_HEADER)) {
            Employee entity = message.getHeaders().get(ApplicationConstants.ENTITY_HEADER, Employee.class);
            assert entity != null;
            entity.setState(EmployeeState.valueOf(state.getId()));
            log.info("Persisting: the new entity.. {}", entity);
            entityRepository.save(entity);
        }
    }
}
