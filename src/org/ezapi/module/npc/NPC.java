package org.ezapi.module.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ezapi.chat.ChatMessage;

import java.util.List;

public interface NPC {

    /**
     * Set item in npc main hand
     * @param itemStack item
     */
    void setItemInMainHand(ItemStack itemStack);

    /**
     * Set item in npc off hand
     * @param itemStack item
     */
    void setItemInOffHand(ItemStack itemStack);

    /**
     * Set item on npc feet
     * @param itemStack boots
     */
    void setBoots(ItemStack itemStack);

    /**
     * Set item on npc legs
     * @param itemStack leggings
     */
    void setLeggings(ItemStack itemStack);

    /**
     * Set item on npc chest
     * @param itemStack chestplate
     */
    void setChestplate(ItemStack itemStack);

    /**
     * Set item on npc head
     * @param itemStack helmet
     */
    void setHelmet(ItemStack itemStack);

    /**
     * Set npc name
     * @param name name
     */
    void setName(ChatMessage name);

    /**
     * Set npc type
     * @param type type
     */
    void setType(NPCType<?> type);

    /**
     * Get npc type
     * @return type
     */
    NPCType<?> getType();

    /**
     * Set npc special data
     * Player: String - Set player skin to {data}'s
     * Villager & Zombie Villager: VillagerData - Set villager data (1.14+)
     * Entity has babies: boolean - Set entity to baby mode
     * Slime & Magma Cube: int - Set size
     * Sheep: SheepData - Set color and baby mode
     * Enderman: Material - Set carried block (material type must be block)
     * Bee: boolean[] - first boolean is has nectar, second boolean is has stung, third boolean is baby mode
     *
     * @param data data
     */
    void setData(Object data);

    /**
     * Get npc special data
     *
     * @return data
     */
    Object getData();

    /**
     * Make npc look at player
     *
     * @param isLook look
     */
    void look(boolean isLook);

    /**
     * Make npc look at the location in player's eyes
     *
     * @param player player
     * @param target location
     */
    void lookAt(Player player, Location target);

    /**
     * Set npc location
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
     * Refresh npc in all players' eyes
     */
    default void refreshAll() {
        if (isDropped()) return;
        for (Player player : getViewers()) {
            refresh(player);
        }
    }

    /**
     * Refresh npc in player's eyes
     * @param player player
     */
    void refresh(Player player);

    /**
     * Remove npc in player's eyes
     * @param player player
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
     * Drop the npc
     */
    void drop();

    /**
     * Get if the npc is dropped
     * @return is dropped
     */
    boolean isDropped();

}
