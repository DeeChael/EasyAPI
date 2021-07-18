package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.World;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentWorld implements Argument {

    private ArgumentWorld() {}

    public static ArgumentWorld instance() {
        return new ArgumentWorld();
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
            return ArgumentDimension().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static World worldServerToBukkitWorld(Object worldServer) {
        try {
            return (World) worldServer.getClass().getMethod("getWorld").invoke(worldServer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentDimension().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentDimension() {
        if (ReflectionUtils.getVersion() < 9) return null;
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentDimension");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentDimension");
        }
    }

}
