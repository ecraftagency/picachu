package com.ss.gameLogic.scene;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ss.GMain;
import com.ss.core.transitions.GTransition;
import com.ss.core.transitions.GTransitionSlice;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;

public class GMenu extends GScreen {
    TextureAtlas menuAtlas;

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        Group menuGroup = new Group();
        GStage.addToLayer(GLayer.ui, menuGroup);

        menuAtlas = GAssetsManager.getTextureAtlas("menu.atlas");
        Image bg = GUI.createImage(menuAtlas, "bg");
        menuGroup.addActor(bg);

        Button btn = GUI.creatButton(menuAtlas.findRegion("newgame"));
        menuGroup.addActor(btn);
        btn.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                GTransition tra =  GTransitionSlice.init(1f, 1, 10, Interpolation.fastSlow);
                setScreen(GMain.playScreen(),tra);
            }
        });
    }

    @Override
    public void run() {

    }
}
