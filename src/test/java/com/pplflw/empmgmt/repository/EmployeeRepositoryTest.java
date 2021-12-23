package com.pplflw.empmgmt.repository;

import com.pplflw.empmgmt.domain.Employee;
import com.pplflw.empmgmt.domain.EmployeeState;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmployeeRepository repository;

    @Test
    void injectedComponentsAreNotNull() {
        Assertions.assertThat(dataSource).isNotNull();
        Assertions.assertThat(jdbcTemplate).isNotNull();
        Assertions.assertThat(entityManager).isNotNull();
        Assertions.assertThat(repository).isNotNull();
    }

    @Test
    void whenSaved_thenFind() {
        Employee employee =
                Employee.builder()
                        .age(1).name("A").state(EmployeeState.ADDED).build();
        repository.save(employee);

        // find all
        Iterable<Employee> all = repository.findAll();
        Assertions.assertThat(all).isNotNull();
        List<Employee> employees = IterableUtils.toList(all);
        assertEquals(1, employees.size());
        Employee employee1 = employees.get(0);
        assertNotNull(employee1);
        assertEquals(EmployeeState.ADDED, employee1.getState());
        assertEquals(1, employee1.getAge());
        assertEquals("A", employee1.getName());
        assertEquals(1, employee1.getId());

        // find by ID
        Optional<Employee> optionalEmployee = repository.findById(1L);
        assertTrue(optionalEmployee.isPresent());
        Employee employee2 = optionalEmployee.get();
        assertNotNull(employee2);
        assertEquals(EmployeeState.ADDED, employee2.getState());
        assertEquals(1, employee2.getAge());
        assertEquals("A", employee2.getName());
        assertEquals(1, employee2.getId());

        // update
        employee.setState(EmployeeState.IN_CHECK);
        repository.save(employee);
        Optional<Employee> optionalEmployee1 = repository.findById(1L);
        assertTrue(optionalEmployee1.isPresent());
        Employee employee3 = optionalEmployee.get();
        assertNotNull(employee3);
        assertNotEquals(EmployeeState.ADDED, employee3.getState());
        assertEquals(1, employee3.getAge());
        assertEquals("A", employee3.getName());
        assertEquals(1, employee3.getId());

        // delete
        repository.deleteById(1L);
        assertTrue(IterableUtils.isEmpty(repository.findAll()));
        assertFalse(repository.findById(1L).isPresent());
    }

}