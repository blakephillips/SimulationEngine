package com.blakephillips.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blakephillips.game.Orchestrator;
import com.blakephillips.game.data.UIState;

public class GameUserInterfaceStage {
    public Stage stage;
    private TextButton button1;
    Skin skin;

    public GameUserInterfaceStage(Viewport viewport) {

        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        button1 = new TextButton("Cut", skin);
        button1.sizeBy(1, 3);


        button1.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Orchestrator.gameIgnoreInput = true;
                if (Orchestrator.uiState != UIState.SELECTING) {
                    Orchestrator.uiState = UIState.SELECTING;
                    button1.setText("Stop Cutting");
                    button1.sizeBy(16 * 4, 1);
                } else {
                    Orchestrator.uiState = UIState.DEFAULT;
                    button1.setText("Cut");
                    button1.sizeBy(-16 * 4, 1);
                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Orchestrator.gameIgnoreInput = false;
            }
        });
        stage.addActor(button1);
    }

}
