package org.ezapi.module.packet.play.in;

import org.bukkit.entity.Player;
import org.ezapi.module.packet.play.Packet;

public final class InteractEntity implements Packet {

    private final Object nmsPacket;

    private final Player player;

    private final int entityId;

    private final boolean isSneaking;

    private final ClickType clickType;

    public InteractEntity(Object nmsPacket, Player player, int id, boolean isSneaking, ClickType clickType) {
        this.nmsPacket = nmsPacket;
        this.player = player;
        this.entityId = id;
        this.isSneaking = isSneaking;
        this.clickType = clickType;
    }

    public Player getPlayer() {
        return player;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public Object getHandle() {
        return nmsPacket;
    }

    public enum ClickType {
        RIGHT, LEFT;
    }

}
