package com.opentext.automation.common.autenv.request.put;

public class AUTEnvironmnentParameter {
   public static final String ALM_PARAMETER_ID_FIELD = "id";
   public static final String ALM_PARAMETER_PARENT_ID_FIELD = "parent-id";
   public static final String ALM_PARAMETER_NAME_FIELD = "name";
   public static final String ALM_PARAMETER_VALUE_FIELD = "value";
   private final String id;
   private final String parentId;
   private final String name;
   private String value;
   private String fullPath;

   public AUTEnvironmnentParameter(String id, String parentId, String name) {
      this.id = id;
      this.parentId = parentId;
      this.name = name;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getFullPath() {
      return this.fullPath;
   }

   public void setFullPath(String fullPath) {
      this.fullPath = fullPath;
   }

   public String getId() {
      return this.id;
   }

   public String getParentId() {
      return this.parentId;
   }

   public String getName() {
      return this.name;
   }
}
