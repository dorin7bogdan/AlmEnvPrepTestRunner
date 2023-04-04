package com.opentext.automation.common.model;

public class AUTEnvironmentFolder {
   public static final String ALM_PARAMETER_FOLDER_ID_FIELD = "id";
   public static final String ALM_PARAMETER_FOLDER_PARENT_ID_FIELD = "parent-id";
   public static final String ALM_PARAMETER_FOLDER_NAME_FIELD = "name";
   private final String id;
   private final String name;
   private final String parentId;
   private String path;

   public AUTEnvironmentFolder(String id, String parentId, String name) {
      this.id = id;
      this.parentId = parentId;
      this.name = name;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getParentId() {
      return this.parentId;
   }
}
