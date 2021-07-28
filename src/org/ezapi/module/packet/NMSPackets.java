package org.ezapi.module.packet;

import org.ezapi.reflect.EzClass;
import org.ezapi.util.ReflectionUtils;

public final class NMSPackets {

    //Play In Packets
    public final static EzClass PacketPlayInUseEntity = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayInUseEntity", "PacketPlayInUseEntity"));
    public final static EzClass PacketPlayInBlockDig = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayInBlockDig", "PacketPlayInBlockDig"));

    //Play Out Packets

    private NMSPackets() {}

}
