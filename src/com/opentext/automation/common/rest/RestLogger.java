package com.opentext.automation.common.rest;

import com.opentext.automation.common.sdk.Logger;

public class RestLogger implements Logger {
   public void log(String message) {
      if (message != null && !message.isEmpty()) {
         System.out.println(message);
      }
   }
}
