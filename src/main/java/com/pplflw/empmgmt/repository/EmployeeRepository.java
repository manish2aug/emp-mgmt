package com.pplflw.empmgmt.repository;

import com.pplflw.empmgmt.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
