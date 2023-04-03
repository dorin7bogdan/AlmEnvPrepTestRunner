package com.opentext.automation.common.autenv;

import com.opentext.automation.common.SSEException;
import com.opentext.automation.common.StringUtils;
import com.opentext.automation.common.XPathUtils;
import com.opentext.automation.common.autenv.request.get.GetAutEnvFoldersByIdRequest;
import com.opentext.automation.common.autenv.request.get.GetParametersByAutEnvConfIdRequest;
import com.opentext.automation.common.autenv.request.put.AUTEnvironmnentParameter;
import com.opentext.automation.common.autenv.request.put.PutAutEnvironmentParametersBulkRequest;
import com.opentext.automation.common.model.AutEnvironmentParameterModel;
import com.opentext.automation.common.model.AutEnvironmentParameterType;
import com.opentext.automation.common.sdk.Client;
import com.opentext.automation.common.sdk.Logger;
import com.opentext.automation.common.sdk.Response;
import com.opentext.automation.common.sdk.handler.JsonHandler;
import com.opentext.automation.common.model.AUTEnvironmentFolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.opentext.automation.common.model.AutEnvironmentParameterType.ENVIRONMENT;
import static com.opentext.automation.common.model.AutEnvironmentParameterType.EXTERNAL;

public class AUTEnvironmentParametersManager {
   public static final String PARAMETER_PATH_DELIMITER = "/";
   private Logger logger;
   private Client client;
   private String parametersRootFolderId;
   private String autEnvironmentConfigurationId;
   private Map<String, AUTEnvironmnentParameter> parameters;
   private List<AutEnvironmentParameterModel> parametersToAssign;
   private String pathToJsonFile;

   public AUTEnvironmentParametersManager(Client client, List<AutEnvironmentParameterModel> parametersToAssign, String parametersRootFolderId, String autEnvironmentConfigurationId, String pathToJsonFile, Logger logger) {
      this.logger = logger;
      this.client = client;
      this.parametersToAssign = parametersToAssign;
      this.parametersRootFolderId = parametersRootFolderId;
      this.autEnvironmentConfigurationId = autEnvironmentConfigurationId;
      this.pathToJsonFile = pathToJsonFile;
   }

   public Collection<AUTEnvironmnentParameter> getParametersToUpdate() {
      parameters = getAllParametersByAutEnvConfId();
      Map<String, AUTEnvironmentFolder> parametersFolders = getAllRelevantParametersFolders();

      for (AUTEnvironmnentParameter parameter : parameters.values()) {
         parameter.setFullPath(parametersFolders.get(parameter.getParentId()).getPath()
                 + PARAMETER_PATH_DELIMITER
                 + parameter.getName());

      }

      resolveValuesOfParameters();
      return getResolvedParametersWithAssignedValues();
   }

   public void updateParametersValues(Collection<AUTEnvironmnentParameter> parametersToUpdate) {
      Response response = (new PutAutEnvironmentParametersBulkRequest(this.client, parametersToUpdate)).execute();
      if (!response.isOk()) {
         throw new SSEException(String.format("Failed to update the parameters of AUT Environment Configuration with ID: [%s]", this.autEnvironmentConfigurationId), response.getFailure());
      } else {
         this.logger.log("Submitted all parameters to ALM");
      }
   }

   private Map<String, AUTEnvironmnentParameter> getAllParametersByAutEnvConfId() {
      Map<String, AUTEnvironmnentParameter> parametersMap = new HashMap<String, AUTEnvironmnentParameter>();
      Response response = new GetParametersByAutEnvConfIdRequest(client, autEnvironmentConfigurationId).execute();
      if (!response.isOk()) {
         throw new SSEException(
                 String.format(
                         "Failed to retrieve the parameters of AUT Environment Configuration with ID: [%s]",
                         autEnvironmentConfigurationId),
                 response.getFailure());
      }

      List<Map<String, String>> parameters = XPathUtils.toEntities(response.toString());

      for (Map<String, String> parameter : parameters) {
         String id = parameter.get(AUTEnvironmnentParameter.ALM_PARAMETER_ID_FIELD);
         AUTEnvironmnentParameter param =
                 new AUTEnvironmnentParameter(
                         id,
                         parameter.get(AUTEnvironmnentParameter.ALM_PARAMETER_PARENT_ID_FIELD),
                         parameter.get(AUTEnvironmnentParameter.ALM_PARAMETER_NAME_FIELD));
         parametersMap.put(id, param);
      }

      return parametersMap;
   }

