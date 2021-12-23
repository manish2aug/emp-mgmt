package com.pplflw.empmgmt.controller;

import com.pplflw.empmgmt.domain.EmployeeEvent;
import com.pplflw.empmgmt.representation.EmployeeReadRepresentation;
import com.pplflw.empmgmt.representation.EmployeeWriteRepresentation;
import com.pplflw.empmgmt.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    EmployeeReadRepresentation mockEmployeeReadRepresentation =
            EmployeeReadRepresentation.builder()
                    .age(1).name("A").id(1L).state("ADDED").build();
    String exampleEmployeeJson = "{\"name\":\"A\",\"age\":1}";

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(service.findAllEmployees())
                .thenReturn(Collections.singletonList(mockEmployeeReadRepresentation));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/employees").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1,\"name\":\"A\",\"age\":1,\"state\":\"ADDED\"}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void addEmployee() throws Exception {
        EmployeeReadRepresentation mockReadRepresentation
                = EmployeeReadRepresentation.builder().age(1).id(1).name("A").state("ADDED").build();

        Mockito.when(
                service.addEmployee(Mockito.any(EmployeeWriteRepresentation.class))).thenReturn(mockReadRepresentation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/employees")
                .accept(MediaType.APPLICATION_JSON).content(exampleEmployeeJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("{\"id\":1,\"name\":\"A\",\"age\":1,\"state\":\"ADDED\"}",
                response.getContentAsString());
    }

    @Test
    void find() throws Exception {
        Mockito.when(service.findEmployeeById(Mockito.anyLong()))
                .thenReturn(mockEmployeeReadRepresentation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/employees/1").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"name\":\"A\",\"age\":1,\"state\":\"ADDED\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void sendEvent() throws Exception {
        EmployeeReadRepresentation mockReadRepresentation
                = EmployeeReadRepresentation.builder().age(1).id(1).name("A").state("ADDED").build();

        Mockito.doNothing().when(service).sendEmployeeEvent(Mockito.anyLong(), Mockito.any(EmployeeEvent.class));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/employees/1/VALIDATE")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
    }
}