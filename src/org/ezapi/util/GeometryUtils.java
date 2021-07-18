package org.ezapi.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.ezapi.function.NonReturnWithThree;

public final class GeometryUtils {

    public static void horizontalCircle(Location centerLocation, double radius, Sequence sequence, Direction direction, NonReturnWithThree<Double,Double,Double> nonReturnWithThree) {
        World world = centerLocation.getWorld();
        double x = centerLocation.getX();
        double y = centerLocation.getY();
        double z = centerLocation.getZ();
        if (sequence.equals(Sequence.ANTICLOCKWISE)) {
            if (direction.equals(Direction.WEST)) {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x - x_change, y, z + z_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x + z_change, y, z + x_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x + x_change, y, z - z_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x - z_change, y, z - x_change);
                        }
                    }
                }
            } else if (direction.equals(Direction.SOUTH)) {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x + z_change, y, z + x_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x + x_change, y, z - z_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x - z_change, y, z - x_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x - x_change, y, z + z_change);
                        }
                    }
                }
            } else if (direction.equals(Direction.NORTH)) {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x - z_change, y, z - x_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x - x_change, y, z + z_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x + z_change, y, z + x_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x + x_change, y, z - z_change);
                        }
                    }
                }
            } else {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x + x_change, y, z - z_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x - z_change, y, z - x_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x - x_change, y, z + z_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x + z_change, y, z + x_change);
                        }
                    }
                }
            }
        } else {
            if (direction.equals(Direction.WEST)) {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x - x_change, y, z - z_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x + z_change, y, z - x_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x + x_change, y, z + z_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x - z_change, y, z + x_change);
                        }
                    }
                }
            } else if (direction.equals(Direction.SOUTH)) {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x - z_change, y, z + x_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x - x_change, y, z - z_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x + z_change, y, z - x_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x + x_change, y, z + z_change);
                        }
                    }
                }
            } else if (direction.equals(Direction.NORTH)) {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x + z_change, y, z - x_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x + x_change, y, z + z_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x - z_change, y, z + x_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x - x_change, y, z - z_change);
                        }
                    }
                }
            } else {
                for (int i = 0; i < 361; i++) {
                    if (i == 0 || i == 360) {
                        nonReturnWithThree.apply(x + radius, y, z);
                    } else if (i == 90) {
                        nonReturnWithThree.apply(x, y, z + radius);
                    } else if (i == 180) {
                        nonReturnWithThree.apply(x - radius, y, z);
                    } else if (i == 270) {
                        nonReturnWithThree.apply(x, y, z - radius);
                    } else {
                        double a = i;
                        if (a > 90 && a < 180) a = a - 90;
                        if (a > 180 && a < 270) a = a - 180;
                        if (a > 270) a = a - 270;
                        a = Math.toRadians(a);
                        double x_change = Math.cos(a) * radius;
                        double z_change = Math.sin(a) * radius;
                        if (i < 90) {
                            nonReturnWithThree.apply(x + x_change, y, z + z_change);
                        }
                        if (i > 90 && i < 180) {
                            nonReturnWithThree.apply(x - z_change, y, z + x_change);
                        }
                        if (i > 180 && i < 270) {
                            nonReturnWithThree.apply(x - x_change, y, z - z_change);
                        }
                        if (i > 270) {
                            nonReturnWithThree.apply(x + z_change, y, z - x_change);
                        }
                    }
                }
            }
        }
    }

    public enum Direction {
        EAST, WEST, SOUTH, NORTH, UP, DOWN;
    }

    public enum Sequence {
        CLOCKWISE, ANTICLOCKWISE;
    }

}
