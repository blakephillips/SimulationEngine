package com.blakephillips.engine.ecs.components.gfx;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    public TextureRegion region;
    public int z;

    public TextureComponent(TextureRegion region, int z) {
        this.region = region;
        this.z = z;
    }
}
