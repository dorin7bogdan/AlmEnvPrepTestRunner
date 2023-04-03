package com.opentext.automation.common.integration;

public class CommonUtils {
   private CommonUtils() {
   }

   public static boolean doCheck(String... args) {
      String[] var1 = args;
      int var2 = args.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String arg = var1[var3];
         if (arg == null || arg == "" || arg.length() == 0) {
            return false;
         }
      }

      return true;
   }
}
