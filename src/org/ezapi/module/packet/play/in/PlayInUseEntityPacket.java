package org.ezapi.module.packet.play.in;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.ezapi.module.packet.play.Packet;

public final class PlayInUseEntityPacket extends PlayerEvent implements Packet {

    private static final HandlerList handlers = new HandlerList();

    private final Object nmsPacket;

    private final int entityId;

    private final ClickType clickType;

    public PlayInUseEntityPacket(Object nmsPacket, Player player, int id, ClickType clickType) {
        super(player);
        this.nmsPacket = nmsPacket;
        this.player = player;
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

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
