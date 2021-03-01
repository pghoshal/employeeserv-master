package com.paypal.bfs.test.employeeserv.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Integer> {

}
