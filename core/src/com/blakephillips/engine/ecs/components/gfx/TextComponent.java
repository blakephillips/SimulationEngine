package com.blakephillips.engine.ecs.components.gfx;

import com.badlogic.ashley.core.Component;

public class TextComponent implements Component {
    public String content;

    public TextComponent(String content) {
        this.content = content;
    }
}
