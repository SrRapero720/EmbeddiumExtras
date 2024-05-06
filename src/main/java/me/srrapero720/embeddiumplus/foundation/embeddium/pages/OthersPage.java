package me.srrapero720.embeddiumplus.foundation.embeddium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
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

public class OthersPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation(EmbeddiumPlus.ID, "others"));
    public OthersPage() {
        super(ID, Component.translatable("embeddium.plus.options.others.page"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(EmbyConfig.AttachMode.class, STORAGE)
                        .setName(Component.translatable("embeddium.plus.options.others.borderless.attachmode.title"))
                        .setTooltip(Component.translatable("embeddium.plus.options.others.borderless.attachmode.desc"))
                        .setControl(option -> new CyclingControl<>(option, EmbyConfig.AttachMode.class, new Component[] {
                                Component.translatable("embeddium.plus.options.common.attach"),
                                Component.translatable("embeddium.plus.options.common.replace"),
                                Component.translatable("embeddium.plus.options.common.off")
                        }))
                        .setBinding((options, value) -> EmbyConfig.borderlessAttachModeF11.set(value),
                                (options) -> EmbyConfig.borderlessAttachModeF11.get())
                        .build())
                .add(OptionImpl.createBuilder(boolean.class, STORAGE)
                        .setName(Component.translatable("embeddium.plus.options.others.languagescreen.fastreload.title"))
                        .setTooltip(Component.translatable("embeddium.plus.options.others.languagescreen.fastreload.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> {
                                    EmbyConfig.fastLanguageReload.set(value);
                                    EmbyConfig.fastLanguageReloadCache = value;
                                },
                                (options) -> EmbyConfig.fastLanguageReloadCache)
                        .build()
                )
                .build()
        );

        return ImmutableList.copyOf(groups);
    }
}
