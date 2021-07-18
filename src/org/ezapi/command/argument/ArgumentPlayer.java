package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentPlayer implements Argument {

    private ArgumentPlayer() {}

    public static ArgumentPlayer instance() {
        return new ArgumentPlayer();
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
            return ArgumentEntity().getMethod("f", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentEntity().getMethod("d").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentEntity() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ArgumentEntity");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.arguments.ArgumentEntity");
        }
    }

}
