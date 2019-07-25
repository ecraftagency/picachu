package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.ss.core.exSprite.particle.GParticleSprite;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.gameLogic.model.AniModel;


public class AniView extends Group {
  public static TextureAtlas textures;
  private Image tile;
  private Image ani;
  private AniModel model;
  public static final int ANIWIDTH = 58;
  public static final int ANIHEIGHT = 68;
  private GParticleSprite effect1;
  private GParticleSprite effect2;


  public AniView(final AniModel model) {
    this.model = model;
    tile = new Image(textures.findRegion("cucxilau"));
    ani = new Image(textures.findRegion("" + model.getId()));
    ani.setAlign(Align.center);
    this.addActor(tile);
    this.addActor(ani);
    this.ani.setPosition(4, 12);
  }

  public AniModel getModel() {
    return model;
  }

  public void select(boolean active) {
    float scale = (active) ? 0.9f : 1f;
    addAction(Actions.scaleTo(scale, scale));
    if(active){
      effect1 = GParticleSystem.getGParticleSystem("selected5.p").create(this, AniView.ANIWIDTH/2, AniView.ANIHEIGHT/2);
      effect1.getEffect().scaleEffect(0.3f);
      effect2 = GParticleSystem.getGParticleSystem("selected2.p").create(this, AniView.ANIWIDTH/2, AniView.ANIHEIGHT/2);
      effect2.getEffect().scaleEffect(0.5f);
    }
    else {
      removeEffect();
    }
  }

  public void hint() {
    addAction(Actions.sequence(
            Actions.run(() -> {select(true);}),
            Actions.delay(3f),
            Actions.run(() -> {select(false);})
    ));
  }

  private void removeEffect() {
    if (effect1 != null){
      effect1.remove();
    }

    if (effect2 != null){
      effect2.remove();
    }
  }

  public void destroy() {
    this.remove();
  }

  public void changeTexture() {
    ani.setDrawable(new TextureRegionDrawable(textures.findRegion("" + model.getId())));
  }
}
