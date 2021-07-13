package org.ezapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EzHolder implements InventoryHolder {

    private Inventory inventory = Bukkit.createInventory(null, 54);

    private final String id;

    public EzHolder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
