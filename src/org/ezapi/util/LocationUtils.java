package org.ezapi.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public final class LocationUtils {

    public static Location add(Location original, int x, int y, int z) {
        return new Location(original.getWorld(), original.getX() + x, original.getY() + y, original.getZ() + z);
    }

    public static void setBlock(Location location, Material block) {
        location.getBlock().setType(block);
    }

    public static void summonEntity(Location location, EntityType entityType) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnEntity(location, entityType);
        }
    }

}
