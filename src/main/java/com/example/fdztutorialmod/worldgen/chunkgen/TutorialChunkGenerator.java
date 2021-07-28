package com.example.fdztutorialmod.worldgen.chunkgen;


import com.example.fdztutorialmod.worldgen.biomes.providers.TutorialBiomeSource;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TutorialChunkGenerator extends ChunkGenerator {

    private static final BlockState AIR;
    private static final BlockState[] EMPTY_COLUMN;

    private static final Codec<Settings> SETTINGS_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("base").forGetter(Settings::getBaseHeight),
                    Codec.FLOAT.fieldOf("verticalvariance").forGetter(Settings::getVerticalVariance),
                    Codec.FLOAT.fieldOf("horizontalvariance").forGetter(Settings::getHorizontalVariance)
            ).apply(instance, Settings::new));

    public static final Codec<TutorialChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(TutorialChunkGenerator::getBiomeRegistry),
                    SETTINGS_CODEC.fieldOf("settings").forGetter(TutorialChunkGenerator::getTutorialSettings)
            ).apply(instance, TutorialChunkGenerator::new));

    private final Settings settings;

    public TutorialChunkGenerator(Registry<Biome> registry, Settings settings) {
        super(new TutorialBiomeSource(registry), new StructureSettings(false));
//        super(new TutorialBiomeProvider(registry), new DimensionStructuresSettings(false));
        this.settings = settings;
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

        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                chunkAccess.setBlockState(pos.set(x, 0, z), bedrock, false);
            }
        }

        int baseHeight = settings.getBaseHeight();
        float verticalVariance = settings.getVerticalVariance();
        float horizontalVariance = settings.getHorizontalVariance();
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                int realx = chunkpos.x * 16 + x;
                int realz = chunkpos.z * 16 + z;
                int height = (int) (baseHeight + Math.sin(realx / horizontalVariance)*verticalVariance + Math.cos(realz / horizontalVariance)*verticalVariance);
                for (int y = 1 ; y < height ; y++) {
                    chunkAccess.setBlockState(pos.set(x, y, z), stone, false);
                }
            }
        }
        /**
         */
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
/*
        NoiseSettings noisesettings = ((Settings)this.settings.get()).noiseSettings();
        int i = Math.max(noisesettings.minY(), chunkAccess.getMinBuildHeight());
        int j = Math.min(noisesettings.minY() + noisesettings.height(), chunkAccess.getMaxBuildHeight());
        int k = Mth.intFloorDiv(i, this.cellHeight);
        int l = Mth.intFloorDiv(j - i, this.cellHeight);
        if (l <= 0) {
            return CompletableFuture.completedFuture(chunkAccess);
        } else {
            int i1 = chunkAccess.getSectionIndex(l * this.cellHeight - 1 + i);
            int j1 = chunkAccess.getSectionIndex(i);
            return CompletableFuture.supplyAsync(() -> {
                HashSet set = Sets.newHashSet();
                boolean var15 = false;

                ChunkAccess chunkaccess;
                LevelChunkSection levelchunksection1x;
                try {
                    var15 = true;
                    int k1 = i1;

                    while(true) {
                        if (k1 < j1) {
                            chunkaccess = this.doFill(p_158464_, chunkAccess, k, l);
                            var15 = false;
                            break;
                        }

                        levelchunksection1x = chunkAccess.getOrCreateSection(k1);
                        levelchunksection1x.acquire();
                        set.add(levelchunksection1x);
                        --k1;
                    }
                } finally {
                    if (var15) {
                        Iterator var12 = set.iterator();

                        while(var12.hasNext()) {
                            LevelChunkSection levelchunksection1 = (LevelChunkSection)var12.next();
                            levelchunksection1.release();
                        }

                    }
                }

                Iterator var17 = set.iterator();

                while(var17.hasNext()) {
                    levelchunksection1x = (LevelChunkSection)var17.next();
                    levelchunksection1x.release();
                }

                return chunkaccess;
            }, Util.backgroundExecutor());
        } /**/
    }
    private ChunkAccess doFill(StructureFeatureManager p_158428_, ChunkAccess p_158429_, int p_158430_, int p_158431_) {
        /*
        Heightmap heightmap = p_158429_.getOrCreateHeightmapUnprimed(Types.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = p_158429_.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE_WG);
        ChunkPos chunkpos = p_158429_.getPos();
        int i = chunkpos.getMinBlockX();
        int j = chunkpos.getMinBlockZ();
        Beardifier beardifier = new Beardifier(p_158428_, p_158429_);
        Aquifer aquifer = this.getAquifer(p_158430_, p_158431_, chunkpos);
        NoiseInterpolator noiseinterpolator = new NoiseInterpolator(this.cellCountX, p_158431_, this.cellCountZ, chunkpos, p_158430_, this::fillNoiseColumn);
        List<NoiseInterpolator> list = Lists.newArrayList(new NoiseInterpolator[]{noiseinterpolator});
        Objects.requireNonNull(list);
        Consumer<NoiseInterpolator> consumer = list::add;
        DoubleFunction<BaseStoneSource> doublefunction = this.createBaseStoneSource(p_158430_, chunkpos, consumer);
        DoubleFunction<NoiseModifier> doublefunction1 = this.createCaveNoiseModifier(p_158430_, chunkpos, consumer);
        list.forEach(NoiseInterpolator::initializeForFirstCellX);
        MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

        for(int k = 0; k < this.cellCountX; ++k) {
            list.forEach((p_158426_) -> {
                p_158426_.advanceCellX(k);
            });

            for(int i1 = 0; i1 < this.cellCountZ; ++i1) {
                LevelChunkSection levelchunksection = p_158429_.getOrCreateSection(p_158429_.getSectionsCount() - 1);

                for(int j1 = p_158431_ - 1; j1 >= 0; --j1) {
                    list.forEach((p_158412_) -> {
                        p_158412_.selectCellYZ(j1, i1);
                    });

                    for(int i2 = this.cellHeight - 1; i2 >= 0; --i2) {
                        int j2 = (p_158430_ + j1) * this.cellHeight + i2;
                        int k2 = j2 & 15;
                        int l2 = p_158429_.getSectionIndex(j2);
                        if (p_158429_.getSectionIndex(levelchunksection.bottomBlockY()) != l2) {
                            levelchunksection = p_158429_.getOrCreateSection(l2);
                        }

                        double d0 = (double)i2 / (double)this.cellHeight;
                        list.forEach((p_158476_) -> {
                            p_158476_.updateForY(d0);
                        });

                        for(int i3 = 0; i3 < this.cellWidth; ++i3) {
                            int j3 = i + k * this.cellWidth + i3;
                            int k3 = j3 & 15;
                            double d1 = (double)i3 / (double)this.cellWidth;
                            list.forEach((p_158390_) -> {
                                p_158390_.updateForX(d1);
                            });

                            for(int l3 = 0; l3 < this.cellWidth; ++l3) {
                                int i4 = j + i1 * this.cellWidth + l3;
                                int j4 = i4 & 15;
                                double d2 = (double)l3 / (double)this.cellWidth;
                                double d3 = noiseinterpolator.calculateValue(d2);
                                BlockState blockstate = this.updateNoiseAndGenerateBaseState(beardifier, aquifer, (BaseStoneSource)doublefunction.apply(d2), (NoiseModifier)doublefunction1.apply(d2), j3, j2, i4, d3);
                                if (blockstate != AIR) {
                                    if (blockstate.getLightEmission() != 0 && p_158429_ instanceof ProtoChunk) {
                                        blockpos$mutableblockpos.set(j3, j2, i4);
                                        ((ProtoChunk)p_158429_).addLight(blockpos$mutableblockpos);
                                    }

                                    levelchunksection.setBlockState(k3, k2, j4, blockstate, false);
                                    heightmap.update(k3, j2, j4, blockstate);
                                    heightmap1.update(k3, j2, j4, blockstate);
                                    if (aquifer.shouldScheduleFluidUpdate() && !blockstate.getFluidState().isEmpty()) {
                                        blockpos$mutableblockpos.set(j3, j2, i4);
                                        p_158429_.getLiquidTicks().scheduleTick(blockpos$mutableblockpos, blockstate.getFluidState().getType(), 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            list.forEach(NoiseInterpolator::swapSlices);
        }
/**/
        return p_158429_;
    }

    @Override
    public int getBaseHeight(int i, int i1, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor) {
        return this.settings.getBaseHeight();
    }

    @Override
    public NoiseColumn getBaseColumn(int p_158401_, int p_158402_, LevelHeightAccessor p_158403_) {
        /*
        int i = Math.max(((NoiseGeneratorSettings)this.settings.get()).noiseSettings().minY(), p_158403_.getMinBuildHeight());
        int j = Math.min(((NoiseGeneratorSettings)this.settings.get()).noiseSettings().minY() + ((NoiseGeneratorSettings)this.settings.get()).noiseSettings().height(), p_158403_.getMaxBuildHeight());
        int k = Mth.intFloorDiv(i, this.cellHeight);
        int l = Mth.intFloorDiv(j - i, this.cellHeight);
        if (l <= 0) {
            return new NoiseColumn(i, EMPTY_COLUMN);
        } else {
            BlockState[] ablockstate = new BlockState[l * this.cellHeight];
            this.iterateNoiseColumn(p_158401_, p_158402_, ablockstate, (Predicate)null, k, l);
            return new NoiseColumn(i, ablockstate);
        }   /**/
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
