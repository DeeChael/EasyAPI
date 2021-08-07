package org.ezapi.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzCommand;
import org.ezapi.command.EzCommandManager;
import org.ezapi.configuration.AutoReloadFile;
import org.ezapi.util.PlayerUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class EasyPlugin extends JavaPlugin {

    private static CommandMap BUKKIT_COMMAND_MAP;

    private final File configFile;

    private String locale = "en_us";

    private AutoReloadFile configReloader = null;

    private boolean isConfigAutoReload = false;

    public EasyPlugin() {
        super();
        configFile = new File(getDataFolder(), "config.yml");
        init();
    }

    protected EasyPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        configFile = new File(getDataFolder(), "config.yml");
        init();
    }

    @Override
    public final void onLoad() {
        load();
    }

    @Override
    public final void onEnable() {
        if (BUKKIT_COMMAND_MAP == null) {
            try {
                BUKKIT_COMMAND_MAP = (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                getLogger().severe("Failed to get CommandMap");
            }
        }
        this.configReloader = new AutoReloadFile(this, configFile);
        this.configReloader.onModified(() -> {
            if (isConfigAutoReload) {
                onConfigReload();
            }
        });
        this.configReloader.onDeleted(() -> {
            if (isConfigAutoReload) {
                onConfigDelete();
            }
        });
        enable();
    }

    @Override
    public final void onDisable() {
        if (configReloader != null) {
            configReloader.stop();
        }
        disable();
    }

    public final void createDefaultConfig() {
        if (configFile.exists() && configFile.isFile()) return;
        this.saveResource("config.yml", false);
        if (!configFile.exists() || configFile.isDirectory()) {
            try {
                if (!configFile.createNewFile()) {
                    getLogger().severe("Failed to create config file");
                }
            } catch (IOException ignored) {
                getLogger().severe("Failed to create config file");
            }
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public final void setLocale(String locale) {
        this.locale = locale;
    }

    public final String getLocale() {
        return locale;
    }

    public final void sendConsole(String text) {
        this.sendConsole(text, false);
    }

    public final void sendConsole(String data, boolean flag) {
        this.sendConsole(new ChatMessage(data, flag));
    }

    public final void sendConsole(ChatMessage message) {
        Bukkit.getConsoleSender().sendMessage(message.getText(this.getLocale()));
    }

    public final void sendMessage(Player player, String text) {
        this.sendMessage(player, text, false);
    }

    public final void sendMessage(Player player, String data, boolean flag) {
        this.sendMessage(player, new ChatMessage(data, flag));
    }

    public final void sendMessage(Player player, ChatMessage message) {
        PlayerUtils.sendMessage(player, message);
    }

    public final void broadcast(String text) {
        this.broadcast(text, false);
    }

    public final void broadcast(String data, boolean flag) {
        this.broadcast(new ChatMessage(data, flag));
    }

    public final void broadcast(ChatMessage message) {
        if (!(getPlayers() > 0)) return;
        for (Player player : getOnlinePlayers()) {
            sendMessage(player, message);
        }
    }

    public final void broadcastWithPermission(String text, String permission) {
        this.broadcastWithPermission(text, false, permission);
    }

    public final void broadcastWithPermission(String data, boolean flag, String permission) {
        this.broadcastWithPermission(new ChatMessage(data, flag), permission);
    }


    public final void broadcastWithPermission(ChatMessage message, String permission) {
        if (!(getPlayers() > 0)) return;
        for (Player player : getOnlinePlayers()) {
            if (!player.hasPermission(permission)) continue;
            sendMessage(player, message);
        }
    }

    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public int getPlayers() {
        return getOnlinePlayers().size();
    }

    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public final void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public final void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public final void unregisterListenerAll() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return super.onTabComplete(sender, command, label, args);
    }

    public final void registerCommand(EzCommand command) {
        this.registerCommand(this.getName().toLowerCase(), command);
    }

    public final void registerCommand(String prefix, EzCommand command) {
        EzCommandManager.register(prefix, command);
    }

    public final void registerBukkitCommand(Command command) {
        registerBukkitCommand(this.getName().toLowerCase(), command);
    }

    public final void registerBukkitCommand(String prefix, Command command) {
        BUKKIT_COMMAND_MAP.register(prefix, command);
    }

    public final boolean isConfigAutoReload() {
        return isConfigAutoReload;
    }

    public final void setConfigAutoReload(boolean configAutoReload) {
        isConfigAutoReload = configAutoReload;
    }

    public final void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResource(resourcePath);
            if (in != null) {
                File outFile = new File(this.getDataFolder(), resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(this.getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));
                if (!outDir.exists()) {
                    if (!outDir.mkdirs()) {
                        getLogger().severe("Failed to create " + outDir.getPath());
                    }
                }

                try {
                    if (!outFile.exists() || replace) {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    public final boolean hasResource(String resourcePath) {
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = this.getResource(resourcePath);
        boolean bool = in != null;
        if (bool) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
        return bool;
    }

    public void init() {}

    public void load() {}

    public abstract void enable();

    public abstract void disable();

    public void onConfigReload() {
        reloadConfig();
    }

    public void onConfigDelete() {
        for (String key : getConfig().getKeys(false)) {
            getConfig().set(key, null);
        }
    }

}
