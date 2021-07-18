package org.ezapi.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.Bukkit;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class EzCommandRegistered {

    protected CommandNode<Object> commandNode;

    protected final EzCommand ezCommand;

    protected List<CommandNode<Object>> aliases = new ArrayList<>();

    protected EzCommandRegistered(EzCommand ezCommand) {
        this.ezCommand = ezCommand;
        if (ezCommand.isRegistered()) {
            commandNode = ezCommand.register().commandNode;
        } else {
            commandNode = ezCommand.literalArgumentBuilder.build();
            /*
            CommandDispatcher<Object> commandDispatcher = getMojangCommandDispatcher();
            if (commandDispatcher != null) {
                commandNode = commandDispatcher.register(ezCommand.literalArgumentBuilder);
            }
            */
        }
    }

    /*
    public void addAliases(String... aliases) {
        if (aliases.length > 0) {
            for (String string : aliases) {
                this.aliases.add(new EzCommand(string).redirect(this).register().commandNode);
            }
        }
    }
    */

    private static CommandDispatcher<Object> getMojangCommandDispatcher() {
        try {
            return (CommandDispatcher<Object>) nmsCommandDispatcher().getMethod("a").invoke(getNmsCommandDispatcher());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> nmsCommandDispatcher() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("CommandDispatcher");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.CommandDispatcher");
        }
    }

    private static Object getNmsCommandDispatcher() {
        Object minecraftServer = getMinecraftServer();
        try {
            return MinecraftServer().getMethod("getCommandDispatcher").invoke(minecraftServer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> MinecraftServer() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("MinecraftServer");
        } else {
            return ReflectionUtils.getClass("net.minecraft.server.MinecraftServer");
        }
    }

    private static Object getMinecraftServer() {
        try {
            return Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
