package com.opentext.automation.common.integration;

public class HttpConnectionException extends Exception {
   private String errorMsg;

   public HttpConnectionException() {
   }

   public HttpConnectionException(String msg) {
      super(msg);
      this.errorMsg = msg;
   }

   public String getMessage() {
      return this.errorMsg;
   }
}
