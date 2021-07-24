package com.example.fdztutorialmod.worldgen.surfacebuilders;

import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "fdztutorialmod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDZSurfaceBuilders {
    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, "fdztutorialmod");

    // Biomes are registered before surface builders and need the raw objects. So don't use DeferredRegister here.
    public static final RegistryObject<SurfaceBuilder<?>> EMPTY_FDZ = SURFACE_BUILDERS.register("fdz_empty_surfacebuilder_class", () -> new FDZEmptySurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
    public static final RegistryObject<SurfaceBuilder<?>> DEFAULT_FDZ = SURFACE_BUILDERS.register("fdz_default_surfacebuilder_class", () -> new FDZDefaultSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
}

