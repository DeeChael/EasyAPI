package org.ezapi.module.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface Hologram {

    void setLocation(Location location);

    void addViewer(Player player);

    void removeViewer(Player player);

    void refresh(Player player);

    void destroy(Player player);

    List<Player> getViewers();

    void removeAll();

}
