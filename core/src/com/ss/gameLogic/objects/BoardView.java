package com.ss.gameLogic.objects;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

  public void animationNewGame(BoardModel model, final IBoardEvent listener, GSimpleAction animationFinished ){
    this.model = model;
    centerBoard();
    anis = new AniView[model.getRow()][model.getCol()];

    AniView lastAniView = null;
    for (int i = 0; i < model.getRow(); i++) {
      float dt = 0.3f;
      for (int j = 0; j < model.getCol(); j++) {
        final AniView ani = new AniView(this.model.getAniModel(i, j));
        anis[i][j] = ani;
        float toX = j*AniView.ANIWIDTH;
        float toY = i*AniView.ANIHEIGHT;
        ani.addListener(new ClickListener(){
          @Override
          public void clicked(InputEvent event, float x, float y) {
            if(listener !=null)
              listener.AniSelect(ani);
          }
        });

        anis[i][j].setPosition( toX, toY /*- MathUtils.random(300,1000)*/);
        dt += 0.05f;
        this.addActor(ani);
        lastAniView = anis[i][j];

        ani.getModel().registerRowCowChange(new AniModel.RowColChangeEvent() {
          @Override
          public void OnChange(final int newRow, final int newCol, int dr, int dc) {
            final float toX = newCol*AniView.ANIWIDTH;
            final float toY = newRow*AniView.ANIHEIGHT;
            final int z = (ani.getModel().getRow()*18 + ani.getModel().getCol())*200;
            ani.addAction(Actions.sequence(
                    Actions.delay(0.2f),
                    Actions.moveTo(toX, toY, 0.3f, Interpolation.swingOut),
                    Actions.run(new Runnable() {
                      @Override
                      public void run() {
                          ani.setZIndex(z);
                      }
                    })));
          }
        });

        addPaddingActor();
      }
    }
    lastAniView.addAction(animationFinished);
  }


  public void animationResumeGame(BoardModel model, final IBoardEvent listener, GSimpleAction animationFinished ){
    this.model = model;
    centerBoard();
    anis = new AniView[model.getRow()][model.getCol()];

    AniView lastAniView = null;
    for (int i = 0; i < model.getRow(); i++) {
      float dt = 0.3f;
      for (int j = 0; j < model.getCol(); j++) {
        if(this.model.getAniModel(i,j) != null) {
          final AniView ani = new AniView(this.model.getAniModel(i, j));
          anis[i][j] = ani;
          float toX = j * AniView.ANIWIDTH;
          float toY = i * AniView.ANIHEIGHT;
          ani.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
              if (listener != null)
                listener.AniSelect(ani);
            }
          });

          anis[i][j].setPosition(toX  -MathUtils.random(300, 1000), toY );
          anis[i][j].addAction(Actions.moveTo(toX, toY, dt, Interpolation.swing));
          dt += 0.05f;
          this.addActor(anis[i][j]);
          lastAniView = anis[i][j];
        }
      }
    }
    if(lastAniView!=null)
      lastAniView.addAction(animationFinished);
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

  public void addPaddingActor() {
    for (int i = 0; i < 200; i++)
      addActor(new Actor());
  }
}
