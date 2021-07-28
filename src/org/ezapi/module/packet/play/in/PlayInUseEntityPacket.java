package org.ezapi.module.packet.play.in;

import org.bukkit.entity.Player;
import org.ezapi.module.packet.play.Packet;

public final class PlayInUseEntityPacket implements Packet {

    private final Object nmsPacket;

    private final int entityId;

    private final ClickType clickType;

    public PlayInUseEntityPacket(Object nmsPacket, Player player, int id, ClickType clickType) {
        this.nmsPacket = nmsPacket;
        this.entityId = id;
        this.clickType = clickType;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public Object getHandle() {
        return nmsPacket;
    }

    public enum ClickType {
        RIGHT, LEFT, UNKNOWN;
    }

}
