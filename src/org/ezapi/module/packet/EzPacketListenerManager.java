package org.ezapi.module.packet;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class EzPacketListenerManager {

    private EzPacketListenerManager() {}

    //private final static Map<PacketListener, List<Class<Packet>>> registered = new HashMap<>();

    private final static List<PacketListener> registered = new ArrayList<>();

    public static void register(PacketListener listener) {
        if (!registered.contains(listener)) {
            registered.add(listener);
        }
        /*
        if (!registered.containsKey(listener)) {
            registered.put(listener, new ArrayList<>());
        }
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                if (method.getAnnotation(PacketHandler.class) != null) {
                    if (method.getParameterTypes().length == 1 && Packet.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        Class<Packet> packetClass = (Class<Packet>) method.getParameterTypes()[0];
                        registered.get(listener).add(packetClass);
                        try {
                            Method m = packetClass.getDeclaredMethod("addListener", PacketListener.class, Method.class);
                            m.setAccessible(true);
                            m.invoke(null, listener, method);
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                        }
                    }
                }
            }
        }
        */
    }

    public static void unregister(PacketListener listener) {
        registered.remove(listener);
        /*
        if (!registered.containsKey(listener)) return;
        for (Class<Packet> packetClass : registered.get(listener)) {
            try {
                Method method = packetClass.getMethod("removeListener", PacketListener.class);
                method.setAccessible(true);
                method.invoke(null, listener);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }
         */
    }

    public static void dispatchPlayIn(Player player, Object packet) {
        for (PacketListener listener : registered) {
            listener.onPlayIn(player, packet);
        }
    }

    public static void dispatchPlayOut(Player player, Object packet) {
        for (PacketListener listener : registered) {
            listener.onPlayOut(player, packet);
        }
    }

    /*
    public static void dispatch(Packet packet) {
        try {
            Method method = packet.getClass().getMethod("dispatch", packet.getClass());
            method.setAccessible(true);
            method.invoke(null, packet);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
    }
     */

}
