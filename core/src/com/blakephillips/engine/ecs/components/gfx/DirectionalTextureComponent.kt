package com.blakephillips.engine.ecs.components.gfx;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.blakephillips.engine.utilities.Direction;

import java.util.HashMap;
import java.util.Map;

public class DirectionalTextureComponent implements Component {
    private final Map<Direction, TextureRegion> textureMap;

    public DirectionalTextureComponent(TextureRegion north, TextureRegion east,
                                       TextureRegion south, TextureRegion west) {
        textureMap = new HashMap<>(4);
        textureMap.put(Direction.NORTH, north);
        textureMap.put(Direction.EAST, east);
        textureMap.put(Direction.SOUTH, south);
        textureMap.put(Direction.WEST, west);
    }

    public TextureRegion getTexture(Direction direction) {
        return textureMap.get(direction);
    }
}
