package com.ss.gameLogic.model;

public class Path {
  public Point sp;
  public Point dt;

  public Path(Point sp, Point dt) {
    this.sp = sp;
    this.dt = dt;
  }

  @Override
  public String toString() {
    return "start at: " + sp.toString() + "draw to: " + dt.toString();
  }
}
