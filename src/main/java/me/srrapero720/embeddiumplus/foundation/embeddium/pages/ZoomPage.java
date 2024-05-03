package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.srrapero720.embeddiumplus.foundation.embeddium.storage.ZumeOptionsStorage;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ZoomPage extends OptionPage {

    public ZoomPage() {
        super(Component.translatable("embeddium.plus.options.zoom.page"), create(new ZumeOptionsStorage()));
    }

    private static ImmutableList<OptionGroup> create(ZumeOptionsStorage zoomOptionsStorage) {
        final List<OptionGroup> groups = new ArrayList<>();

        final var enableZume = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.isDisabled = !value,
                        options -> !options.isDisabled
                )
                .build();

        final var cinematicZoom = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.cinematic.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.cinematic.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.isCinematicZoomEnabled = value,
                        options -> options.isCinematicZoomEnabled
                )
                .build();

        final var mouseSensitive = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.sensitive.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.sensitive.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 5, ControlValueFormatter.percentage()))
                .setBinding(
                        (options, value) -> options.mouseSensitivityFloor = Math.min(Math.floor(value / 100d), 1.0d),
                        (options) -> (int) Math.floor(options.mouseSensitivityFloor * 100d)
                )
                .build();

        final var zoomSpeed = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.speed.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.speed.desc"))
                .setControl((option) -> new SliderControl(option, 5, 150, 5, i -> Component.literal(i + "").append(Component.translatable("embeddium.plus.options.common.nojils"))))
                .setBinding(
                        (options, value) -> options.zoomSpeed = value.shortValue(),
                        (options) -> (int) options.zoomSpeed
                )
                .build();

        final var zoomScrolling = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.scrolling.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.scrolling.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.isZoomScrollingEnabled = value,
                        options -> options.isZoomScrollingEnabled
                )
                .build();

        final var zoomSmoothnessMS = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.smoothness.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.smoothness.desc"))
                .setControl((option) -> new SliderControl(option, 50, 150, 10, i -> Component.literal(i + "").append(Component.translatable("embeddium.plus.options.common.millis"))))
                .setBinding(
                        (options, value) -> options.zoomSmoothnessMilliseconds = value.shortValue(),
                        (options) -> (int) options.zoomSmoothnessMilliseconds
                )
                .build();

        final var animationEasingExponent = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.easing_exponent.animation.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.easing_exponent.animation.desc"))
                .setControl(opt -> new SliderControl(opt, 0, 25000, 25, i -> Component.literal("x").append((i / 1000F) + "")))
                .setBinding(
                        (options, value) -> options.animationEasingExponent = value / 1000F,
                        opts -> (int) opts.animationEasingExponent * 1000
                )
                .build();

        final var zoomEasingExponent = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.easing_exponent.zoom.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.easing_exponent.zoom.desc"))
                .setControl(opt -> new SliderControl(opt, 0, 25000, 25, i -> Component.literal("x").append((i / 1000F) + "")))
                .setBinding(
                        (options, value) -> options.zoomEasingExponent = value / 1000F,
                        opts -> (int) opts.zoomEasingExponent * 1000
                )
                .build();

        final var defaultZoom = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.default.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.default.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding(
                        (options, value) -> options.defaultZoom = Math.min(Math.floor(value / 100f), 1.0d),
                        (options) -> (int) Math.floor(options.defaultZoom * 100f)
                )
                .build();

        final var toggleMode = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.toggle.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.toggle.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.isFirstPersonToggleModeEnabled = value,
                        options -> options.isFirstPersonToggleModeEnabled
                )
                .build();

        final var thirdPersonToggleMode = OptionImpl.createBuilder(boolean.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.toggle.third_person.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.toggle.third_person.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> options.isThirdPersonToggleModeEnabled = value,
                        options -> options.isThirdPersonToggleModeEnabled
                )
                .build();

        final var minFov = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.min.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.min.desc"))
                .setControl((option) -> new SliderControl(option, 0, 100, 10, v -> Component.literal(v / 10F + "°")))
                .setBinding(
                        (options, value) -> options.minimumFOV = value / 10F,
                        (options) -> (int) options.minimumFOV * 10
                )
                .build();

        final var thirdPersonMinFov = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.third_person.min.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.third_person.min.desc"))
                .setControl(opt -> new SliderControl(opt, 0, 10000, 25, v -> Component.translatable("sodium.options.biome_blend.value", v / 1000F)))
                .setBinding(
                        (options, value) -> options.minimumThirdPersonZoomBlocks = value / 1000F,
                        opts -> (int) opts.minimumThirdPersonZoomBlocks * 1000
                )
                .build();

        final var thirdPersonMaxFov = OptionImpl.createBuilder(int.class, zoomOptionsStorage)
                .setName(Component.translatable("embeddium.plus.options.zoom.third_person.max.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.zoom.third_person.max.desc"))
                .setControl(opt -> new SliderControl(opt, 0, 10000, 25, v -> Component.translatable("sodium.options.biome_blend.value", v / 1000F)))
                .setBinding(
                        (options, value) -> options.maximumThirdPersonZoomBlocks = value / 1000F,
                        opts -> (int) opts.maximumThirdPersonZoomBlocks * 1000
                )
                .build();



        groups.add(OptionGroup.createBuilder()
                .add(enableZume)
                .add(zoomEasingExponent)
                .add(animationEasingExponent)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(cinematicZoom)
                .add(zoomSpeed)
                .add(zoomScrolling)
                .add(zoomSmoothnessMS)
                .add(defaultZoom)
                .add(minFov)
                .add(thirdPersonMinFov)
                .add(thirdPersonMaxFov)
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(mouseSensitive)
                .add(toggleMode)
                .add(thirdPersonToggleMode)
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
