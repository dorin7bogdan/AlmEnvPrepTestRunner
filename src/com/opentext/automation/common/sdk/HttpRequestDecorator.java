package com.opentext.automation.common.sdk;

import com.opentext.automation.common.SSEException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HttpRequestDecorator {
   public static void decorateHeaderWithUserInfo(Map<String, String> headers, String userName, ResourceAccessLevel resourceAccessLevel) {
      if (headers == null) {
         throw new IllegalArgumentException("header must not be null");
      } else {
         if (resourceAccessLevel.equals(ResourceAccessLevel.PROTECTED) || resourceAccessLevel.equals(ResourceAccessLevel.PRIVATE)) {
            String userHeaderName = resourceAccessLevel.getUserHeaderName();
            String encryptedUserName = getDigestString("MD5", userName);
            if (userHeaderName != null) {
               headers.put(userHeaderName, encryptedUserName);
            }
         }
      }
   }

   private static String getDigestString(String algorithmName, String dataToDigest) {
      try {
         MessageDigest md = MessageDigest.getInstance(algorithmName);
         byte[] digested = md.digest(dataToDigest.getBytes());
         return digestToString(digested);
      } catch (NoSuchAlgorithmException ex) {
         throw new SSEException(ex);
      }
   }

   private static String digestToString(byte[] b) {
      StringBuilder result = new StringBuilder(128);
      for (byte aB : b) {
         result.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
      }

      return result.toString();
   }
}
