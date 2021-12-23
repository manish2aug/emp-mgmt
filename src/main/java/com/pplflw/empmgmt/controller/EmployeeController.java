package com.pplflw.empmgmt.controller;

import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.representation.EmployeeReadRepresentation;
import com.pplflw.empmgmt.representation.EmployeeWriteRepresentation;
import com.pplflw.empmgmt.representation.ErrorRepresentation;
import com.pplflw.empmgmt.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@RestController
@Slf4j
@RequestMapping(value = "/employees")
@Tag(name = "On-boarding", description = "Employee on-boarding API")
public class EmployeeController {

    @Autowired
    EmployeeService service;

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeReadRepresentation.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRepresentation.class))})})
    @GetMapping
    public List<EmployeeReadRepresentation> findAll() {
        return service.findAllEmployees();
    }

    @Operation(summary = "Add an employees",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = EmployeeWriteRepresentation.class))))
    @PostMapping
    public EmployeeReadRepresentation addEmployee(@RequestBody EmployeeWriteRepresentation employee) {
        return service.addEmployee(employee);
    }

    @Operation(summary = "Get an employees by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeReadRepresentation.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRepresentation.class))})})
    @GetMapping(value = "/{id}")
    public EmployeeReadRepresentation find(@PathVariable("id") Long id) {
        return service.findEmployeeById(id);
    }

    @Operation(summary = "Send event to change state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Event processed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeReadRepresentation.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid event or invalid transition",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRepresentation.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRepresentation.class))})})
    @PutMapping(value = "/{id}/{event}")
    public ResponseEntity<String> sendEvent(@PathVariable("id") Long id, @PathVariable("event") EmployeeEvent event) {
        service.sendEmployeeEvent(id, event);
        return ResponseEntity.accepted().body("ACCEPTED");
    }
}
