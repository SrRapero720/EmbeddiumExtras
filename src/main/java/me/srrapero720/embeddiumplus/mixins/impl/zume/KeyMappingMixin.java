package me.srrapero720.embeddiumplus.mixins.impl.zume;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @WrapOperation(method = "<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants$Type;getOrCreate(I)Lcom/mojang/blaze3d/platform/InputConstants$Key;"))
    private InputConstants.Key redirect$init(InputConstants.Type instance, int pKeyCode, Operation<InputConstants.Key> original, String pName, InputConstants.Type pType, int methodkeycode, String pCategory) {
        return original.call(instance, (pCategory.equals("category.zume") && pKeyCode == GLFW.GLFW_KEY_Z) ? GLFW.GLFW_KEY_C : pKeyCode);
    }
}
