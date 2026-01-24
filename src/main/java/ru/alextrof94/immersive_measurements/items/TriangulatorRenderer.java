package ru.alextrof94.immersive_measurements.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import ru.alextrof94.immersive_measurements.ModDataComponents;

import java.awt.*;


public class TriangulatorRenderer implements SpecialModelRenderer<CustomData> {
    private static final float SCREEN_WIDTH = 0.875f;

    public static class Unbaked implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public SpecialModelRenderer<?> bake(@NotNull EntityModelSet entityModelSet) {
            return new TriangulatorRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }

    @Nullable
    @Override
    public CustomData extractArgument(ItemStack stack) {
        CustomData res = new CustomData("NO", "DATA");
        if (stack.has(ModDataComponents.TRIANGULATED_POS.get())) {
            var a = stack.get(ModDataComponents.TRIANGULATED_POS.get());
            if (a == null)
                return res;

            res.line1 = "x: " + a.pos().getX();
            res.line2 = "z: " + a.pos().getZ();
        }
        return res;
    }

    @Override
    public void render(@Nullable CustomData data, @NotNull ItemDisplayContext displayContext, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay, boolean hasFoil) {
        Minecraft mc = Minecraft.getInstance();
        poseStack.pushPose();
        poseStack.translate(0.5, 0.08, 0.5);
        poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(-90f)).rotateZ((float) Math.toRadians(-90f)));

        String text = "x: N/A";
        if (data != null)
            text = (data.line1.length() > data.line2.length()) ? data.line1 : data.line2;
        float textWidthPx = mc.font.width(text);
        float scale = (SCREEN_WIDTH * 0.85f) / textWidthPx;
        scale = Math.min(scale, 0.02f);
        poseStack.scale(scale, -scale, scale);

        float w = mc.font.width(text);
        float h = mc.font.lineHeight;
        mc.font.drawInBatch(data != null ? data.line1 : "x: N/A", -w/2f, -h/2f - h/2f, 0xFFFFFF, false, poseStack.last().pose(), buffer, net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, packedLight);
        mc.font.drawInBatch(data != null ? data.line2 : "z: N/A", -w/2f, -h/2f + h/2f, 0xFFFFFF, false, poseStack.last().pose(), buffer, net.minecraft.client.gui.Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }
}

class CustomData {
    public String line1 = "";
    public String line2 = "";

    public CustomData (String l1, String l2) {
        line1 = l1;
        line2 = l2;
    }
}