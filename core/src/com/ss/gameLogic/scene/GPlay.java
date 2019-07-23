package com.ss.gameLogic.scene;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.controller.PlayController;
import com.ss.gameLogic.model.BoardModel;
import com.ss.gameLogic.objects.AniView;
import com.ss.gameLogic.objects.BoardView;

public class GPlay extends GScreen {
    TextureAtlas playAtlas;
    Group playGroup;
    Group pauseGroup;
    BoardView board;

    private int row = 8;
    private int col = 18;
    Array<AniView> anis;


    @Override
    public void dispose() {

    }

    private void initUi(){
        Image bg = GUI.createImage(playAtlas, "bg");
        playGroup.addActor(bg);

        Button btn_pause = GUI.creatButton(playAtlas, "btn_pause");
        playGroup.addActor(btn_pause);
        btn_pause.setPosition(GMain.screenWidth-btn_pause.getWidth(), 0);
        btn_pause.addListener(new ClickListener(){
            public void clicked (InputEvent event, float x, float y) {
                initPause();
            }
        });

        Button btn_hint = GUI.creatButton(playAtlas, "btn_hint");
        playGroup.addActor(btn_hint);
        Button btn_swap = GUI.creatButton(playAtlas, "btn_random");
        playGroup.addActor(btn_swap);
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
            }
        });

    }
    @Override
    public void init() {
        playAtlas = GAssetsManager.getTextureAtlas("play.atlas");
        int headerHeight = 60;
        AniView.textures = playAtlas;
        playGroup = new Group();

        /*board = new BoardView(8,19,  36);




        GStage.addToLayer(GLayer.ui, playGroup);
        GStage.addToLayer(GLayer.ui, board);*/
        initUi();
        PlayController.instance.init(this);

        //if(BoardModel.loadLastBoardModel()==null)
            PlayController.instance.newGame();
        //else
            //PlayController.instance.resume();
    }

    @Override
    public void run() {

    }
}
