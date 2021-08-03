package org.ezapi.module.scoreboard;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;

import java.util.List;

public interface Scoreboard {

    /**
     * Get line text
     * @param line line number
     * @return text
     */
    ChatMessage getText(int line);

    /**
     * Set text to line
     * @param line line number
     * @param text text
     */
    void setText(int line, ChatMessage text);

    /**
     * Remove text to line
     * @param line line number
     */
    void removeText(int line);

    /**
     * Get hologram text
     * @return text
     */
    ChatMessage getTitle();

    /**
     * Set hologram text
     * @param message text
     */
    void setTitle(ChatMessage message);

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
