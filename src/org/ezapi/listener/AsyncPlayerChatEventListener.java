package org.ezapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatEventListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (event.getMessage().contains("@" + player.getName())) {
                player.sendMessage(event.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", event.getMessage().replace("@" + player.getName(), "§6@" + player.getName())));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1f, 1f);
                event.getRecipients().remove(player);
            }
        }
    }

}
