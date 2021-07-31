package org.ezapi.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.ezapi.command.argument.ArgumentAttribute;
import org.ezapi.command.argument.ArgumentEntityType;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public final class EzArgument {

    protected final RequiredArgumentBuilder<Object,?> requiredArgumentBuilder;

    /**
     * Command API argument</br>
     * It's not static
     *
     * @param argumentType argument type
     * @param argumentName argument name
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName) {
        argumentName = argumentName.toLowerCase();
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument</br>
     * It's not static
     *
     * @param argumentType argument type
     * @param argumentName argument name
     * @param permission argument requires int permission over 0 less than 4
     * @param bukkitPermission argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, int permission, String bukkitPermission, PermissionDefault permissionDefault) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        requiredArgumentBuilder.requires((requirement) -> permissionCheck(requirement, finalPermission, bukkitPermission));
        Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Set next argument
     * @param ezArgument argument
     * @return self
     */
    public EzArgument then(EzArgument ezArgument) {
        requiredArgumentBuilder.then(ezArgument.requiredArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     * @param ezCommand static argument
     * @return self
     */
    public EzArgument then(EzCommand ezCommand) {
        requiredArgumentBuilder.then(ezCommand.literalArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     * @param ezCommandRegistered static argument
     * @return self
     */
    public EzArgument then(EzCommandRegistered ezCommandRegistered) {
        requiredArgumentBuilder.then(ezCommandRegistered.commandNode);
        return this;
    }

    /**
     * Executes on arguments length equals this argument position in main command
     *
     * @param executes lambda with EzSender and EzArgumentHelper returns int
     * @return self
     */
    public EzArgument executes(BiFunction<EzSender,EzArgumentHelper,Integer> executes) {
        requiredArgumentBuilder.executes(commandContext -> executes.apply(new EzSender(commandContext.getSource()), new EzArgumentHelper(commandContext)));
        return this;
    }

    /**
     * Redirect to the other command
     *
     * @param ezCommandRegistered command to be redirected
     */
    public void redirect(EzCommandRegistered ezCommandRegistered) {
        requiredArgumentBuilder.redirect(ezCommandRegistered.commandNode);
    }

    /**
     * Set tab complete suggestion
     *
     * @param biFunction lambda with EzSender and SuggestionsBuilder returns CompletableFuture
     * @return
     */
    public EzArgument suggest(BiFunction<EzSender, SuggestionsBuilder, CompletableFuture<Suggestions>> biFunction) {
        requiredArgumentBuilder.suggests(((commandContext, suggestionsBuilder) -> biFunction.apply(new EzSender(commandContext.getSource()), suggestionsBuilder)));
        return this;
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
