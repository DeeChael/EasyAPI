package org.ezapi.module.bossbar;

import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;

import java.util.List;

public interface BossBar {

    /**
     * Set boss bar progress with integer percent
     * @param percent percent
     */
    void setProgress(int percent);

    /**
     * Set boss bar progress
     * @param progress percent
     */
    void setProgress(float progress);

    /**
     * Get boss bar progress
     * @return percent
     */
    float getProgress();

    /**
     * Set if is darken sky
     * @param darkenSky is darken sky
     */
    void setDarkenSky(boolean darkenSky);

    /**
     * Set if create fog
     * @param createFog create fog
     */
    void setCreateFog(boolean createFog);

    /**
     * Set if play music
     * @param playMusic play music
     */
    void setPlayMusic(boolean playMusic);

    /**
     * Get if is darken sky
     * @return is darken sky
     */
    boolean isDarkenSky();

    /**
     * Get if create fog
     * @return create fog
     */
    boolean isCreateFog();

    /**
     * Get if play music
     * @return play music
     */
    boolean isPlayMusic();

    /**
     * Get boss bar style
     * @return style
     */
    BarStyle getStyle();

    /**
     * Set boss bar style
     * @param style style
     */
    void setStyle(BarStyle style);

    /**
     * Get boss bar color
     * @return color
     */
    BarColor getColor();

    /**
     * Set boss bar color
     * @param color color
     */
    void setColor(BarColor color);

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
     * Show boss bar to player
     * <p>Player must be added to viewers and has hid the boss bar</p>
     *
     * @param player player
     */
    void show(Player player);

    /**
     * Hide boss bar to player
     * <p>Player must be added to viewers and hasn't hid the boss bar</p>
     *
     * @param player player
     */
    void hide(Player player);

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
