package org.ezapi.scheduler;

import org.bukkit.scheduler.BukkitTask;

public final class EzTask {

    private final BukkitTask bukkitTask;

    public EzTask(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    public void stop() {
        if (!bukkitTask.isCancelled()) {
            bukkitTask.cancel();
        }
    }

}
