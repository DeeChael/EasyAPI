package org.ezapi.world;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.ezapi.util.MathUtils;

public class Location2D {

    private World world;

    private double x;

    private double z;

    public Location2D(World world, double x, double z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public Chunk getChunk() {
        return this.getWorld().getChunkAt(this.getLocation(0));
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public int getBlockX() {
        return NumberConversions.floor(x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public int getBlockZ() {
        return NumberConversions.floor(z);
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Location2D add(double x, double z) {
        return new Location2D(this.world, this.x + x, this.z + z);
    }

    public Location2D subtract(double x, double z) {
        return add(-x, -z);
    }

    public double distance(Location2D location2D) {
        double minX = Math.min(this.x, location2D.x);
        double maxX = Math.max(this.x, location2D.x);
        double minZ = Math.min(this.z, location2D.z);
        double maxZ = Math.max(this.z, location2D.z);
        return MathUtils.hypotenuse(maxX - minX, maxZ - minZ);
    }

    public Location2D multiply(double times) {
        return this.multiply(times, times);
    }

    public Location2D multiply(double xTimes, double zTimes) {
        return new Location2D(world, x * xTimes, z * zTimes);
    }

    public Location2D zero() {
        this.x = 0.0d;
        this.z = 0.0d;
        return this;
    }

    public Location getLocation(double y) {
        return new Location(this.world, x, y, z);
    }

}
