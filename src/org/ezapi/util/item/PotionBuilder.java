package org.ezapi.util.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotionBuilder implements ItemBuilderBase {

    public enum PotionType {
        POTION, SPLASH, LINGERING;
    }

    public static PotionBuilder newItemStack(PotionType potionType) {
        return new PotionBuilder(potionType);
    }

    private final ItemStack itemStack;

    private PotionBuilder(PotionType potionType) {
        switch (potionType) {
            case SPLASH:
                this.itemStack = new ItemStack(Material.SPLASH_POTION);
                break;
            case LINGERING:
                this.itemStack = new ItemStack(Material.LINGERING_POTION);
                break;
            default:
                this.itemStack = new ItemStack(Material.POTION);
                break;
        }
    }

    @Override
    public PotionBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public PotionBuilder setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public PotionBuilder setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public PotionBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    @Override
    public PotionBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(lore);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public PotionBuilder addLore(String lore) {
        return addLore(new String[] {lore});
    }

    @Override
    public PotionBuilder addLore(String... lore) {
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
    public PotionBuilder setUnbreakable(boolean isUnbreakable) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setUnbreakable(isUnbreakable);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public PotionBuilder addEnchant(Enchantment enchantment, int level) {
        if (level <= 0) return this;
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addEnchant(enchantment, level, true);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public PotionBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addAttributeModifier(attribute, attributeModifier);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public PotionBuilder setCustomModelData(int data) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    @Override
    public PotionBuilder addItemFlags(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(itemFlags);
            this.itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public PotionBuilder addPotionEffect(PotionEffect potionEffect) {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        if (potionMeta != null) {
            potionMeta.addCustomEffect(potionEffect, true);
            this.itemStack.setItemMeta(potionMeta);
        }
        return this;
    }

    public PotionBuilder setBasePotionData(PotionData potionData) {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionData(potionData);
            this.itemStack.setItemMeta(potionMeta);
        }
        return this;
    }

    public PotionBuilder setColor(Color color) {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setColor(color);
            this.itemStack.setItemMeta(potionMeta);
        }
        return this;
    }

    @Override
    public ItemStack build() {
        return itemStack;
    }
    
}
