package org.ezapi.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.ezapi.world.Location2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public final class BuildingUtils {

    public static void polygon(List<Location2D> locations, int y, int height, double radius, Material block) {
        if (locations.size() < 2) return;
        if (!block.isBlock()) return;
        World world = locations.get(0).getWorld();
        if (world == null) return;
        if (height < 0) {
            height = -height;
            y = y - height;
        }
        int start = y;
        int end = y + height;
        Loop.foreach(locations, ((integer, location) -> {
            int i = integer + 1;
            if (i >= locations.size()) i = 0;
            Location2D second = locations.get(i);
            if (location.getWorld() == world) {
                for (int o = start; o < end; o++) {
                    drawALine(location.getLocation(o), second.getLocation(o), radius, block, true);
                }
            }
        }));
    }

    public static void replace(Location from, Location to, Material blockToBeReplace, Material blockToReplace) {
        if (from.getWorld() != to.getWorld()) return;
        if (!blockToBeReplace.isBlock() || !blockToReplace.isBlock()) return;
        World world = from.getWorld();
        if (world == null) return;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        replace0(new Location(world, minX, minY, minZ), new Location(world, maxX, maxY, maxZ), blockToBeReplace, blockToReplace);
    }

    public static void replace0(Location from, Location to, Material blockToBeReplace, Material blockToReplace) {
        World world = from.getWorld();
        for (int x = from.getBlockX(); x <= to.getBlockX(); x++) {
            for (int y = from.getBlockY(); y <= to.getBlockY(); y++) {
                for (int z = from.getBlockZ(); z <= to.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().equals(blockToBeReplace)) {
                        block.setType(blockToReplace);
                    }
                }
            }
        }
    }

    public static void outline(Location from, Location to, Material block) {
        if (from.getWorld() != to.getWorld()) return;
        if (!block.isBlock()) return;
        World world = from.getWorld();
        if (world == null) return;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        outline0(new Location(world, minX, minY, minZ), new Location(world, maxX, maxY, maxZ), block);
    }

    private static void outline0(Location from, Location to, Material block) {
        World world = from.getWorld();
        for (int x = from.getBlockX(); x < to.getBlockX(); x++) {
            setBlock(new Location(world, x, from.getY(), from.getZ()), block);
            setBlock(new Location(world, x, from.getY(), to.getZ()), block);
            setBlock(new Location(world, x, to.getY(), from.getZ()), block);
            setBlock(new Location(world, x, to.getY(), to.getZ()), block);
        }
        for (int y = from.getBlockY(); y < to.getBlockY(); y++) {
            setBlock(new Location(world, from.getX(), y, from.getZ()), block);
            setBlock(new Location(world, from.getX(), y, to.getZ()), block);
            setBlock(new Location(world, to.getX(), y, from.getZ()), block);
            setBlock(new Location(world, to.getX(), y, to.getZ()), block);
        }
        for (int z = from.getBlockZ(); z < to.getBlockZ(); z++) {
            setBlock(new Location(world, from.getX(), from.getY(), z), block);
            setBlock(new Location(world, from.getX(), to.getY(), z), block);
            setBlock(new Location(world, to.getX(), from.getY(), z), block);
            setBlock(new Location(world, to.getX(), to.getY(), z), block);
        }
        setBlock(to, block);
    }

    private static void fill0(Location from, Location to, Material block) {
        World world = from.getWorld();
        for (int x = from.getBlockX(); x <= to.getBlockX(); x++) {
            for (int y = from.getBlockY(); y <= to.getBlockY(); y++) {
                for (int z = from.getBlockZ(); z <= to.getBlockZ(); z++) {
                    world.getBlockAt(x, y, z).setType(block);
                }
            }
        }
    }

    public static void fill(Location from, Location to, Material block) {
        if (from.getWorld() != to.getWorld()) return;
        if (!block.isBlock()) return;
        World world = from.getWorld();
        if (world == null) return;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        fill0(new Location(world, minX, minY, minZ), new Location(world, maxX, maxY, maxZ), block);
        /*
        for (int i = 0; i < Math.abs(x2 - x1); i++) {
            if (minX + i > maxX + 1) break;
            for (int o = 0; o < Math.abs(z2 - z1); o++) {
                if (minZ + o > maxZ + 1) break;
                for (int p = 0; p < Math.abs(y2 - y1); p++) {
                    if (minY + p > maxY + 1) break;
                    world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).setType(block);
                }
            }
        }
        */
    }

    public static void hollow(Location from, Location to, Material block) {
        if (from.getWorld() != to.getWorld()) return;
        if (!block.isBlock()) return;
        World world = from.getWorld();
        if (world == null) return;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        fill0(new Location(world, minX, minY, minZ), new Location(world, maxX, maxY, maxZ), block);
        if ((maxX - minX) < 3) return;
        if ((maxY - minY) < 3) return;
        if ((maxZ - minZ) < 3) return;
        fill0(new Location(world, minX + 1, minY + 1, minZ + 1), new Location(world, maxX - 1, maxY - 1, maxZ - 1), Material.AIR);
    }

    public static void ellipse(Location center, double radiusX, double radiusZ, Material block, boolean filled) {
        if (!block.isBlock()) return;
        World world = center.getWorld();
        if (world == null) return;
        radiusX += 0.5D;
        radiusZ += 0.5D;
        double invRadiusX = 1.0D / radiusX;
        double invRadiusZ = 1.0D / radiusZ;
        int ceilRadiusX = (int)Math.ceil(radiusX);
        int ceilRadiusZ = (int)Math.ceil(radiusZ);
        double nextXn = 0.0D;
        int x;
        label43: for (x = 0; x <= ceilRadiusX; x++) {
            double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextZn = 0.0D;
            for (int z = 0; z <= ceilRadiusZ; z++) {
                double zn = nextZn;
                nextZn = (z + 1) * invRadiusZ;
                double distanceSq = (MathUtils.square(xn) + MathUtils.square(zn));
                if (distanceSq > 1.0D) {
                    if (z == 0)
                        break label43;
                    break;
                }
                if (filled || (MathUtils.square(nextXn) + MathUtils.square(zn)) > 1.0D || (MathUtils.square(xn) + MathUtils.square(nextZn)) > 1.0D) {
                    setBlock(LocationUtils.add(center, x, 0, z), block);
                    setBlock(LocationUtils.add(center, -x, 0, z), block);
                    setBlock(LocationUtils.add(center, x, 0, -z), block);
                    setBlock(LocationUtils.add(center, -x, 0, -z), block);
                }
            }
        }
    }

    public static void circle(Location center, double radius, Material block, boolean filled) {
        ellipse(center, radius, radius, block, filled);
    }

    public static void sphere(Location center, double radius, Material block, boolean filled) {
        ellipsoid(center, radius, radius, radius, block, filled);
    }

    public static void drawALine(Location from, Location to, Material block) {
        drawALine(from, to, 1, block, true);
    }

    public static void drawALine(Location from, Location to, double radius, Material block, boolean filled) {
        if (from.getWorld() != to.getWorld()) return;
        if (!block.isBlock()) return;
        World world = from.getWorld();
        if (world == null) return;
        Collection<Location> collection = new HashSet<>();
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int tipx = x1;
        int tipy = y1;
        int tipz = z1;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);
        if (dx + dy + dz == 0) {
            collection.add(new Location(world, tipx, tipy, tipz));
        } else {
            int dMax = Math.max(Math.max(dx, dy), dz);
            if (dMax == dx) {
                for (int domstep = 0; domstep <= dx; domstep++) {
                    tipx = x1 + domstep * ((x2 - x1 > 0) ? 1 : -1);
                    tipy = Math.round(y1 + domstep * dy / dx * ((y2 - y1 > 0) ? 1 : -1));
                    tipz = Math.round(z1 + domstep * dz / dx * ((z2 - z1 > 0) ? 1 : -1));
                    collection.add(new Location(world, tipx, tipy, tipz));
                }
            } else if (dMax == dy) {
                for (int domstep = 0; domstep <= dy; domstep++) {
                    tipy = y1 + domstep * ((y2 - y1 > 0) ? 1 : -1);
                    tipx = Math.round(x1 + domstep * dx / dy * ((x2 - x1 > 0) ? 1 : -1));
                    tipz = Math.round(z1 + domstep * dz / dy * ((z2 - z1 > 0) ? 1 : -1));
                    collection.add(new Location(world, tipx, tipy, tipz));
                }
            } else {
                for (int domstep = 0; domstep <= dz; domstep++) {
                    tipz = z1 + domstep * ((z2 - z1 > 0) ? 1 : -1);
                    tipy = Math.round(y1 + domstep * dy / dz * ((y2 - y1 > 0) ? 1 : -1));
                    tipx = Math.round(x1 + domstep * dx / dz * ((x2 - x1 > 0) ? 1 : -1));
                    collection.add(new Location(world, tipx, tipy, tipz));
                }
            }
        }
        if (filled) {
            Collection<Location> locations = new HashSet<>();
            int ceilrad = (int) Math.ceil(radius);
            for (Location location : collection) {
                int get_x = location.getBlockX();
                int get_y = location.getBlockY();
                int get_z = location.getBlockZ();
                for (int loopx = get_x - ceilrad; loopx <= get_x + ceilrad; loopx++) {
                    for (int loopy = get_y - ceilrad; loopy <= get_y + ceilrad; loopy++) {
                        for (int loopz = get_z - ceilrad; loopz <= get_z + ceilrad; loopz++) {
                            double sum = 0.0D;
                            for (double d : new double[] { (loopx - get_x), (loopy - get_y), (loopz - get_z) }) {
                                sum += Math.pow(d, 2.0D);
                            }
                            if (Math.sqrt(sum) <= radius) {
                                locations.add(new Location(world, loopx, loopy, loopz));
                            }
                        }
                    }
                }
            }
            collection = locations;
        } else {
            Collection<Location> locations = new HashSet<>();
            for (Location location : collection) {
                double x = location.getX();
                double y = location.getY();
                double z = location.getZ();
                if (!collection.contains(new Location(location.getWorld(), x + 1.0D, y, z)) ||
                        !collection.contains(new Location(location.getWorld(), x - 1.0D, y, z)) ||
                        !collection.contains(new Location(location.getWorld(), x, y + 1.0D, z)) ||
                        !collection.contains(new Location(location.getWorld(), x, y - 1.0D, z)) ||
                        !collection.contains(new Location(location.getWorld(), x, y, z + 1.0D)) ||
                        !collection.contains(new Location(location.getWorld(), x, y, z - 1.0D)))
                    locations.add(location);
            }
            collection = locations;
        }
        setBlocks(collection, block);
    }

    public static void ellipsoid(Location center, double radiusX, double radiusY, double radiusZ, Material block, boolean filled) {
        radiusX += 0.5D;
        radiusY += 0.5D;
        radiusZ += 0.5D;
        double invRadiusX = 1.0D / radiusX;
        double invRadiusY = 1.0D / radiusY;
        double invRadiusZ = 1.0D / radiusZ;
        int ceilRadiusX = (int) Math.ceil(radiusX);
        int ceilRadiusY = (int) Math.ceil(radiusY);
        int ceilRadiusZ = (int) Math.ceil(radiusZ);
        double nextXn = 0.0D;
        int x;
        loop1: for (x = 0; x <= ceilRadiusX; x++) {
            double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0.0D;
            int y;
            loop2: for (y = 0; y <= ceilRadiusY; y++) {
                double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0.0D;
                for (int z = 0; z <= ceilRadiusZ; z++) {
                    double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;
                    double distanceSq = (MathUtils.cuboidArea(xn, zn, yn) / 2);
                    if (distanceSq > 1.0D) {
                        if (z == 0) {
                            if (y == 0) break loop1;
                            break loop2;
                        }
                        break;
                    }
                    if (filled || (MathUtils.cuboidArea(nextXn, zn, yn) / 2) > 1.0D || (MathUtils.cuboidArea(xn, zn, nextYn) / 2) > 1.0D || (MathUtils.cuboidArea(xn, nextZn, yn) / 2) > 1.0D) {
                        setBlock(LocationUtils.add(center, x, y, z), block);
                        setBlock(LocationUtils.add(center, -x, y, z), block);
                        setBlock(LocationUtils.add(center, x, -y, z), block);
                        setBlock(LocationUtils.add(center, x, y, -z), block);
                        setBlock(LocationUtils.add(center, -x, -y, z), block);
                        setBlock(LocationUtils.add(center, x, -y, -z), block);
                        setBlock(LocationUtils.add(center, -x, y, -z), block);
                        setBlock(LocationUtils.add(center, -x, -y, -z), block);
                    }
                }
            }
        }
    }

    public static void setBlocks(Collection<Location> collection, Material block) {
        if (!block.isBlock()) return;
        for (Location location : collection) {
            setBlock(location, block);
        }
    }

    public static void setBlock(Location location, Material block) {
        if (!block.isBlock()) return;
        World world = location.getWorld();
        if (world == null) return;
        world.getBlockAt(location).setType(block);
    }

    public static int count(Location from, Location to, Material block) {
        if (!block.isBlock()) return 0;
        if (from.getWorld() != to.getWorld()) return 0;
        World world = from.getWorld();
        if (world == null) return 0;
        int amount = 0;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (world.getBlockAt(x, y, z).getType() == block) {
                        amount++;
                    }
                }
            }
        }
        return amount;
    }

    public static int countNotAir(Location from, Location to) {
        if (from.getWorld() != to.getWorld()) return 0;
        World world = from.getWorld();
        if (world == null) return 0;
        int amount = 0;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (world.getBlockAt(x, y ,z).getType() != Material.AIR) {
                        amount++;
                    }
                }
            }
        }
        return amount;
    }

    public static int count(Location from, Location to) {
        if (from.getWorld() != to.getWorld()) return 0;
        World world = from.getWorld();
        if (world == null) return 0;
        int amount = 0;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    amount++;
                }
            }
        }
        return amount;
    }

    static void move(Location from, Location to, GeometryUtils.Direction direction, int blocks) {
        if (from.getWorld() != to.getWorld()) return;
        World world = from.getWorld();
        if (world == null) return;
        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();
        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        switch (direction) {
            case UP:
                for (int p = 0; p < Math.abs(y2 - y1); p++) {
                    if (maxY - p < minY - 1) break;
                    for (int i = 0; i < Math.abs(x2 - x1); i++) {
                        if (minX + i > maxX) break;
                        for (int o = 0; o < Math.abs(z2 - z1); o++) {
                            if (minZ + o > maxZ) break;
                            world.getBlockAt(new Location(world, minX + i, maxY - p + blocks, minZ + o)).setType(world.getBlockAt(new Location(world, minX + i, maxY - p, minZ + o)).getType());
                            world.getBlockAt(new Location(world, minX + i, maxY - p, minZ + o)).setType(Material.AIR);
                        }
                    }
                }
                break;
            case DOWN:
                for (int p = 0; p < Math.abs(y2 - y1); p++) {
                    if (minY + p < maxY) break;
                    for (int i = 0; i < Math.abs(x2 - x1); i++) {
                        if (minX + i > maxX) break;
                        for (int o = 0; o < Math.abs(z2 - z1); o++) {
                            if (minZ + o > maxZ) break;
                            world.getBlockAt(new Location(world, minX + i, minY + p - blocks, minZ + o)).setType(world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).getType());
                            world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).setType(Material.AIR);
                        }
                    }
                }
                break;
            case EAST:
                for (int i = 0; i < Math.abs(x2 - x1); i++) {
                    if (maxX - i < minX - 1) break;
                    for (int o = 0; o < Math.abs(z2 - z1); o++) {
                        if (minZ + o > maxZ) break;
                        for (int p = 0; p < Math.abs(y2 - y1); p++) {
                            if (minY + p > maxY) break;
                            world.getBlockAt(new Location(world, maxX - i + blocks, minY + p, minZ + o)).setType(world.getBlockAt(new Location(world, maxX - i, minY + p, minZ + o)).getType());
                            world.getBlockAt(new Location(world, maxX - i, minY + p, minZ + o)).setType(Material.AIR);
                        }
                    }
                }
                break;
            case WEST:
                for (int i = 0; i < Math.abs(x2 - x1); i++) {
                    if (minX + i > maxX) break;
                    for (int o = 0; o < Math.abs(z2 - z1); o++) {
                        if (minZ + o > maxZ) break;
                        for (int p = 0; p < Math.abs(y2 - y1); p++) {
                            if (minY + p > maxY) break;
                            world.getBlockAt(new Location(world, minX + i - blocks, minY + p, minZ + o)).setType(world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).getType());
                            world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).setType(Material.AIR);
                        }
                    }
                }
                break;
            case SOUTH:
                for (int o = 0; o < Math.abs(z2 - z1); o++) {
                    if (minZ + o > maxZ) break;
                    for (int p = 0; p < Math.abs(y2 - y1); p++) {
                        if (minY + p > maxY) break;
                        for (int i = 0; i < Math.abs(x2 - x1); i++) {
                            if (minX + i > maxX) break;
                            world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o - blocks)).setType(world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).getType());
                            world.getBlockAt(new Location(world, minX + i, minY + p, minZ + o)).setType(Material.AIR);
                        }
                    }
                }
                break;
            case NORTH:
                for (int o = 0; o < Math.abs(z2 - z1); o++) {
                    if (maxZ - o < minZ - 1) break;
                    for (int p = 0; p < Math.abs(y2 - y1); p++) {
                        if (minY + p > maxY) break;
                        for (int i = 0; i < Math.abs(x2 - x1); i++) {
                            if (minX + i > maxX) break;
                            world.getBlockAt(new Location(world, minX + i, minY + p, maxZ - o)).setType(world.getBlockAt(new Location(world, minX + i, minY + p, maxZ - o)).getType());
                            world.getBlockAt(new Location(world, minX + i, minY + p, maxZ - o)).setType(Material.AIR);
                        }
                    }
                }
                break;
        }

    }

}
