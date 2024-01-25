package com.example.user_service.exceptions;


public class RemoteServerException extends RuntimeException {

  private final transient Object[] args;

  public RemoteServerException() {
    args = new Object[] {};
  }

  public RemoteServerException(String message) {
    super(message);
    args = new Object[] {};
  }

  public RemoteServerException(Object[] args) {
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }
}
