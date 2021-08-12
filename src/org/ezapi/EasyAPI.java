package org.ezapi;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.ezapi.command.EzCommandManager;
import org.ezapi.configuration.LanguageManager;
import org.ezapi.plugin.EasyPlugin;
import org.ezapi.util.Ref;

public final class EasyAPI extends EasyPlugin {

    public static final int VERSION = 1003001;

    /*
    private final Map<Player, BukkitTask> started = new HashMap<>();
    */

    @Override
    public final void load() {
    }

    @Override
    public final void enable() {
        if (Ref.getVersion() < 9) {
            getLogger().severe("Unsupported Server Version " + Ref.getServerVersion());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        //Java 9 not support
        //Trying to find way to solve this problem
        /*
        File libraries = new File(this.getDataFolder(), "libraries");
        if (!libraries.exists()) {
            if (!libraries.mkdirs()) {
                this.getLogger().warning("Failed to create libraries directory");
            }
        }
        File[] files = libraries.listFiles();
        if (files != null) {
            if (files.length > 0) {
                for (File file : files) {
                    if (file.getName().endsWith(".jar")) {
                        EzClassLoader.load(file);
                    }
                }
            }
        }
        try {
            Class.forName("com.zaxxer.hikari.HikariConfig");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        /*
        this.registerCommand("ez-api", new EzCommand("testing")
                .executes((sender, argument) -> {
                    if (sender.isPlayer()) {
                        Player player = sender.player();
                        if (player != null) {
                            EzAnvil anvil = new EzAnvil(this);
                            anvil.setLeftInput((Drawer) (target, drawSetting) -> drawSetting.setType(Material.DIAMOND));
                            anvil.setOutput((Click) (target, click, action) -> {
                                Inventory inventory = anvil.getBukkit(target);
                                if (inventory != null) {
                                    target.setItemOnCursor(inventory.getItem(2));
                                    inventory.setItem(0, new ItemStack(Material.AIR));
                                    inventory.setItem(2, new ItemStack(Material.AIR));
                                    target.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                }
                            });
                            anvil.openToPlayer(player);
                            return 1;
                        }
                    }
                    return 0;
                })
        );
        */
        //new EzApiCommand().register();
        //new HologramCommand().register();
        //new NPCCommand().register();
        //new SBCommand().register();
        //new BBarCommand().register();
        LanguageManager.INSTANCE.reload();
        reload();
        setLocale(getConfig().getString("Setting.Language", "en_us"));
        //Break bedrock - Just for fun
        /*
        protocol = new Protocol(this) {
            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                if (packet.getClass().equals(NMSPackets.PacketPlayInBlockDig.getInstanceClass())) {
                    if (sender.getGameMode() == GameMode.SURVIVAL) {
                        EzClass PacketPlayInBlockDig = new EzClass(NMSPackets.PacketPlayInBlockDig.getInstanceClass());
                        PacketPlayInBlockDig.setInstance(packet);
                        Location location = ArgumentBlockLocation.blockPositionToLocation(PacketPlayInBlockDig.getField("a"));
                        location.setWorld(sender.getWorld());
                        if (location.getBlock().getType() == Material.BEDROCK) {
                            EzEnum EnumPlayerDigType = new EzEnum(PacketPlayInBlockDig.getInstanceClass().getName() + "$EnumPlayerDigType");
                            EnumPlayerDigType.setInstance(PacketPlayInBlockDig.invokeMethod("d", new Class[0], new Object[0]));
                            if (EnumPlayerDigType.getInstance().equals(EnumPlayerDigType.valueOf("START_DESTROY_BLOCK"))) {
                                if (!started.containsKey(sender)) {
                                    started.put(sender, new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            for (int i = -10; i <= 90; i++) {
                                                if (started.containsKey(sender)) {
                                                    if ((i % 10) == 0) {
                                                        PlayerUtils.setBlockBreakStage(sender, location, BlockBreakAnimation.valueOf(i / 10));
                                                        try {
                                                            Thread.sleep(1000L);
                                                        } catch (InterruptedException ignored) {
                                                        }
                                                        if (i == 90) {
                                                            new BukkitRunnable() {
                                                                @Override
                                                                public void run() {
                                                                    location.getBlock().breakNaturally();
                                                                    location.getWorld().dropItemNaturally(location, new ItemStack(Material.BEDROCK));
                                                                }
                                                            }.runTask(EasyAPI.this);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }.runTaskAsynchronously(EasyAPI.this));
                                }
                            } else {
                                if (started.containsKey(sender)) {
                                    started.remove(sender).cancel();
                                    PlayerUtils.setBlockBreakStage(sender, location, BlockBreakAnimation.CLEAR);
                                }
                            }
                        }
                    }
                }
                return super.onPacketInAsync(sender, channel, packet);
            }
        };
        */
    }

    @Override
    public final void disable() {
    }

    public static String getLanguage() {
        return getInstance().getLocale();
    }

    public static EasyAPI getInstance() {
        return JavaPlugin.getPlugin(EasyAPI.class);
    }

    @Override
    public void onConfigReload() {
        reload();
    }

    @Override
    public void onConfigDelete() {
        reload();
    }

    public void reload() {
        createDefaultConfig();
        reloadConfig();
        if (!getConfig().contains("Setting.Language")) {
            getConfig().set("Setting.Language", "en_us");
            saveConfig();
        }
    }

}
