package com.ss.gameLogic.model;

public class AniModel {
  private int id;
  private int row;
  private int col;
  private boolean highLight = false;
  private DataChangeEvent rowColChangeListener;

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
    if(rowColChangeListener!=null)
      rowColChangeListener.OnIdChange();
  }

  public void setRowCol(int row, int col, int dr, int dc){
    this.row = row;
    this.col = col;
    if(rowColChangeListener!=null)
      rowColChangeListener.OnRowColChange(this.row, this.col,dr,dc);
  }

  public void registerDataChange(DataChangeEvent listener){
    this.rowColChangeListener = listener;
  }

  public void setHighLight(boolean highLight) {
    this.highLight = highLight;
    if(rowColChangeListener!=null)
      rowColChangeListener.OnHighlightChange();
  }

  public boolean isHighLight() {
    return highLight;
  }

  public interface DataChangeEvent {
    public void OnRowColChange(int newRow, int newCol, int dr, int dc);
    public void OnIdChange();
    public void OnHighlightChange();
  }

  @Override
  public String toString() {
    return "" + id;
  }
}
