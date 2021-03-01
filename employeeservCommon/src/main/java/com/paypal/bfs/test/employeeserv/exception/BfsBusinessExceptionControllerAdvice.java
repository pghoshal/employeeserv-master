package com.paypal.bfs.test.employeeserv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class BfsBusinessExceptionControllerAdvice {

  @ResponseBody
  @ExceptionHandler(BfsBusinessException.class)
  public ResponseEntity<BfsBusinessError> employeeNotFoundHandler(BfsBusinessException ex) {

    return new ResponseEntity<>(ex.getBfsBusinessError(), ex.getStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  BfsValidationError onConstraintValidationException(ConstraintViolationException e) {
    BfsValidationError error = new BfsValidationError();
    for (ConstraintViolation violation : e.getConstraintViolations()) {
      error.getValidationErrors().add(
          new BfsBusinessError(violation.getPropertyPath().toString(), violation.getMessage(),
              null));
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  BfsValidationError onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    BfsValidationError error = new BfsValidationError();
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      error.getValidationErrors()
          .add(new BfsBusinessError(fieldError.getField(), fieldError.getDefaultMessage(), null));
    }
    return error;
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  BfsBusinessError onIllegalArgumentException(IllegalArgumentException e) {
    BfsValidationError error = new BfsValidationError();
    return new BfsBusinessError(null, e.getMessage(), null);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  BfsBusinessError onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    BfsValidationError error = new BfsValidationError();
    return new BfsBusinessError(e.getName(), e.getMessage(), e.getParameter().getParameterName());
  }



}
