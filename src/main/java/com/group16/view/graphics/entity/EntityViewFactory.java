package com.group16.view.graphics.entity;

import com.group16.model.entity.GameEntity;
import com.group16.model.entity.actors.Mobs;
import com.group16.model.entity.actors.Player;
import com.group16.model.entity.elements.*;
import com.group16.model.items.weapons.Bullet;
import com.group16.view.graphics.entity.actors.ActorType;
import com.group16.view.graphics.entity.actors.ActorView;
import com.group16.view.graphics.entity.elements.*;
import com.group16.view.graphics.entity.projectile.BulletView;
import com.group16.model.utils.maths.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A factory class for creating visual representations (EntityViews) of game entities.
 * It maps each GameEntity subclass to its corresponding EntityView implementation.
 */
public class EntityViewFactory {

    // Registry that maps entity classes to their view constructors
    private static final Map<Class<? extends GameEntity>, BiFunction<GameEntity, Vector, EntityView>> registry = new HashMap<>();

    static {
        // Actor views
        register(Player.class, (entity, position) -> new ActorView(ActorType.Player, position));
        register(Mobs.class, (entity, position) -> new ActorView(ActorType.Mob, position));

        // Element views
        register(Rock.class, (entity, position) -> new RockView(position));
        register(Tree.class, (entity, position) -> new TreeView(position));
        register(StoneBlock.class, (entity, position) -> new StoneBlockView(position));
        register(WoodBlock.class, (entity, position) -> new WoodBlockView(position));

        // Projectile view
        register(Bullet.class, (entity, position) -> new BulletView(position));
    }

    /**
     * Registers a constructor for a specific type of GameEntity.
     *
     * @param entityClass The entity class type.
     * @param constructor A function that takes the entity and its position to construct a view.
     * @param <T>         A type extending GameEntity.
     */
    private static <T extends GameEntity> void register(Class<T> entityClass, BiFunction<T, Vector, EntityView> constructor) {
        // Unchecked cast needed for generic handling
        registry.put(entityClass, (BiFunction<GameEntity, Vector, EntityView>) constructor);
    }

    /**
     * Creates an EntityView for a given GameEntity using its registered constructor.
     *
     * @param entity   The GameEntity instance.
     * @param position The current position of the entity.
     * @return The visual representation of the entity.
     * @throws IllegalArgumentException if no view is registered for the entity's class.
     */
    public static EntityView createView(GameEntity entity, Vector position) {
        BiFunction<GameEntity, Vector, EntityView> constructor = registry.get(entity.getClass());
        if (constructor != null) {
            return constructor.apply(entity, position);
        } else {
            throw new IllegalArgumentException("No view registered for entity type: " + entity.getClass());
        }
    }
}
