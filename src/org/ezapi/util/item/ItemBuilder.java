package org.ezapi.util.item;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder implements ItemBuilderBase {

    public static ItemBuilder newItemStack(Material material) {
        return new ItemBuilder(material);
    }

    private final ItemStack itemStack;

    private ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    @Override
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuilder setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Override
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilder addLore(String lore) {
        return addLore(new String[] {lore});
    }

    @Override
    public ItemBuilder addLore(String... lore) {
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
    public ItemBuilder setUnbreakable(boolean isUnbreakable) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setUnbreakable(isUnbreakable);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        if (level <= 0) return this;
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addAttributeModifier(attribute, attributeModifier);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilder setCustomModelData(int data) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
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
