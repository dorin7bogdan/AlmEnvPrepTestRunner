package com.opentext.automation.ado;

import com.opentext.automation.common.rest.RestLogger;
import com.opentext.automation.common.model.AutEnvironmentConfigModel;
import com.opentext.automation.common.model.AutEnvironmentParameterModel;
import com.opentext.automation.common.model.AutEnvironmentParameterType;
import com.opentext.automation.common.rest.RestClient;
import com.opentext.automation.common.sdk.AUTEnvironmentBuilderPerformer;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class LabEnvPrepTask extends AbstractTask {
   private String AlmServ;
   private String UserName;
   private String Pass;
   private String Domain;
   private String Project;
   private int EnvId;
   private EnvConfigAction Action;
   private String NewConfName;
   private int UseAsConfId;
   private String PathToJSON;
   private boolean ParamOnlyFirst;
   private final List<AutEnvironmentParameterModel> AutEnvironmentParameters = new ArrayList();

   public void parseArgs(String[] args) throws Exception {
      if (args.length < 1) {
         throw new Exception("'ALM server' parameter missing");
      } else {
         this.AlmServ = args[0];
         if (args.length < 2) {
            throw new Exception("'User name' parameter missing");
         } else {
            this.UserName = args[1];
            if (args.length < 3) {
               throw new Exception("'Password' parameter missing");
            } else {
               try {
                  this.Pass = this.extractPasswordFromParameter(args[2]);
               } catch (Throwable t) {
                  throw new Exception("Failed to extract 'Password' from argument (use 'pass:' prefix)");
               }

               if (args.length < 4) {
                  throw new Exception("'Domain' parameter missing");
               } else {
                  this.Domain = args[3];
                  if (args.length < 5) {
                     throw new Exception("'Project' parameter missing");
                  } else {
                     this.Project = args[4];
                     if (args.length < 6) {
                        throw new Exception("'AUT Environment ID' parameter missing");
                     } else {
                        try {
                           this.EnvId = Integer.valueOf(args[5]);
                        } catch (Throwable t) {
                           throw new Exception("Failed to parse AUT Environment ID parameter (use unsigned integer value)");
                        }

                        if (args.length < 7) {
                           throw new Exception("'New/Existing' parameter missing");
                        } else {
                           try {
                              this.Action = Enum.valueOf(EnvConfigAction.class, args[6].toLowerCase());
                           } catch (Throwable t) {
                              throw new Exception("Failed to parse 'New/Existing' parameter (use 'newconf' or 'existing' value)");
                           }

                           if (args.length < 8) {
                              throw new Exception("'Create new configuration named' parameter missing");
                           } else {
                              try {
                                 this.NewConfName = this.extractvalueFromParameter(args[7], "newnamed:");
                              } catch (Throwable t) {
                                 throw new Exception("Failed to extract 'Create new configuration named' parameter (use 'newnamed:' prefix)");
                              }

                              if (this.Action == EnvConfigAction.existing) {
                                 if (args.length < 10) {
                                    throw new Exception("'Use as existing config with ID' parameter missing");
                                 }

                                 try {
                                    this.UseAsConfId = Integer.valueOf(this.extractvalueFromParameter(args[9].toLowerCase(), "useasexisting:"));
                                 } catch (Throwable t) {
                                    throw new Exception("Failed to parse 'Use as existing config with ID' parameter (use unsigned integer value with 'useasexisting:' prefix)");
                                 }
                              }

                              if (args.length < 11) {
                                 throw new Exception("'Path to JSON file' parameter missing");
                              } else {
                                 try {
                                    this.PathToJSON = this.extractvalueFromParameter(args[10], "jsonpath:");
                                 } catch (Throwable t) {
                                    throw new Exception("Failed to extract 'Path to JSON file' parameter (use 'jsonpath:' preffix)");
                                 }

                                 if (args.length < 12) {
                                    throw new Exception("'Get only the first value...' parameter missing");
                                 } else {
                                    try {
                                       this.ParamOnlyFirst = Boolean.parseBoolean(args[11]);
                                    } catch (Throwable t) {
                                       throw new Exception("Failed to extract 'Get only the first value...' parameter");
                                    }

                                    for(int i = 1; i <= (args.length - 12) / 3; ++i) {
                                       String paramName = this.extractvalueFromParameter(args[12 + (i - 1) * 3 + 1], "parname" + i + ":");
                                       String paramValue = this.extractvalueFromParameter(args[12 + (i - 1) * 3 + 2], "parval" + i + ":");

                                       EnvParamType paramType;
                                       try {
                                          paramType = Enum.valueOf(EnvParamType.class, this.extractvalueFromParameter(args[12 + (i - 1) * 3], "partype" + i + ":").toLowerCase());
                                       } catch (Throwable t) {
                                          throw new Exception("Failed to parse 'Parameter type' of " + i + " parameter value: '" + args[12 + (i - 1) * 3] + "'. Use 'json' or 'environment' or 'manual' value.");
                                       }

                                       AutEnvironmentParameterType type = this.convertType(paramType);
                                       this.AutEnvironmentParameters.add(new AutEnvironmentParameterModel(paramName, paramValue, type, this.ParamOnlyFirst));
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void execute() throws Throwable {
      boolean useExistingAutEnvConf = this.Action == EnvConfigAction.existing;
      RestClient restClient = new RestClient(this.AlmServ, this.Domain, this.Project, this.UserName);
      AutEnvironmentConfigModel autEnvModel = new AutEnvironmentConfigModel(this.AlmServ, this.UserName, this.Pass, this.Domain, this.Project, useExistingAutEnvConf, String.valueOf(this.EnvId), useExistingAutEnvConf ? String.valueOf(this.UseAsConfId) : this.NewConfName, this.PathToJSON, this.AutEnvironmentParameters);
      AUTEnvironmentBuilderPerformer performer = new AUTEnvironmentBuilderPerformer(restClient, new RestLogger(), autEnvModel);
      performer.start();
      String workingDirectory = Paths.get(LabEnvPrepTask.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent().toString();
      File resultFile = new File(Paths.get(workingDirectory, "res", "updateVariable.txt").toString());
      FileUtils.writeStringToFile(resultFile, autEnvModel.getCurrentConfigID(), StandardCharsets.UTF_8);
   }

   private AutEnvironmentParameterType convertType(EnvParamType sourceType) {
      switch(sourceType) {
      case environment:
         return AutEnvironmentParameterType.ENVIRONMENT;
      case manual:
         return AutEnvironmentParameterType.USER_DEFINED;
      case json:
         return AutEnvironmentParameterType.EXTERNAL;
      default:
         return AutEnvironmentParameterType.UNDEFINED;
      }
   }
}
