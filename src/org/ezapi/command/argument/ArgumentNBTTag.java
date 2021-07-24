package org.ezapi.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.ezapi.util.NBTUtils;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public class ArgumentNBTTag implements Argument {

    private ArgumentNBTTag() {
    }

    public static ArgumentNBTTag instance() {
        return new ArgumentNBTTag();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public JsonObject get(CommandContext<Object> commandContext, String name) {
        try {
            return NBTUtils.parseNBTTagCompound(ArgumentNBTTag().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentNBTTag().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentNBTTag() {
        if (ReflectionUtils.getVersion() < 9) return null;
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentNBTTag");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentNBTTag");
        }
    }

}
