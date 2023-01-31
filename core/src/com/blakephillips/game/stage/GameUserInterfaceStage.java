package com.blakephillips.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blakephillips.game.Orchestrator;

public class GameUserInterfaceStage {
    public Stage stage;
    private Table table;
    Skin skin;

    public GameUserInterfaceStage(Viewport viewport) {

        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        TextButton button1 = new TextButton("Do a thing", skin);
        button1.setPosition(0, 0);

        button1.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Orchestrator.gameIgnoreInput = true;
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Orchestrator.gameIgnoreInput = false;
            }
        });
        stage.addActor(button1);
    }

}
