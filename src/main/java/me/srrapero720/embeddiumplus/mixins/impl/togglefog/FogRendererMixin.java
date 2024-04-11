package me.srrapero720.embeddiumplus.mixins.impl.togglefog;

import com.mojang.blaze3d.shaders.FogShape;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FogRenderer.class, priority = 910)
public abstract class FogRendererMixin {
    @Unique private static final float FOG_START = -8.0F;
    @Unique private static final float FOG_END = 1_000_000.0F;

    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void inject$fogDistance(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci, FogType fogtype, Entity entity, FogRenderer.FogData fogrenderer$fogdata, FogRenderer.MobEffectFogFunction fogrenderer$mobeffectfogfunction) {
        if (EmbyConfig.fogCache) return;

        fogrenderer$fogdata.start = FOG_START;
        fogrenderer$fogdata.end = FOG_END;
        fogrenderer$fogdata.shape = FogShape.SPHERE;
    }
}