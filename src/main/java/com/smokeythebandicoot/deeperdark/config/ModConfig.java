package com.smokeythebandicoot.deeperdark.config;

import com.smokeythebandicoot.deeperdark.DeeperDark;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


@Config(modid = DeeperDark.MODID, name = "deeperdark")
@Mod.EventBusSubscriber(modid = DeeperDark.MODID)
public class ModConfig {

    @Config.Comment("Configuration about this mod")
    @Config.Name("Mod Configuration")
    @Config.LangKey("config.main.mod_configuration")
    public static ModConfiguration modConfiguration;

    @Config.Comment("Configuration about the bedrock floor")
    @Config.Name("Bedrock Floor Configuration")
    @Config.LangKey("config.main.bedrock_floor")
    public static BedrockFloor bedrockFloorConfig;

    @Config.Comment("Configuration about the ground")
    @Config.Name("Ground Configuration")
    @Config.LangKey("config.main.ground")
    public static Ground groundConfig;

    @Config.Comment("Configuration about the air layer")
    @Config.Name("Air Layer Configuration")
    @Config.LangKey("config.main.air_layer")
    public static Air airConfig;

    @Config.Comment("Configuration about the spires")
    @Config.Name("Spires Configuration")
    @Config.LangKey("config.main.spires")
    public static Spire spireConfig;

    @Config.Comment("Configuration about the ceiling")
    @Config.Name("Ceiling Configuration")
    @Config.LangKey("config.main.ceiling")
    public static Ceiling ceilingConfig;

    @Config.Comment("Configuration about the bedrock ceiling")
    @Config.Name("Bedrock Ceiling Configuration")
    @Config.LangKey("config.main.bedrock_ceiling")
    public static BedrockCeiling bedrockCeilingConfig;

    @Config.Comment("Configuration about structures")
    @Config.Name("Structure Configuration")
    @Config.LangKey("config.main.structure")
    public static StructureSpawn structureConfig;



    public static class ModConfiguration {
        @Config.Comment("Set to false to completely disable this mod. Useful to test things without losing configuration")
        @Config.RequiresMcRestart
        @Config.LangKey("config.modconfig.main")
        public static boolean masterSwitch = true;
    }

    public static class BedrockFloor {
        @Config.Comment("The max y-height of the bedrock floor")
        @Config.RangeInt(min = 1, max = 4)
        @Config.RequiresMcRestart
        @Config.LangKey("config.bedrock_floor.max_height")
        public static int bedrockFloorMaxHeight = 4;

        @Config.Comment("Replace the CEILING BEDROCK blockstate with the following block. Not recommended to change")
        @Config.RequiresMcRestart
        @Config.LangKey("config.bedrock_floor.replacement")
        public static ConfigBlockState floorBedrockReplacement = new ConfigBlockState("minecraft:bedrock");
    }

    public static class Ground {

        @Config.Comment("Ground min height. Everything from the bottom bedrock to this height is ground.\nIn vanilla XU " +
                "this represents the stone that fills the bottom part of the dimension")
        @Config.RangeInt(min = 2, max = 240)
        @Config.RequiresMcRestart
        @Config.LangKey("config.ground.height")
        public static int groundFloorMin = 62;

        @Config.Comment("Ground max height. In vanilla XU this represents the surface of the bottom part of the dimension.\n" +
                "Total floor height is groundFloorMin + groundFloorThickness")
        @Config.RangeInt(min = 1, max = 64)
        @Config.RequiresMcRestart
        @Config.LangKey("config.ground.thickness")
        public static int groundFloorThickness = 3;

        @Config.Comment("Replace the GROUND blockstate with the following block")
        @Config.RequiresMcRestart
        @Config.LangKey("config.ground.replacement")
        public static ConfigBlockState groundReplacement = new ConfigBlockState("minecraft:stone");

        @Config.Comment("Replace the SURFACE blockstate with the following block")
        @Config.RequiresMcRestart
        @Config.LangKey("config.ground.surface_replacement")
        public static ConfigBlockState surfaceReplacement = new ConfigBlockState("minecraft:cobblestone");

        @Config.Comment("How rough the bottom surface is. This number N represents a chance of 1 in N to place a block on the surface.\n" +
                "At around 50% (so 1 in 2) the roughness is max")
        @Config.RangeInt(min = 1)
        @Config.RequiresMcRestart
        @Config.LangKey("config.ground.roughness")
        public static int surfaceRoughness = 4;
    }

    public static class Air {
        @Config.Comment("Sets the maximum height of the stalactites. The bottom-most block of all stalactites can only be " +
                "as low as ceilingHeight minus this value")
        @Config.RequiresMcRestart
        @Config.LangKey("config.air.max_height")
        public static int stalactiteMaxHeight = 30;

        @Config.Comment("Replace the AIR blockstate with the following block. Changing this is NOT recommended")
        @Config.RequiresMcRestart
        @Config.LangKey("config.air.replacement")
        public static ConfigBlockState airReplacement = new ConfigBlockState("minecraft:air");
    }

    public static class Spire {

        @Config.Comment("The minimum radius of each spire. All blocks inside this radius are placed. Too big values untested" +
                "and unrecommended, try to keep this below 10")
        @Config.RangeInt(min = 1, max = 16)
        @Config.RequiresMcRestart
        @Config.LangKey("config.spires.inner_radius")
        public static int spireInnerRadius = 4;

        @Config.Comment("The maximum radius of each spire. All outside the inner radius but inside the outer ones, have a " +
                "random chance to be placed, giving spired that jagged look. Setting this as the same of innerRadius smooths them out")
        @Config.RangeInt(min = 1, max = 16)
        @Config.RequiresMcRestart
        @Config.LangKey("config.spires.outer_radius")
        public static int spireOuterRadius = 5;

