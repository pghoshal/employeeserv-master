package com.paypal.bfs.test.employeeserv.exception;

import org.springframework.http.HttpStatus;

public class BfsBusinessException extends RuntimeException {

  private BfsBusinessError bfsBusinessError;

  private HttpStatus status;

  public BfsBusinessException(String errorMessage, String name, String type, HttpStatus status) {
    super(errorMessage);
    bfsBusinessError = new BfsBusinessError();
    bfsBusinessError.setError(errorMessage);
    bfsBusinessError.setFieldName(name);
    bfsBusinessError.setType(type);
    this.status = status;
  }

  public BfsBusinessError getBfsBusinessError() {
    return bfsBusinessError;
  }

  public HttpStatus getStatus() {
    return status;
  }

}
