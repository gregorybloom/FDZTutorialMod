package com.example.fdztutorialmod.worldgen.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;

import java.util.Random;

public class FDZEmptySurfaceBuilder extends SurfaceBuilder {


    public FDZEmptySurfaceBuilder(Codec p_75221_) {
        super(p_75221_);
    }

    @Override
    public void apply(Random random, ChunkAccess p_164214_, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int p_164223_, long seed, SurfaceBuilderConfiguration config) {

    }
}
