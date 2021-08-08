package org.ezapi.module.npc;

import org.bukkit.Location;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.npc.fake.*;
import org.ezapi.reflect.EzClass;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NPCType<T extends FakeEntity> {

    public final static NPCType<FakePlayer> PLAYER = new NPCType<>(new FakePlayer());

    public final static NPCType<FakeVillager> VILLAGER = new NPCType<>(new FakeVillager());

    public final static NPCType<FakeBlaze> BLAZE = new NPCType<>(new FakeBlaze());

    public final static NPCType<FakeCaveSpider> CAVE_SPIDER = new NPCType<>(new FakeCaveSpider());

    public final static NPCType<FakeCreeper> CREEPER = new NPCType<>(new FakeCreeper());

    public final static NPCType<FakeDrowned> DROWNED = new NPCType<>(new FakeDrowned());

    public final static NPCType<FakeEnderman> ENDERMAN = new NPCType<>(new FakeEnderman());

    public final static NPCType<FakeElderGuardian> ELDER_GUARDIAN = new NPCType<>(new FakeElderGuardian());

    public final static NPCType<FakeEvoker> EVOKER = new NPCType<>(new FakeEvoker());

    public final static NPCType<FakeGhast> GHAST = new NPCType<>(new FakeGhast());

    public final static NPCType<FakeGiant> GIANT = new NPCType<>(new FakeGiant());

    public final static NPCType<FakeGuardian> GUARDIAN = new NPCType<>(new FakeGuardian());

    public final static NPCType<FakeIllusioner> ILLUSIONER = new NPCType<>(new FakeIllusioner());

    public final static NPCType<FakeMagmaCube> MAGMA_CUBE = new NPCType<>(new FakeMagmaCube());

    public final static NPCType<FakePhantom> PHANTOM = new NPCType<>(new FakePhantom());

    public final static NPCType<FakeWanderingTrader> WANDERING_TRADER = new NPCType<>(new FakeWanderingTrader());

    public final static NPCType<FakePigZombie> ZOMBIE_PIGMAN = new NPCType<>(new FakePigZombie());

    public final static NPCType<FakePigZombie> ZOMBIFIED_PIGLIN = new NPCType<>(new FakePigZombie());

    public final static NPCType<FakePillager> PILLAGER = new NPCType<>(new FakePillager());

    private final BiFunction<String, Location, EzClass> create;

    private final Function<Object, List<EzClass>> packet;

    private final NonReturnWithTwo<Object, Object> data;

    private NPCType(T entity) {
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
