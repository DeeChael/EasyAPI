package org.ezapi.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.ezapi.util.Ref;

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

    /**
     * Command API command
     *
     * @param commandName command name
     */
    public EzCommand(String commandName) {
        this.literalArgumentBuilder = createCommand(commandName);
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it
     *
     * @param commandName command name
     * @param permission argument requires int permission over 0 less than 4
     * @param bukkitPermission argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzCommand(String commandName, int permission, String bukkitPermission, PermissionDefault permissionDefault) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, finalPermission, bukkitPermission));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        } else {
            Bukkit.getPluginManager().getPermission(bukkitPermission).setDefault(permissionDefault);
        }
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it
     *
     * @param commandName command name
     * @param permission argument requires int permission over 0 less then 4
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzCommand(String commandName, int permission, Permission bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, finalPermission, bukkitPermission.getName().toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission.getName()) == null) {
            Bukkit.getPluginManager().addPermission(bukkitPermission);
        }
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it with OP as default
     *
     * @param commandName command name
     * @param permission argument requires int permission over 0 less then 4
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzCommand(String commandName, int permission, String bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, finalPermission, bukkitPermission));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, PermissionDefault.OP));
        }
    }

    /**
     * Command API command
     * Won't check bukkit permission
     *
     * @param commandName command name
     * @param permission argument requires int permission over 0 less then 4
     */
    public EzCommand(String commandName, int permission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, finalPermission));
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it with OP as default
     * Won't check integer permission
     *
     * @param commandName command name
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzCommand(String commandName, String bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, bukkitPermission));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, PermissionDefault.OP));
        }
    }

    /**
     * Command API command
     *
     * @param commandName command name
     * @param bukkitPermission argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzCommand(String commandName, String bukkitPermission, PermissionDefault permissionDefault) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, bukkitPermission));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        } else {
            Bukkit.getPluginManager().getPermission(bukkitPermission).setDefault(permissionDefault);
        }
    }

    public EzCommand(String commandName, Permission bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = createCommand(commandName);
        literalArgumentBuilder.requires((requirement) -> permissionCheck(requirement, bukkitPermission.getName().toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission.getName()) == null) {
            Bukkit.getPluginManager().addPermission(bukkitPermission);
        }
    }

    /**
     * Set next argument
     * @param ezArgument argument
     * @return self
     */
    public EzCommand then(EzArgument ezArgument) {
        if (registered) return this;
        literalArgumentBuilder.then(ezArgument.requiredArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     * @param ezCommand static argument
     * @return self
     */
    public EzCommand then(EzCommand ezCommand) {
        if (registered) return this;
        literalArgumentBuilder.then(ezCommand.literalArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     * @param ezCommandRegistered static argument
     * @return self
     */
    public EzCommand then(EzCommandRegistered ezCommandRegistered) {
        if (registered) return this;
        literalArgumentBuilder.then(ezCommandRegistered.commandNode);
        return this;
    }

    /**
     * Executes on arguments length equals this argument position in main command
     *
     * @param executes lambda with EzSender and EzArgumentHelper returns int
     * @return self
     */
    public EzCommand executes(BiFunction<EzSender,EzArgumentHelper,Integer> executes) {
        if (registered) return this;
        literalArgumentBuilder.executes(commandContext -> executes.apply(new EzSender(commandContext.getSource()), new EzArgumentHelper(commandContext)));
        return this;
    }

    /**
     * Redirect to the other command
     *
     * @param ezCommandRegistered command to be redirected
     */
    public EzCommand redirect(EzCommandRegistered ezCommandRegistered) {
        if (registered) return this;
        literalArgumentBuilder.redirect(ezCommandRegistered.commandNode);
        return this;
    }

    /**
     * Add aliases to the command
     *
     * @param aliases aliases
     */
    public void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    /**
     * Get if this command is registered
     *
     * @return registered
     */
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

    private static boolean permissionCheck(Object commandListenerWrapper, String bukkitPermission) {
        try {
            return ((CommandSender) CommandListenerWrapper().getMethod("getBukkitSender").invoke(commandListenerWrapper)).hasPermission(bukkitPermission);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean permissionCheck(Object commandListenerWrapper, int permission) {
        try {
            return (((Boolean) CommandListenerWrapper().getMethod("hasPermission", int.class).invoke(commandListenerWrapper, permission)));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static Class<?> CommandListenerWrapper() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("CommandListenerWrapper");
        } else {
            return Ref.getClass("net.minecraft.commands.CommandListenerWrapper");
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
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("CommandDispatcher");
        } else {
            return Ref.getClass("net.minecraft.commands.CommandDispatcher");
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
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("MinecraftServer");
        } else {
            return Ref.getClass("net.minecraft.server.MinecraftServer");
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
