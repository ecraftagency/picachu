package com.ss.gameLogic.scene.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GClipGroup;
import com.ss.core.util.GUI;

public class TimeBar extends GClipGroup {
  Image bar;
  Runnable onTimeOut;
  private float runningTime = 0;
  private float time;

  public TimeBar(Image bar, float time, Runnable onTimeOut){
    this.bar = bar;
    this.time = time;
    this.onTimeOut = onTimeOut;
    init();
  }

  private void init() {
    this.addActor(bar);
    bar.setPosition(0,0);
    setClipArea(0,0,bar.getWidth(), bar.getHeight());
    this.addAction(GSimpleAction.simpleAction((dt, var2) -> {
        runningTime += dt;
        if (runningTime <= time) {
          float percent = runningTime/time;
          float dx = bar.getWidth()*percent;
          bar.setX(-1*dx);
          return false;
        }
        else {
          if (onTimeOut != null)
            onTimeOut.run();
          return true;
        }
    }));
  }
}
