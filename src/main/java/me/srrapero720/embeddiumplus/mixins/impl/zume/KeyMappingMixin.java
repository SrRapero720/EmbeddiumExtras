package me.srrapero720.embeddiumplus.mixins.impl.zume;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Shadow private InputConstants.Key key;
    @Shadow @Final @Mutable private InputConstants.Key defaultKey;

    @WrapOperation(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/KeyMapping;category:Ljava/lang/String;"))
    private void redirect$init(KeyMapping instance, String value, Operation<Void> original) {
        if (value.equals("category.zume")) {
            defaultKey = key = InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_C);
        }
    }
}
