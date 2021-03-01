package com.paypal.bfs.test.employeeserv.data;

import java.util.UUID;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmployeeIdempotentKeyData {
  @Column(name = "ID")
  private @Id
  @GeneratedValue
  Integer id;

  @Column(name = "IDEMPOTENT_KEY")
  private UUID idempotentKey;

  @OneToOne(fetch = FetchType.LAZY)
  private Employee employee;
}
