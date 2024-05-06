package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.embeddiumplus.foundation.embeddium.EmbPlusOptions.STORAGE;

public class MetricsPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation(EmbeddiumPlus.ID, "metrics"));
    public MetricsPage() {
        super(ID, Component.translatable("embeddium.plus.options.metrics.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        var builder = OptionGroup.createBuilder();

        builder.add(OptionImpl.createBuilder(EmbyConfig.FPSDisplayMode.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.displayfps.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.FPSDisplayMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.simple"),
                        Component.translatable("embeddium.plus.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayMode.set(value),
                        (opts) -> EmbyConfig.fpsDisplayMode.get())
                .setImpact(OptionImpact.LOW)
                .build()
        );

        builder.add(OptionImpl.createBuilder(EmbyConfig.FPSDisplaySystemMode.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.displayfps.system.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.FPSDisplaySystemMode.class, new Component[]{
                        Component.translatable("embeddium.plus.options.common.off"),
                        Component.translatable("embeddium.plus.options.common.on"),
                        Component.translatable("embeddium.plus.options.displayfps.system.gpu"),
                        Component.translatable("embeddium.plus.options.displayfps.system.ram")
                }))
                .setBinding((options, value) -> EmbyConfig.fpsDisplaySystemMode.set(value),
                        (options) -> EmbyConfig.fpsDisplaySystemMode.get())
                .build()
        );

        var components = new Component[EmbyConfig.FPSDisplayGravity.values().length];
        for (int i = 0; i < components.length; i++) {
            components[i] = Component.translatable("embeddium.plus.options.displayfps.gravity." + EmbyConfig.FPSDisplayGravity.values()[i].name().toLowerCase());
        }

        builder.add(OptionImpl.createBuilder(EmbyConfig.FPSDisplayGravity.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.displayfps.gravity.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.gravity.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.FPSDisplayGravity.class, components))
                .setBinding(
                        (opts, value) -> EmbyConfig.fpsDisplayGravity.set(value),
                        (opts) -> EmbyConfig.fpsDisplayGravity.get())
                .build()
        );


        builder.add(OptionImpl.createBuilder(Integer.TYPE, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.displayfps.margin.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.margin.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayMargin.set(value);
                            EmbyConfig.fpsDisplayMarginCache = value;
                        },
                        (opts) -> EmbyConfig.fpsDisplayMarginCache)
                .build()
        );

        builder.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("embeddium.plus.options.displayfps.shadow.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.displayfps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fpsDisplayShadow.set(value);
                            EmbyConfig.fpsDisplayShadowCache = value;
                        },
                        (options) -> EmbyConfig.fpsDisplayShadowCache)
                .build()
        );

        groups.add(builder.build());

        return ImmutableList.copyOf(groups);
    }
}
