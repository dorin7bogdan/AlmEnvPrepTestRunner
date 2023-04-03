package com.opentext.automation.common.sdk;

public class Base64Encoder {
   private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
   private static int[] _toInt = new int[128];

   public static String encode(byte[] buf) {
      int size = buf.length;
      char[] ar = new char[(size + 2) / 3 * 4];
      int a = 0;

      byte b2;
      byte mask;
      for(int i = 0; i < size; ar[a++] = ALPHABET[b2 & mask]) {
         byte b0 = buf[i++];
         byte b1 = i < size ? buf[i++] : 0;
         b2 = i < size ? buf[i++] : 0;
         mask = 63;
         ar[a++] = ALPHABET[b0 >> 2 & mask];
         ar[a++] = ALPHABET[(b0 << 4 | (b1 & 255) >> 4) & mask];
         ar[a++] = ALPHABET[(b1 << 2 | (b2 & 255) >> 6) & mask];
      }

      switch(size % 3) {
      case 1:
         --a;
         ar[a] = '=';
      case 2:
         --a;
         ar[a] = '=';
      default:
         return new String(ar);
      }
   }

   static {
      for(int i = 0; i < ALPHABET.length; _toInt[ALPHABET[i]] = i++) {
      }

   }
}
