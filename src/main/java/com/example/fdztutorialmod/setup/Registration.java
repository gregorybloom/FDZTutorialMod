package com.example.fdztutorialmod.setup;


import com.example.fdztutorialmod.worldgen.surfacebuilders.FDZSurfaceBuilders;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class Registration {
    public static void init() {
        FDZSurfaceBuilders.SURFACE_BUILDERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
