package org.ezapi.module.packet;

import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class NMSPackets {

    //Play In Packets
    public final static EzClass PacketPlayInUseEntity = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayInUseEntity", "PacketPlayInUseEntity"));
    public final static EzClass PacketPlayInBlockDig = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayInBlockDig", "PacketPlayInBlockDig"));

    //Play Out Packets

    private NMSPackets() {}

}
