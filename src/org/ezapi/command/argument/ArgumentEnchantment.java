package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.enchantments.Enchantment;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentEnchantment implements Argument {

    private ArgumentEnchantment() {}

    public static ArgumentEnchantment instance() {
        return new ArgumentEnchantment();
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
            return ArgumentEnchantment().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Enchantment nmsEnchantmentToBukkitEnchantment(Object nmsEnchantment) {
        try {
            return (Enchantment) ReflectionUtils.getObcClass("enchantments.CraftEnchantment").getConstructor(Enchantment()).newInstance(nmsEnchantment);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentEnchantment().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentEnchantment() {
        if (ReflectionUtils.getVersion() < 9) return null;
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentEnchantment");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentEnchantment");
        }
    }

    private static Class<?> Enchantment() {
        if (ReflectionUtils.getVersion() < 9) return null;
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("Enchantment");
        } else {
            return ReflectionUtils.getClass("net.minecraft.world.item.enchantment.Enchantment");
        }
    }

}
