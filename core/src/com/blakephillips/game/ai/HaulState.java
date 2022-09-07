package com.blakephillips.game.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blakephillips.engine.ai.State;
import com.blakephillips.engine.ecs.components.PathComponent;
import com.blakephillips.engine.ecs.components.ai.ReservedComponent;
import com.blakephillips.engine.ecs.components.position.PositionComponent;
import com.blakephillips.engine.ecs.components.position.SnapToPositionComponent;
import com.blakephillips.engine.ecs.systems.PathFollowingSystem;
import com.blakephillips.engine.utilities.grid.Pathfinding;
import com.blakephillips.engine.utilities.grid.TileMap;
import com.blakephillips.engine.utilities.grid.Vertex;
import com.blakephillips.game.Orchestrator;
import space.earlygrey.simplegraphs.Path;

public class HaulState extends State {

    public HaulState(Entity actorEntity, Entity haulEntity, Vector2 haulToPosition) {
        super(actorEntity);
        setHaulEntity(haulEntity);
        setHaulToPosition(haulToPosition);
    }

    private Entity haulEntity;
    private Vector2 haulToPosition;
    private TileMap tileMap;
    private HaulStatus haulStatus = HaulStatus.NONE;
    PathComponent pathComponent;

    ComponentMapper<PositionComponent> posComponents = ComponentMapper.getFor(PositionComponent.class);
    ComponentMapper<PathComponent> pathComponents = ComponentMapper.getFor(PathComponent.class);
    ComponentMapper<ReservedComponent> reservedComponents = ComponentMapper.getFor(ReservedComponent.class);
    @Override
    public void enter() {
        Gdx.app.log("Game", "Haul state entered");

        if (haulEntity == null) {
            Gdx.app.error("Game", "HaulObject is null");
            this.exit();
            return;
        }

        if (posComponents.has(entity) == false) {
            Gdx.app.error("Game", "Haul actor has no position component");
            this.exit();
            return;
        }

        if (posComponents.has(haulEntity) == false) {
            Gdx.app.error("Game", "HaulObject has no position component");
            this.exit();
            return;
        }

        if (isAlreadyReserved(haulEntity)) {
            Gdx.app.error("Game", "HaulObject is already reserved by another actor");
            this.exit();
            return;
        }

        PositionComponent posComponent = posComponents.get(entity);
        PositionComponent haulPosComponent = posComponents.get(haulEntity);

        Engine engine = Orchestrator.getEngine();
        this.tileMap = engine.getSystem(PathFollowingSystem.class).getTileMap();


        Path<Vertex> pathToHaulObject = Pathfinding.getPath(posComponent.pos, haulPosComponent.pos, tileMap);
        if (pathToHaulObject.isEmpty()) {
            Gdx.app.debug("Game", "HaulObject cannot be reached by actor");
            this.exit();
            return;
        }

        if (!Pathfinding.isReachable(haulPosComponent.pos, haulToPosition, tileMap)) {
            Gdx.app.debug("Game", "HaulTo destination cannot be reached");
            this.exit();
            return;
        }

        this.pathComponent = new PathComponent(pathToHaulObject);
        entity.add(pathComponent);
        entity.add(new ReservedComponent(entity));
        haulStatus = HaulStatus.TRAVELLING_TO_OBJECT;
        stateStatus = StateStatus.RUNNING;
    }

    @Override
    public void exit() {
        Gdx.app.log("Game", "Haul state exited");
        stateStatus = StateStatus.EXITED;
        haulStatus = HaulStatus.DONE;
    }

    //State status must be RUNNING for update() to be called
    @Override
    public void update(float deltaTime) {

        PositionComponent posComponent = posComponents.get(entity);
        PositionComponent haulPosComponent = posComponents.get(haulEntity);
        //Gdx.app.log("s", String.valueOf(Pathfinding.chebyshevDistance(posComponent.pos, haulPosComponent.pos)));
        if (!pathComponents.has(entity) && haulStatus == HaulStatus.TRAVELLING_TO_OBJECT
                && Pathfinding.chebyshevDistance(posComponent.pos, haulPosComponent.pos) <= 15) {

            Gdx.app.log("Game", "Arrived at haul destination");
            Path<Vertex> pathToDestination = Pathfinding.getPath(posComponent.pos, haulToPosition, tileMap);

            //Validate we can reach the destination
            if (pathToDestination.isEmpty()) {
                Gdx.app.debug("Game", "HaulTo destination cannot be reached");
                this.exit();
                return;
            }

            haulEntity.add(new SnapToPositionComponent(posComponent));
            entity.add(new PathComponent(pathToDestination));
            haulStatus = HaulStatus.HAULING_TO_DESTINATION;
        }

        if (!pathComponents.has(entity) &&
                haulStatus == HaulStatus.HAULING_TO_DESTINATION) {

            //stop being bound to actor
            haulEntity.remove(SnapToPositionComponent.class);
            haulEntity.remove(ReservedComponent.class);

            //temp
            entity.add(new PathComponent(new Vector2(200, 200)));
            //

            exit();
        }


    }
    public void setHaulToPosition(Vector2 haulToPosition) {
        this.haulToPosition = haulToPosition;
    }
    public Vector2 getHaulToPosition() {
        return haulToPosition;
    }
    public void setHaulEntity(Entity haulEntity) {
        this.haulEntity = haulEntity;
    }
    public Entity getHaulEntity() {
        return haulEntity;
    }

    private enum HaulStatus {
        NONE,
        TRAVELLING_TO_OBJECT,
        HAULING_TO_DESTINATION,
        DONE,
    }

}
