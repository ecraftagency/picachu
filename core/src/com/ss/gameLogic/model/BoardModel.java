package com.ss.gameLogic.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.ss.gameLogic.data.Level;
import com.ss.gameLogic.model.util.D;
import com.ss.gameLogic.model.util.Path;
import com.ss.gameLogic.model.util.PathFinder;
import com.ss.gameLogic.model.util.Point;
import com.ss.gameLogic.model.util.SlicePartition;
import com.ss.gameLogic.model.util.Tuple;
import com.ss.gameLogic.model.util.Utils;
import com.ss.gameLogic.objects.BoardConfig;

import java.util.ArrayList;

public class BoardModel {
  private static BoardModel currentBoardModel = null;

  private int row;
  private int col;
  private int numPair;
  private AniModel[][] anis;
  private int currentLevel = 0;
  public float playTime = 360;

  public BoardModel(int level) { //call stack
    this.currentLevel = level;
    Level lv = Level.getLevelData(level);
    this.row = lv.row;
    this.col = lv.col;
    this.numPair = lv.numPair;
    anis = new AniModel[row][col];
    currentBoardModel = this;
    this.playTime = lv.time;
    SlicePartition.initPartitions(level, anis); //session
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
  public static BoardModel getLastBoardModel(){ return currentBoardModel; }
  public AniModel[][] getArray() {return anis;}

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

  public void shuffleBoard() {
    Array<Integer> ids = new Array<Integer>();

    for (int i = 0; i < row; i++)
      for (int j = 0; j < col; j++)
        if (anis[i][j] != null)
          ids.add(anis[i][j].getId());

    ids.shuffle();
    int idx = 0;

    for (int i = 0; i < row; i++)
      for (int j = 0; j < col; j++)
        if (anis[i][j] != null) {
          anis[i][j].setId(ids.get(idx++));
        }
  }

  public Array<AniModel> findMath() {
    Array<AniModel> output = new Array<>();
    for (int i = 0; i < numPair; i++) {
      Array<AniModel> items = Utils.findItems(anis, i);
      for (int j = 0; j < items.size - 1; j++)
        for (int k = j + 1; k < items.size; k++){
          PathFinder<AniModel> pf = new PathFinder<AniModel>(anis, row, col);
          AniModel a1 = items.get(j);
          AniModel a2 = items.get(k);
          if (pf.findPath(new Point(a1.getRow(), a1.getCol()), new Point(a2.getRow(), a2.getCol()))){
            output.add(a1);
            output.add(a2);
            return output;
          }
        }
    }
    return output;
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
    for (SlicePartition sp : SlicePartition.instances) {
      ArrayList<Tuple<AniModel, D>> slices = sp.calcSlices(ani);

      for (int i = slices.size() - 1; i >= 0; i--) {
        AniModel m = slices.get(i).obj;
        D d = slices.get(i).delta;

        anis[m.getRow()][m.getCol()] = null;
        m.setRowCol(m.getRow() + d.dr, m.getCol() + d.dc, d.dr, d.dc);
        anis[m.getRow()][m.getCol()] = m;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static void saveData(){
    Preferences pref = Gdx.app.getPreferences(BoardConfig.saveName);
    BoardModel board = getLastBoardModel();
    pref.putInteger("col", board.col);
    pref.putInteger("row", board.row);
    pref.putInteger("numPair", board.numPair);
    pref.putInteger("currentLevel", board.currentLevel);
    pref.putFloat("playTime", board.playTime);

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
      int currentLevel = pref.getInteger("currentLevel");
      float playTime = pref.getFloat("playTime");

      Level lv = Level.getLevelData(currentLevel);
      if (col != lv.col || row != lv.row || numPair != lv.numPair) {
        return null;
      }

      int[][] resumeData = new int[row][col];
      for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
          resumeData[i][j] = pref.getInteger("ani_" + i + "_" + j, -1);
        }
      }

      BoardModel boardModel = new BoardModel(currentLevel);
      boardModel.resumeBoard(resumeData, playTime);
      BoardModel.currentBoardModel = boardModel;
      return boardModel;
    }
    catch(Exception e){
      e.printStackTrace();
      currentBoardModel = null;
    }
    return null;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////

  public void resumeBoard(int[][] data, float playTime){
    this.playTime = playTime;

    for (int i = 0; i < row; i++)
      for (int j = 0; j < col; j++)
        if(data[i][j]!=-1)
          anis[i][j] = new AniModel(data[i][j], i, j);
        else anis[i][j] = null;
  }

  public void generateBoard() { //>< resumeBoard
    Array<Integer> indexList = initBoard();
    int num = 0;

    for (int i = 0; i < row; i++)
      for (int j = 0; j < col; j++)
        anis[i][j] = new AniModel(indexList.get(num++), i, j);

  }
}
