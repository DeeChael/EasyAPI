package org.ezapi.module.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;

import java.util.List;

public interface Hologram {

    World getWorld();

    Location getLocation();

    ChatMessage getText();

    void setText(ChatMessage message);

    void setLocation(Location location);

    void addViewer(Player player);

    void removeViewer(Player player);

    void refresh(Player player);

    void destroy(Player player);

    List<Player> getViewers();

    void removeAll();

}
