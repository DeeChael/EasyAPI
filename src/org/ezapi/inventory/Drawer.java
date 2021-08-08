package org.ezapi.inventory;

import org.bukkit.entity.Player;

public interface Drawer extends Input {

    /**
     * Be called on open inventory to a player
     * <p>You can add different input for different players</p>
     *
     * @param player caller
     * @param drawSetting input item settings
     */
    void onDraw(Player player, DrawSetting drawSetting);

}
