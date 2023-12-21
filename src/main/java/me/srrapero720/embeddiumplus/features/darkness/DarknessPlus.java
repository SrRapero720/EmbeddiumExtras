package me.srrapero720.embeddiumplus.features.darkness;

import me.srrapero720.embeddiumplus.internal.EmbPlusConfig;
import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DarknessPlus {

	@SubscribeEvent
	public static void onConfigChange(ModConfigEvent e) {
		EmbPlusConfig.darkNetherFogEffective = EmbPlusConfig.darkNether.get() ? EmbPlusConfig.darkNetherFogConfigured.get() : 1.0;
		EmbPlusConfig.darkEndFogEffective = EmbPlusConfig.darkEnd.get() ? EmbPlusConfig.darkEndFogConfigured.get() : 1.0;
	}

	public static boolean blockLightOnly() {
		return EmbPlusConfig.blockLightOnly.get();
	}

	public static double darkNetherFog() {
		return EmbPlusConfig.darkNetherFogEffective;
	}

	public static double darkEndFog() {
		return EmbPlusConfig.darkEndFogEffective;
	}

	public static void getDarkenedFogColor(CallbackInfoReturnable<Vec3> ci, double factor) {
		if (factor != 1.0) {
			Vec3 result = ci.getReturnValue();
			double MIN = 0.03D;
			result = new Vec3(Math.max(MIN, result.x * factor), Math.max(MIN, result.y * factor), Math.max(MIN, result.z * factor));
			ci.setReturnValue(result);
		}
	}

	private static boolean isDark(Level world) {
		if (!EmbPlusConfig.trueDarknessEnabled.get())
			return false;

		final ResourceKey<Level> dimType = world.dimension();
		if (dimType == Level.OVERWORLD) {
			return EmbPlusConfig.darkOverworld.get();
		} else if (dimType == Level.NETHER) {
			return EmbPlusConfig.darkNether.get();
		} else if (dimType == Level.END) {
			return EmbPlusConfig.darkEnd.get();
		} else if (world.dimensionType().hasSkyLight()) {
			return EmbPlusConfig.darkDefault.get();
		} else {
			return EmbPlusConfig.darkSkyless.get();
		}
	}

	private static float skyFactor(Level world) {
		if (!EmbPlusConfig.blockLightOnly.get() && isDark(world)) {
			if (world.dimensionType().hasSkyLight()) {
				final float angle = world.getTimeOfDay(0);
				if (angle > 0.25f && angle < 0.75f) {
					final float oldWeight = Math.max(0, (Math.abs(angle - 0.5f) - 0.2f)) * 20;
					final float moon = EmbPlusConfig.ignoreMoonPhase.get() ? 0 : world.getMoonBrightness();
					final float moonInterpolated = (float) Mth.lerp(moon, EmbPlusConfig.minimumMoonLevel.get(), EmbPlusConfig.maximumMoonLevel.get());
					return Mth.lerp(oldWeight * oldWeight * oldWeight, moonInterpolated, 1f) ;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	public static boolean enabled = false;
	private static final float[][] LUMINANCE = new float[16][16];

	public static int darken(int c, int blockIndex, int skyIndex) {
		final float lTarget = LUMINANCE[blockIndex][skyIndex];
		final float r = (c & 0xFF) / 255f;
		final float g = ((c >> 8) & 0xFF) / 255f;
		final float b = ((c >> 16) & 0xFF) / 255f;
		final float l = luminance(r, g, b);
		final float f = l > 0 ? Math.min(1, lTarget / l) : 0;

		return f == 1f ? c : 0xFF000000 | Math.round(f * r * 255) | (Math.round(f * g * 255) << 8) | (Math.round(f * b * 255) << 16);
	}

	public static float luminance(float r, float g, float b) {
		return r * 0.2126f + g * 0.7152f + b * 0.0722f;
	}

	public static void updateLuminance(float tickDelta, Minecraft client, GameRenderer worldRenderer, float prevFlicker) {
		final ClientLevel world = client.level;
		if (world != null) {

			if (!isDark(world) || client.player.hasEffect(MobEffects.NIGHT_VISION) ||
							(client.player.hasEffect(MobEffects.CONDUIT_POWER) && client.player.getWaterVision() > 0) || world.getSkyFlashTime() > 0) {
				enabled = false;
				return;
			} else {
				enabled = true;
			}

			final float dimSkyFactor = DarknessPlus.skyFactor(world);
			final float ambient = world.getSkyDarken(1.0F);
			final DimensionType dim = world.dimensionType();
			final boolean blockAmbient = !DarknessPlus.isDark(world);

			for (int skyIndex = 0; skyIndex < 16; ++skyIndex) {
				float skyFactor = 1f - skyIndex / 15f;
				skyFactor = 1 - skyFactor * skyFactor * skyFactor * skyFactor;
				skyFactor *= dimSkyFactor;

				float min = Math.max(skyFactor * 0.05f, EmbPlusConfig.darknessOption.get().value);
				final float rawAmbient = ambient * skyFactor;
				final float minAmbient = rawAmbient * (1 - min) + min;
				final float skyBase = LightTexture.getBrightness(dim, skyIndex) * minAmbient;

				min = Math.max(0.35f * skyFactor, EmbPlusConfig.darknessOption.get().value);
				float v = skyBase * (rawAmbient * (1 - min) + min);
				float skyRed = v;
				float skyGreen = v;
				float skyBlue = skyBase;

				if (worldRenderer.getDarkenWorldAmount(tickDelta) > 0.0F) {
					final float skyDarkness = worldRenderer.getDarkenWorldAmount(tickDelta);
					skyRed = skyRed * (1.0F - skyDarkness) + skyRed * 0.7F * skyDarkness;
					skyGreen = skyGreen * (1.0F - skyDarkness) + skyGreen * 0.6F * skyDarkness;
					skyBlue = skyBlue * (1.0F - skyDarkness) + skyBlue * 0.6F * skyDarkness;
				}

				for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
					float blockFactor = 1f;
					if (!blockAmbient) {
						blockFactor = 1f - blockIndex / 15f;
						blockFactor = 1 - blockFactor * blockFactor * blockFactor * blockFactor;
					}

					final float blockBase = blockFactor * LightTexture.getBrightness(dim, blockIndex) * (prevFlicker * 0.1F + 1.5F);
					min = 0.4f * blockFactor;
					final float blockGreen = blockBase * ((blockBase * (1 - min) + min) * (1 - min) + min);
					final float blockBlue = blockBase * (blockBase * blockBase * (1 - min) + min);

					float red = skyRed + blockBase;
					float green = skyGreen + blockGreen;
					float blue = skyBlue + blockBlue;

					final float f = Math.max(skyFactor, blockFactor);
					min = 0.03f * f;
					red = red * (0.99F - min) + min;
					green = green * (0.99F - min) + min;
					blue = blue * (0.99F - min) + min;

					//the end
					if (world.dimension() == Level.END) {
						red = skyFactor * 0.22F + blockBase * 0.75f;
						green = skyFactor * 0.28F + blockGreen * 0.75f;
						blue = skyFactor * 0.25F + blockBlue * 0.75f;
					}

					if (red > 1.0F) {
						red = 1.0F;
					}

					if (green > 1.0F) {
						green = 1.0F;
					}

					if (blue > 1.0F) {
						blue = 1.0F;
					}

					final float gamma = client.options.gamma().get().floatValue() * f;
					float invRed = 1.0F - red;
					float invGreen = 1.0F - green;
					float invBlue = 1.0F - blue;
					invRed = 1.0F - invRed * invRed * invRed * invRed;
					invGreen = 1.0F - invGreen * invGreen * invGreen * invGreen;
					invBlue = 1.0F - invBlue * invBlue * invBlue * invBlue;
					red = red * (1.0F - gamma) + invRed * gamma;
					green = green * (1.0F - gamma) + invGreen * gamma;
					blue = blue * (1.0F - gamma) + invBlue * gamma;

					min = Math.max(0.03f * f, EmbPlusConfig.darknessOption.get().value);
					red = red * (0.99F - min) + min;
					green = green * (0.99F - min) + min;
					blue = blue * (0.99F - min) + min;

					if (red > 1.0F) {
						red = 1.0F;
					}

					if (green > 1.0F) {
						green = 1.0F;
					}

					if (blue > 1.0F) {
						blue = 1.0F;
					}

					if (red < 0.0F) {
						red = 0.0F;
					}

					if (green < 0.0F) {
						green = 0.0F;
					}

					if (blue < 0.0F) {
						blue = 0.0F;
					}

					LUMINANCE[blockIndex][skyIndex] = DarknessPlus.luminance(red, green, blue);
				}
			}
		}
	} 
}