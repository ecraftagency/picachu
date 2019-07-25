package com.ss.gameLogic.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.ss.gameLogic.model.util.D;
import com.ss.gameLogic.model.util.Path;
import com.ss.gameLogic.model.util.PathFinder;
import com.ss.gameLogic.model.util.Point;
import com.ss.gameLogic.model.util.SlicePartition;
import com.ss.gameLogic.model.util.Tuple;
import com.ss.gameLogic.objects.BoardConfig;

import java.util.ArrayList;

public class BoardModel {
  private static BoardModel currentBoardModel = null;

  private int row;
  private int col;
  private int numPair;
  private AniModel[][] anis;
  private Array<SlicePartition> slicePartitions;

  public BoardModel(int row, int col, int numPair) {
    this.row = row;
    this.col = col;
    this.numPair = numPair;
    anis = new AniModel[row][col];
    currentBoardModel = this;
  }

  public void removeAni(int row, int col){
    anis[row][col] = null;
  }
  public int getRow() { return row; }
  public int getCol() { return col; }
  public AniModel getAniModel(int row, int col) { return anis[row][col]; }
  public static BoardModel getSaveBoard(){
    return null;
  }
  public static BoardModel getLastBoardModel(){
    return currentBoardModel;
  }

  public void newBoard() {
    Array<Integer> indexList = initBoard();
    int num = 0;

    for (int i = 0; i < row; i++)
      for (int j = 0; j < col; j++)
        anis[i][j] = new AniModel(indexList.get(num++), i, j);

    this.slicePartitions = new Array<SlicePartition>();
    SlicePartition p1 = new SlicePartition(this.anis, 0, 0, 0, 3);
    SlicePartition p2 = new SlicePartition(this.anis, 0, 1, 4, 7);
    slicePartitions.add(p1);
    slicePartitions.add(p2);
  }

  private Array<Integer> initBoard() {
    int size = col*row;
    int tmpIndex = 0;
    Array<Integer> indexList = new Array<Integer>();
    for(int i=0;i<size/2;i++){
      indexList.add(tmpIndex);
      indexList.add(tmpIndex);
      tmpIndex++;
      if(tmpIndex>=numPair)
        tmpIndex=0;
    }
    indexList.shuffle();
    return indexList;
  }

  public ArrayList<Path> match(AniModel a1, AniModel a2) {
    if (a1.getId() == a2.getId() && (a1.getCol() != a2.getCol() || a1.getRow() != a2.getRow())) {
      PathFinder<AniModel> pf = new PathFinder<AniModel>(anis, row, col);
      if (pf.findPath(new Point(a1.getRow(), a1.getCol()), new Point(a2.getRow(), a2.getCol()))){
        pf.printPath();
        return pf.path;
      }
    }
    return null;
  }

  public void nullSlice(AniModel ani) {
    for (SlicePartition sp : this.slicePartitions) {
      ArrayList<Tuple<AniModel, D>> slices = sp.calcSlices(ani);

      for (int i = slices.size() - 1; i >= 0; i--) {
        AniModel m = slices.get(i).obj;
        D d = slices.get(i).delta;

        anis[m.getRow()][m.getCol()] = null;
        m.setRowCol(m.getRow() + d.dr, m.getCol() + d.dc, d.dr, d.dc);
        anis[m.getRow()][m.getCol()] = m;
        //m.setRowCol(m.getRow(), m.getCol());
      }
    }
  }


  public void resumeBoard(int[][] data){
    for (int i = 0; i < row; i++)
      for (int j = 0; j < col; j++)
        if(data[i][j]!=-1)
          anis[i][j] = new AniModel(data[i][j], i, j);
        else anis[i][j] = null;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static void saveData(){
    Preferences pref = Gdx.app.getPreferences(BoardConfig.saveName);
    BoardModel board = getLastBoardModel();
    pref.putInteger("col", board.col);
    pref.putInteger("row", board.row);
    pref.putInteger("numPair", board.numPair);
    for(int i=0;i<board.row;i++){
      for(int j=0;j<board.col;j++){
        int id = (board.anis[i][j]==null)?-1:board.anis[i][j].getId();
        pref.putInteger("ani_"+i+"_"+j,id);
      }
    }
    pref.putBoolean("isResume", true);
    pref.flush();
    Gdx.app.log("BoardModel", "save");
  }

  public static BoardModel loadLastBoardModel(){
    try {
      Preferences pref = Gdx.app.getPreferences(BoardConfig.saveName);
      if(pref.getBoolean("isResume", false) == false)
        return null;
      int col = pref.getInteger("col");
      int row = pref.getInteger("row");
      int numPair = pref.getInteger("numPair");

      int[][] resumeData = new int[row][col];
      for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
          resumeData[i][j] = pref.getInteger("ani_" + i + "_" + j, -1);
        }
      }
      BoardModel boardModel = new BoardModel(row, col, numPair);
      boardModel.resumeBoard(resumeData);
      return boardModel;
    }
    catch(Exception e){
      e.printStackTrace();
      currentBoardModel = null;
    }
    return null;
  }
}
