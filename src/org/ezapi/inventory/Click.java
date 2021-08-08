package org.ezapi.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

public interface Click extends Input {

    /**
     * Be called on player clicking a button
     * <p>You can make different action for different players</p>
     *
     * @param player caller
     * @param click click type
     * @param action inventory action
     */
    void onClick(Player player, ClickType click, InventoryAction action);

}
