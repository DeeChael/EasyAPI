package org.ezapi.util.item;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.ezapi.EasyAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkullBuilder implements ItemBuilderBase {

    public static SkullBuilder newItemStack(String playerName) {
        return new SkullBuilder(playerName);
    }

    private final ItemStack itemStack;

    private SkullBuilder(String playerName) {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        new BukkitRunnable() {
            @Override
            public void run() {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                if (skullMeta != null) {
                    skullMeta.setOwner(playerName);
                }
                itemStack.setItemMeta(skullMeta);
            }
        }.runTaskAsynchronously(EasyAPI.getInstance());
    }

    @Override
    public SkullBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public SkullBuilder setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public SkullBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Override
    public SkullBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder addLore(String lore) {
        return addLore(new String[] {lore});
    }

    @Override
    public SkullBuilder addLore(String... lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            List<String> list = new ArrayList<>(itemMeta.getLore());
            list.addAll(Arrays.asList(lore));
            itemMeta.setLore(list);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder setUnbreakable(boolean isUnbreakable) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setUnbreakable(isUnbreakable);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder addEnchant(Enchantment enchantment, int level) {
        if (level <= 0) return this;
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addAttributeModifier(attribute, attributeModifier);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder setCustomModelData(int data) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public SkullBuilder addItemFlags(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(itemFlags);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemStack build() {
        return itemStack;
    }
    
}
