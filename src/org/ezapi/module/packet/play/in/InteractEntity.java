package org.ezapi.module.packet.play.in;

import org.bukkit.entity.Player;
import org.ezapi.module.packet.ClickType;
import org.ezapi.module.packet.PacketListener;
import org.ezapi.module.packet.play.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InteractEntity implements Packet {

    private static final Map<PacketListener, List<Method>> listeners = new HashMap<>();

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

    public static void addListener(PacketListener listener, Method method) {
        if (!listeners.containsKey(listener)) listeners.put(listener, new ArrayList<>());
        if (!listeners.get(listener).contains(method)) {
            listeners.get(listener).add(method);
        }
    }

    public static void removeListener(PacketListener listener) {
        listeners.remove(listener);
    }

    public static void dispatch(InteractEntity interactEntity) {
        for (PacketListener listener : listeners.keySet()) {
            for (Method method : listeners.get(listener)) {
                try {
                    method.invoke(listener, interactEntity);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
