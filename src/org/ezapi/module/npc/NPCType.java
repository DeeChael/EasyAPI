package org.ezapi.module.npc;

import org.bukkit.Location;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.npc.fake.FakeEntity;
import org.ezapi.module.npc.fake.FakePlayer;
import org.ezapi.module.npc.fake.FakeVillager;
import org.ezapi.reflect.EzClass;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NPCType<T extends FakeEntity> {

    public final static NPCType<FakePlayer> PLAYER = new NPCType<>(new FakePlayer());

    public final static NPCType<FakeVillager> VILLAGER = new NPCType<>(new FakeVillager());

    private final BiFunction<String, Location, EzClass> create;

    private final Function<Object, List<EzClass>> packet;

    private final NonReturnWithTwo<Object, Object> data;

    private NPCType(FakeEntity entity) {
        this.create = entity::create;
        this.packet = entity::packet;
        this.data = entity::data;
    }

    public EzClass createNPCEntity(String name, Location location) {
        return create.apply(name, location);
    }

    public List<EzClass> createSpawnPacket(Object nmsEntity) {
        return packet.apply(nmsEntity);
    }

    public void setSpecialData(Object nmsEntity, Object data) {
        this.data.apply(nmsEntity, data);
    }

}
