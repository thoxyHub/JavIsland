package com.group16.view.graphics.items;

import com.group16.view.Sprite;
import com.group16.view.utils.RegionOfInterest;
import com.group16.model.items.resources.ResourceType;

import java.util.EnumMap;

/**
 * {@code ResourceView} handles the graphical representation of different resource items
 * (e.g., wood, stone, gold, ammo) in the inventory.
 */
public class ResourceView extends ItemView {

    // Maps each ResourceType to its corresponding Sprite (image and region of interest)
    private static final EnumMap<ResourceType, Sprite> SPRITES = new EnumMap<>(ResourceType.class);

    static {
        // Stone: from main decoration sprite sheet
        SPRITES.put(ResourceType.STONE,
                new Sprite("/sprites/Outdoor_decoration/Outdoor_Decor_Free.png",
                        new RegionOfInterest(0, 64, 16, 16)));

        // Wood: custom sprite from wood inventory image
        SPRITES.put(ResourceType.WOOD,
                new Sprite("/sprites/Outdoor_decoration/inventory_wood.png",
                        new RegionOfInterest(0, 0, 500, 500)));

        // Gold: another region from the same decoration sheet
        SPRITES.put(ResourceType.GOLD,
                new Sprite("/sprites/Outdoor_decoration/Outdoor_Decor_Free.png",
                        new RegionOfInterest(0, 96, 16, 16)));

        // Ammo: small pickup icon
        SPRITES.put(ResourceType.AMMO,
                new Sprite("/sprites/pickups.png",
                        new RegionOfInterest(32, 64, 32, 32)));
    }

    /**
     * Constructs a view for a given resource type and quantity.
     *
     * @param type     the type of resource (STONE, WOOD, GOLD, AMMO)
     * @param quantity the amount of this resource in the inventory
     */
    public ResourceView(ResourceType type, int quantity) {
        super(SPRITES.get(type).getImage(), quantity);
    }
}
