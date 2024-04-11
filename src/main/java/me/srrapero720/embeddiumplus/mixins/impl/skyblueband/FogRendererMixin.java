package me.srrapero720.embeddiumplus.mixins.impl.skyblueband;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"))
    private static Vec3 redirect$gausanSampleColor(Vec3 vec, CubicSampler.Vec3Fetcher fetcher, Operation<Vec3> original) {
        if (!EmbyConfig.blueBandCache) {
            final Minecraft mc = Minecraft.getInstance();

            if (mc.level.dimensionType().hasSkyLight())
                return mc.level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), mc.getFrameTime());

            return vec;
        }
        return original.call(vec, fetcher);
    }

    @WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3f;dot(Lorg/joml/Vector3fc;)F"))
    private static float redirect$dot(Vector3f instance, Vector3fc v, Operation<Float> original) {
        if (!EmbyConfig.blueBandCache) return 0;
        return original.call(instance, v);
    }

    @WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    private static float redirect$getRainLevel(ClientLevel instance, float v, Operation<Float> original) {
        if (!EmbyConfig.blueBandCache) return 0;
        return original.call(instance, v);
    }

    @WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"))
    private static float redirect$getThunderLevel(ClientLevel instance, float v, Operation<Float> original) {
        if (!EmbyConfig.blueBandCache) return 0;
        return original.call(instance, v);
    }
}
