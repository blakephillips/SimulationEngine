package com.blakephillips.game.ai.jobs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ecs.components.ai.JobComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.game.Orchestrator;
import com.blakephillips.game.ai.states.HaulState;
import com.blakephillips.game.data.JobStatus;
import com.blakephillips.game.data.JobType;
import com.blakephillips.game.data.ResourceType;
import com.blakephillips.game.ecs.components.JobTypeComponent;
import com.blakephillips.game.ecs.systems.ResourceSystem;

public class GetResourceTypeToDestination {
    ResourceSystem resourceSystem;
    ComponentMapper<PositionComponent> positionComponents = ComponentMapper.getFor(PositionComponent.class);
    public GetResourceTypeToDestination(Entity actorEntity, Vector2 haulToPos, ResourceType resourceType) {

        if (!positionComponents.has(actorEntity)) {
            Gdx.app.log("SimEngine", "GetResourceTypeToDestination actorEntity has no position component");
            return;
        }
        PositionComponent actorPosition = positionComponents.get(actorEntity);
        resourceSystem = Orchestrator.engine.getSystem(ResourceSystem.class);
        Entity resource = resourceSystem.getClosestReachableResourceOfType(resourceType, actorPosition);

        Entity jobEntity = new Entity();
        if (resource == null) {
            Gdx.app.log("SimEngine", String.format("GetResourceTypeToDestination " +
                    "Cannot find resource of type %s", resourceType.toString()));
            return;
        }


        HaulState haulState = new HaulState(actorEntity, resource, haulToPos);
        JobComponent jobComponent = new JobComponent("Haul resource", JobStatus.IDLE, haulState);
        jobEntity.add(jobComponent);
        jobEntity.add(new JobTypeComponent(JobType.HAUL));
        Orchestrator.engine.addEntity(jobEntity);
    }

}
