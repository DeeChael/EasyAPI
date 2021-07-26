package org.ezapi.module.npc;

import org.bukkit.Location;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.npc.fake.FakeEntity;
import org.ezapi.module.npc.fake.FakePlayer;
import org.ezapi.reflect.EzClass;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NPCType<T extends FakeEntity> {

    public final static NPCType<FakePlayer> PLAYER = new NPCType<>(FakePlayer::create, FakePlayer::packet, FakePlayer::skin);

    private final BiFunction<String, Location, EzClass> method;

    private final Function<Object, List<EzClass>> packet;

    private final NonReturnWithTwo<Object, Object> data;

    private NPCType(BiFunction<String, Location,EzClass> method, Function<Object, List<EzClass>> packet, NonReturnWithTwo<Object, Object> data) {
        this.method = method;
        this.packet = packet;
        this.data = data;
    }

    public EzClass createNPCEntity(String name, Location location) {
        return method.apply(name, location);
    }

    public List<EzClass> createSpawnPacket(Object nmsEntity) {
        return packet.apply(nmsEntity);
    }

    public void setSpecialData(Object nmsEntity, Object data) {
        this.data.apply(nmsEntity, data);
    }

}
