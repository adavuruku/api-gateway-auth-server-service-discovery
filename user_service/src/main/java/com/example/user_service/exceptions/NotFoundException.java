package com.example.user_service.exceptions;

public class NotFoundException extends RuntimeException {
  private final transient Object[] args;

  public NotFoundException() {
    args = new Object[] {};
  }

  public NotFoundException(String message) {
    super(message);
    args = new Object[] {};
  }

  public NotFoundException(Object[] args) {
    this.args = args;
  }


  public Object[] getArgs() {
    return args;
  }
}
