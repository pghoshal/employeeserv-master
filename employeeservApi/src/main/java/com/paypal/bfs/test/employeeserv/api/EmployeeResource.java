package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @RequestMapping("/v1/bfs/employees/{id}")
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") @Pattern(regexp = "^[0-9]*$", message = "must be a number") String id);

    // ----------------------------------------------------------
    // TODO - add a new operation for creating employee resource.
    // ----------------------------------------------------------

    @RequestMapping("/v1/bfs/employees")
    ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee,
        @RequestHeader("idempotent-key")  UUID idempotentKey);
    }
