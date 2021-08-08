package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Particle;
import org.bukkit.World;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.Ref;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentParticle implements Argument {

    private ArgumentParticle() {}

    public static ArgumentParticle instance() {
        return new ArgumentParticle();
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
            return ArgumentParticle().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Particle nmsParticleToBukkitParticle(Object particleParam) {
        EzClass ParticleParam = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Ref.getNmsClass("ParticleParam")) : new EzClass("net.minecraft.core.particles.ParticleParam");
        EzEnum CraftParticle = new EzEnum(Ref.getObcClass("CraftParticle"));
        return (Particle) CraftParticle.invokeStaticMethod("toBukkit", new Class[] { ParticleParam.getInstanceClass() }, new Object[] { particleParam });
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentParticle().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentParticle() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentParticle");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentParticle");
        }
    }

}
