package com.blakephillips.engine.ecs.systems.mouse;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blakephillips.engine.ecs.components.mouse.MousePositionComponent;

public class MousePositionSystem extends IteratingSystem {

    private final ComponentMapper<MousePositionComponent> mouseComponents = ComponentMapper.getFor(MousePositionComponent.class);
    private final Viewport viewport;
    private Vector3 v3pos;
    private Vector2 v2pos;

    public MousePositionSystem(Viewport viewport) {
        super(Family.all(MousePositionComponent.class).get());
        this.viewport = viewport;
        v3pos = new Vector3();
        v2pos = new Vector2();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MousePositionComponent mousePos = mouseComponents.get(entity);

        v2pos = unprojectedMousePos();

        mousePos.pos.x = v2pos.x;
        mousePos.pos.y = v2pos.y;
    }

    public Vector2 unprojectedMousePos() {
        v3pos.x = Gdx.input.getX();
        v3pos.y = Gdx.input.getY();
        v3pos.z = 0;

        v3pos = viewport.unproject(v3pos);
        v2pos.x = v3pos.x;
        v2pos.y = v3pos.y;
        return v2pos;
    }
}
