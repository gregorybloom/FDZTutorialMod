package com.example.fdztutorialmod.worldgen.biomes.providers;

import com.example.fdztutorialmod.FdzTutorialMod;
import com.example.fdztutorialmod.worldgen.biomes.FDZBiomes;
import com.example.fdztutorialmod.worldgen.biomes.layer.GenLayerFDZBiomes;
import com.mojang.serialization.Codec;
import com.sun.jna.Structure;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.layer.Layer;
import net.minecraft.world.level.newbiome.layer.Layers;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import net.minecraft.world.level.newbiome.area.Area;


import java.util.List;
import java.util.function.LongFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TutorialBiomeSource extends BiomeSource {

    public static final Codec<TutorialBiomeSource> CODEC = RegistryLookupCodec.create(Registry.BIOME_REGISTRY)
            .xmap(TutorialBiomeSource::new, TutorialBiomeSource::getBiomeRegistry).codec();

    //    private final Biome biome;
    private final Registry<Biome> biomeRegistry;
    private static List<ResourceKey<Biome>> SPAWN = null;

//    private final Layer noiseBiomeLayer;

    private final Layer genBiomes;
    private final long seed;


    public TutorialBiomeSource(Registry<Biome> biomeRegistry) {
        super(getStartBiomes(biomeRegistry));

        /*
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        List<Biome> starter = getStartBiomes(biomeRegistry);
        for(int i=0; i<starter.size(); i++)
        {
            System.out.println(starter.get(i).getRegistryName().toString());
        } /**/
        long s = 894539054;
        this.biomeRegistry = biomeRegistry;
        this.seed = s;

        boolean legacyBiomeInitLayer = false;
        boolean largeBiomes = false;

        genBiomes = makeLayers(s, biomeRegistry);
//        this.noiseBiomeLayer = Layers.getDefaultLayer(seed, legacyBiomeInitLayer, largeBiomes ? 6 : 4, 4);
    }

    private static List<Biome> getStartBiomes(Registry<Biome> registry) {
        if(SPAWN == null) {
            SPAWN = new java.util.ArrayList<ResourceKey<Biome>>();
            //  * currently no longer driven by the Biome Provider and ChunkGenerator
            SPAWN.add(Biomes.JUNGLE);
            SPAWN.add(Biomes.PLAINS);
            SPAWN.add(FDZBiomes.getBiome("tut1b_biome1_mcdef_red"));
        }

        /*
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@xxxxxxxxxxxx");
        List<Biome> starter = SPAWN.stream().map(s -> registry.get(s.location())).collect(Collectors.toList());
        for(int i=0; i<starter.size(); i++)
        {
            System.out.println(starter.get(i).getRegistryName().toString());
        } /**/

        return SPAWN.stream().map(s -> registry.get(s.location())).collect(Collectors.toList());
    }

    public Registry<Biome> getBiomeRegistry() {
        return biomeRegistry;
    }

    public static int getBiomeId(ResourceKey<Biome> biome, Registry<Biome> registry) {
        return registry.getId(registry.get(biome));
    }


    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long l) {
        return new TutorialBiomeSource(biomeRegistry);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
//        if(y > 30 && y < 90)    return biomeRegistry.get(SPAWN.get(2).location());
//        System.out.println(this.noiseBiomeLayer.get(this.biomeRegistry, x, z).getRegistryName().toString());
        return biomeRegistry.get(SPAWN.get(Math.abs((int)Math.floor(x/4))%SPAWN.size()).location());
//        return this.noiseBiomeLayer.get(this.biomeRegistry, x, z);
//        return this.biomeRegistry.get(SPAWN.get(0));
//        return biome;
    }



    private static <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed, Registry<Biome> registry) {
        AreaFactory<T> biomes = GenLayerFDZBiomes.INSTANCE.setup(registry).run(seed.apply(1L));
        return biomes;
    }
    public static Layer makeLayers(long seed, Registry<Biome> registry) {
        AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, seed, context), registry);

        return new Layer(areaFactory) {
            @Override
            public Biome get(Registry<Biome> p_242936_1_, int p_242936_2_, int p_242936_3_) {
                int i = this.area.getValue(p_242936_2_, p_242936_3_);
                Biome biome = registry.byId(i);
                if (biome == null)
                    throw new IllegalStateException("Unknown biome id emitted by layers: " + i);
                return biome;
            }
        };
    }


}
