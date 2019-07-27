package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.gameLogic.controller.IBoardEvent;
import com.ss.gameLogic.model.AniModel;
import com.ss.gameLogic.model.BoardModel;

public class BoardView extends Group {
  private AniView[][] anis;


  private BoardModel model;

  public BoardView() {

  }

  private void zIndexAniList(){
    int c = 0;
    for(int i=0;i<model.getCol();i++){
      for(int j=0;j<model.getRow();j++){
        if(anis[j][i]!=null) anis[j][i].setZIndex(c++);
      }
    }
  }
  private void initAniView(final AniView aniView, final IBoardEvent listener) {
    aniView.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if(listener !=null)
          listener.AniSelect(aniView);
      }
    });

    aniView.getModel().registerDataChange(new AniModel.DataChangeEvent() {
      @Override
      public void OnRowColChange(final int newRow, final int newCol, int dr, int dc) {
        final float toX = newCol * AniView.ANIWIDTH;
        final float toY = newRow * AniView.ANIHEIGHT;
        anis[newRow][newCol] = aniView;
        zIndexAniList();
        aniView.addAction(Actions.moveTo(toX, toY, 0.5f, Interpolation.swing));

      }

      @Override
      public void OnIdChange() {
        aniView.addAction(Actions.sequence(
                Actions.run(() -> {
                  aniView.changeTexture();
                })));
      }

      @Override
      public void OnHighlightChange() {
        aniView.hint();
      }
    });
  }
  public void animationNewGame(BoardModel model, final IBoardEvent listener, GSimpleAction animationFinished ){
    this.model = model;
    centerBoard();
    anis = new AniView[model.getRow()][model.getCol()];

    AniView lastAniView = null;
    for (int i = 0; i < model.getRow(); i++) {
      for (int j = 0; j < model.getCol(); j++) {
        final AniView ani = new AniView(this.model.getAniModel(i, j));
        anis[i][j] = ani;
        initAniView(ani, listener);
        float toX = j*AniView.ANIWIDTH;
        float toY = i*AniView.ANIHEIGHT;
        ani.setPosition( toX, toY - MathUtils.random(300,1000));
        ani.addAction(Actions.moveTo(toX, toY, 1f, Interpolation.swing));

        this.addActor(ani);
        lastAniView = ani;
      }
    }
    lastAniView.addAction(animationFinished);
    zIndexAniList();
  }

  public void animationResumeGame(BoardModel model, final IBoardEvent listener, GSimpleAction animationFinished ){
    
  }


  public  void centerBoard(){
    BoardView board = this;
    float scaleX = board.getBoardWidth()/(GMain.screenWidth*1f);
    float scaleY = (board.getBoardHeight()+40 + BoardConfig.headerHeight)/((GMain.screenHeight)*1f);
    float scale = 1/(((scaleX > scaleY) ? scaleX : scaleY) + 0.03f);
    scale = (scale > 1.3) ? 1.4f : scale;
    board.setScale(scale);

    board.setWidth(board.getBoardWidth()*(scale));
    board.setHeight(board.getBoardHeight()*(scale));
    board.setPosition(GMain.screenWidth/2,(GMain.screenHeight - BoardConfig.headerHeight)/2f  +BoardConfig.headerHeight, Align.center);
  }

  public int getBoardWidth(){
    return AniView.ANIWIDTH*model.getCol();
  }

  public int getBoardHeight(){
    return AniView.ANIHEIGHT*model.getRow();
  }

  public void removeAni(AniView ani) {
    anis[ani.getModel().getRow()][ani.getModel().getCol()] = null;
    ani.destroy();
  }
}
