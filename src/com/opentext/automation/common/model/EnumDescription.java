package com.opentext.automation.common.model;

public class EnumDescription {
   private String description;
   private String value;

   public EnumDescription(String value, String description) {
      this.value = value;
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }

   public String getValue() {
      return this.value;
   }
}
