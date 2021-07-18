package org.ezapi.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ezapi.EasyAPI;
import org.ezapi.function.NonReturnWithFive;
import org.ezapi.function.NonReturnWithSix;

public final class ParticleUtils {

    public static void asyncOutline(Location from, Location to, Particle particle, double distance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                outline(from, to, particle, distance);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static void outline(Location from, Location to, Particle particle, double distance) {
        if (from.getWorld() != to.getWorld()) return;
        World world = from.getWorld();
        double x1 = from.getX();
        double y1 = from.getY();
        double z1 = from.getZ();
        double x2 = to.getX();
        double y2 = to.getY();
        double z2 = to.getZ();
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);
        for (double i = 0; i < Math.abs(x2 - x1); i += distance) {
            if (minX + i > maxX) break;
            world.spawnParticle(particle, minX + i, y1, z1, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, minX + i, y1, z2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, minX + i, y2, z1, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, minX + i, y2, z2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        for (double i = 0; i < Math.abs(z2 - z1); i += distance) {
            if (minZ + i > maxZ) break;
            world.spawnParticle(particle, x1, y1, minZ + i, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, x2, y1, minZ + i, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, x1, y2, minZ + i, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, x2, y2, minZ + i, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        for (double i = 0; i < Math.abs(y2 - y1); i += distance) {
            if (minY + i > maxY) break;
            world.spawnParticle(particle, x1, minY + i, z1, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, x2, minY + i, z1, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, x1, minY + i, z2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(particle, x2, minY + i, z2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private final static boolean ballFinished = false;

    public static void asyncBall(Location location, double radius, Particle particle) {
        if (!ballFinished) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                ball(location, radius, particle);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static void ball(Location location, double radius, Particle particle) {
        if (!ballFinished) return;
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        for (int i = 0; i < 361; i++) {
            if (i == 0 || i == 360) {
                world.spawnParticle(particle, x + radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                for (int b = 0; b < 361; b++) {
                    double c = b;
                    if (c > 90 && c < 180) c = c - 90;
                    if (c > 180 && c < 270) c = c - 180;
                    if (c > 270) c = c - 270;
                    c = Math.toRadians(c);
                    double x_change = Math.cos(c) * radius;
                    double y_change = Math.sin(c) * radius;
                    double z_change = 0.0D;
                    if (b < 90) {
                        world.spawnParticle(particle, x + x_change, y + y_change, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (b > 90 && b < 180) {
                        world.spawnParticle(particle, x - y_change, y + x_change, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (b > 180 && b < 270) {
                        world.spawnParticle(particle, x - y_change, y - x_change, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (b > 270) {
                        world.spawnParticle(particle, x + y_change, y - x_change, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else if (i == 90) {
                world.spawnParticle(particle, x, y, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                for (int b = 0; b < 361; b++) {
                    double c = b;
                    if (c > 90 && c < 180) c = c - 90;
                    if (c > 180 && c < 270) c = c - 180;
                    if (c > 270) c = c - 270;
                    c = Math.toRadians(c);
                    double x_change = 0.0D;
                    double y_change = Math.sin(c) * radius;
                    double z_change = Math.cos(c) * radius;
                    if (b < 90) {
                        world.spawnParticle(particle, x + x_change, y + y_change, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (b > 90 && b < 180) {
                        world.spawnParticle(particle, x + x_change, y + z_change, z - y_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (b > 180 && b < 270) {
                        world.spawnParticle(particle, x + x_change, y - y_change, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    if (b > 270) {
                        world.spawnParticle(particle, x + x_change, y - z_change, z + y_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else if (i == 180) {
                world.spawnParticle(particle, x - radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            } else if (i == 270) {
                world.spawnParticle(particle, x, y, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            } else {
                double a = i;
                if (a > 90 && a < 180) a = a - 90;
                if (a > 180 && a < 270) a = a - 180;
                if (a > 270) a = a - 270;
                a = Math.toRadians(a);
                double x_change = Math.cos(a) * radius;
                double z_change = Math.sin(a) * radius;
                if (i < 90) {
                    world.spawnParticle(particle, x + x_change, y, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    for (int b = 0; b < 361; b++) {
                        double c = b;
                        if (c > 90 && c < 180) c = c - 90;
                        if (c > 180 && c < 270) c = c - 180;
                        if (c > 270) c = c - 270;
                        c = Math.toRadians(c);
                        double xx_change = Math.cos(a) * (Math.cos(c) * radius);
                        double yy_change = Math.sin(c) * radius;
                        double zz_change = Math.sin(a) * (Math.cos(c) * radius);
                        if (b < 90) {
                            world.spawnParticle(particle, x + xx_change, y + yy_change, z + zz_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (b > 90 && b < 180) {
                            world.spawnParticle(particle, x - radius + xx_change, y + radius - yy_change, z - zz_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (b > 180 && b < 270) {
                            world.spawnParticle(particle, x - xx_change, y - yy_change, z + radius - zz_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (b > 270) {
                            world.spawnParticle(particle, x + radius - xx_change, y - radius + yy_change, z + radius - zz_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
                if (i > 90 && i < 180) {
                    world.spawnParticle(particle, x - z_change, y, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    /*
                    for (int b = 0; b < 361; b++) {
                        double c = b;
                        if (c > 90 && c < 180) c = c - 90;
                        if (c > 180 && c < 270) c = c - 180;
                        if (c > 270) c = c - 270;
                        c = Math.toRadians(c);
                        double xx_change = Math.cos(a) * (Math.cos(c) * radius);
                        double yy_change = Math.sin(c) * radius;
                        double zz_change = Math.sin(a) * (Math.cos(c) * radius);
                        if (b < 90) {
                            world.spawnParticle(particle, x - zz_change, y + yy_change, z + xx_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (b > 90 && b < 180) {
                            world.spawnParticle(particle, x - radius + zz_change, y + radius - yy_change, z + radius - xx_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (b > 180 && b < 270) {
                            world.spawnParticle(particle, x + zz_change, y - yy_change, z - xx_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (b > 270) {
                            world.spawnParticle(particle, x + radius - zz_change, y - radius + yy_change, z - radius + xx_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                    }
                    */
                }
                if (i > 180 && i < 270) {
                    world.spawnParticle(particle, x - x_change, y, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
                if (i > 270) {
                    world.spawnParticle(particle, x + z_change, y, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public static <T> void asyncRepeatDrawWithRising(int times, NonReturnWithSix<Player,Double,Double,Particle,T,Long> nonReturnWithSix, Player player, double radius, double risingDistance, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                repeatDrawWithRising(times, nonReturnWithSix, player, radius, risingDistance, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void repeatDrawWithRising(int times, NonReturnWithSix<Player,Double,Double,Particle,T,Long> nonReturnWithSix, Player player, double radius, double risingDistance, Particle particle, T data, long speed_ms) {
        for (int i = 0; i < times; i++) {
            nonReturnWithSix.apply(player, radius, risingDistance, particle, data, speed_ms);
        }
    }


    public static <T> void asyncRepeatDraw(int times, NonReturnWithFive<Player,Double,Particle,T,Long> nonReturnWithFive, Player player, double radius, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                repeatDraw(times, nonReturnWithFive, player, radius, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void repeatDraw(int times, NonReturnWithFive<Player,Double,Particle,T,Long> nonReturnWithFive, Player player, double radius, Particle particle, T data, long speed_ms) {
        for (int i = 0; i < times; i++) {
            nonReturnWithFive.apply(player, radius, particle, data, speed_ms);
        }
    }

    public static <T> void asyncDrawACircleWithRising(Player player, double radius, double risingDistance, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                drawACircleWithRising(player, radius, risingDistance, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void drawACircleWithRising(Player player, double radius, double risingDistance, Particle particle, T data, long speed_ms) {
        double b = 0.0;
        for (int i = 0; i < 361; i++) {
            World world = player.getWorld();
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            if (i == 0 || i == 360) {
                world.spawnParticle(particle, x + radius, y + b, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 90) {
                world.spawnParticle(particle, x, y + b, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 180) {
                world.spawnParticle(particle, x - radius, y + b, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 270) {
                world.spawnParticle(particle, x, y + b, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else {
                double a = i;
                if (a > 90 && a < 180) a = a - 90;
                if (a > 180 && a < 270) a = a - 180;
                if (a > 270) a = a - 270;
                a = Math.toRadians(a);
                double x_change = Math.cos(a) * radius;
                double z_change = Math.sin(a) * radius;
                if (i < 90) {
                    world.spawnParticle(particle, x + x_change, y + b, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 90 && i < 180) {
                    world.spawnParticle(particle, x - z_change, y + b, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 180 && i < 270) {
                    world.spawnParticle(particle, x - x_change, y + b, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 270) {
                    world.spawnParticle(particle, x + z_change, y + b, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                try {
                    Thread.sleep(speed_ms);
                } catch (InterruptedException ignored) {
                }
            }
            b += risingDistance;
        }
    }

    public static <T> void asyncDrawACircle(Player player, double radius, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                drawACircle(player, radius, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void drawACircle(Player player, double radius, Particle particle, T data, long speed_ms) {
        for (int i = 0; i < 361; i++) {
            World world = player.getWorld();
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            if (i == 0 || i == 360) {
                world.spawnParticle(particle, x + radius, y, z, 1, data);
            } else if (i == 90) {
                world.spawnParticle(particle, x, y, z + radius, 1, data);
            } else if (i == 180) {
                world.spawnParticle(particle, x - radius, y, z, 1, data);
            } else if (i == 270) {
                world.spawnParticle(particle, x, y, z - radius, 1, data);
            } else {
                double a = i;
                if (a > 90 && a < 180) a = a - 90;
                if (a > 180 && a < 270) a = a - 180;
                if (a > 270) a = a - 270;
                a = Math.toRadians(a);
                double x_change = Math.cos(a) * radius;
                double z_change = Math.sin(a) * radius;
                if (i < 90) {
                    world.spawnParticle(particle, x + x_change, y, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 90 && i < 180) {
                    world.spawnParticle(particle, x - z_change, y, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 180 && i < 270) {
                    world.spawnParticle(particle, x - x_change, y, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 270) {
                    world.spawnParticle(particle, x + z_change, y, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                try {
                    Thread.sleep(speed_ms);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public static <T> void asyncDrawACircleTwiceWithRising(Player player, double radius, double risingDistance, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                drawACircleTwiceWithRising(player, radius, risingDistance, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void drawACircleTwiceWithRising(Player player, double radius, double risingDistance, Particle particle, T data, long speed_ms) {
        double b = 0.0;
        for (int i = 0; i < 361; i++) {
            World world = player.getWorld();
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            if (i == 0 || i == 360) {
                world.spawnParticle(particle, x + radius, y + b, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x - radius, y + b, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 90) {
                world.spawnParticle(particle, x, y + b, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x, y + b, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 180) {
                world.spawnParticle(particle, x - radius, y + b, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x + radius, y + b, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 270) {
                world.spawnParticle(particle, x, y + b, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x, y + b, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else {
                double a = i;
                if (a > 90 && a < 180) a = a - 90;
                if (a > 180 && a < 270) a = a - 180;
                if (a > 270) a = a - 270;
                a = Math.toRadians(a);
                double x_change = Math.cos(a) * radius;
                double z_change = Math.sin(a) * radius;
                if (i < 90) {
                    world.spawnParticle(particle, x + x_change, y + b, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x - x_change, y + b, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 90 && i < 180) {
                    world.spawnParticle(particle, x - z_change, y + b, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x + z_change, y + b, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 180 && i < 270) {
                    world.spawnParticle(particle, x - x_change, y + b, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x + x_change, y + b, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 270) {
                    world.spawnParticle(particle, x + z_change, y + b, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x - z_change, y + b, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                try {
                    Thread.sleep(speed_ms);
                } catch (InterruptedException ignored) {
                }
            }
            b += risingDistance;
        }
    }

    public static <T> void asyncDrawACircleTwice(Player player, double radius, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                drawACircleTwice(player, radius, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void drawACircleTwice(Player player, double radius, Particle particle, T data, long speed_ms) {
        for (int i = 0; i < 361; i++) {
            World world = player.getWorld();
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            if (i == 0 || i == 360) {
                world.spawnParticle(particle, x + radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x - radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 90) {
                world.spawnParticle(particle, x, y, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x, y, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 180) {
                world.spawnParticle(particle, x - radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x + radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 270) {
                world.spawnParticle(particle, x, y, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                world.spawnParticle(particle, x, y, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else {
                double a = i;
                if (a > 90 && a < 180) a = a - 90;
                if (a > 180 && a < 270) a = a - 180;
                if (a > 270) a = a - 270;
                a = Math.toRadians(a);
                double x_change = Math.cos(a) * radius;
                double z_change = Math.sin(a) * radius;
                if (i < 90) {
                    world.spawnParticle(particle, x + x_change, y, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x - x_change, y, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 90 && i < 180) {
                    world.spawnParticle(particle, x - z_change, y, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x + z_change, y, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 180 && i < 270) {
                    world.spawnParticle(particle, x - x_change, y, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x + x_change, y, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 270) {
                    world.spawnParticle(particle, x + z_change, y, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                    world.spawnParticle(particle, x - z_change, y, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                try {
                    Thread.sleep(speed_ms);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public static <T> void asyncDrawACircle(Location location, double radius, Particle particle, T data, long speed_ms) {
        new BukkitRunnable() {
            @Override
            public void run() {
                drawACircle(location, radius, particle, data, speed_ms);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    public static <T> void drawACircle(Location location, double radius, Particle particle, T data, long speed_ms) {
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        for (int i = 0; i < 361; i++) {
            if (i == 0 || i == 360) {
                world.spawnParticle(particle, x + radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 90) {
                world.spawnParticle(particle, x, y, z + radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 180) {
                world.spawnParticle(particle, x - radius, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else if (i == 270) {
                world.spawnParticle(particle, x, y, z - radius, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
            } else {
                double a = i;
                if (a > 90 && a < 180) a = a - 90;
                if (a > 180 && a < 270) a = a - 180;
                if (a > 270) a = a - 270;
                a = Math.toRadians(a);
                double x_change = Math.cos(a) * radius;
                double z_change = Math.sin(a) * radius;
                if (i < 90) {
                    world.spawnParticle(particle, x + x_change, y, z + z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 90 && i < 180) {
                    world.spawnParticle(particle, x - z_change, y, z + x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 180 && i < 270) {
                    world.spawnParticle(particle, x - x_change, y, z - z_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                if (i > 270) {
                    world.spawnParticle(particle, x + z_change, y, z - x_change, 1, 0.0D, 0.0D, 0.0D, 0.0D, data);
                }
                try {
                    Thread.sleep(speed_ms);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

}
