package org.ezapi.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public final class PlayerListener implements Listener {

    public final static PlayerListener INSTANCE = new PlayerListener();

    private PlayerListener() {}

    @EventHandler
    public void command(PlayerCommandSendEvent event) {
    }

}
