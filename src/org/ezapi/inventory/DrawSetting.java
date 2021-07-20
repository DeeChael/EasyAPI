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

    public DrawSetting(int slot) {
        this.slot = slot;
    }

    public void clone(ItemStack itemStack) {
        this.clone = true;
        cloneItem = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public Material getType() {
        return type;
    }

    public void setType(Material type) {
        this.type = type;
    }

    public List<ChatMessage> getLore() {
        return lore;
    }

    public void setLore(List<ChatMessage> lore) {
        this.lore = lore;
    }

    public void setLore(ChatMessage... chatMessages) {
        lore.clear();
        lore.addAll(Arrays.asList(chatMessages));
    }

    public void setDisplayName(ChatMessage displayName) {
        this.displayName = displayName;
    }

    public ChatMessage getDisplayName() {
        return displayName;
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

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
