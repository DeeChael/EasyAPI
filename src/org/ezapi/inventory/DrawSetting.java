package org.ezapi.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.ezapi.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DrawSetting {

    private Material type = Material.AIR;

    private ChatMessage displayName = new ChatMessage("", false);

    private List<ChatMessage> lore = new ArrayList<>();

    private boolean enchanted = false;

    private boolean unbreakable = false;

    private boolean clone = false;

    private ItemStack cloneItem = new ItemStack(Material.AIR);

    private final int slot;

    /**
     * You shouldn't create a draw setting by your self
     * @param slot slot
     */
    public DrawSetting(int slot) {
        this.slot = slot;
    }

    /**
     * Clone a item stack, render() method will return this item stack directly
     * @param itemStack item stack to be cloned
     */
    public void clone(ItemStack itemStack) {
        this.clone = true;
        cloneItem = itemStack.clone();
    }

    /**
     * Get Input slot number
     * @return slot number
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Get item stack material type
     * @return material
     */
    public Material getType() {
        return type;
    }

    /**
     * Set item stack material type
     * @param type material
     */
    public void setType(Material type) {
        this.type = type;
    }

    /**
     * Get item stack lore
     * @return lore
     */
    public List<ChatMessage> getLore() {
        return lore;
    }

    /**
     * Set item stack lore
     * @param lore lore
     */
    public void setLore(List<ChatMessage> lore) {
        this.lore = lore;
    }

    /**
     * Set item stack lore
     * @param chatMessages lore
     */
    public void setLore(ChatMessage... chatMessages) {
        lore.clear();
        lore.addAll(Arrays.asList(chatMessages));
    }

    /**
     * Set item stack display name
     * @param displayName display name
     */
    public void setDisplayName(ChatMessage displayName) {
        this.displayName = displayName;
    }

    /**
     * Get item stack display name
     * @return display name
     */
    public ChatMessage getDisplayName() {
        return displayName;
    }

    /**
     * Set item stack is enchanted
     * @param enchanted is enchanted
     */
    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    /**
     * Get if item stack is enchanted
     * @return is enchanted
     */
    public boolean isEnchanted() {
        return enchanted;
    }

    /**
     * Get if item stack is unbreakable
     * @return is unbreakable
     */
    public boolean isUnbreakable() {
        return unbreakable;
    }

    /**
     * Set item stack is unbreakable
     * @param unbreakable is unbreakable
     */
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    /**
     * You shouldn't call this method
     * @param player player
     * @return item stack
     */
    public ItemStack render(Player player) {
        if (clone) return cloneItem;
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName.getText(player));
            List<String> list = new ArrayList<>();
            for (ChatMessage chatMessage : lore) {
                list.add(chatMessage.getText(player));
            }
            itemMeta.setLore(list);
            if (enchanted) {
                itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if (unbreakable) {
                itemMeta.setUnbreakable(true);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

}
