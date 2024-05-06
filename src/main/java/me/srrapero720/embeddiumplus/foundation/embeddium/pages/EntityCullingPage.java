package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.embeddiumplus.foundation.embeddium.EmbPlusOptions.STORAGE;

public class EntityCullingPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation(EmbeddiumPlus.ID, "culling"));
    public EntityCullingPage() {
        super(ID, Component.translatable("embeddium.plus.options.culling.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var enableDistanceChecks = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.entityDistanceCulling.set(value);
                            EmbyConfig.entityDistanceCullingCache = value;
                        },
                        (options) -> EmbyConfig.entityDistanceCullingCache)
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistance = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.horizontal.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> {
                            int result = value * value;
                            EmbyConfig.entityCullingDistanceX.set(result);
                            EmbyConfig.entityCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.entityCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxEntityDistanceVertical = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.entity.distance.vertical.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.entity.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.entityCullingDistanceY.set(value);
                            EmbyConfig.entityCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.entityCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );

        var monsterDistanceChecks = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.monster.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.monster.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.monsterDistanceCulling.set(value);
                            EmbyConfig.monsterDistanceCullingCache = value;
                        },
                        (options) -> EmbyConfig.monsterDistanceCullingCache)
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxMonsterDistance = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.monster.distance.horizontal.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.monster.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> {
                            int result = value * value;
                            EmbyConfig.monsterCullingDistanceX.set(result);
                            EmbyConfig.monsterCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.monsterCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxMonsterDistanceVertical = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.monster.distance.vertical.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.monster.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.monsterCullingDistanceY.set(value);
                            EmbyConfig.monsterCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.monsterCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();


        groups.add(OptionGroup.createBuilder()
                .add(monsterDistanceChecks)
                .add(maxMonsterDistance)
                .add(maxMonsterDistanceVertical)
                .build()
        );


        var enableTileDistanceChecks = OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.tiles.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tiles.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.tileEntityDistanceCulling.set(value);
                            EmbyConfig.tileEntityDistanceCullingCache = value;
                        },
                        (options) -> EmbyConfig.tileEntityDistanceCullingCache)
                .setImpact(OptionImpact.HIGH)
                .build();


        var maxTileEntityDistance = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.horizontal.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.horizontal.desc"))
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.biomeBlend()))
                .setBinding((options, value) -> {
                            int result = value * value;
                            EmbyConfig.tileEntityCullingDistanceX.set(result);
                            EmbyConfig.tileEntityCullingDistanceXCache = result;
                        },
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(EmbyConfig.tileEntityCullingDistanceXCache))))
                .setImpact(OptionImpact.HIGH)
                .build();

        var maxTileEntityDistanceVertical = OptionImpl.createBuilder(int.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.culling.tile.distance.vertical.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.culling.tile.distance.vertical.desc"))
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((options, value) -> {
                            EmbyConfig.tileEntityCullingDistanceY.set(value);
                            EmbyConfig.tileEntityCullingDistanceYCache = value;
                        },
                        (options) -> EmbyConfig.tileEntityCullingDistanceYCache)
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(enableTileDistanceChecks)
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
