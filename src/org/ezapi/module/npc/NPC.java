package org.ezapi.module.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ezapi.chat.ChatMessage;

import java.util.List;

public interface NPC {

    void setItemInMainHand(ItemStack itemStack);

    void setItemInOffHand(ItemStack itemStack);

    void setBoots(ItemStack itemStack);

    void setLeggings(ItemStack itemStack);

    void setChestplate(ItemStack itemStack);

    void setHelmet(ItemStack itemStack);

    void setName(ChatMessage name);

    void setType(NPCType<?> type);

    NPCType<?> getType();

    void setData(Object data);

    void look(boolean isLook);

    void lookAt(Player player, Location target);

    void setLocation(Location location);

    void addViewer(Player player);

    void removeViewer(Player player);

    default void refreshAll() {
        if (isDropped()) return;
        for (Player player : getViewers()) {
            refresh(player);
        }
    }

    void refresh(Player player);

    void destroy(Player player);

    List<Player> getViewers();

    void removeAll();

    void drop();

    boolean isDropped();

}
