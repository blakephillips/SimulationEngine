package com.blakephillips.game.ai.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.blakephillips.engine.ai.State;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.components.position.SnapToPositionComponent;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.game.ecs.components.HoldingItemComponent;


public class PickupItemState extends State {

    Entity item;
    private final ComponentMapper<PositionComponent> positionComponents = ComponentMapper.getFor(PositionComponent.class);

    public PickupItemState(Entity actor, Entity item) {
        super(actor);
        this.item = item;
    }

    @Override
    public void enter() {
        if (!positionComponents.has(entity)) {
            Gdx.app.error("SimEngine", "PickupItemState actor has no position component");
            this.exit(true);
            return;
        }

        if (!positionComponents.has(item)) {
            Gdx.app.error("SimEngine", "PickupItemState item has no position component");
            this.exit(true);
            return;
        }

        PositionComponent actorPos = positionComponents.get(entity);
        PositionComponent itemPos = positionComponents.get(item);

        if (isAlreadyReserved(entity)) {
            Gdx.app.error("SimEngine", "PickupItemState item is already reserved.");
            this.exit(true);
            return;
        }

        if (Pathfinding.chebyshevDistance(actorPos.pos, itemPos.pos) > 15) {
            Gdx.app.error("SimEngine", "PickupItemState item is too far away from actor to pickup");
            this.exit(true);
            return;
        }

        HoldingItemComponent holdingItemComponent = new HoldingItemComponent(item);
        entity.add(holdingItemComponent);
        item.add(new SnapToPositionComponent(actorPos));

        exit();
    }

    @Override
    public void update(float deltaTime) {

    }
}
