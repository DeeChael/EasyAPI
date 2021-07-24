package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import org.bukkit.attribute.Attribute;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class ArgumentAttribute implements Argument {

    private ArgumentAttribute() {}

    public static ArgumentAttribute instance() {
        return new ArgumentAttribute();
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
            return ReflectionUtils.getVersion() >= 16 ? ArgumentMinecraftKeyRegistered().getMethod("f", CommandContext.class, String.class).invoke(null, commandContext, name) : ArgumentMinecraftKeyRegistered().getMethod("e", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Attribute nmsAttributeBaseToBukkitAttribute(Object nmsAttribute) {
        try {
            return (Attribute) ReflectionUtils.getObcClass("attribute.CraftAttributeMap").getMethod("fromMinecraft", String.class).invoke(null, nmsAttribute.getClass().getMethod("getKey").invoke(nmsAttribute).toString());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentMinecraftKeyRegistered().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> ArgumentMinecraftKeyRegistered() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentMinecraftKeyRegistered");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered");
        }
    }

    private static Class<?> CommandAttribute() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("CommandAttribute");
        } else {
            return ReflectionUtils.getClass("net.minecraft.server.commands.CommandAttribute");
        }
    }

    public static SuggestionProvider<Object> suggests() {
        try {
            Field field = CommandAttribute().getDeclaredField("a");
            field.setAccessible(true);
            return (SuggestionProvider<Object>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
