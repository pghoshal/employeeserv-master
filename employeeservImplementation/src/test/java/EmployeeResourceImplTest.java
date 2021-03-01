package com.paypal.bfs.test;


import com.paypal.bfs.test.employeeserv.EmployeeservApplication;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeservApplication.class)
@AutoConfigureMockMvc
public class EmployeeResourceImplTest {

  @InjectMocks
  EmployeeResourceImpl controller;

  @Autowired
  WebApplicationContext context;

  MockMvc mockMvc;

  @Before
  public void initTests() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void testCreateEmployee() throws Exception {
    Random random = new Random();
    String employee = "{\n" +
        "    \"first_name\": \"sdfhsdfdfds\",\n" +
        "    \"last_name\":\"fsdg3fsgsad\",\n" +
        "  \n" +
        "    \"address\":{\n" +
        "    \t\"line1\":\"cbn\",\n" +
        "    \t\"line2\":\"abc\",\n" +
        "    \t\"city\":\"asdfsd\",\n" +
        "    \t\"state\":\"sdfsdfd\",\n" +
        "    \t\"country\":\"afdfsdf\",\n" +
        "    \t\"zipcode\": \"zipcode\"\n" +
        "    },\n" +
        "    \"dob\":\"1989-12-01\"\n" +
        "}";

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/bfs/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(employee).header("idempotent-key", UUID.randomUUID())
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());

  }

  @Test
  public void testCreateEmployeeForInvalidInput() throws Exception {
    Random random = new Random();
    String employee = "{\n" +
        "    \"first_name\": \"sdfhsdfdfdssdfsfs\",\n" +
        "    \"last_name\":\"fsdg3fsgsad\",\n" +
        "  \n" +
        "    \"address\":{\n" +
        "    \t\"line1\":\"cbn\",\n" +
        "    \t\"line2\":\"abc\",\n" +
        "    \t\"city\":\"asdfsd\",\n" +
        "    \t\"state\":\"sdfsdfd\",\n" +
        "    \t\"country\":\"afdfsdf\",\n" +
        "    \t\"zipcode\": \"zipcode\"\n" +
        "    },\n" +
        "    \"dob\":\"1989-12-01\"\n" +
        "}";

    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/bfs/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(employee).header("idempotent-key", UUID.randomUUID())
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

  }

  @Test
  public void testGetEmployeeNonExistent() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/bfs/employees/1")
        .accept(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    Assert.assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());

  }


}