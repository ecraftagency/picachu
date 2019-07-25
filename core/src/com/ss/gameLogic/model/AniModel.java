package com.ss.gameLogic.model;

public class AniModel {
  private int id;
  private int row;
  private int col;
  private RowColChangeEvent rowColChangeListener;

  public AniModel(int id, int row, int col) {
    this.id = id;
    this.row = row;
    this.col = col;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setRowCol(int row, int col, int dr, int dc){
    this.row = row;
    this.col = col;
    if(rowColChangeListener!=null)
      rowColChangeListener.OnChange(this.row, this.col,dr,dc);
  }

  public void registerRowCowChange(RowColChangeEvent listener){
    this.rowColChangeListener = listener;
  }

  public interface RowColChangeEvent{
    public void OnChange(int newRow, int newCol, int dr, int dc);
  }
}
