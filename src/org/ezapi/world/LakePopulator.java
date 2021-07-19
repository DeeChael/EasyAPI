package org.ezapi.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

class LakePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (random.nextInt(100) < 10) {
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

            int randomX = chunkX * 16 + random.nextInt(16);
            int randomZ = chunkZ * 16 + random.nextInt(16);
            int y = 0;

            for (y = world.getMaxHeight() - 1; world.getBlockAt(randomX, y, randomZ).getType() == Material.AIR; y--) ;
            y -= 7;

            Block block = world.getBlockAt(randomX + 8, y, randomZ + 8);

            if (random.nextInt(100) < 90) {
                block.setType(Material.WATER);
            } else {
                block.setType(Material.LAVA);
            }

            boolean[] booleans = new boolean[2048];

            int i = random.nextInt(4) + 4;

            int j, j1, k1;

            for (j = 0; j < i; ++j) {
                double d0 = random.nextDouble() * 6.0D + 3.0D;
                double d1 = random.nextDouble() * 4.0D + 2.0D;
                double d2 = random.nextDouble() * 6.0D + 3.0D;
                double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int k = 1; k < 15; ++k) {
                    for (int l = 1; l < 15; ++l) {
                        for (int i1 = 0; i1 < 7; ++i1) {
                            double d6 = (k - d3) / (d0 / 2.0D);
                            double d7 = (i1 - d4) / (d1 / 2.0D);
                            double d8 = (l - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D) {
                                booleans[(k * 16 + l) * 8 + i1] = true;
                            }
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j) {
                for (k1 = 0; k1 < 16; ++k1) {
                    for (j1 = 0; j1 < 8; ++j1) {
                        if (booleans[(j * 16 + k1) * 8 + j1]) {
                            world.getBlockAt(randomX + j, y + j1, randomZ + k1).setType(j1 > 4 ? Material.AIR : block.getType());
                        }
                    }
                }
            }

            for (j = 0; j < 16; ++j) {
                for (k1 = 0; k1 < 16; ++k1) {
                    for (j1 = 4; j1 < 8; ++j1) {
                        if (booleans[(j * 16 + k1) * 8 + j1]) {
                            int X1 = randomX + j;
                            int Y1 = y + j1 - 1;
                            int Z1 = randomZ + k1;

                            if (world.getBlockAt(X1, Y1, Z1).getType() == Material.DIRT) {
                                world.getBlockAt(X1, Y1, Z1).setType(Material.GRASS);
                            }
                        }
                    }
                }
            }
        }
    }

}
