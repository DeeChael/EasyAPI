package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentBlockLocation implements Argument {

    private ArgumentBlockLocation() {}

    public static ArgumentBlockLocation instance() {
        return new ArgumentBlockLocation();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            return ArgumentPosition().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Location blockPositionToLocation(World world, Object blockPosition) {
        Location location = blockPositionToLocation(blockPosition);
        location.setWorld(world);
        return location;
    }

    public static Location blockPositionToLocation(Object blockPosition) {
        return new Location(Bukkit.getWorlds().get(0), getX(blockPosition), getY(blockPosition), getZ(blockPosition));
    }

    private static int getX(Object blockPosition) {
        if (ReflectionUtils.getVersion() < 9) return 0;
        if (ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 16) {
            try {
                return (int) blockPosition.getClass().getMethod("getX").invoke(blockPosition);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static int getY(Object blockPosition) {
        if (ReflectionUtils.getVersion() < 9) return 0;
        if (ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 16) {
            try {
                return (int) blockPosition.getClass().getMethod("getY").invoke(blockPosition);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static int getZ(Object blockPosition) {
        if (ReflectionUtils.getVersion() < 9) return 0;
        if (ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 16) {
            try {
                return (int) blockPosition.getClass().getMethod("getZ").invoke(blockPosition);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentPosition().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentPosition() {
        if (ReflectionUtils.getVersion() < 9) return null;
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentPosition");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.coordinates.ArgumentPosition");
        }
    }

}
