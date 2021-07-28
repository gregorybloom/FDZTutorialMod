package com.example.fdztutorialmod.setup;


import com.example.fdztutorialmod.FdzTutorialMod;
import com.example.fdztutorialmod.worldgen.biomes.providers.TutorialBiomeSource;
import com.example.fdztutorialmod.worldgen.chunkgen.TutorialChunkGenerator;
import com.example.fdztutorialmod.worldgen.chunkgen.TutorialChunkGeneratorFlats;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = FdzTutorialMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
    public static void init(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
            Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(FdzTutorialMod.MODID, "chunkgen"),
                    TutorialChunkGenerator.CODEC);
            Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(FdzTutorialMod.MODID, "chunkgenflats"),
                    TutorialChunkGeneratorFlats.CODEC);

            Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(FdzTutorialMod.MODID, "biomes"),
                    TutorialBiomeSource.CODEC);
        });
    }
}
