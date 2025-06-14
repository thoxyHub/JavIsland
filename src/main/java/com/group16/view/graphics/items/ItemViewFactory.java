package com.group16.view.graphics.items;

import com.group16.model.items.resources.Item;
import com.group16.model.items.resources.Resources;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.weapons.Gun;
import com.group16.model.items.weapons.Sword;

/**
 * A factory class to create the appropriate {@link ItemView} for a given {@link Item}.
 * Determines the view based on the runtime type of the item.
 */
public class ItemViewFactory {

    /**
     * Creates an {@link ItemView} corresponding to the given {@link Item} type.
     *
     * @param item the item to create a view for
     * @return the appropriate ItemView subclass instance
     * @throws IllegalArgumentException if the item type is not supported
     */
    public static ItemView createView(Item item) {
        if (item instanceof Resources resource) {
            ResourceType type = resource.getType();
            return new ResourceView(type, resource.getQuantity());
        }

        if (item instanceof Sword sword) {
            return new SwordView(sword.getQuantity());
        }

        if (item instanceof Gun gun) {
            return new GunView(gun.getQuantity());
        }

        throw new IllegalArgumentException("Unsupported item type for view: " + item.getClass().getSimpleName());
    }
}
