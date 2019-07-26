package com.ss.gameLogic.data;

public final class Level {
  public final int row;
  public final int col;
  public final int time;
  public final int nPartition;
  public final int sliceDirection;
  public final int numPair;
  public final int sliceAnchor;

  private Level(int row, int col, int numPair, int time, int nPartition, int sliceDirection, int sliceAnchor) {
    this.row = row;
    this.col = col;
    this.time = time;
    this.numPair = numPair;
    this.nPartition = nPartition;
    this.sliceDirection = sliceDirection; //0 ngang | 1 dung
    this.sliceAnchor = sliceAnchor; //0 slice ve dau array | 1 slice ve cuoi array
  }

  public static final Level[] levelData;

  static {
    levelData = new Level[9];
    levelData[0] = new Level(2,12, 10,360,0,0,0);
    levelData[1] = new Level(8,18, 12,360,1,0,0);
    levelData[2] = new Level(8,18, 14,360,1,0,1);
    levelData[3] = new Level(8,18, 14,360,1,1,0);
    levelData[4] = new Level(8,18, 14,360,1,1,1);
    levelData[5] = new Level(8,18, 14,360,2,0,0);
    levelData[6] = new Level(8,18, 14,360,2,0,1);
    levelData[7] = new Level(8,17, 14,360,2,1,0);
    levelData[8] = new Level(8,18, 14,360,2,1,1);
  }

  public static Level getLevelData(int level) {
    level = (level < 0) ? 0 : level%9;
    return levelData[level];
  }
}