package com.paypal.bfs.test.employeeserv.data;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeIdempotentKeyDataDAO extends
    JpaRepository<EmployeeIdempotentKeyData, Integer> {

  List<EmployeeIdempotentKeyData> findByIdempotentKey(UUID idempotentKey);
}
