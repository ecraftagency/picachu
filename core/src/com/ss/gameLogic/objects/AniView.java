package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ss.gameLogic.model.AniModel;


public class AniView extends Group {
  public static TextureAtlas textures;
  private Image tile;
  private Image ani;
  private AniModel model;
  public static final int ANIWIDTH = 58;
  public static final int ANIHEIGHT = 68;

  public AniView(final AniModel model) {
    this.model = model;
    tile = new Image(textures.findRegion("cucxilau"));
    ani = new Image(textures.findRegion("" + model.getId()));
    this.addActor(tile);
    this.addActor(ani);
    this.ani.setPosition(4, 12);
    addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.log("z index", "" + getZIndex() + " row: " + model.getRow() + " col: " + model.getCol());
        super.clicked(event, x, y);
      }
    });
  }

  public AniModel getModel() {
    return model;
  }

  public void highlight(boolean active) {
    float scale = (active) ? 0.9f : 1f;
    addAction(Actions.scaleTo(scale, scale));
  }

  public void destroy() {
    this.getParent().removeActor(this);
    //this.setVisible(false);
    //this.setZIndex(1000);
  }
}
