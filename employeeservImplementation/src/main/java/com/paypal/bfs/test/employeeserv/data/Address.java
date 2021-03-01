package com.paypal.bfs.test.employeeserv.data;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Address {

  @Column(name = "id")
  private @Id
  @GeneratedValue
  Integer id;
  private String line1;
  private String line2;
  private String city;
  private String state;
  private String country;
  private String zipcode;
  @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
  private Employee employee;
}
