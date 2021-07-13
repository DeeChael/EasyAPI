package org.ezapi.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

public interface Button extends Input {

    void onClick(Player player, ClickType click, InventoryAction action);

}
