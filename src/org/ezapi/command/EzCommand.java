package org.ezapi.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public final class EzCommand {

    private static final int SINGLE_SUCCESS = 1;

    protected final LiteralArgumentBuilder<Object> literalArgumentBuilder;

    private boolean registered = false;

    private EzCommandRegistered ezCommandRegistered;

    protected List<String> aliases = new ArrayList<>();

    public EzCommand(String commandName) {
        this.literalArgumentBuilder = createCommand(commandName);
    }

    public EzCommand(String commandName, int permission, String bukkitPermission, PermissionDefault permissionDefault) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, finalPermission, bukkitPermission));
        Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
    }

    public EzCommand then(EzArgument ezArgument) {
        if (registered) return this;
        literalArgumentBuilder.then(ezArgument.requiredArgumentBuilder);
        return this;
    }

    public EzCommand then(EzCommand ezCommand) {
        if (registered) return this;
        literalArgumentBuilder.then(ezCommand.literalArgumentBuilder);
        return this;
    }

    public EzCommand then(EzCommandRegistered ezCommandRegistered) {
        if (registered) return this;
        literalArgumentBuilder.then(ezCommandRegistered.commandNode);
        return this;
    }

    public EzCommand executes(BiFunction<EzSender,EzArgumentHelper,Integer> executes) {
        if (registered) return this;
        literalArgumentBuilder.executes(commandContext -> executes.apply(new EzSender(commandContext.getSource()), new EzArgumentHelper(commandContext)));
        return this;
    }

    public EzCommand redirect(EzCommandRegistered ezCommandRegistered) {
        if (registered) return this;
        literalArgumentBuilder.redirect(ezCommandRegistered.commandNode);
        return this;
    }

    public void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public boolean isRegistered() {
        return registered;
    }

    protected EzCommandRegistered register() {
        if (!registered) {
            ezCommandRegistered = new EzCommandRegistered(this);
            registered = true;
        }
        return ezCommandRegistered;
    }

    private static boolean permissionCheck(Object commandListenerWrapper, int permission, String bukkitPermission) {
        try {
            return (((Boolean) CommandListenerWrapper().getMethod("hasPermission", int.class).invoke(commandListenerWrapper, permission)) && ((CommandSender) CommandListenerWrapper().getMethod("getBukkitSender").invoke(commandListenerWrapper)).hasPermission(bukkitPermission));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Class<?> CommandListenerWrapper() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("CommandListenerWrapper");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.CommandListenerWrapper");
        }
    }

    private static LiteralArgumentBuilder<Object> createCommand(String commandName) {
        try {
            return (LiteralArgumentBuilder<Object>) nmsCommandDispatcher().getMethod("a", String.class).invoke(null, commandName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

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
