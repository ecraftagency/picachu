package com.ss.gameLogic.model.util;

public class Point {
  public int r = 0;
  public int c = 0;

  public Point(int r, int c) {
    this.r = r;
    this.c = c;
  }

  @Override
  public String toString() {
    return "(" + r + " " + c + ")";
  }
}