   private Map<String, AUTEnvironmentFolder> getAllRelevantParametersFolders() {
      Map<String, AUTEnvironmentFolder> parametersFolders = new HashMap<String, AUTEnvironmentFolder>();
      StringBuilder foldersToGet = new StringBuilder(parametersRootFolderId);

      for (AUTEnvironmnentParameter parameter : parameters.values()) {
         foldersToGet.append("%20OR%20" + parameter.getParentId());
      }

      Response response = new GetAutEnvFoldersByIdRequest(client, foldersToGet.toString()).execute();
      if (!response.isOk()) {
         throw new SSEException(
                 String.format(
                         "Failed to retrieve parameters folders of AUT Environment Configuration with ID: [%s]",
                         autEnvironmentConfigurationId),
                 response.getFailure());
      }

      List<Map<String, String>> folders = XPathUtils.toEntities(response.toString());

      for (Map<String, String> folder : folders) {
         String folderId = folder.get(AUTEnvironmentFolder.ALM_PARAMETER_FOLDER_ID_FIELD);
         if (!parametersFolders.containsKey(folderId)) {
            AUTEnvironmentFolder autEnvironmentFolder =
                    new AUTEnvironmentFolder(
                            folderId,
                            folder.get(AUTEnvironmentFolder.ALM_PARAMETER_FOLDER_PARENT_ID_FIELD),
                            folder.get(AUTEnvironmentFolder.ALM_PARAMETER_FOLDER_NAME_FIELD));
            parametersFolders.put(folderId, autEnvironmentFolder);
         }
      }

      for (AUTEnvironmentFolder folder : parametersFolders.values()) {
         calculatePaths(folder, parametersFolders);
      }
      return parametersFolders;
   }

   private String calculatePaths(AUTEnvironmentFolder folder, Map<String, AUTEnvironmentFolder> parametersFolders) {
      String calculatedPath;
      if (folder.getId().equals(parametersRootFolderId)) {
         calculatedPath = folder.getName();
      } else {
         calculatedPath =
                 StringUtils.isNullOrEmpty(folder.getPath())
                         ? calculatePaths(
                         parametersFolders.get(folder.getParentId()),
                         parametersFolders)
                         + PARAMETER_PATH_DELIMITER
                         + folder.getName() : folder.getPath();
      }

      folder.setPath(calculatedPath);
      return calculatedPath;
   }

   private void resolveValuesOfParameters() {
      boolean shouldLoadJsonObject = true;
      Object jsonObject = null;
      JsonHandler jsonHandler = new JsonHandler(logger);

      for (AutEnvironmentParameterModel parameter : parametersToAssign) {
         String resolvedValue = "";
         switch (parameter.getParamType()) {
            case ENVIRONMENT:
            case USER_DEFINED:
               resolvedValue = parameter.getValue();
               break;
            case EXTERNAL:
               if (shouldLoadJsonObject) {
                  jsonObject = jsonHandler.load(pathToJsonFile);
                  shouldLoadJsonObject = false;
               }
               resolvedValue =
                       jsonHandler.getValueFromJsonAsString(
                               jsonObject,
                               parameter.getValue(),
                               parameter.isShouldGetOnlyFirstValueFromJson());
               break;
            case UNDEFINED:
               resolvedValue = "";
               break;
         }

         parameter.setResolvedValue(resolvedValue);
      }
   }

   private Collection<AUTEnvironmnentParameter> getResolvedParametersWithAssignedValues() {
      Collection<AUTEnvironmnentParameter> valuesToReturn = new ArrayList<AUTEnvironmnentParameter>();
      for (AutEnvironmentParameterModel paramByModel : parametersToAssign) {
         String parameterPathByModel = paramByModel.getName();
         for (AUTEnvironmnentParameter param : parameters.values()) {
            if (parameterPathByModel.equalsIgnoreCase(param.getFullPath())) {
               String resolvedValue = paramByModel.getResolvedValue();
               param.setValue(resolvedValue);
               logger.log(String.format(
                       "Parameter: [%s] of type: [%s] will get the value: [%s] ",
                       param.getFullPath(),
                       paramByModel.getParamType(),
                       resolvedValue));
               valuesToReturn.add(param);
               break;
            }
         }
      }
      logger.log(parametersToAssign.size() > 0
              ? "Finished assignment of values for all parameters"
              : "There was no parameters to assign");

      return valuesToReturn;
   }
}
