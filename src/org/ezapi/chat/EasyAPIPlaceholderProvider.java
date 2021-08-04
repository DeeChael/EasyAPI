package org.ezapi.chat;

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ezapi.util.ServerUtils;

import java.lang.management.ManagementFactory;

public final class EasyAPIPlaceholderProvider implements PlaceholderProvider {

    public EasyAPIPlaceholderProvider() {}

    @Override
    public String setPlaceholder(String message) {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        String tps_one_minute = String.valueOf(Math.min(20.0, ServerUtils.getTps()[0]));
        String tps_five_minute = String.valueOf(Math.min(20.0, ServerUtils.getTps()[1]));
        String tps_fifteen_minute = String.valueOf(Math.min(20.0, ServerUtils.getTps()[2]));
        String jvm_using_memory = String.valueOf(runtime.totalMemory() - runtime.freeMemory());
        String jvm_free_memory = String.valueOf(runtime.freeMemory());
        String jvm_total_memory = String.valueOf(runtime.totalMemory());
        String os = System.getProperty("os.name");
        String os_using_memory = String.valueOf((operatingSystemMXBean.getTotalPhysicalMemorySize() - operatingSystemMXBean.getFreePhysicalMemorySize()) / (1024 * 1024));
        String os_free_memory = String.valueOf((operatingSystemMXBean.getFreePhysicalMemorySize()) / (1024 * 1024));
        String os_total_memory = String.valueOf((operatingSystemMXBean.getTotalPhysicalMemorySize()) / (1024 * 1024));
        String onlinePlayers = String.valueOf(Bukkit.getOnlinePlayers().size());
        String maxPlayers = String.valueOf(Bukkit.getMaxPlayers());
        String serverName = Bukkit.getName();
        message = message.replace("{tps_1m}", tps_one_minute);
        message = message.replace("{tps_5m}", tps_five_minute);
        message = message.replace("{tps_15m}", tps_fifteen_minute);
        message = message.replace("{jvm_using_memory}", jvm_using_memory);
        message = message.replace("{jvm_free_memory}", jvm_free_memory);
        message = message.replace("{jvm_total_memory}", jvm_total_memory);
        message = message.replace("{system_name}", os);
        message = message.replace("{system_using_memory}", os_using_memory);
        message = message.replace("{system_free_memory}", os_free_memory);
        message = message.replace("{system_total_memory}", os_total_memory);
        message = message.replace("{player_online}", onlinePlayers);
        message = message.replace("{player_max}", maxPlayers);
        message = message.replace("{server_name}", serverName);
        return message;
    }

    @Override
    public String setPlaceholder(String message, Player player) {
        message = setPlaceholder(message);
        String name = player.getName();
        String displayName = player.getDisplayName();
        String playerListName = player.getPlayerListName();
        String health = String.valueOf(player.getHealth());
        String maxHealth = String.valueOf(player.getMaxHealth());
        String gameMode = String.valueOf(StringUtils.capitalize(player.getGameMode().name().toLowerCase()));
        String world = player.getWorld().getName();
        String x = String.valueOf(player.getLocation().getX());
        String y = String.valueOf(player.getLocation().getY());
        String z = String.valueOf(player.getLocation().getZ());
        String level = String.valueOf(player.getLevel());
        String foodLevel = String.valueOf(player.getFoodLevel());
        String locale = player.getLocale();
        String address = player.getAddress().toString();
        String uuid = player.getUniqueId().toString();
        String saturation = String.valueOf(player.getSaturation());
        message = message.replace("{player_name}", name);
        message = message.replace("{player_name_display}", displayName);
        message = message.replace("{player_name_list}", playerListName);
        message = message.replace("{player_health}", health);
        message = message.replace("{player_max_health}", maxHealth);
        message = message.replace("{player_gamemode}", gameMode);
        message = message.replace("{player_world}", world);
        message = message.replace("{player_x}", x);
        message = message.replace("{player_y}", y);
        message = message.replace("{player_z}", z);
        message = message.replace("{player_level}", level);
        message = message.replace("{player_food_level}", foodLevel);
        message = message.replace("{player_locale}", locale);
        message = message.replace("{player_address}", address);
        message = message.replace("{player_uuid}", uuid);
        message = message.replace("{player_saturation}", saturation);
        return message;
    }

}
