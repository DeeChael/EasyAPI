package org.ezapi.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.ezapi.function.NonReturn;

public final class Countdown {

    public static EzTask startCountdown(int seconds, Plugin plugin, NonReturn nonReturn) {
        return new EzTask(new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < seconds; i ++) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                    }
                }
                nonReturn.apply();
            }
        }.runTaskAsynchronously(plugin));
    }

}
