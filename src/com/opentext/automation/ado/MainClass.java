package com.opentext.automation.ado;

import java.util.Arrays;

public class MainClass {
   private static final Integer ExitCode_Error_MissingArgument = 1;
   private static final Integer ExitCode_Error_TaskExecutionInnerException = 4;
   private static final Integer ExitCode_Error_FailedParseArgs = 5;
   private static final Integer ExitCode_Error_FailedExecute = 6;

   public static void main(String[] args) {
      if (args.length < 1) {
         System.out.println("Missing arguments");
         System.exit(ExitCode_Error_MissingArgument);
      }

      AbstractTask t = new LabEnvPrepTask();

      try {
         try {
            t.parseArgs(Arrays.copyOfRange(args, 1, args.length));
         } catch (Throwable var5) {
            System.out.println("Parameter parsing FAILED with error: " + var5.getMessage());
            System.exit(ExitCode_Error_FailedParseArgs);
         }

         try {
            t.execute();
         } catch (Throwable var4) {
            System.out.println("Execution FAILED with error: " + var4.getMessage());
            System.exit(ExitCode_Error_FailedExecute);
         }
      } catch (Throwable var6) {
         System.out.println("Failed run with error: " + var6.getMessage());
         System.exit(ExitCode_Error_TaskExecutionInnerException);
      }
   }
}
