package org.ezapi.chat;

import org.bukkit.entity.Player;

public interface PlaceholderProvider {

    /**
     * Set placeholders
     * @param message message
     * @return replaced message
     */
    String setPlaceholder(String message);

    /**
     * Set placeholders
     * @param message message
     * @param player placeholder requires
     * @return replaced message
     */
    String setPlaceholder(String message, Player player);

}
