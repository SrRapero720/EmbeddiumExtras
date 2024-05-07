package me.srrapero720.embeddiumplus.foundation.embeddium;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyConfig.FullScreenMode;
import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.foundation.embeddium.pages.*;
import me.srrapero720.embeddiumplus.foundation.embeddium.storage.EmbPlusOptionsStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.api.OptionPageConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

import java.util.List;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EmbPlusOptions {
    public static final OptionStorage<?> STORAGE = new EmbPlusOptionsStorage();

    @SubscribeEvent
    public static void onEmbeddiumPagesRegister(OptionGUIConstructionEvent e) {
        var pages = e.getPages();

        pages.add(new MetricsPage());
        pages.add(new QualityPlusPage());
        pages.add(new TrueDarknessPage());
        pages.add(new EntityCullingPage());
        if (EmbyTools.isModInstalled("zume")) {
            for (int i = 0; i < pages.size(); i++) {
                var page = pages.get(i);
                if (page.getId() != null && page.getId().getModId().equals("zume")) {
                    break;
                }
                if (i == pages.size() - 1) {
                    pages.add(new ZoomPage());
                }
            }
        }
        pages.add(new OthersPage());
    }

    @SubscribeEvent
    public static void onEmbeddiumPagesRegister(OptionGroupConstructionEvent e) {
        if (e.getId() != null && e.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
            var opions = e.getOptions();
            for (int i = 0; i < opions.size(); i++) {
                if (opions.get(i).getId().toString().equals(StandardOptions.Option.FULLSCREEN.toString())) {
                    opions.set(i, getFullscreenOption());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEmbeddiumGroupRegister(OptionPageConstructionEvent e) {
        if (e.getId() != null && e.getId().equals(StandardOptions.Pages.PERFORMANCE)) {
            var builder = OptionGroup.createBuilder();
            var sodiumOpts = SodiumGameOptionPages.getVanillaOpts();
            var fontShadow = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(new ResourceLocation(EmbeddiumPlus.ID, "font_shadow"))
                    .setName(Component.translatable("embeddium.plus.options.fontshadow.title"))
                    .setTooltip(Component.translatable("embeddium.plus.options.fontshadow.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (options, value) -> {
                                EmbyConfig.fontShadows.set(value);
                                EmbyConfig.fontShadowsCache = value;
                            },
                            (options) -> EmbyConfig.fontShadowsCache)
                    .setImpact(OptionImpact.VARIES)
                    .build();

            var leavesCulling = OptionImpl.createBuilder(EmbyConfig.LeavesCullingMode.class, sodiumOpts)
                    .setId(new ResourceLocation(EmbeddiumPlus.ID, "leaves_culling"))
                    .setName(Component.translatable("embeddium.plus.options.leaves_culling.title"))
                    .setTooltip(Component.translatable("embeddium.plus.options.leaves_culling.desc"))
                    .setControl(opt -> new CyclingControl<>(opt, EmbyConfig.LeavesCullingMode.class, new Component[] {
                            Component.translatable("embeddium.plus.options.leaves_culling.all"),
                            Component.translatable("embeddium.plus.options.leaves_culling.off")
                    }))
                    .setBinding((opt, v) -> EmbyConfig.leavesCulling.set(v),
                            (options) -> EmbyConfig.leavesCulling.get())
                    .setImpact(OptionImpact.HIGH)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            var fastChest = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(new ResourceLocation(EmbeddiumPlus.ID, "fast_chests"))
                    .setName(Component.translatable("embeddium.plus.options.fastchest.title"))
                    .setTooltip(Component.translatable("embeddium.plus.options.fastchest.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (options, value) -> {
                                EmbyConfig.fastChests.set(value);
                                EmbyConfig.fastChestsCache = value;
                            },
                            (options) -> EmbyConfig.fastChestsCache)
                    .setImpact(OptionImpact.HIGH)
//                .setEnabled(FastModels.canUseOnChests())
                    .setEnabled(false)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            var fastBeds = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(new ResourceLocation(EmbeddiumPlus.ID, "fast_beds"))
                    .setName(Component.translatable("embeddium.plus.options.fastbeds.title"))
                    .setTooltip(Component.translatable("embeddium.plus.options.fastbeds.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (options, value) -> {
                                EmbyConfig.fastBeds.set(value);
                                EmbyConfig.fastBedsCache = value;
                            },
                            (options) -> EmbyConfig.fastBedsCache)
                    .setImpact(OptionImpact.LOW)
//                .setEnabled(EmbyTools.isFlywheelOff())
                    .setEnabled(false)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build();

            var hideJEI = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                    .setId(new ResourceLocation(EmbeddiumPlus.ID, "hide_jremi"))
                    .setName(Component.translatable("embeddium.plus.options.jei.title"))
                    .setTooltip(Component.translatable("embeddium.plus.options.jei.desc"))
                    .setControl(TickBoxControl::new)
                    .setBinding(
                            (options, value) -> {
                                EmbyConfig.hideJREMI.set(value);
                                EmbyConfig.hideJREMICache = value;
                            },
                            (options) -> EmbyConfig.hideJREMICache)
                    .setImpact(OptionImpact.LOW)
                    .setEnabled(EmbyTools.isModInstalled("jei") || EmbyTools.isModInstalled("roughlyenoughitems") || EmbyTools.isModInstalled("emi"))
                    .build();

            builder.add(leavesCulling);
            builder.add(fontShadow);
            builder.add(fastChest);
            builder.add(fastBeds);
            builder.add(hideJEI);

            e.addGroup(builder.build());
        }
    }

    private static Option<FullScreenMode> getFullscreenOption() {
        var options = SodiumGameOptionPages.getVanillaOpts();
        return OptionImpl.createBuilder(FullScreenMode.class, options)
                .setId(new ResourceLocation(EmbeddiumPlus.ID, "fullscreen"))
                .setName(Component.translatable("embeddium.plus.options.screen.title"))
                .setTooltip(Component.translatable("embeddium.plus.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("embeddium.plus.options.screen.windowed"),
                        Component.translatable("embeddium.plus.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(EmbyConfig::setFullScreenMode, (opts) -> EmbyConfig.fullScreen.get()).build();
    }
}
