package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Particle;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentChatColor implements Argument {

    private ArgumentChatColor() {}

    public static ArgumentChatColor instance() {
        return new ArgumentChatColor();
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
            return ArgumentChatFormat().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static char getColorChar(Object enumChatFormat) {
        EzClass EnumChatFormat = ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzClass(ReflectionUtils.getNmsClass("EnumChatFormat")) : new EzClass("net.minecraft.EnumChatFormat");
        EnumChatFormat.setInstance(enumChatFormat);
        return (char) (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? EnumChatFormat.getField("character") : EnumChatFormat.invokeMethod("a", new Class[0], new Object[0]));
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentChatFormat().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentChatFormat() {
        if (ReflectionUtils.getVersion() < 9) return null;
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentChatFormat");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentChatFormat");
        }
    }

}
