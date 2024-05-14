package me.srrapero720.embeddiumplus.foundation.fps;

import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FrameTimer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DebugOverlayEvent {
    private static final FPSDisplay DISPLAY = new FPSDisplay();

    private static final Component MSG_FPS = Component.translatable("embeddium.plus.options.displayfps.fps");
    private static final Component MSG_MIN = Component.translatable("embeddium.plus.options.displayfps.min");
    private static final Component MSG_AVG = Component.translatable("embeddium.plus.options.displayfps.avg");
    private static final Component MSG_GPU = Component.translatable("embeddium.plus.options.displayfps.gpu");
    private static final Component MSG_MEM = Component.translatable("embeddium.plus.options.displayfps.mem");

    public static final AverageQueue AVERAGE = new AverageQueue();

    private static int fps = -1;
    private static int minFPS = -1;
    private static int avgFPS = -1;
    private static int gpuPercent = -1;
    private static int memUsage = -1;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderOverlayItem(RenderGuiOverlayEvent.Pre event) {
        if (!event.getOverlay().id().getPath().equals("debug_text")) return;

        // cancel rendering text if chart is displaying
        if (Minecraft.getInstance().options.renderFpsChart) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        var mc = Minecraft.getInstance();

        // PRECALCULATE
        fps = mc.getFps();
        minFPS = minFPS(mc);
        memUsage = (int) ((EmbyTools.ramUsed() * 100) / Runtime.getRuntime().maxMemory());
        gpuPercent = Math.min((int) mc.getGpuUtilization(), 100);
        // DELEFATED TO MIXIN CLASS
        // AVERAGE.push(fps).calculate();
        avgFPS = AVERAGE.calculate();
        renderFPSChar(mc, event.getGuiGraphics(), mc.font, event.getWindow().getGuiScale());
    }

    private static void renderFPSChar(Minecraft mc, GuiGraphics graphics, Font font, double scale) {
        if (mc.options.renderDebug || mc.options.renderFpsChart) return; // No render when F3 is open

        final var mode = EmbyConfig.fpsDisplayMode.get();
        final var systemMode = EmbyConfig.fpsDisplaySystemMode.get();

        if (mode.off() && systemMode.off()) return; // NOTHING TO DO HERE, BACK TO WORK

        DISPLAY.release();

        // FPS
        switch (mode) {
            case SIMPLE -> DISPLAY.append(EmbyTools.colorByLow(fps)).add(fix(fps)).add(" ").add(MSG_FPS.getString()).add(ChatFormatting.RESET);
            case ADVANCED -> {
                DISPLAY.append(EmbyTools.colorByLow(fps)).add(fix(fps)).add(ChatFormatting.RESET);
                DISPLAY.append(EmbyTools.colorByLow(minFPS)).add(MSG_MIN).add(" ").add(fix(minFPS)).add(ChatFormatting.RESET);
                DISPLAY.append(EmbyTools.colorByLow(avgFPS)).add(MSG_AVG).add(" ").add(fix(avgFPS)).add(ChatFormatting.RESET);
            }
        }
        if (!DISPLAY.isEmpty()) DISPLAY.split();

        // GPU AND RAM
        switch (systemMode) {
            case GPU ->
                    DISPLAY.append(EmbyTools.colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
            case RAM ->
                    DISPLAY.append(EmbyTools.colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            case ON -> {
                DISPLAY.append(EmbyTools.colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
                DISPLAY.append(EmbyTools.colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
            }
        }

        if (DISPLAY.isEmpty()) DISPLAY.add("FATAL ERROR");

        float margin = (scale > 0) ? EmbyConfig.fpsDisplayMarginCache / (float) scale : EmbyConfig.fpsDisplayMarginCache;

        // Prevent FPS-Display to render outside screenspace
        String displayString = DISPLAY.toString();
        float maxPosX = graphics.guiWidth() - font.width(displayString);
        float posX, posY;

        posX = switch (EmbyConfig.fpsDisplayGravity.get()) {
            case LEFT -> margin;
            case CENTER -> (maxPosX / 2);
            case RIGHT -> maxPosX - margin;
        };
        posY = margin;

        graphics.pose().pushPose();
        if (EmbyConfig.fpsDisplayShadowCache) {
            graphics.fill((int) posX - 2, (int) posY - 2, (int) posX + font.width(displayString) + 2, (int) (posY + font.lineHeight) + 1, -1873784752);
            graphics.flush();
        }

        graphics.drawString(font, displayString, posX, posY, 0xffffffff, true);
        DISPLAY.release();
        graphics.pose().popPose();
    }

    private static String fix(int value) {
        return (value == -1) ? "--" : "" + value;
    }

    private static int minFPS(Minecraft mc) {
        FrameTimer timer = mc.getFrameTimer();

        int start = timer.getLogStart();
        int end = timer.getLogEnd();

        if (end == start) return minFPS;

        int fps = mc.getFps();
        if (fps <= 0) fps = 1;

        long[] frames = timer.getLog();
        long maxNS = (long) (1 / (double) fps * 1000000000);
        long totalNS = 0;

        int index = Math.floorMod(end - 1, frames.length);
        while (index != start && (double) totalNS < 1000000000) {
            long timeNs = frames[index];
            if (timeNs > maxNS) {
                maxNS = timeNs;
            }

            totalNS += timeNs;
            index = Math.floorMod(index - 1, frames.length);
        }

        return (int) (1 / ((double) maxNS / 1000000000));
    }
}