package org.ezapi.util.item;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantedBookBuilder implements ItemBuilderBase {

    public static EnchantedBookBuilder newItemStack() {
        return new EnchantedBookBuilder();
    }

    private final ItemStack itemStack;

    private EnchantedBookBuilder() {
        this.itemStack = new ItemStack(Material.ENCHANTED_BOOK);
    }

    public EnchantedBookBuilder addStoredEnchant(Enchantment enchantment, int level) {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) itemStack.getItemMeta();
        if (itemMeta != null) {
            if (level <= 0) {
                if (itemMeta.hasEnchant(enchantment)) {
                    itemMeta.removeStoredEnchant(enchantment);
                }
                return this;
            }
            itemMeta.addStoredEnchant(enchantment, level, true);
        }
        return this;
    }

    @Override
    public EnchantedBookBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public EnchantedBookBuilder setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public EnchantedBookBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public EnchantedBookBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Override
    public EnchantedBookBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public EnchantedBookBuilder addLore(String lore) {
        return addLore(new String[] {lore});
    }

    @Override
    public EnchantedBookBuilder addLore(String... lore) {
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
    public EnchantedBookBuilder setUnbreakable(boolean isUnbreakable) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setUnbreakable(isUnbreakable);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public EnchantedBookBuilder addEnchant(Enchantment enchantment, int level) {
        return addStoredEnchant(enchantment, level);
    }

    @Override
    public EnchantedBookBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addAttributeModifier(attribute, attributeModifier);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public EnchantedBookBuilder setCustomModelData(int data) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public EnchantedBookBuilder addItemFlags(ItemFlag... itemFlags) {
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
