package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;

public final class ArgumentEntityType implements Argument {

    private ArgumentEntityType() {}

    public static ArgumentEntityType instance() {
        return new ArgumentEntityType();
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
            Object minecraftKey = ArgumentEntitySummon().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);

            String key = (String) minecraftKey.getClass().getMethod("getKey").invoke(minecraftKey);
            Optional<Object> optional = (Optional<Object>) EntityTypes().getMethod("a", String.class).invoke(null, key);
            if (optional.isPresent()) {
                return optional.get();
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EntityType nmsEntityTypesToBukkitEntityType(Object nmsEntityTypes) {
        try {
            Object nmsEntity = EntityTypes().getMethod("a", nmsWorld().getInstanceClass()).invoke(nmsEntityTypes, getNmsWorld(Bukkit.getWorlds().get(0)));
            Entity entity = (Entity) nmsEntity.getClass().getMethod("getBukkitEntity").invoke(nmsEntity);
            EntityType entityType = entity.getType();
            if (EntityLiving().getInstanceClass().isInstance(nmsEntity)) {
                EzClass EntityLiving = EntityLiving();
                EntityLiving.setInstance(nmsEntity);
                EntityLiving.setField("drops", new ArrayList<>());
                EntityLiving.setField("expToDrop", 0);
            }
            nmsEntity.getClass().getMethod("die").invoke(nmsEntity);
            return entityType;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SuggestionProvider<Object> suggests() {
        EzClass ezClass = CompletionProviders();
        return (SuggestionProvider<Object>) ((Ref.getVersion() <= 12 && Ref.getVersion() >= 9) ? ezClass.getStaticField("d") : ezClass.getStaticField("e"));
    }

    private static EzClass nmsWorld() {
        return Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Ref.getNmsClass("World")) : new EzClass("net.minecraft.world.level.World");
    }

    private static EzClass EntityLiving() {
        return Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Ref.getNmsClass("EntityLiving")) : new EzClass("net.minecraft.world.entity.EntityLiving");
    }

    private static Object getNmsWorld(World world) {
        try {
            return Bukkit.getWorlds().get(0).getClass().getMethod("getHandle").invoke(world);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentEntitySummon().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> EntityTypes() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("EntityTypes");
        } else {
            return Ref.getClass("net.minecraft.world.entity.EntityTypes");
        }
    }

    public static Class<?> ArgumentEntitySummon() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentEntitySummon");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentEntitySummon");
        }
    }

    private static EzClass CompletionProviders() {
        return Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Ref.getNmsClass("CompletionProviders")) : new EzClass("net.minecraft.commands.synchronization.CompletionProviders");
    }

}
