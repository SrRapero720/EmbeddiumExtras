package me.srrapero720.embeddiumplus.mixins.impl.zume;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// nolij will hate me for do that XD
@Mixin({ dev.nolij.zume.lexforge18.ZumeKeyBind.class, dev.nolij.zume.lexforge.ZumeKeyBind.class })
@Pseudo
public class ZumeKeyBindsForgeMixin {
    @ModifyArg(
            method = "<init>(Ljava/lang/String;ILjava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;<init>(Ljava/lang/String;Lcom/mojang/blaze3d/platform/InputConstants$Type;ILjava/lang/String;)V"
            ),
            index = 2
    )
    private int modify$keyBindId2(int pKeyCode) {
        return (pKeyCode == GLFW.GLFW_KEY_Z) ? GLFW.GLFW_KEY_C : pKeyCode;
    }
}
