package com.example.fdztutorialmod.worldgen.biomes;

import com.example.fdztutorialmod.FdzTutorialMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Hashtable;
import java.util.Map;

public class FDZBiomes {


    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, FdzTutorialMod.MODID);

    private static Map<String, ResourceKey<Biome>> biomeList = new Hashtable<>();
    public static Map<String, ResourceLocation> biomeRegLocList = new Hashtable<>();


    public static final BiomeDictionary.Type TUTDIM1_BIOMES = BiomeDictionary.Type.getType("tutdim1_biomes");
    public static final BiomeDictionary.Type TUTDIM2_BIOMES = BiomeDictionary.Type.getType("tutdim2_biomes");



    private static ResourceKey<Biome> makeKey(String name) {
        if(!biomeRegLocList.containsKey(name))
        {
            biomeRegLocList.put(name, new ResourceLocation(FdzTutorialMod.MODID, name));
        }
        return ResourceKey.create(Registry.BIOME_REGISTRY, biomeRegLocList.get(name));
    }
    public static void addBiome(String name)
    {
        biomeList.put(name,makeKey(name));
    }
    public static ResourceKey<Biome> getBiome(String name) {
        if(!biomeList.containsKey(name))
        {
            biomeList.put(name, makeKey(name));
        }
        return biomeList.get(name);
    }


    public static void addBiomes()
    {
        FDZBiomes.addBiome("tut1b_biome1_mcdef_red");
    }

    public static void addBiomeTypes() {
        for (String key : biomeList.keySet()) {
            BiomeDictionary.addTypes(biomeList.get(key), TUTDIM2_BIOMES, BiomeDictionary.Type.OCEAN);
        }
    }
}
