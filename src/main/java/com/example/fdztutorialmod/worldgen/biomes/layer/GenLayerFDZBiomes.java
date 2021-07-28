package com.example.fdztutorialmod.worldgen.biomes.layer;

import com.example.fdztutorialmod.worldgen.biomes.FDZBiomes;
import com.example.fdztutorialmod.worldgen.biomes.providers.TutorialBiomeSource;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;
import net.minecraft.world.level.newbiome.context.Context;

import java.util.List;

public enum GenLayerFDZBiomes implements AreaTransformer0 {
    INSTANCE;
    private static final int RARE_BIOME_CHANCE = 15;

    protected static final List<ResourceKey<Biome>> commonBiomes = ImmutableList.of(
            FDZBiomes.getBiome("tut1b_biome1_mcdef_red"),
            Biomes.PLAINS
    );
    protected static final List<ResourceKey<Biome>> rareBiomes = ImmutableList.of(
            Biomes.JUNGLE
    );

    private Registry<Biome> registry;

    public GenLayerFDZBiomes setup(Registry<Biome> registry) {
        this.registry = registry;
        return this;
    }

    GenLayerFDZBiomes() {

    }

    @Override
    public int applyPixel(Context iNoiseRandom, int x, int y) {
        //return 0; //getRandomBiome(iNoiseRandom, commonBiomes));

        if (iNoiseRandom.nextRandom(RARE_BIOME_CHANCE) == 0) {
            // make rare biome
            return getRandomBiome(iNoiseRandom, rareBiomes);
        } else {
            // make common biome
            return getRandomBiome(iNoiseRandom, commonBiomes);
        }
    }

    private int getRandomBiome(Context random, List<ResourceKey<Biome>> biomes) {
        return TutorialBiomeSource.getBiomeId(biomes.get(random.nextRandom(biomes.size())), registry);
    }

}
