package com.ss.gameLogic.controller;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.gameLogic.data.Level;
import com.ss.gameLogic.model.AniModel;
import com.ss.gameLogic.model.BoardModel;
import com.ss.gameLogic.model.util.Path;
import com.ss.gameLogic.model.util.SlicePartition;
import com.ss.gameLogic.objects.AniView;
import com.ss.gameLogic.objects.BoardView;
import com.ss.gameLogic.scene.GPlay;

import java.util.ArrayList;

public class PlayController implements IBoardEvent {
  private GPlay screen;
  private GLayerGroup playGroup;
  private BoardView boardView;
  private AniView selected = null;
  public static PlayController instance = new PlayController();
  private float playTime; //second
  private int level;

  public void init(GPlay screen) {
    this.screen = screen;
    this.playGroup = new GLayerGroup();
    GStage.addToLayer(GLayer.ui, instance.playGroup);
    initTimer(10);
  }

  public void pause() {

  }

  public void newGame(int level) {
    Level lv = Level.getLevelData(level);
    BoardModel bmodel = new BoardModel(lv.row, lv.col, lv.numPair);
    bmodel.newBoard();

    boardView = new BoardView();
    playGroup.addActor(instance.boardView);
    boardView.setTouchable(Touchable.disabled);

    instance.boardView.animationNewGame(bmodel, this, GSimpleAction.simpleAction(new GSimpleAction.ActInterface() {
      @Override
      public boolean act(float var1, Actor var2) {
        instance.boardView.setTouchable(Touchable.enabled);
        Gdx.app.log("newGame", "animation new game finished");
        return true;
      }
    }));

    SlicePartition.initPartitions(level, bmodel.getArray());
  }

  public void getHint() {
    Array<AniModel> nextMatch = BoardModel.getLastBoardModel().findMath();
    if (nextMatch.size == 2){
      AniModel a1 = nextMatch.get(0);
      AniModel a2 = nextMatch.get(1);
      a1.setHighLight(true);
      a2.setHighLight(true);
    }
  }

  public void shuffle() {
    BoardModel.getLastBoardModel().shuffleBoard();
  }

  private void initTimer(float plt) {
    this.playTime = plt;
    playGroup.addAction(GSimpleAction.simpleAction(new GSimpleAction.ActInterface() {
      @Override
      public boolean act(float dt, Actor actor) {
        playTime -= dt; //0.0016
        if (playTime <= 0) {
          timeOut();
          return true;
        }
        else {
          return false;
        }
      }
    }));
  }

  public void timeOut() {
    Gdx.app.log("het gio choi", "");
  }

  public void resume() {
    boardView = new BoardView();
    playGroup.addActor(instance.boardView);
    boardView.setTouchable(Touchable.disabled);

    instance.boardView.animationResumeGame(BoardModel.getLastBoardModel(), this, GSimpleAction.simpleAction(new GSimpleAction.ActInterface() {
      @Override
      public boolean act(float var1, Actor var2) {
        instance.boardView.setTouchable(Touchable.enabled);
        Gdx.app.log("resume", "animation new game finished");
        return true;
      }
    }));
  }

  public void animationPath(ArrayList<Path> path){
    for(Path p:path){
      GShapeSprite shape = new GShapeSprite();
      shape.createLine(p.sp.c*AniView.ANIWIDTH, p.sp.r*AniView.ANIHEIGHT,  (p.sp.c+p.dt.c)*AniView.ANIWIDTH,(p.sp.r+p.dt.r)*AniView.ANIHEIGHT);
      shape.setPosition(AniView.ANIWIDTH/2, AniView.ANIHEIGHT/2);
      boardView.addActor(shape);
      shape.addAction(Actions.sequence(Actions.delay(0.5f), Actions.removeActor()));
    }
  }
  @Override
  public void AniSelect(AniView ani) {
    if (selected == null) {
      selected = ani;
      ani.select(true);
    }
    else {
      ArrayList<Path> path = BoardModel.getLastBoardModel().match(selected.getModel(), ani.getModel());
      if (path!=null) {
        BoardModel.getLastBoardModel().removeAni(ani.getModel().getRow(), ani.getModel().getCol());
        BoardModel.getLastBoardModel().removeAni(selected.getModel().getRow(), selected.getModel().getCol());
        boardView.removeAni(ani);
        boardView.removeAni(selected);
        animationPath(path);

        BoardModel.getLastBoardModel().nullSlice(ani.getModel());
        BoardModel.getLastBoardModel().nullSlice(selected.getModel()); //0.7s

        Array<AniModel> nextMatch = BoardModel.getLastBoardModel().findMath();
        if (nextMatch.size == 0)
          BoardModel.getLastBoardModel().shuffleBoard(); // 0.7 + ..

        BoardModel.saveData();
        selected = null;
      }
      else {
        selected.select(false);
        ani.select(true);
        selected = ani;
      }
    }
  }

  public float getTime() {
    return this.playTime;
  }
}
