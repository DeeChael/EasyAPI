package org.ezapi.module.packet.play.in;

import org.bukkit.entity.Player;
import org.ezapi.module.packet.play.Packet;

public final class PlayInBlockDigPacket implements Packet {

    private final Object nmsPacket;

    private final Player player;

    public PlayInBlockDigPacket(Object nmsPacket, Player player) {
        this.nmsPacket = nmsPacket;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Object getHandle() {
        return nmsPacket;
    }

    public enum EnumPlayerDigType {

        START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM, SWAP_ITEM_WITH_OFFHAND;

    }


}
