package com.pplflw.empmgmt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class PersistHandlerConfig {

  @Autowired
  private StateMachine<String, String> entityStateMachine;

  @Autowired
  private StateChangeListener stateChangeListener;

  @Bean
  public PersistStateMachineHandler persistStateMachineHandler() {
    PersistStateMachineHandler handler = new PersistStateMachineHandler(entityStateMachine);
    handler.addPersistStateChangeListener(stateChangeListener);
    return handler;
  }
}
