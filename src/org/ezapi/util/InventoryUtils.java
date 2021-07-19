package org.ezapi.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.ezapi.util.item.ItemUtils;

import java.util.Map;

public final class InventoryUtils {

    public static void toInventory(String jsonObjectString, Player player) {
        if (JsonUtils.isJsonObject(jsonObjectString)) {
            toInventory(new JsonParser().parse(jsonObjectString).getAsJsonObject(), player);
        }
    }

    public static void toInventory(JsonObject jsonObject, Player player) {
        if (jsonObject.has("owner")) {
            if (jsonObject.get("owner").getAsString().equalsIgnoreCase(player.getName())) {
                player.getInventory().clear();
                JsonObject items = jsonObject.getAsJsonObject("items");
                for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                    String slot = entry.getKey();
                    if (slot.startsWith("slot_")) {
                        player.getInventory().setItem(Integer.parseInt(slot.replace("slot_", "")), ItemUtils.toItemStack(entry.getValue().getAsJsonObject()));
                    } else if (slot.equalsIgnoreCase("helmet")) {
                        player.getInventory().setHelmet(ItemUtils.toItemStack(entry.getValue().getAsJsonObject()));
                    } else if (slot.equalsIgnoreCase("chestplate")) {
                        player.getInventory().setChestplate(ItemUtils.toItemStack(entry.getValue().getAsJsonObject()));
                    } else if (slot.equalsIgnoreCase("leggings")) {
                        player.getInventory().setLeggings(ItemUtils.toItemStack(entry.getValue().getAsJsonObject()));
                    } else if (slot.equalsIgnoreCase("boots")) {
                        player.getInventory().setBoots(ItemUtils.toItemStack(entry.getValue().getAsJsonObject()));
                    } else if (slot.equalsIgnoreCase("offhand")) {
                        player.getInventory().setItemInOffHand(ItemUtils.toItemStack(entry.getValue().getAsJsonObject()));
                    }
                }
                if (jsonObject.has("holding")) {
                    player.getInventory().setHeldItemSlot(jsonObject.get("holding").getAsInt());
                }
            }
        }
    }

    public static String toJsonObjectString(PlayerInventory inventory) {
        return new Gson().toJson(toJsonObject(inventory));
    }

    public static JsonObject toJsonObject(PlayerInventory inventory) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("owner", inventory.getHolder().getName());
        jsonObject.addProperty("holding", inventory.getHeldItemSlot());
        JsonObject items = new JsonObject();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                items.add("slot_" + i, ItemUtils.toJsonObject(itemStack));
            }
        }
        items.add("helmet", ItemUtils.toJsonObject(inventory.getHelmet()));
        items.add("chestplate", ItemUtils.toJsonObject(inventory.getChestplate()));
        items.add("leggings", ItemUtils.toJsonObject(inventory.getLeggings()));
        items.add("boots", ItemUtils.toJsonObject(inventory.getBoots()));
        items.add("offhand", ItemUtils.toJsonObject(inventory.getItemInOffHand()));
        jsonObject.add("items", items);
        return jsonObject;
    }

}
