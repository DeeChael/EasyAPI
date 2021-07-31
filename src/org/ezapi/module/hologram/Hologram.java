package org.ezapi.module.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;

import java.util.List;

public interface Hologram {

    /**
     * Get hologram world
     * @return world
     */
    World getWorld();

    /**
     * Get hologram location
     * @return location
     */
    Location getLocation();

    /**
     * Get hologram text
     * @return text
     */
    ChatMessage getText();

    /**
     * Set hologram text
     * @param message text
     */
    void setText(ChatMessage message);

    /**
     * Set hologram location
     * @param location location
     */
    void setLocation(Location location);

    /**
     * Add viewer
     * @param player viewer
     */
    void addViewer(Player player);

    /**
     * Remove viewer
     * @param player viewer
     */
    void removeViewer(Player player);

    /**
     * Refresh hologram in player's eyes
     * @param player viewer
     */
    void refresh(Player player);

    /**
     * Remove hologram in player's eyes
     * @param player
     */
    void destroy(Player player);

    /**
     * Get all viewers
     * @return all viewers
     */
    List<Player> getViewers();

    /**
     * Remove all viewers
     */
    void removeAll();

    /**
     * Drop the hologram
     */
    void drop();

    /**
     * Get if the hologram is dropped
     * @return is dropped
     */
    boolean isDropped();

}
