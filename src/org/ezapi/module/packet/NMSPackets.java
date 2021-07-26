package org.ezapi.module.packet;

import org.ezapi.reflect.EzClass;
import org.ezapi.util.ReflectionUtils;

public final class NMSPackets {

    public final static EzClass PacketPlayInUseEntity = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayInUseEntity", "PacketPlayInUseEntity"));

    private NMSPackets() {}

}
