package me.srrapero720.embeddiumplus.mixins.impl.jei_rei_emi;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.emi.runtime.EmiDrawContext;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.widget.EmiSearchWidget;
import me.srrapero720.embeddiumplus.EmbyConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EmiScreenManager.class, remap = false)
@Pseudo
public class EmiOverlayMixin {
    @Shadow public static EmiSearchWidget search;
    @Unique private static int checkedPos = 0;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/EmiScreenManager$SidebarPanel;render(Ldev/emi/emi/runtime/EmiDrawContext;IIF)V"))
    private static void inject$renderStackOverlay(EmiScreenManager.SidebarPanel instance, EmiDrawContext totalPages, int i, int context, float mouseX, Operation<Void> original) {
        if (!EmbyConfig.hideJREMICache) {
            original.call(instance, totalPages, i, context, mouseX);
        } else {
            if (checkedPos == 1 && search.getValue().isEmpty()) {
                checkedPos++;
                return;
            }
            original.call(instance, totalPages, i, context, mouseX);
            checkedPos++;
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private static void inject$renderCleanup(EmiDrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        checkedPos = 0;
    }
}