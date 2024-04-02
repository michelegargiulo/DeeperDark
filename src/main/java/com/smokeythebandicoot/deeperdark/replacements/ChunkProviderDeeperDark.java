package com.smokeythebandicoot.deeperdark.replacements;

import com.google.common.collect.Lists;
import com.rwtema.extrautils2.dimensions.deep_dark.ChunkProviderDeepDark;
import com.smokeythebandicoot.deeperdark.config.ModConfig;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ChunkProviderDeeperDark extends ChunkProviderDeepDark {

    private final World world;
    Random random = new Random();
    GenerationLayer[] nextState;
    Biome[] biomes = null;
    int seedOffset = 0;
    private final List<MapGenStructure> structureGenerators = Lists.newArrayList();
    private final List<MapGenBase> generators = Lists.newArrayList();
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;



    // Floor
    int floorBedrockHeight;
    IBlockState bedrockFloorState;

    // Ground
    int groundEnd;
    int surfaceThickness;
    int surfaceRoughness;
    IBlockState groundState;
    IBlockState surfaceState;

    // Air
    int stalactiteMaxHeight;

    // Spire
    IBlockState spireReplacement;
    int spireInnerRadius;
    int spireOuterRadius;

    // Ceiling
    int ceilingStart;

    // Bedrock Ceiling
    int bedrockCeilingStart;
    int bedrockCeilingThickness;
    IBlockState bedrockCeilingReplacement;

    public ChunkProviderDeeperDark(World worldIn, long seed) {
        // Stuff
        super(worldIn, seed);
        this.world = worldIn;

        int length = GenerationLayer.values().length;
        this.nextState = new GenerationLayer[length - 1];
        System.arraycopy(GenerationLayer.values(), 1, this.nextState, 0, length - 1);

        // Structures
        if (ModConfig.StructureSpawn.enableScatteredFeatures)
            this.structureGenerators.add(new MapGenScatteredFeature());

        if (ModConfig.StructureSpawn.enableMineshafts)
            this.structureGenerators.add(new MapGenMineshaft());

        if (ModConfig.StructureSpawn.enableCaves)
            this.generators.add(TerrainGen.getModdedMapGen(new MapGenCaves(), InitMapGenEvent.EventType.CAVE));

        if (ModConfig.StructureSpawn.enableRavines)
            this.generators.add(TerrainGen.getModdedMapGen(new MapGenRavine(), InitMapGenEvent.EventType.RAVINE));

        if (ModConfig.StructureSpawn.enableWaterLakes)
            this.waterLakeGenerator = new WorldGenLakes(ModConfig.StructureSpawn.waterReplacement.parseToBlockstate().getBlock());

        if (ModConfig.StructureSpawn.enableLavaLakes)
            this.lavaLakeGenerator = new WorldGenLakes(ModConfig.StructureSpawn.lavaReplacement.parseToBlockstate().getBlock());

        // Floor config
        floorBedrockHeight = ModConfig.BedrockFloor.bedrockFloorMaxHeight;
        bedrockFloorState = ModConfig.BedrockFloor.floorBedrockReplacement.parseToBlockstate();

        // Ground
        groundEnd = ModConfig.Ground.groundFloorMin;
        surfaceThickness = ModConfig.Ground.groundFloorThickness;
        surfaceRoughness = ModConfig.Ground.surfaceRoughness;
        groundState = ModConfig.Ground.groundReplacement.parseToBlockstate();
        surfaceState = ModConfig.Ground.surfaceReplacement.parseToBlockstate();

        // Air
        stalactiteMaxHeight = ModConfig.Air.stalactiteMaxHeight;

        // Spire
        spireInnerRadius = ModConfig.Spire.spireInnerRadius;
        spireOuterRadius = ModConfig.Spire.spireOuterRadius;
        spireReplacement = ModConfig.Spire.spireReplacement.parseToBlockstate();

        // Ceiling
        ceilingStart = ModConfig.Ceiling.ceilingStart;

        // Bedrock Ceiling
        bedrockCeilingStart = ModConfig.BedrockCeiling.bedrockCeilingStart;
        bedrockCeilingThickness = ModConfig.BedrockCeiling.bedrockCeilingThickness;
        bedrockCeilingReplacement = ModConfig.BedrockCeiling.bedrockCeilingReplacement.parseToBlockstate();

    }
    
    @Override
    @Nonnull
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.random.setSeed(this.world.getSeed() + (long)((x >> 2) * '\uffff') + (long)(z >> 2));
        int spire_x = (x >> 2) * 64 + 8 + this.random.nextInt(48) - x * 16;
        int spire_z = (z >> 2) * 64 + 8 + this.random.nextInt(48) - z * 16;
        this.random.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        GenerationLayer[] values = GenerationLayer.values();

        int dx;
        int dz;
        int dy;
        for(dx = 0; dx < 16; ++dx) {
            for(dz = 0; dz < 16; ++dz) {
                dy = (spire_x - dx) * (spire_x - dx) + (spire_z - dz) * (spire_z - dz);
                double spire_dist = dy < 256 ? Math.sqrt(dy) : Double.MAX_VALUE;
                GenerationLayer curState = GenerationLayer.FLOOR_BEDROCK;

                for(dy = 0; dy < 256; ++dy) {
                    IBlockState state = curState.state;
                    if (curState == GenerationLayer.AIR) {
                        int m = Math.min(dy - groundEnd, ceilingStart - dy);
                        double t = spire_dist;
                        if (m < 9) {
                            t = spire_dist - Math.sqrt(9 - m);
                        }

                        if (t <= spireInnerRadius || t <= spireOuterRadius && this.random.nextBoolean()) {
                            state = spireReplacement;
                        }
                    }

                    if (dy >= ModConfig.BedrockCeiling.bedrockCeilingStart) {
                        state = bedrockCeilingReplacement;
                    }

                    chunkprimer.setBlockState(dx, dy, dz, state);
                    boolean advance;
                    switch (curState) {
                        case FLOOR_BEDROCK:
                            advance = dy > floorBedrockHeight || dy > 0 && this.random.nextBoolean();
                            break;
                        case GROUND:
                            advance = dy >= groundEnd + surfaceThickness || dy >= groundEnd && this.random.nextInt(surfaceRoughness) != 0;
                            break;
                        case AIR:
                            advance = dy >= ceilingStart - stalactiteMaxHeight && (dy >= ceilingStart || this.random.nextInt(1 + 2 * (ceilingStart - dy) * (ceilingStart - dy)) == 0);
                            break;
                        case CEILING:
                            advance = dy >= ceilingStart && this.random.nextInt(40) == 0;
                            break;
                        case CEILING_STONE:
                            advance = dy >= bedrockCeilingStart || ((dy >= bedrockCeilingStart - bedrockCeilingThickness + 1) && random.nextBoolean());
                            break;
                        case CEILING_BEDROCK:
                            advance = false;
                            break;
                        default:
                            throw new RuntimeException("Invalid State " + curState);
                    }

                    if (advance) {
                        curState = values[curState.ordinal() + 1];
                    }
                }
            }
        }

        Iterator<MapGenBase> generatorIterator = this.generators.iterator();

        MapGenBase mapgenbase;
        while (generatorIterator.hasNext()) {
            mapgenbase = (MapGenBase)generatorIterator.next();
            mapgenbase.generate(this.world, x, z, chunkprimer);
        }

        for (MapGenStructure structureGenerator : this.structureGenerators) {
            mapgenbase = structureGenerator;
            mapgenbase.generate(this.world, x, z, chunkprimer);
        }

        for(dx = 0; dx < 16; ++dx) {
            for(dz = 0; dz < 16; ++dz) {
                for(dy = groundEnd; dy <= groundEnd + surfaceThickness; ++dy) {
                    if (chunkprimer.getBlockState(dx, dy, dz) == ModConfig.Ground.groundReplacement.parseToBlockstate()) {
                        chunkprimer.setBlockState(dx, dy, dz, ModConfig.Ground.surfaceReplacement.parseToBlockstate());
                    }
                }
            }
        }

        // Biome Overrides
        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

        this.biomes = this.world.getBiomeProvider().getBiomesForGeneration(this.biomes, x * 16, z * 16, 16, 16);
        byte[] biomeIDs = chunk.getBiomeArray();

        for(dy = 0; dy < biomeIDs.length; ++dy) {
            biomeIDs[dy] = (byte) Biome.getIdForBiome(this.biomes[dy]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;

        // Chunk coords
        int chunkX = x * 16;
        int chunkZ = z * 16;
        BlockPos blockpos = new BlockPos(chunkX, 0, chunkZ);

        // Original version uses divide by 2 then mult by 2 + 1
        // This should be much faster
        this.random.setSeed(this.world.getSeed());
        long k = this.random.nextLong();
        k += (k % 2) + 1;

        long l = this.random.nextLong();
        l += (l % 2) + 1;

        this.random.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());


        ChunkPos chunkpos = new ChunkPos(x, z);
        ForgeEventFactory.onChunkPopulate(true, this, this.world, this.random, x, z, false);

        Biome biome = this.world.getBiomeForCoordsBody(new BlockPos(chunkX + 16, 0, chunkZ + 16));
        biome.decorate(this.world, this.random, new BlockPos(chunkX, 0, chunkZ));

        boolean villageHasGenerated = false;
        for (MapGenStructure mapgenstructure : this.structureGenerators) {
            boolean structureHasGenerated = mapgenstructure.generateStructure(this.world, this.random, chunkpos);
            if (mapgenstructure instanceof MapGenVillage) {
                villageHasGenerated |= structureHasGenerated;
            }
        }

        if (this.waterLakeGenerator != null && !villageHasGenerated && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.world, this.random, blockpos.add(
                    this.random.nextInt(16) + 8,
                    this.random.nextInt(ModConfig.StructureSpawn.waterLakesMaxHeight - ModConfig.StructureSpawn.waterLakesMinHeight) + ModConfig.StructureSpawn.waterLakesMinHeight,
                    this.random.nextInt(16) + 8));
        }

        if (this.lavaLakeGenerator != null && !villageHasGenerated && this.random.nextInt(8) == 0) {
            BlockPos lakePos = blockpos.add(
                    this.random.nextInt(16) + 8,
                    this.random.nextInt(ModConfig.StructureSpawn.lavaLakesMaxHeight - ModConfig.StructureSpawn.lavaLakesMinHeight) + ModConfig.StructureSpawn.lavaLakesMinHeight,
                    this.random.nextInt(16) + 8);
            if (lakePos.getY() < groundEnd || this.random.nextInt(10) == 0) {
                this.lavaLakeGenerator.generate(this.world, this.random, lakePos);
            }
        }

        if (ModConfig.StructureSpawn.enableDungeons)
            for(int cnt = 0; cnt < 8; ++cnt) {
                WorldGenDungeons dungeon = new WorldGenDungeons();
                dungeon.generate(this.world, this.random, blockpos.add(
                        this.random.nextInt(16) + 8,
                        this.random.nextInt(256),
                        this.random.nextInt(16) + 8)
                );
            }

        if (this.seedOffset == 0) {
            this.seedOffset = 1;
            biome.decorate(this.world, this.random, blockpos);
            ForgeEventFactory.onChunkPopulate(false, this, this.world, this.random, x, z, villageHasGenerated);
            BlockFalling.fallInstantly = false;
            GameRegistry.generateWorld(x, z, this.world, this, this.world.getChunkProvider());
            this.seedOffset = 0;
        }
    }

    enum GenerationLayer {
        FLOOR_BEDROCK(ModConfig.BedrockFloor.floorBedrockReplacement.parseToBlockstate()),
        GROUND(ModConfig.Ground.groundReplacement.parseToBlockstate()),
        AIR(ModConfig.Air.airReplacement.parseToBlockstate()),
        CEILING(ModConfig.Ceiling.ceilingSurfaceReplacement.parseToBlockstate()),
        CEILING_STONE(ModConfig.Ceiling.ceilingReplacement.parseToBlockstate()),
        CEILING_BEDROCK(ModConfig.BedrockCeiling.bedrockCeilingReplacement.parseToBlockstate());

        final IBlockState state;

        GenerationLayer(IBlockState state) {
            this.state = state;
        }
    }
}
