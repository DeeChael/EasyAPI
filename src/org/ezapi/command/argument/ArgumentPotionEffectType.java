package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.potion.PotionEffectType;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ArgumentPotionEffectType implements Argument {

    private ArgumentPotionEffectType() {
    }

    public static ArgumentPotionEffectType instance() {
        return new ArgumentPotionEffectType();
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
            return ArgumentMobEffect().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PotionEffectType mobEffectListToPotionEffectType(Object mobEffectList) {
        Class<?> clazz = ReflectionUtils.getObcClass("potion.CraftPotionEffectType");
        try {
            Constructor<?> constructor = clazz.getConstructor(mobEffectList.getClass());
            return (PotionEffectType) constructor.newInstance(mobEffectList);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentMobEffect().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentMobEffect() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentMobEffect");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentMobEffect");

        }
    }

}