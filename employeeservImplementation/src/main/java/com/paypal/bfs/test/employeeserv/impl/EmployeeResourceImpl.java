package com.paypal.bfs.test.employeeserv.impl;

import com.google.common.primitives.Ints;
import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.data.EmployeeDAO;
import com.paypal.bfs.test.employeeserv.data.EmployeeIdempotentKeyData;
import com.paypal.bfs.test.employeeserv.data.EmployeeIdempotentKeyDataDAO;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BfsBusinessException;
import java.util.UUID;
import javax.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

  private static final Logger LOG = LoggerFactory.getLogger(EmployeeResourceImpl.class);

  @Inject
  EmployeeDAO employeeDAO;

  @Inject
  EmployeeIdempotentKeyDataDAO idempotentKeyDataDAO;


  /**
   * Retrieves employee for the supplied id.
   *
   * @param id employee id.
   */
  @Override
  @RequestMapping(value = "/v1/bfs/employees/{id}", method = RequestMethod.GET)
  public ResponseEntity<Employee> employeeGetById(
      @PathVariable("id") String id) {
    int empId;
    try {
      empId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new BfsBusinessException("id must be a number", "id", "path variable",
          HttpStatus.BAD_REQUEST);
    }
    LOG.info("getting employee {}", id);
    Optional<com.paypal.bfs.test.employeeserv.data.Employee> employee1 = employeeDAO.findById(
        empId);

    if (employee1.isPresent()) {
      LOG.info("found employee {}", employee1.get());
      Employee response = prepareResponseEntity(employee1.get());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      LOG.info(" not found employee for {}", id);
      throw new com.paypal.bfs.test.employeeserv.exception.BfsBusinessException(
          "employee not found", "id", "request param",
          HttpStatus.NOT_FOUND);
    }

  }

  /**
   * Creates employee. idempotentKey is a unique id used for making post idempotent
   * valid idempotentKey is UUID version4
   */
  @Override
  @RequestMapping(value = "/v1/bfs/employees", method = RequestMethod.POST)
  public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee,
      @RequestHeader("idempotent-key")
      @Pattern(regexp = "^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$",
          message = "must be valid UUID") UUID idempotentKey) {

    List<EmployeeIdempotentKeyData> idempotentKeyDataList = idempotentKeyDataDAO.
        findByIdempotentKey(idempotentKey);

    com.paypal.bfs.test.employeeserv.data.Employee employee1;
    if (CollectionUtils.isEmpty(idempotentKeyDataList)) {
      LOG.info("creating new employee");
      employee1 = saveEmployeeToRepository(employee, idempotentKey);
      Employee response = prepareResponseEntity(employee1);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } else {
      LOG.info("employee exists in DB");
      employee1 = idempotentKeyDataList.get(0).getEmployee();
      Employee response = prepareResponseEntity(employee1);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }


  }

  private com.paypal.bfs.test.employeeserv.data.Employee saveEmployeeToRepository(Employee employee,
      UUID idempotentKey) {

    com.paypal.bfs.test.employeeserv.data.Employee employeeData = new com.paypal.bfs.test.employeeserv.data.Employee(
        employee.getFirstName(), employee.getLastName());
    long dob;
    try {
      dob = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(employee.getDob()).getTime() / 1000;
    } catch (ParseException e) {
      throw new com.paypal.bfs.test.employeeserv.exception.BfsBusinessException("wrong data format",
          null, null, HttpStatus.BAD_REQUEST);
    }
    com.paypal.bfs.test.employeeserv.data.Address addressData = new com.paypal.bfs.test.employeeserv.data.Address();
    addressData.setLine1(employee.getAddress().getLine1());
    addressData.setLine2(employee.getAddress().getLine2());
    addressData.setCity(employee.getAddress().getCity());
    addressData.setState(employee.getAddress().getState());
    addressData.setCountry(employee.getAddress().getCountry());
    addressData.setZipcode(employee.getAddress().getZipcode());
    employeeData.setAddress(addressData);
    EmployeeIdempotentKeyData idempotentKeyData = new EmployeeIdempotentKeyData();
    idempotentKeyData.setIdempotentKey(idempotentKey);
    employeeData.setIdempotentData(idempotentKeyData);
    employeeData.setDateOfBirth(dob);
    return employeeDAO.save(employeeData);

  }

  private Employee prepareResponseEntity(com.paypal.bfs.test.employeeserv.data.Employee employee) {
    Employee response = new Employee();
    response.setId(employee.getEmployeeId());
    response.setFirstName(employee.getFirstName());
    response.setLastName(employee.getLastName());
    String dob = new java.text.SimpleDateFormat("yyyy-MM-dd")
        .format(new java.util.Date(employee.getDateOfBirth() * 1000));
    Address address = new Address();
    address.setLine1(employee.getAddress().getLine1());
    address.setLine2(employee.getAddress().getLine2());
    address.setCity(employee.getAddress().getCity());
    address.setState(employee.getAddress().getState());
    address.setCountry(employee.getAddress().getCountry());
    address.setZipcode(employee.getAddress().getZipcode());
    response.setAddress(address);
    response.setDob(dob);
    return response;
  }
}
