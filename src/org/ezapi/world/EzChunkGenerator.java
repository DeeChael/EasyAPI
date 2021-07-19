package org.ezapi.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class EzChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        Random rrr = new Random(world.getSeed());
        SimplexOctaveGenerator simplexOctaveGenerator = new SimplexOctaveGenerator(rrr, 8);
        ChunkData chunk = createChunkData(world);
        simplexOctaveGenerator.setScale(0.005D);
        int height = 50;
        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                height = (int) (simplexOctaveGenerator.noise(chunkX * 16 + X, chunkZ * 16 + Z, 0.5D, 0.5D) * 15D + 50D);
                chunk.setBlock(X, height, Z, Material.GRASS_BLOCK);
                int dirtHeight = new Random().nextInt(6);
                for (int i = height - 1; i > height - dirtHeight; i--) {
                    chunk.setBlock(X, i, Z, Material.DIRT);
                }
                for (int i = height - dirtHeight; i > 0; i--)
                    chunk.setBlock(X, i, Z, Material.STONE);
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        }

        SimplexNoiseGenerator simplexNoiseGenerator = new SimplexNoiseGenerator(rrr);
        return chunk;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new TreePopulator(), new LakePopulator());
    }

}
