package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
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
import com.ss.gameLogic.model.D;
import com.ss.gameLogic.model.Tuple;
import com.ss.gameLogic.model.Utils;

import java.util.ArrayList;

public class BoardView extends Group {
  private AniView[][] anis;

  private AniView selected = null;

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
        //anis[i][j].addAction(Actions.moveTo(toX, toY, dt, Interpolation.swing));
        dt += 0.05f;
        //int zindex = (int)((j+1)*100-i);
        //Gdx.app.log("test", "test " +zindex );

        this.addActor(ani);
        ani.updateZIndex();
        //ani.setZIndex(100000 - ani.getModel().getRow() - 100*ani.getModel().getCol());
        //ani.setZIndex()
        //this.addActorAt(zindex, anis[i][j]);
        lastAniView = anis[i][j];


        ani.getModel().registerRowCowChange(new AniModel.RowColChangeEvent() {
          @Override
          public void OnChange(final int newRow, final int newCol) {
            final float toX = newCol*AniView.ANIWIDTH;
            final float toY = newRow*AniView.ANIHEIGHT;
            ani.setPosition(toX, toY);
            ani.updateZIndex();
            //int zindex = (int)(100000 - newRow - newCol*100);
           // ani.setZIndex(zindex);
//
//            ani.addAction(Actions.sequence( Actions.moveTo(toX, toY, 0.5f, Interpolation.swing), GSimpleAction.simpleAction(new GSimpleAction.ActInterface() {
//              @Override
//              public boolean act(float var1, Actor var2) {
//                //ani.setZIndex((int)(1000000+toY*1000-toX));
//                int zindex = (int)(100000-(newCol*newRow));
//                ani.setZIndex(zindex);
//                Gdx.app.log("test", "test");
//                return true;
//              }
//            })));


            //ani.setZIndex(newRow*100+newCol);
            //ani.setZIndex(1000-newCol*newRow);
          }
        });
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

  public void nullSlice(ArrayList<Tuple<AniModel, D>> slices) {
    for (Tuple<AniModel, D> slice : slices) {
      AniModel aniM = slice.obj;
      AniView aniV = anis[aniM.getRow()][aniM.getCol()];
      aniV.addAction(Actions.moveTo(slice.delta.dc * AniView.ANIWIDTH, slice.delta.dr * AniView.ANIWIDTH, 0.4f));
    }
  }

//  private void animalSelect(AniView ani) {
//    if (selected == null) {
//      selected = ani;
//    }
//    else {
//      if (ani.id == selected.id) {
//        this.removeActor(selected);
//        this.removeActor(ani);
//        selected = null;
//      }
//      else {
//        selected = ani;
//      }
//    }
//  }
}
