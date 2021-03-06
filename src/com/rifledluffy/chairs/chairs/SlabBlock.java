/**
 * Borrowed code from BlackScarx's BetterChairs plugin.
 * Just in case it seems familiar.
 * https://github.com/BlackScarx/BetterChairs/blob/master/src/net/blackscarx/betterchairs/SlabBlock.java
 */

package com.rifledluffy.chairs.chairs;

import org.bukkit.Material;

public enum SlabBlock {

    ACACIA("acacia_slab"),
    BIRCH("birch_slab"),
    BRICK("brick_slab"),
    COBBLESTONE("cobblestone_slab"),
    OAK("oak_slab"),
    DARK_OAK("dark_oak_slab"),
    DARK_PRISMARINE("dark_prismarine_slab"),
    JUNGLE("jungle_slab"),
    NETHER_BRICK("nether_brick_slab"),
    PETRIFIED_OAK("petrified_oak_slab"),
    PRISMARINE("prismarine_slab"),
    PRISMARINE_BRICK("prismarine_brick_slab"),
    QUARTZ("quartz_slab"),
    RED_SANDSTONE("red_sandstone_slab"),
    SANDSTONE("sandstone_slab"),
    SMOOTH("smooth_slab"),
    SPRUCE("spruce_slab"),
    STONE("stone_slab"),
    STONE_BRICK("stone_brick_slab"),
    WOOD("wood_slab"),
    PURPUR_STAIRS("purpur_slab");

    final private String name;

    SlabBlock(String name) {
        this.name = name;
    }

    public static String from(Material mat) {
        for (SlabBlock slab : values()) {
            if (slab.getName().equalsIgnoreCase(mat.name())) {
                return slab.getName();
            }
        }
        return "null";
    }

    public String getName() {
        return name;
    }

    public static SlabBlock[] getList() {
    	return values();
    }

}