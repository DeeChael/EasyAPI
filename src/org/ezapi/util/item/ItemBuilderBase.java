package org.ezapi.util.item;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

interface ItemBuilderBase {

    ItemBuilderBase setAmount(int amount);

    ItemBuilderBase setItemMeta(ItemMeta itemMeta);

    ItemBuilderBase setDisplayName(String displayName);

    ItemBuilderBase setLore(String... lore);

    ItemBuilderBase setLore(List<String> lore);

    ItemBuilderBase addLore(String lore);

    ItemBuilderBase addLore(String... lore);

    ItemBuilderBase setUnbreakable(boolean isUnbreakable);

    ItemBuilderBase addEnchant(Enchantment enchantment, int level);

    ItemBuilderBase addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier);

    ItemBuilderBase setCustomModelData(int data);

    ItemBuilderBase addItemFlags(ItemFlag... itemFlags);

    ItemStack build();

}
