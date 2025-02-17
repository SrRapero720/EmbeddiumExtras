package me.srrapero720.embeddiumplus.mixins.impl.jei_rei_emi;

import me.srrapero720.embeddiumplus.EmbyConfig;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.input.GuiTextFieldFilter;
import mezz.jei.gui.overlay.IngredientListOverlay;
import mezz.jei.gui.overlay.ScreenPropertiesCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IngredientListOverlay.class, remap = false)
@Pseudo
public class JeiOverlayMixin {
    @Shadow @Final private GuiTextFieldFilter searchField;
    @Shadow @Final private GuiIconToggleButton configButton;
    @Shadow @Final private ScreenPropertiesCache screenPropertiesCache;

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation;draw(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IIF)V"), cancellable = true)
    public void inject$renderOverlay(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (!EmbyConfig.hideJREMICache) return;

        String value = searchField.getValue();
        if (value.isEmpty()) {
            if (screenPropertiesCache.hasValidScreen()) {
                configButton.draw(guiGraphics, mouseX, mouseY, partialTicks);
            }
            ci.cancel();
        }
    }

    @Inject(method = "drawTooltips", at = @At(value = "HEAD"), cancellable = true)
    public void inject$renderOverlay(Minecraft minecraft, GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (!EmbyConfig.hideJREMICache) return;

        String value = searchField.getValue();
        if (value.isEmpty()) {
            ci.cancel();
        }
    }
}