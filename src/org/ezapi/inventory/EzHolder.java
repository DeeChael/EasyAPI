package org.ezapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class EzHolder implements InventoryHolder {

    private Inventory inventory = Bukkit.createInventory(null, 54);

    private final String id;

    /**
     * You shouldn't create a new EzHolder by your self
     * @param id holder id
     */
    public EzHolder(String id) {
        this.id = id;
    }

    /**
     * Get holder id
     * @return holder id
     */
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
