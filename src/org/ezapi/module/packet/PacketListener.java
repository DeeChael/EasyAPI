package org.ezapi.module.packet;

import org.bukkit.entity.Player;

public interface PacketListener {

    void onPlayIn(Player sender, Object packet);

    void onPlayOut(Player receiver, Object packet);

}
