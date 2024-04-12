package me.srrapero720.embeddiumplus;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.blaze3d.platform.Window;
import me.srrapero720.embeddiumplus.mixins.impl.borderless.accessors.MainWindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmbyConfig {
    public static final Marker IT = MarkerManager.getMarker("Config");
    public static final ForgeConfigSpec SPECS;

    // GENERAL
    public static final EnumValue<FullScreenMode> fullScreen;
    public static final EnumValue<FPSDisplayMode> fpsDisplayMode;
    public static final EnumValue<FPSDisplayGravity> fpsDisplayGravity;
    public static final EnumValue<FPSDisplaySystemMode> fpsDisplaySystemMode;
    public static final IntValue fpsDisplayMargin;
    public static final BooleanValue fpsDisplayShadow;
    public static int fpsDisplayMarginCache;
    public static boolean fpsDisplayShadowCache;

    // QUALITY
    public static final BooleanValue fog;
    public static final BooleanValue blueBand;
    public static final IntValue cloudsHeight;
    public static final BooleanValue disableNameTagRender;
    public static final EnumValue<ChunkFadeSpeed> chunkFadeSpeed;
    public static boolean fogCache;
    public static boolean blueBandCache;
    public static int cloudsHeightCache;
    public static boolean disableNameTagRenderCache;

    // QUALITY: TRUE DARKNESS
    public static final EnumValue<DarknessMode> darknessMode;
    public static final BooleanValue darknessOnOverworld;
    public static final BooleanValue darknessOnNether;
    public static final DoubleValue darknessNetherFogBright;
    public static final BooleanValue darknessOnEnd;
    public static final DoubleValue darknessEndFogBright;
    public static final BooleanValue darknessByDefault;
    public static final ConfigValue<List<? extends String>> darknessDimensionWhiteList;
    public static final BooleanValue darknessOnNoSkyLight;
    public static final BooleanValue darknessBlockLightOnly;
    public static final BooleanValue darknessAffectedByMoonPhase;
    public static final DoubleValue darknessNewMoonBright;
    public static final DoubleValue darknessFullMoonBright;
    public static boolean darknessOnOverworldCache;
    public static boolean darknessOnNetherCache;
    public static double darknessNetherFogBrightCache;
    public static boolean darknessOnEndCache;
    public static double darknessEndFogBrightCache;
    public static boolean darknessByDefaultCache;
    public static boolean darknessOnNoSkyLightCache;
    public static boolean darknessBlockLightOnlyCache;
    public static boolean darknessAffectedByMoonPhaseCache;
    public static double darknessNewMoonBrightCache;
    public static double darknessFullMoonBrightCache;

    // PERFORMANCE;
    public static final BooleanValue hideJREMI;
    public static final BooleanValue fontShadows;
    public static final BooleanValue fastChests;
    public static final BooleanValue fastBeds;
    public static boolean hideJREMICache;
    public static boolean fontShadowsCache;
    public static boolean fastChestsCache;
    public static boolean fastBedsCache;

    public static final BooleanValue tileEntityDistanceCulling;
    public static final IntValue tileEntityCullingDistanceX;
    public static final IntValue tileEntityCullingDistanceY;
    public static final BooleanValue entityDistanceCulling;
    public static final IntValue entityCullingDistanceX;
    public static final IntValue entityCullingDistanceY;
    public static final ConfigValue<List<? extends String>> entityWhitelist; // QUICK CHECK
    public static final ConfigValue<List<? extends String>> tileEntityWhitelist; // QUICK CHECK
    public static boolean tileEntityDistanceCullingCache;
    public static int tileEntityCullingDistanceXCache;
    public static int tileEntityCullingDistanceYCache;
    public static boolean entityDistanceCullingCache;
    public static int entityCullingDistanceXCache;
    public static int entityCullingDistanceYCache;

    // OTHERS
    public static final EnumValue<AttachMode> borderlessAttachModeF11;
    public static final BooleanValue fastLanguageReload;
    public static boolean fastLanguageReloadCache; // this theoretically wasn't needed,
    // but I am seeing people complaining why fast language reload wasn't cached
    // and takes 0.0001ms extra to update lang by an idiotic check

    // DYN LIGHTS
    public static final EnumValue<DynLightsSpeed> dynLightSpeed;
    public static final BooleanValue dynLightsOnEntities;
    public static final BooleanValue dynLightsOnTileEntities;
    public static final BooleanValue dynLightsUpdateOnPositionChange;
    public static volatile boolean dynLightsOnEntitiesCache;
    public static volatile boolean dynLightsOnTileEntitiesCache;
    public static volatile boolean dynLightsUpdateOnPositionChangeCache;

    private static final String[] DEFAULT_TILE_ENTITIES_WHITELIST = new String[] {
            "waterframes:*", // WaterFrames includes their own code
    };

    private static final String[] DEFAULT_ENTITIES_WHITELIST = new String[] {
            "minecraft:ghast",
            "minecraft:ender_dragon",
            "iceandfire:*",
            "create:*",
    };

    static {
        var BUILDER = new Builder();

        // embeddiumplus ->
        BUILDER.push("embeddiumplus");

        // embeddiumplus -> general ->
        BUILDER.push("general");
        fullScreen = BUILDER
                .comment("Set Fullscreen mode", "Borderless let you change between screens more faster and move your mouse across monitors")
                .defineEnum("fullscreen", FullScreenMode.WINDOWED);

        fpsDisplayMode = BUILDER
                .comment("Configure FPS Display mode", "Complete mode gives you min FPS count and average count")
                .defineEnum("fpsDisplay", FPSDisplayMode.ADVANCED);

        fpsDisplayGravity = BUILDER
                .comment("Configure FPS Display gravity", "Places counter on specified corner of your screen")
                .defineEnum("fpsDisplayGravity", FPSDisplayGravity.LEFT);

        fpsDisplaySystemMode = BUILDER
                .comment("Shows GPU and memory usage onto FPS display")
                .defineEnum("fpsDisplaySystem", FPSDisplaySystemMode.OFF);

        fpsDisplayMargin = BUILDER
                .comment("Configure FPS Display margin", "Give some space between corner and text")
                .defineInRange("fpsDisplayMargin", 12, 0, 48);

        fpsDisplayShadow = BUILDER
                .comment("Toggle FPS Display shadow", "In case sometimes you can't see the text")
                .define("fpsDisplayShadow", false);

        // embeddiumplus ->
        BUILDER.pop();

        // embeddiumplus -> quality
        BUILDER.push("quality");
        fog = BUILDER
                .comment("Toggle fog feature", "Fog was a vanilla feature")
                .define("fog", true);
        blueBand = BUILDER
                .comment("Clean my skies", "Blue band was a vanilla feature, toggle off will show sky color directly")
                .define("blueBand", true);
        cloudsHeight = BUILDER
                .comment("Raise clouds", "Modify clouds height perfect for a adaptative world experience")
                .defineInRange("cloudsHeight", 192, 0, 512);

        disableNameTagRender = BUILDER
                .comment("Do not show me your name", "disables nametag rendering for players and entities")
                .define("disableNameTagRendering", false);

        chunkFadeSpeed = BUILDER
                .comment("Chunks fade in speed", "This option doesn't affect performance, just changes speed")
                .defineEnum("chunkFadeSpeed", ChunkFadeSpeed.SLOW);

        // embeddiumplus -> quality -> darkness
        BUILDER.push("darkness");
        darknessMode = BUILDER
                .comment("Configure Darkness Mode", "Each config changes what is considered 'true darkness'")
                .defineEnum("mode", DarknessMode.OFF);

        darknessOnOverworld = BUILDER
                .comment("Toggle Darkness on Overworld dimension")
                .define("enableOnOverworld", true);

        darknessOnNether = BUILDER
                .comment("Toggle Darkness on Nether dimension")
                .define("enableOnNether", false);

        darknessNetherFogBright = BUILDER
                .comment("Configure fog brightness on nether when darkness is enabled")
                .defineInRange("netherFogBright", 0.5f, 0d, 1d);

        darknessOnEnd = BUILDER
                .comment("Toggle Darkness on End dimension")
                .define("enableOnEnd", false);

        darknessEndFogBright = BUILDER
                .comment("Configure fog brightness on nether when darkness is enabled")
                .defineInRange("endFogBright", 0.5f, 0d, 1d);

        darknessByDefault = BUILDER
                .comment("Toggle Darkness default mode for modded dimensions")
                .define("valueByDefault", false);

        darknessDimensionWhiteList = BUILDER
                .comment("List of all dimensions to use True Darkness", "This option overrides 'valueByDefault' state")
                .defineListAllowEmpty(Collections.singletonList("dimensionWhitelist"), Collections::emptyList, s -> s.toString().contains(":"));

        darknessOnNoSkyLight = BUILDER
                .comment("Toggle darkness when dimension has no SkyLight")
                .define("enableOnNoSkyLight", false);

        darknessBlockLightOnly = BUILDER
                .comment("Disables all bright sources of darkness like moon or fog", "Only affects darkness effect")
                .define("enableBlockLightOnly", false);

        darknessAffectedByMoonPhase = BUILDER
                .comment("Toggles if moon phases affects darkness in the overworld")
                .define("affectedByMoonPhase", true);

        darknessFullMoonBright = BUILDER
                .comment("Configure max moon brightness level with darkness")
                .defineInRange("fullMoonBright",0.25d, 0, 1d);

        darknessNewMoonBright = BUILDER
                .comment("Configure min moon brightness level with darkness")
                .defineInRange("newMoonBright",0, 0, 1d);


        // embeddiumplus ->
        BUILDER.pop(2);

        // embeddiumplus -> performance
        BUILDER.push("performance");

        hideJREMI = BUILDER
                .comment("Toggles JREI item rendering until searching", "Increases performance a little bit and cleans your screen when you don't want to use it")
                .define("hideJREI", false);

        fontShadows = BUILDER
                .comment("Toggles Minecraft Fonts shadows", "Depending of the case may increase performance", "Gives a flat style text")
                .define("fontShadows", true);

        fastChests = BUILDER
                .comment("Toggles FastChest feature", "Without flywheel installed or using any backend, it increases FPS significatly on chest rooms")
                .define("fastChest", false);

        fastBeds = BUILDER
                .comment("Toggles FastBeds feature")
                .define("fastBed", false);

        // embeddiumplus -> performance -> distanceCulling
        BUILDER.push("distanceCulling");

        // embeddiumplus -> performance -> distanceCulling -> tileEntities
        BUILDER.push("tileEntities");
        tileEntityDistanceCulling = BUILDER
                .comment("Toggles distance culling for Block Entities", "Maybe you use another mod for that :(")
                .define("enable", true);

        tileEntityCullingDistanceX = BUILDER
                .comment("Configure horizontal max distance before cull Block entities", "Value is squared, default was 64^2 (or 64x64)")
                .defineInRange("cullingMaxDistanceX", 4096, 0, Integer.MAX_VALUE);

        tileEntityCullingDistanceY = BUILDER
                .comment("Configure vertical max distance before cull Block entities", "Value is raw")
                .defineInRange("cullingMaxDistanceY", 32, 0, 512);

        tileEntityWhitelist = BUILDER
                .comment("List of all Block Entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:chest\" - Ignores chests only", "Example 2: \"ae2:*\" - ignores all Block entities from Applied Energetics 2")
                .defineListAllowEmpty(Collections.singletonList("whitelist"), Arrays.asList(DEFAULT_TILE_ENTITIES_WHITELIST), s -> s.toString().contains(":"));

        // embeddiumplus -> performance -> distanceCulling ->
        BUILDER.pop();

        // embeddiumplus -> performance -> distanceCulling -> entities
        BUILDER.push("entities");
        entityDistanceCulling = BUILDER
                .comment("Toggles distance culling for entities", "Maybe you use another mod for that :(")
                .define("enable", true);
        entityCullingDistanceX = BUILDER
                .comment("Configure horizontal max distance before cull entities", "Value is squared, default was 64^2 (or 64x64)")
                .defineInRange("cullingMaxDistanceX", 4096, 0, Integer.MAX_VALUE);

        entityCullingDistanceY = BUILDER
                .comment("Configure vertical max distance before cull entities", "Value is raw")
                .defineInRange("cullingMaxDistanceY", 32, 0, 512);

        entityWhitelist = BUILDER
                .comment("List of all Entities to be ignored by distance culling", "Uses ResourceLocation to identify it", "Example 1: \"minecraft:bat\" - Ignores bats only", "Example 2: \"alexsmobs:*\" - ignores all entities for alexmobs mod")
                .defineListAllowEmpty(Collections.singletonList("whitelist"), Arrays.asList(DEFAULT_ENTITIES_WHITELIST), (s) -> s.toString().contains(":"));

        // embeddiumplus ->
        BUILDER.pop(3);

        // embeddiumplus -> others
        BUILDER.push("others");
        borderlessAttachModeF11 = BUILDER
                .comment("Configure if borderless fullscreen option should be attached to F11 or replace vanilla fullscreen")
                .defineEnum("borderlessAttachModeOnF11", AttachMode.ATTACH);
        fastLanguageReload = BUILDER
                .comment("Toggles fast language reload", "Embeddedt points it maybe cause troubles to JEI, so ¿why not add it as a toggleable option?")
                .define("fastLanguageReload", true);

        BUILDER.pop();

        // embeddiumplus -> dynlights
        BUILDER.push("dynlights");
        dynLightSpeed = BUILDER
                .comment("Configure how fast light whould be updated")
                .defineEnum("updateSpeed", DynLightsSpeed.REALTIME);

        dynLightsOnEntities = BUILDER
                .comment("Toggle if Entities should have dynamic lights")
                .define("onEntities", true);

        dynLightsOnTileEntities = BUILDER
                .comment("Toggle if Block Entities should have dynamic lights")
                .define("onTileEntities", true);

        dynLightsUpdateOnPositionChange = BUILDER
                .define("updateOnlyOnPositionChange", true);

        // embeddiumplus ->
        BUILDER.pop();

        SPECS = BUILDER.build();
    }

    public static boolean isLoaded() {
        return SPECS.isLoaded();
    }

    public static void load() {
        if (isLoaded()) return;
        LOGGER.warn(IT, "Loading Embeddium++Config");

        // FORCE LOAD
        var path = FMLPaths.CONFIGDIR.get().resolve("embeddium++.toml");
        try {
            final var configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

            configData.load();
            SPECS.setConfig(configData);
            updateCache(null);
        } catch (Exception e) {
            var file = path.toFile();
            if (!file.exists()) throw new RuntimeException("Failed to read configuration file");
            if (!file.delete()) throw new RuntimeException("Failed to remove corrupted configuration file");
            load();
        }
    }

    @SubscribeEvent
    public static void updateCache(ModConfigEvent ignored) {
        LOGGER.info(IT,"Updating config cache");

        fpsDisplayMarginCache = fpsDisplayMargin.get();
        fpsDisplayShadowCache = fpsDisplayShadow.get();

        fogCache = fog.get();
        blueBandCache = blueBand.get();
        cloudsHeightCache = cloudsHeight.get();
        disableNameTagRenderCache = disableNameTagRender.get();

        darknessOnOverworldCache = darknessOnOverworld.get();
        darknessOnNetherCache = darknessOnNether.get();
        darknessNetherFogBrightCache = darknessNetherFogBright.get();
        darknessOnEndCache = darknessOnEnd.get();
        darknessEndFogBrightCache = darknessEndFogBright.get();
        darknessByDefaultCache = darknessByDefault.get();
        darknessOnNoSkyLightCache = darknessOnNoSkyLight.get();
        darknessBlockLightOnlyCache = darknessBlockLightOnly.get();
        darknessAffectedByMoonPhaseCache = darknessAffectedByMoonPhase.get();
        darknessNewMoonBrightCache = darknessNewMoonBright.get();
        darknessFullMoonBrightCache = darknessFullMoonBright.get();

        hideJREMICache = hideJREMI.get();
        fontShadowsCache = fontShadows.get();
        fastChestsCache = fastChests.get();
        fastBedsCache = fastBeds.get();

        tileEntityDistanceCullingCache = tileEntityDistanceCulling.get();
        tileEntityCullingDistanceXCache = tileEntityCullingDistanceX.get();
        tileEntityCullingDistanceYCache = tileEntityCullingDistanceY.get();
        entityDistanceCullingCache = entityDistanceCulling.get();
        entityCullingDistanceXCache = entityCullingDistanceX.get();
        entityCullingDistanceYCache = entityCullingDistanceY.get();

        fastLanguageReloadCache = fastLanguageReload.get();

        dynLightsOnEntitiesCache = dynLightsOnEntities.get();
        dynLightsOnTileEntitiesCache = dynLightsOnTileEntities.get();
        dynLightsUpdateOnPositionChangeCache = dynLightsUpdateOnPositionChange.get();

        LOGGER.info(IT,"Cache updated successfully");
    }

    public static void setFullScreenMode(Options opts, FullScreenMode value) {
        fullScreen.set(value);
        opts.fullscreen.set(value != FullScreenMode.WINDOWED);

        Minecraft client = Minecraft.getInstance();
        Window window = client.getWindow();

        if (window.isFullscreen() != opts.fullscreen.get()) {
            window.toggleFullScreen();
            opts.fullscreen.set(window.isFullscreen());
        }

        if (opts.fullscreen.get()) {
            ((MainWindowAccessor) (Object) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }

    public enum AttachMode {
        ATTACH, REPLACE, OFF;
    }

    /* CONFIG VALUES */
    public enum FPSDisplayMode {
        OFF, SIMPLE, ADVANCED;

        public boolean off() {
            return this == OFF;
        }
    }
    public enum FPSDisplayGravity { LEFT, CENTER, RIGHT; }
    public enum ChunkFadeSpeed { OFF, FAST, SLOW; }
    public enum FPSDisplaySystemMode {
        OFF, ON, GPU, RAM;

        public boolean ram() { return this == RAM || this == ON; }
        public boolean gpu() { return this == GPU || this == ON; }
        public boolean off() { return this == OFF; }
    }
    public enum DynLightsSpeed {
        OFF(-1),
        SLOW(750),
        NORMAL(500),
        FAST(250),
        SUPERFAST(100),
        FASTESTS(50),
        REALTIME(-1);
        private final int delay;

        DynLightsSpeed(int delay) {
            this.delay = delay;
        }
        public int getDelay() { return delay; }

        public boolean off() {
            return this == OFF;
        }
    }
    public enum DarknessMode {
        TOTAL_DARKNESS(0.04f),
        PITCH_BLACK(0f),
        DARK(0.08f),
        DIM(0.12f),
        OFF(-1);

        public final float value;
        DarknessMode(float value) { this.value = value; }
    }
    public enum FullScreenMode {
        WINDOWED, BORDERLESS, FULLSCREEN;

        public static FullScreenMode nextOf(FullScreenMode current) {
            return switch (current) {
                case WINDOWED -> BORDERLESS;
                case BORDERLESS -> FULLSCREEN;
                case FULLSCREEN -> WINDOWED;
            };
        }

        public static FullScreenMode nextBorderless(FullScreenMode current) {
            return switch (current) {
                case FULLSCREEN, BORDERLESS -> WINDOWED;
                case WINDOWED -> BORDERLESS;
            };
        }

        public static FullScreenMode nextFullscreen(FullScreenMode current) {
            return switch (current) {
                case FULLSCREEN, BORDERLESS -> WINDOWED;
                case WINDOWED -> FULLSCREEN;
            };
        }

        public static FullScreenMode getVanillaConfig() {
            return Minecraft.getInstance().options.fullscreen().get() ? BORDERLESS : WINDOWED;
        }

        public boolean isBorderless() {
            return this == BORDERLESS;
        }
    }
}
