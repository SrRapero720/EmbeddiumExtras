/*******************************************************************************
 * Copyright 2019 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package me.srrapero720.embeddiumplus.mixins.darkness;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.srrapero720.embeddiumplus.features.TotalDarkness.Darkness;
import me.srrapero720.embeddiumplus.features.TotalDarkness.LightmapAccess;


@Mixin(GameRenderer.class)
public class MixinGameRenderer {
	@Final
	@Shadow
	Minecraft minecraft;

	@Final
	@Shadow
	private LightTexture lightTexture;

	@Inject(method = "renderLevel", at = @At(value = "HEAD"))
	private void onRenderWorld(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
		final LightmapAccess lightmap = (LightmapAccess) lightTexture;

		if (lightmap.darkness_isDirty()) {
			minecraft.getProfiler().push("lightTex");
			Darkness.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightmap.darkness_prevFlicker());
			minecraft.getProfiler().pop();
		}
	}
}