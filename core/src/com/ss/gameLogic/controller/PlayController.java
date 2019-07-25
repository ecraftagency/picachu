package com.ss.gameLogic.controller;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GStage;
import com.ss.gameLogic.model.BoardModel;
import com.ss.gameLogic.model.util.Path;
import com.ss.gameLogic.objects.AniView;
import com.ss.gameLogic.objects.BoardConfig;
import com.ss.gameLogic.objects.BoardView;
import com.ss.gameLogic.scene.GPlay;

import java.util.ArrayList;

public class PlayController implements IBoardEvent {
  private GPlay screen;
  private GLayerGroup playGroup;
  private BoardView boardView;
  private AniView selected = null;
  public static PlayController instance = new PlayController();

  public void init(GPlay screen) {
    this.screen = screen;
    this.playGroup = new GLayerGroup();
    GStage.addToLayer(GLayer.ui, instance.playGroup);
  }

  public void pause() {

  }

  public void newGame() {
    BoardModel bmodel = new BoardModel(BoardConfig.row, BoardConfig.col, BoardConfig.pair);
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
      ani.highlight(true);
    }
    else {
      ArrayList<Path> path = BoardModel.getLastBoardModel().match(selected.getModel(), ani.getModel());
      if (path!=null) {
        BoardModel.getLastBoardModel().removeAni(ani.getModel().getRow(), ani.getModel().getCol());
        BoardModel.getLastBoardModel().removeAni(selected.getModel().getRow(), selected.getModel().getCol());
        ani.destroy();
        selected.destroy();
        BoardModel.saveData();
        animationPath(path);

        BoardModel.getLastBoardModel().nullSlice(ani.getModel());
        BoardModel.getLastBoardModel().nullSlice(selected.getModel());
        selected = null;
      }
      else {
        selected.highlight(false);
        ani.highlight(true);
        selected = ani;
      }
    }
  }
}
