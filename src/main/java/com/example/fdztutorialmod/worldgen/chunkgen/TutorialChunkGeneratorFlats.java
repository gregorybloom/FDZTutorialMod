package com.example.fdztutorialmod.worldgen.chunkgen;

import com.example.fdztutorialmod.worldgen.biomes.providers.TutorialBiomeSource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TutorialChunkGeneratorFlats extends ChunkGenerator {

    private static Map<String,BlockState> blockList = new Hashtable<>();

    private static final Codec<Settings> SETTINGS_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("base").forGetter(Settings::getBaseHeight),
                    Codec.FLOAT.fieldOf("verticalvariance").forGetter(Settings::getVerticalVariance),
                    Codec.FLOAT.fieldOf("horizontalvariance").forGetter(Settings::getHorizontalVariance)
            ).apply(instance, Settings::new));

    public static final Codec<TutorialChunkGeneratorFlats> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(TutorialChunkGeneratorFlats::getBiomeRegistry),
                    SETTINGS_CODEC.fieldOf("settings").forGetter(TutorialChunkGeneratorFlats::getTutorialSettings)
            ).apply(instance, TutorialChunkGeneratorFlats::new));

    private final Settings settings;

    private static final BlockState AIR;
    private static final BlockState[] EMPTY_COLUMN;


    public TutorialChunkGeneratorFlats(Registry<Biome> registry, Settings settings) {
        super(new TutorialBiomeSource(registry), new StructureSettings(false));
        this.settings = settings;

        blockList.put("stone",Blocks.STONE.defaultBlockState());
        blockList.put("bedrock",Blocks.BEDROCK.defaultBlockState());
        blockList.put("podzol",Blocks.PODZOL.defaultBlockState());
        blockList.put("grass_block",Blocks.GRASS_BLOCK.defaultBlockState());

    }

    public Settings getTutorialSettings() {
        return settings;
    }

    public Registry<Biome> getBiomeRegistry() {
        return ((TutorialBiomeSource)biomeSource).getBiomeRegistry();
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, ChunkAccess chunkAccess) {

        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState stone = Blocks.STONE.defaultBlockState();

        ChunkPos chunkpos = chunkAccess.getPos();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int x;
        int z;

        //  * currently no longer driven by the Biome Provider and ChunkGenerator

        ChunkPos cpos = chunkAccess.getPos();
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                chunkAccess.setBlockState(pos.set(x, 0, z), bedrock, false);

                Biome b = biomeSource.getNoiseBiome(x,1,z);
                int X = cpos.getBlockX(x);
                int Z = cpos.getBlockZ(z);
//                String chk = worldGenRegion.
                        //getBiome(_pos.set(X, _y, Z)).getRegistryName().toString();
            }
        }



        int baseHeight = settings.getBaseHeight();
        float verticalVariance = settings.getVerticalVariance();
        float horizontalVariance = settings.getHorizontalVariance();
        boolean hit=false;
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                int realx = chunkpos.x * 16 + x;
                int realz = chunkpos.z * 16 + z;
                int height = baseHeight;
                for (int y = 1 ; y < height ; y++) {
                    hit=false;
                    if(y==(height-1)) hit=setSurfacePattern(pos.set(x, y, z),x,y,z,worldGenRegion,chunkAccess);
                    if(!hit) chunkAccess.setBlockState(pos.set(x, y, z), stone, false);
                }
            }
        }
    }
    public boolean setSurfacePattern(BlockPos.MutableBlockPos _pos, int _x, int _y, int _z, WorldGenRegion worldGenRegion, ChunkAccess chunkAccess) {
        ChunkPos cpos = chunkAccess.getPos();
        int X = cpos.getBlockX(_x);
        int Z = cpos.getBlockZ(_z);

        String chk = worldGenRegion.getBiome(_pos.set(X, _y, Z)).getRegistryName().toString();
        if(chk.matches(Biomes.PLAINS.location().toString())) chunkAccess.setBlockState(_pos.set(_x, _y, _z), blockList.get("grass_block"), false);
        else if(chk.matches(Biomes.JUNGLE.location().toString())) chunkAccess.setBlockState(_pos.set(_x, _y, _z), blockList.get("podzol"), false);
        else return false;
        return true;
    }


    public void createStructures(RegistryAccess p_62200_, StructureFeatureManager p_62201_, ChunkAccess p_62202_, StructureManager p_62203_, long p_62204_) {
    }
    private void createStructure(ConfiguredStructureFeature<?, ?> p_156164_, RegistryAccess p_156165_, StructureFeatureManager p_156166_, ChunkAccess p_156167_, StructureManager p_156168_, long p_156169_, Biome p_156170_) {
    }
    public void createReferences(WorldGenLevel p_62178_, StructureFeatureManager p_62179_, ChunkAccess p_62180_) {
    }
    public void applyBiomeDecoration(WorldGenRegion p_62168_, StructureFeatureManager p_62169_) {
    }
    public void applyCarvers(long p_62157_, BiomeManager p_62158_, ChunkAccess p_62159_, GenerationStep.Carving p_62160_) {
    }
    private void generateStrongholds() {
    }


    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long l) {
        return this;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    private ChunkAccess doFill(StructureFeatureManager p_158428_, ChunkAccess p_158429_, int p_158430_, int p_158431_) {
        return p_158429_;
    }

    @Override
    public int getBaseHeight(int i, int i1, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor) {
        return this.settings.getBaseHeight();
    }

    @Override
    public NoiseColumn getBaseColumn(int p_158401_, int p_158402_, LevelHeightAccessor p_158403_) {
        return new NoiseColumn(p_158403_.getMinBuildHeight(), EMPTY_COLUMN);
    }

    private static class Settings {
        private final int baseHeight;
        private final float verticalVariance;
        private final float horizontalVariance;

        public Settings(int baseHeight, float verticalVariance, float horizontalVariance) {
            this.baseHeight = baseHeight;
            this.verticalVariance = verticalVariance;
            this.horizontalVariance = horizontalVariance;
        }

        public float getVerticalVariance() {
            return verticalVariance;
        }

        public int getBaseHeight() {
            return baseHeight;
        }

        public float getHorizontalVariance() {
            return horizontalVariance;
        }
    }

    static {
        AIR = Blocks.AIR.defaultBlockState();
        EMPTY_COLUMN = new BlockState[0];
    }
}

