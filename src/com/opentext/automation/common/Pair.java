package com.opentext.automation.common;

public class Pair<TFirst, TSecond> {
   private final TFirst _first;
   private final TSecond _second;

   public Pair(TFirst first, TSecond second) {
      this._first = first;
      this._second = second;
   }

   public TFirst getFirst() {
      return this._first;
   }

   public TSecond getSecond() {
      return this._second;
   }
}
