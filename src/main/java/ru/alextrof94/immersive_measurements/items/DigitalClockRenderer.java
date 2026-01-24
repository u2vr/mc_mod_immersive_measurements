package ru.alextrof94.immersive_measurements.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import ru.alextrof94.immersive_measurements.ModDataComponents;

public class DigitalClockRenderer implements SpecialModelRenderer<String> {
    private static final float SCREEN_WIDTH = 0.875f;

    public static class Unbaked implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public SpecialModelRenderer<?> bake(@NotNull EntityModelSet entityModelSet) {
            return new DigitalClockRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }

    @Nullable
    @Override
    public String extractArgument(ItemStack stack) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return "00:00";
        }

        long rawTime = level.getDayTime() % 24000;

        int hours = (int) ((rawTime / 1000 + 6) % 24);
        int minutes = (int) ((rawTime % 1000) * 60 / 1000);

        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }

    @Override
    public void render(@Nullable String text, @NotNull ItemDisplayContext displayContext, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, boolean hasFoil) {
        Minecraft mc = Minecraft.getInstance();
        poseStack.pushPose();
        poseStack.translate(0.48, 0.08, 0.5);
        poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(-90f)).rotateZ((float) Math.toRadians(-90f)));

        float textWidthPx = mc.font.width(text);
        float scale = (SCREEN_WIDTH * 0.85f) / textWidthPx;
        scale = Math.max(0.03f, Math.min(scale, 0.03f));
        poseStack.scale(scale, -scale, scale);

        float w = mc.font.width(text);
        float h = mc.font.lineHeight;
        mc.font.drawInBatch(text, -w/2f, -h/2f, 0xFFFFFF, false, poseStack.last().pose(), buffer, net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }
}