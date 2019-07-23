package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.gameLogic.model.AniModel;


public class AniView extends Group {
  public static TextureAtlas textures;
  private Image tile;
  private Image ani;
  private AniModel model;
  public static final int ANIWIDTH = 58;
  public static final int ANIHEIGHT = 68;

  public AniView(AniModel model) {
    this.model = model;
    tile = new Image(textures.findRegion("cucxilau"));
    ani = new Image(textures.findRegion("" + model.getId()));
    this.addActor(tile);
    this.addActor(ani);
    this.ani.setPosition(4, 12);
  }

  public AniModel getModel() {
    return model;
  }

  public void highlight(boolean active) {
    float scale = (active) ? 0.9f : 1f;
    addAction(Actions.scaleTo(scale, scale));
  }

  public void destroy() {
    this.setVisible(false);
  }

  public void updateZIndex(){
    this.setZIndex(100000 - this.model.getRow());
  }
}
