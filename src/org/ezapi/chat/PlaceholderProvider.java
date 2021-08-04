package org.ezapi.chat;

import org.bukkit.entity.Player;

public interface PlaceholderProvider {

    String setPlaceholder(String message);

    String setPlaceholder(String message, Player player);

}
