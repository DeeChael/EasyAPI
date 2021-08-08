package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.bukkit.Material;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeEnderman extends FakeLiving {

    public FakeEnderman() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityEnderman", "EntityEnderman"), "ENDERMAN", "w", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof Material) {
            Material type = (Material) data;
            if (type.isBlock()) {
                EzClass Enderman = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityEnderman", "EntityEnderman"));
                Enderman.setInstance(entity);

                EzClass Block = new EzClass(Ref.getNmsOrOld("world.level.block.Block", "Block"));
                EzClass CraftMagicNumbers = new EzClass(Ref.getObcClass("util.CraftMagicNumbers"));
                Block.setInstance(CraftMagicNumbers.invokeStaticMethod("getBlock", new Class[] {Material.class}, new Object[] {type}));

                EzClass IBlockData = new EzClass(Ref.getNmsOrOld("world.level.block.state.IBlockData", "IBlockData"));
                IBlockData.setInstance(Block.invokeMethod("getBlockData", new Class[0], new Object[0]));

                Enderman.invokeMethod("setCarried", new Class[] {IBlockData.getInstanceClass()}, new Object[] {IBlockData.getInstance()});
            }
        }
        if (data == null) {
            EzClass Enderman = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityEnderman", "EntityEnderman"));
            Enderman.setInstance(entity);

            EzClass IBlockData = new EzClass(Ref.getNmsOrOld("world.level.block.state.IBlockData", "IBlockData"));

            Enderman.invokeMethod("setCarried", new Class[] {IBlockData.getInstanceClass()}, new Object[] {null});
        }
    }

}
