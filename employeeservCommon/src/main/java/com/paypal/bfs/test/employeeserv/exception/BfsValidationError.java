package com.paypal.bfs.test.employeeserv.exception;


import java.util.ArrayList;
import java.util.List;

public class BfsValidationError {

  private List<BfsBusinessError> validationErrors = new ArrayList<>();

  public List<BfsBusinessError> getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(List<BfsBusinessError> validationErrors) {
    this.validationErrors = validationErrors;
  }
}