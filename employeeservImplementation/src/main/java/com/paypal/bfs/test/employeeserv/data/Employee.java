package com.paypal.bfs.test.employeeserv.data;

import javax.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Employee {

  @Column(name = "EMPLOYEE_ID")
  private @Id
  @GeneratedValue
  Integer employeeId;
  private String firstName;
  private String lastName;
  private long dateOfBirth;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id", referencedColumnName = "id")
  private Address address;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
  @JoinColumn(name = "idempotentKey", referencedColumnName = "IDEMPOTENT_KEY")
  private EmployeeIdempotentKeyData idempotentData;

  public Employee(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Employee employee = (Employee) o;
    return Objects.equals(employeeId, employee.employeeId) &&
        Objects.equals(firstName, employee.firstName) &&
        Objects.equals(lastName, employee.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, firstName, lastName);
  }

  @PrePersist
  @PreUpdate
  public void updateIdempotentKeyData() {
    idempotentData.setEmployee(this);
  }

}
