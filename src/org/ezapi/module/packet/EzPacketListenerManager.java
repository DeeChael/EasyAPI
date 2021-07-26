package org.ezapi.module.packet;

import org.ezapi.module.packet.play.Packet;
import org.ezapi.module.packet.play.PacketHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class EzPacketListenerManager {

    private EzPacketListenerManager() {}

    private final static Map<PacketListener,List<Method>> methods = new HashMap<>();

    public static void register(PacketListener listener) {
        if (!methods.containsKey(listener)) {
            methods.put(listener, new ArrayList<>());
        }
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) {
                if (method.getAnnotation(PacketHandler.class) != null) {
                    if (method.getParameterTypes().length == 1 &&Packet.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        if (!methods.get(listener).contains(method)) {
                            method.setAccessible(true);
                            methods.get(listener).add(method);
                        }
                    }
                }
            }
        }
    }

    public static void unregister(PacketListener listener) {
        methods.remove(listener);
    }

    public static void dispatch(Packet packet) {
        for (PacketListener packetListener : methods.keySet()) {
            for (Method method : methods.get(packetListener)) {
                if (method.getParameterTypes()[0].equals(packet.getClass())) {
                    try {
                        method.invoke(packetListener, packet);
                    } catch (IllegalAccessException | InvocationTargetException ignored) {
                    }
                }
            }
        }
    }

}
