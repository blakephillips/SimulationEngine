package com.blakephillips.game.ai.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ai.State;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.game.Orchestrator;
import com.blakephillips.game.data.ResourceType;
import com.blakephillips.game.ecs.systems.ResourceSystem;

// head state
public class PathToResourceTypeState extends State {

    public final ResourceType resourceType;
    private final PathFindingState pathFindingState;
    ComponentMapper<PositionComponent> positionComponents = ComponentMapper.getFor(PositionComponent.class);

    public PathToResourceTypeState(Entity entity, ResourceType resourceType) {
        super(entity);
        pathFindingState = new PathFindingState(entity, new Vector2(0, 0));
        setNextState(pathFindingState);
        this.resourceType = resourceType;
    }


    @Override
    public void enter() {
        if (entity == null) {
            Gdx.app.error("PathToResourceType", "Entity is null");
            exit(true);
            return;
        }

        if (!positionComponents.has(entity)) {
            Gdx.app.error("PathToResourceType", "Entity has no PositionComponent");
            exit(true);
            return;
        }

        Entity resource = Orchestrator.getEngine().getSystem(ResourceSystem.class).getClosestReachableResourceOfType(resourceType, positionComponents.get(entity));
        pathFindingState.entity = entity;
        pathFindingState.setDestination(positionComponents.get(resource).pos);
        exit();
    }

    @Override
    public void update(float deltaTime) {}
}
