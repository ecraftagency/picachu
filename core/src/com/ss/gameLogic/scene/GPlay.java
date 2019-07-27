package com.ss.gameLogic.scene;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GLayerGroup;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.controller.PlayController;
import com.ss.gameLogic.model.BoardModel;
import com.ss.gameLogic.objects.AniView;
import com.ss.gameLogic.scene.ui.TimeBar;

public class GPlay extends GScreen {
    TextureAtlas playAtlas;
    GLayerGroup uiGroup;
    Group pauseGroup;

    @Override
    public void dispose() {

    }

    private void initParticle(){
        new GParticleSystem("fireball_boom.p", "particleatlas1.atlas", 1, 1);
        new GParticleSystem("user3boom.p", "particleatlas1.atlas", 1, 1);
        new GParticleSystem("selected2.p", "particleatlas1.atlas", 1, 1);
        new GParticleSystem("selected5.p", "particleatlas1.atlas", 1, 1);
        new GParticleSystem("baozou1p.p", "particleatlas1.atlas", 1, 1);
        new GParticleSystem("baozou2p.p", "particleatlas1.atlas", 1, 1);
        new GParticleSystem("baozou3p.p", "particleatlas1.atlas", 1, 1);
    }

    private void initUi(){
        uiGroup = new GLayerGroup();
        GStage.addToLayer(GLayer.ui, uiGroup);

        Image bg = GUI.createImage(playAtlas, "bg");
        uiGroup.addActor(bg);

        Button btn_pause = GUI.creatButton(playAtlas, "btn_pause");
        uiGroup.addActor(btn_pause);

        btn_pause.setPosition(GMain.screenWidth-btn_pause.getWidth(), 0);
        btn_pause.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                initPause();
            }
        });

        Button btn_hint = GUI.creatButton(playAtlas, "btn_hint");
        btn_hint.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                PlayController.instance.getHint();
            }
        });

        uiGroup.addActor(btn_hint);

        Button btn_swap = GUI.creatButton(playAtlas, "btn_random");
        btn_swap.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                PlayController.instance.shuffle();
            }
        });
        uiGroup.addActor(btn_swap);
        btn_swap.setPosition(btn_hint.getX() + btn_hint.getWidth() + 4, btn_hint.getY());
    }


    public void initPause(){
        if(pauseGroup != null){
            pauseGroup.remove();
        }
        pauseGroup = new Group();
        GStage.addToLayer(GLayer.top, pauseGroup);

        GShapeSprite overlay = new GShapeSprite();
        overlay.createRectangle(true, 0,0,GMain.screenWidth, GMain.screenHeight);
        overlay.setColor(0,0,0,0.8f);

        pauseGroup.addActor(overlay);
        Button btn_resume = GUI.creatButton(playAtlas, "btn_tieptuc");
        pauseGroup.addActor(btn_resume);
        btn_resume.setPosition(GMain.screenWidth/2, GMain.screenHeight/2, Align.center);
        btn_resume.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                pauseGroup.remove();
                pauseGroup = null;
                uiGroup.setPause(false);
                PlayController.instance.pause(false);
            }
        });
        uiGroup.setPause(true);
        PlayController.instance.pause(true);
    }

    @Override
    public void init() {
        playAtlas = GAssetsManager.getTextureAtlas("play.atlas");
        AniView.textures = playAtlas;
        initUi();
        initParticle();
        PlayController.instance.init(this);
        if (BoardModel.loadLastBoardModel() == null){
            PlayController.instance.newGame(0);
            initTimebar();
        }
        else {
            PlayController.instance.resume();
            initTimebar();
        }
            ;
    }

    private void startGame(int level) {

    }

    @Override
    public void run() {

    }

    private void initTimebar() {
        Image barframe = GUI.createImage(playAtlas, "barframe");
        barframe.setPosition(GMain.screenWidth/2 - barframe.getWidth()/2, 5);
        uiGroup.addActor(barframe);
        Image bar = GUI.createImage(playAtlas, "bar");
        TimeBar timeBar = new TimeBar(bar, PlayController.instance.getTime(),this::timeout);
        timeBar.setPosition(GMain.screenWidth/2 - bar.getWidth()/2,10);
        uiGroup.addActor(timeBar);
    }

    private void timeout() {

    }
}
