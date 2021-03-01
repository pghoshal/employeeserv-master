package test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import java.util.Random;
import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CreateEmployeeTest {

  private static final String REST_URI
      = "http://localhost:8080/v1/bfs/employees";



  public Response getJsonEmployee(Employee employee, UUID idempotentKey, Client client) {
    return client
        .target(REST_URI)
        .request(MediaType.APPLICATION_JSON).header("idempotent-key", idempotentKey)
        .post(Entity.entity(employee, MediaType.APPLICATION_JSON));


  }

  private Client getClient() {
    return ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class);
  }

  @Test
  public void createEmployeeTestWithAllValidata() {
    Employee employee = new Employee();
    employee.setFirstName("aaaaaaaaaa");
    employee.setLastName("bbbbbbbbbb");
    employee.setDob("1989-10-1");
    Address address = new Address();
    address.setLine1("afdf");
    address.setLine2("sdfe");
    address.setCity("sdfsdf");
    address.setState("afdfs");
    address.setCountry("afdfs");
    address.setZipcode("2324242");
    employee.setAddress(address);

    UUID rd = UUID.randomUUID();
    Response response = getJsonEmployee(employee, rd, getClient());
    Assert.assertEquals(response.getStatus(), HttpStatus.CREATED.value());
  }

  @Test
  public void createEmployeeTestWithInvalidFirstName() throws Exception {
    Employee employee = new Employee();
    employee.setFirstName("aaaa");
    employee.setLastName("bbbbbbbbbb");
    employee.setDob("1989-10-1");
    Address address = new Address();
    address.setLine1("afdf");
    address.setLine2("sdfe");
    address.setCity("sdfsdf");
    address.setState("afdfs");
    address.setCountry("afdfs");
    address.setZipcode("2324242");
    employee.setAddress(address);

    UUID rd = UUID.randomUUID();
    Response response = getJsonEmployee(employee, rd, getClient());
    Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

    com.paypal.bfs.test.employeeserv.exception.BfsValidationError bfsValidationError = new ObjectMapper()
        .readValue(response.readEntity(String.class),
            com.paypal.bfs.test.employeeserv.exception.BfsValidationError.class);
    Assert.assertNotNull(bfsValidationError);
    Assert.assertNotNull(bfsValidationError.getValidationErrors());
    Assert.assertTrue(bfsValidationError.getValidationErrors().size() == 1);
    Assert
        .assertEquals(bfsValidationError.getValidationErrors().get(0).getFieldName(), "firstName");
  }

  @Test
  public void createEmployeeTestWithInvalidLastName() throws Exception {
    Employee employee = new Employee();
    employee.setFirstName("aaaaaaaaaa");
    employee.setLastName("bbbbbbbbbbsdfsfdsfsdf");
    employee.setDob("1989-10-1");
    Address address = new Address();
    address.setLine1("afdf");
    address.setLine2("sdfe");
    address.setCity("sdfsdf");
    address.setState("afdfs");
    address.setCountry("afdfs");
    address.setZipcode("2324242");
    employee.setAddress(address);
    UUID rd = UUID.randomUUID();
    Response response = getJsonEmployee(employee, rd, getClient());
    Assert.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

    com.paypal.bfs.test.employeeserv.exception.BfsValidationError bfsValidationError = new ObjectMapper()
        .readValue(response.readEntity(String.class),
            com.paypal.bfs.test.employeeserv.exception.BfsValidationError.class);
    Assert.assertNotNull(bfsValidationError);
    Assert.assertNotNull(bfsValidationError.getValidationErrors());
    Assert.assertTrue(bfsValidationError.getValidationErrors().size() == 1);
    Assert.assertEquals(bfsValidationError.getValidationErrors().get(0).getFieldName(), "lastName");
  }

  @Test
  public void createEmployeeTestWithOutAddressLine2() throws Exception {
    Employee employee = new Employee();
    employee.setFirstName("aaaaaaaaaa");
    employee.setLastName("bbbbbbbbbbsdfs");
    employee.setDob("1989-10-1");
    Address address = new Address();
    address.setLine1("afdf");
    address.setCity("sdfsdf");
    address.setState("afdfs");
    address.setCountry("afdfs");
    address.setZipcode("2324242234");
    employee.setAddress(address);
    UUID rd = UUID.randomUUID();
    Response response = getJsonEmployee(employee, rd, getClient());
    Assert.assertEquals(response.getStatus(), HttpStatus.CREATED.value());
  }

}
