package org.ezapi.module.npc;

import org.bukkit.Location;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.npc.fake.*;
import org.ezapi.reflect.EzClass;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NPCType<T extends FakeEntity> {

    public final static NPCType<FakePlayer> PLAYER = new NPCType<>(new FakePlayer(), "player");

    public final static NPCType<FakeVillager> VILLAGER = new NPCType<>(new FakeVillager(), "villager");

    public final static NPCType<FakeBlaze> BLAZE = new NPCType<>(new FakeBlaze(), "blaze");

    public final static NPCType<FakeCaveSpider> CAVE_SPIDER = new NPCType<>(new FakeCaveSpider(), "cave_spider");

    public final static NPCType<FakeCreeper> CREEPER = new NPCType<>(new FakeCreeper(), "creeper");

    public final static NPCType<FakeDrowned> DROWNED = new NPCType<>(new FakeDrowned(), "drowned");

    public final static NPCType<FakeEnderman> ENDERMAN = new NPCType<>(new FakeEnderman(), "enderman");

    public final static NPCType<FakeElderGuardian> ELDER_GUARDIAN = new NPCType<>(new FakeElderGuardian(), "elder_guardian");

    public final static NPCType<FakeEvoker> EVOKER = new NPCType<>(new FakeEvoker(), "evoker");

    public final static NPCType<FakeGhast> GHAST = new NPCType<>(new FakeGhast(), "ghast");

    public final static NPCType<FakeGiant> GIANT = new NPCType<>(new FakeGiant(), "giant");

    public final static NPCType<FakeGuardian> GUARDIAN = new NPCType<>(new FakeGuardian(), "guardian");

    public final static NPCType<FakeIllusioner> ILLUSIONER = new NPCType<>(new FakeIllusioner(), "illusioner");

    public final static NPCType<FakeMagmaCube> MAGMA_CUBE = new NPCType<>(new FakeMagmaCube(), "magma_cube");

    public final static NPCType<FakePhantom> PHANTOM = new NPCType<>(new FakePhantom(), "phantom");

    public final static NPCType<FakeWanderingTrader> WANDERING_TRADER = new NPCType<>(new FakeWanderingTrader(), "wandering_trader");

    public final static NPCType<FakePigZombie> ZOMBIFIED_PIGLIN = new NPCType<>(new FakePigZombie(), "zombified_piglin");

    public final static NPCType<FakePillager> PILLAGER = new NPCType<>(new FakePillager(), "pillager");

    public final static NPCType<FakeRavager> RAVAGER = new NPCType<>(new FakeRavager(), "raveger");

    public final static NPCType<FakeShulker> SHULKER = new NPCType<>(new FakeShulker(), "shulker");

    public final static NPCType<FakeSilverfish> SILVERFISH = new NPCType<>(new FakeSilverfish(), "silverfish");

    public final static NPCType<FakeSkeleton> SKELETON = new NPCType<>(new FakeSkeleton(), "skeleton");

    public final static NPCType<FakeStray> STRAY = new NPCType<>(new FakeStray(), "stray");

    public final static NPCType<FakeWitherSkeleton> WITHER_SKELETON = new NPCType<>(new FakeWitherSkeleton(), "wither_skeleton");

    public final static NPCType<FakeSlime> SLIME = new NPCType<>(new FakeSlime(), "slime");

    public final static NPCType<FakeSpider> SPIDER = new NPCType<>(new FakeSpider(), "spider");

    public final static NPCType<FakeStrider> STRIDER = new NPCType<>(new FakeStrider(), "strider");

    public final static NPCType<FakeVex> VEX = new NPCType<>(new FakeVex(), "vex");

    public final static NPCType<FakeVindicator> VINDICATOR = new NPCType<>(new FakeVindicator(), "vindicator");

    public final static NPCType<FakeWitch> WITCH = new NPCType<>(new FakeWitch(), "witch");

    public final static NPCType<FakeZoglin> ZOGLIN = new NPCType<>(new FakeZoglin(), "zoglin");

    public final static NPCType<FakeZombie> ZOMBIE = new NPCType<>(new FakeZombie(), "zombie");

    public final static NPCType<FakeHusk> HUSK = new NPCType<>(new FakeHusk(), "husk");

    public final static NPCType<FakeZombieVillager> ZOMBIE_VILLAGER = new NPCType<>(new FakeZombieVillager(), "zombie_villager");

    public final static NPCType<FakeHoglin> HOGLIN = new NPCType<>(new FakeHoglin(), "hoglin");

    public final static NPCType<FakePiglin> PIGLIN = new NPCType<>(new FakePiglin(), "piglin");

    private final static NPCType<?>[] VALUES = new NPCType<?>[] {
            PLAYER, VILLAGER, BLAZE, CAVE_SPIDER, CREEPER, DROWNED, ENDERMAN,
            ELDER_GUARDIAN, EVOKER, GHAST, GIANT, GUARDIAN, ILLUSIONER,
            MAGMA_CUBE, PHANTOM, WANDERING_TRADER, ZOMBIFIED_PIGLIN, PILLAGER,
            RAVAGER, SHULKER, SILVERFISH, SKELETON, STRAY, WITHER_SKELETON, SLIME,
            SPIDER, STRIDER, VEX, VINDICATOR, WITCH, ZOGLIN, ZOMBIE, HUSK, ZOMBIE_VILLAGER,
            HOGLIN, PIGLIN
    };

    private final BiFunction<String, Location, EzClass> create;

    private final Function<Object, List<EzClass>> packet;

    private final NonReturnWithTwo<Object, Object> data;

    private final String name;

    private NPCType(T entity, String name) {
        this.create = entity::create;
        this.packet = entity::packet;
        this.data = entity::data;
        this.name = name;
    }

    public String name() {
        return name;
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

    public static NPCType<?>[] values() {
        return VALUES;
    }

    public static NPCType<?> valueOf(String name) {
        for (NPCType<?> type : values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }

}
