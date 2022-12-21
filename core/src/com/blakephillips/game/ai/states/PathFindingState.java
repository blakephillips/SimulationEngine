package com.blakephillips.game.ai.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ai.State;
import com.blakephillips.engine.ecs.components.PathComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.systems.PathFollowingSystem;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;
import com.blakephillips.game.Orchestrator;
import space.earlygrey.simplegraphs.Path;


public class PathFindingState extends State {

    private final Vector2 destination;
    private PositionComponent positionComponent;
    private PathComponent pathComponent;
    private final int proximity = 15;
    private final ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<PathComponent> pathComponents = ComponentMapper.getFor(PathComponent.class);

    public PathFindingState(Entity actor, Vector2 destination) {
        super(actor);
        this.destination = destination;
    }

    @Override
    public void enter() {
        Gdx.app.log("Game", "Path finding state entered");

        if (!posComponents.has(entity)) {
            Gdx.app.error("Game", "Attempted pathing entity has no position component.");
            exit(true);
            return;
        }
        positionComponent = posComponents.get(entity);

        Engine engine = Orchestrator.getEngine();
        TileMap tileMap = engine.getSystem(PathFollowingSystem.class).getTileMap();
        Path<Vertex> vertexPath = Pathfinding.getPath(positionComponent.pos, destination, tileMap);
        pathComponent = new PathComponent(vertexPath);
        entity.add(pathComponent);
        stateStatus = StateStatus.RUNNING;
    }

    @Override
    public void exit() {
        stateStatus = StateStatus.COMPLETE;
        Gdx.app.log("Game", "Exited path finding state");
    }

    @Override
    public void update(float deltaTime) {
        if (stateStatus == StateStatus.RUNNING && !pathComponents.has(entity)) {
            if (Pathfinding.chebyshevDistance(positionComponent.pos, destination) > proximity) {
                exit(true);
                return;
            }
            exit();
        }
    }
}