        @Config.Comment("Replace the SPIRES blockstate with the following block")
        @Config.RequiresMcRestart
        @Config.LangKey("config.spires.replacement")
        public static ConfigBlockState spireReplacement = new ConfigBlockState("minecraft:cobblestone");
    }

    public static class Ceiling {
        @Config.Comment("The y level where the ceiling starts. Setting it higher than 175 might spawn the initial room floating in mid-air")
        @Config.RequiresMcRestart
        @Config.LangKey("config.ceiling.height")
        public static int ceilingStart = 120;

        @Config.Comment("Replace the CEILING SURFACE block state with the following block")
        @Config.RequiresMcRestart
        @Config.LangKey("config.ceiling.surface_replacement")
        public static ConfigBlockState ceilingSurfaceReplacement = new ConfigBlockState("minecraft:cobblestone");

        @Config.Comment("Replace the CEILING block state with the following block")
        @Config.RequiresMcRestart
        @Config.LangKey("config.ceiling.replacement")
        public static ConfigBlockState ceilingReplacement = new ConfigBlockState("minecraft:stone");
    }

    public static class BedrockCeiling {
        @Config.Comment("The y-height where the bedrock ceiling starts. Suggested value: 255 (only 1 top bedrock layer)")
        @Config.RangeInt(min = 160, max = 255)
        @Config.RequiresMcRestart
        @Config.LangKey("config.bedrock_ceiling.height")
        public static int bedrockCeilingStart = 253;

        @Config.Comment("If this value is greater than 1, the bedrock ceiling won't be flat, but it will be rough as the " +
                " default overworld bedrock floor. Roughness will start at bedrockCeilingStart minus this value. Suggested value: 3")
        @Config.RangeInt(min = 1, max = 5)
        @Config.RequiresMcRestart
        @Config.LangKey("config.bedrock_ceiling.thickness")
        public static int bedrockCeilingThickness = 1;

        @Config.Comment("Replace the FLOOR BEDROCK blockstate with the following block. Not recommended to change")
        @Config.RequiresMcRestart
        @Config.LangKey("config.bedrock_ceiling.replacement")
        public static ConfigBlockState bedrockCeilingReplacement = new ConfigBlockState("minecraft:bedrock");
    }

    public static class StructureSpawn {

        @Config.Comment("If false, Scattered Features won't generate (might be temples?)")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.scattered_features")
        public static boolean enableScatteredFeatures = true;

        @Config.Comment("If false, Mineshafts won't generate")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.mineshafts")
        public static boolean enableMineshafts = true;

        @Config.Comment("If false, Caves won't generate")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.caves")
        public static boolean enableCaves = true;

        @Config.Comment("If false, Ravines won't generate")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.ravines")
        public static boolean enableRavines = true;

        @Config.Comment("If false, Dungeons won't generate")
        @Config.LangKey("config.structures.dungeons")
        public static boolean enableDungeons = true;

        @Config.Comment("If false, Water lakes won't generate")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.water_lakes")
        public static boolean enableWaterLakes = true;

        @Config.Comment("Water Lakes minimum generation height. XU default is 1")
        @Config.LangKey("config.structures.water_lakes_min_height")
        public static int waterLakesMinHeight = 1;

        @Config.Comment("Water Lakes minimum generation height. XU default is 256. Recommended to lower it at least to <250 to avoid holes in the bedrock ceiling")
        @Config.LangKey("config.structures.water_lakes_max_height")
        public static int waterLakesMaxHeight = 256;

        @Config.Comment("Block replacement for water in water lakes. Ignores Meta! Generates some stone and cobblestone blocks.")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.water_replacement")
        public static ConfigBlockState waterReplacement = new ConfigBlockState("minecraft:water", 0);

        @Config.Comment("If false, Lava lakes won't generate")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.lava_lakes")
        public static boolean enableLavaLakes = true;

        @Config.Comment("Lava Lakes minimum generation height. XU default is 1")
        @Config.LangKey("config.structures.lava_lakes_min_height")
        public static int lavaLakesMinHeight = 1;

        @Config.Comment("Lava Lakes minimum generation height. XU default is 256. Recommended to lower it at least to <250 to avoid holes in the bedrock ceiling")
        @Config.LangKey("config.structures.lava_lakes_max_height")
        public static int lavaLakesMaxHeight = 256;

        @Config.Comment("Block replacement for lava in lava lakes. Ignores Meta! Generates some stone, cobblestone and water blocks.")
        @Config.RequiresMcRestart
        @Config.LangKey("config.structures.lava_replacement")
        public static ConfigBlockState lavaReplacement = new ConfigBlockState("minecraft:lava", 0);
    }


    @Mod.EventBusSubscriber(modid = DeeperDark.MODID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(DeeperDark.MODID)) {
                ConfigManager.sync(DeeperDark.MODID, Config.Type.INSTANCE);
            }
        }
    }

    public static class ConfigBlockState {

        @Config.Comment("The registry name of the block")
        @Config.LangKey("config.blockstate.registry_name")
        public String registryName;

        @Config.Comment("The meta of the blocks")
        @Config.LangKey("config.blockstate.meta")
        public int meta;

        public IBlockState parseToBlockstate() {
            ResourceLocation resourceLocation = new ResourceLocation(registryName);
            if (!ForgeRegistries.BLOCKS.containsKey(resourceLocation)) return null;
            return ForgeRegistries.BLOCKS.getValue(resourceLocation).getStateFromMeta(meta);
        }

        public ConfigBlockState(String registryName, int meta) {
            this.registryName = registryName;
            this.meta = meta;
        }

        public ConfigBlockState(String registryName) {
            this.registryName = registryName;
            this.meta = 0;
        }
    }
}
