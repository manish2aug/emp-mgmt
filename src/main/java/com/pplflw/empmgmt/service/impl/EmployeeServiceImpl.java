package com.pplflw.empmgmt.service.impl;

import com.pplflw.empmgmt.ApplicationConstants;
import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.domain.EmployeeState;
import com.pplflw.empmgmt.repository.EmployeeRepository;
import com.pplflw.empmgmt.representation.EmployeeReadRepresentation;
import com.pplflw.empmgmt.representation.EmployeeWriteRepresentation;
import com.pplflw.empmgmt.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final PersistStateMachineHandler persistStateMachineHandler;
    @Autowired
    private final EmployeeRepository repository;

    @Override
    public List<EmployeeReadRepresentation> findAllEmployees() {
        List<EmployeeReadRepresentation> representations = new ArrayList<>();
        repository.findAll().forEach(a -> representations.add(new EmployeeReadRepresentation(a)));
        return representations;
    }

    @Override
    public EmployeeReadRepresentation addEmployee(EmployeeWriteRepresentation writeRepresentation) {
        return new EmployeeReadRepresentation(repository.save(writeRepresentation.toEmployee()));
    }

    @Override
    public EmployeeReadRepresentation findEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = repository.findById(id);
        if (optionalEmployee.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new EmployeeReadRepresentation(optionalEmployee.get());
    }

    @Override
    public void sendEmployeeEvent(Long id, EmployeeEvent event) {
        Optional<Employee> entity = repository.findById(id);

        if (entity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(ApplicationConstants.NO_EMPLOYEE_FOUND_WITH_ID, id));

        Employee employee = entity.get();

        EmployeeState state = event.getState();
        if (employee.getState() == state)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            ApplicationConstants.INVALID_TRANSITION,
                            event.name()));

        boolean eventWithState = persistStateMachineHandler.handleEventWithState(
                MessageBuilder.withPayload(event.name()).setHeader(ApplicationConstants.ENTITY_HEADER, employee).build(),
                employee.getState().name()
        );

        if (!eventWithState)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ApplicationConstants.INVALID_OR_DISALLOWED_EVENT_SUPPLIED);

    }


}
