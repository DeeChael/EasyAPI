package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.entity.Entity;
import org.ezapi.util.Ref;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentEntities implements Argument {

    private ArgumentEntities() {}

    public static ArgumentEntities instance() {
        return new ArgumentEntities();
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
            return ArgumentEntity().getMethod("b", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Entity nmsEntityToBukkitEntity(Object nmsEntity) {
        try {
            return (Entity) nmsEntity.getClass().getMethod("getBukkitEntity").invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentEntity().getMethod("multipleEntities").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentEntity() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentEntity");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentEntity");
        }
    }

}
