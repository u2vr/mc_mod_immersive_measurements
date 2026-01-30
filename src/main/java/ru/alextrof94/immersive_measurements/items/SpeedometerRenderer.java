package ru.alextrof94.immersive_measurements.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import ru.alextrof94.immersive_measurements.CustomData;

import java.util.List;

public class SpeedometerRenderer extends BaseDisplayRenderer {
    protected float getScreenWidth() {
        return 0.4f;
    }

    @Override
    protected float getScreenHeight() {
        return 0.3f;
    }

    @Override
    protected Vector3f getScreenOffset() {
        return new Vector3f(0.55f, 0.08f, 0.5f);
    }

    public static final Unbaked<SpeedometerRenderer> UNBAKED = new Unbaked<>(SpeedometerRenderer::new);

    @Nullable
    @Override
    public CustomData extractArgument(@NotNull ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();

        Entity targetEntity = mc.player;

        if (targetEntity == null)
            return new CustomData(false, List.of("0.0"), 0f);

        Entity vehicle = targetEntity.getVehicle();
        Entity trackedEntity = vehicle != null ? vehicle : targetEntity;
        SpeedometerClientTracker.update(trackedEntity);
        double speed = SpeedometerClientTracker.getSpeed(trackedEntity);

        if (speed < 0.05)
            speed = 0;

        float progress = (float) (speed / 100.0);
        progress = Math.max(0, Math.min(1.0f, progress));

        if (speed < 10)
            return new CustomData(false, List.of(String.format("%.1f", speed)), progress);
        else
            return new CustomData(false, List.of(String.format("%.0f", speed)), progress);
    }

    @Override
    public void render(@Nullable CustomData data, @NotNull ItemDisplayContext displayContext,
            @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay,
            boolean hasFoil) {
        super.render(data, displayContext, poseStack, buffer, packedLight, packedOverlay, hasFoil);
        if (data == null)
            return;

        poseStack.pushPose();

        Vector3f offset = getScreenOffset();
        offset.y += 0.01f;
        poseStack.translate(offset.x(), offset.y(), offset.z());
        poseStack.mulPose(getDefItemRotation());

        drawArc(poseStack, buffer, data.progress, getScreenWidth(), getScreenHeight());

        poseStack.popPose();
    }

    protected void drawArc(PoseStack poseStack, MultiBufferSource buffer, float progress, float width, float height) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.gui());
        Matrix4f matrix = poseStack.last().pose();

        float radius = Math.min(width, height) * 1.1f;
        float centerX = 0;
        float centerY = -height * 0.3f;
        int segments = 40;
        float thickness = 0.1f;

        int r = 255;
        int g = (int) (255 * (1f - progress));
        int b = (int) (255 * (1f - progress));

        for (int i = 0; i < segments * progress; i++) {
            float angle1 = (float) Math.PI - (i / (float) segments) * (float) Math.PI;
            float angle2 = (float) Math.PI - ((i + 1) / (float) segments) * (float) Math.PI;

            drawSegment(consumer, matrix,
                    centerX + (float) Math.cos(angle1) * radius, centerY + (float) Math.sin(angle1) * radius,
                    centerX + (float) Math.cos(angle2) * radius, centerY + (float) Math.sin(angle2) * radius,
                    thickness, r, g, b);
        }
    }

    private void drawSegment(VertexConsumer consumer, Matrix4f matrix, float x1, float y1, float x2, float y2, float th,
            int r, int g, int b) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        float nx = -dy / len * th;
        float ny = dx / len * th;

        consumer.addVertex(matrix, x1, y1, 0).setColor(r, g, b, 255).setLight(15728880);
        consumer.addVertex(matrix, x2, y2, 0).setColor(r, g, b, 255).setLight(15728880);
        consumer.addVertex(matrix, x2 + nx, y2 + ny, 0).setColor(r, g, b, 255).setLight(15728880);
        consumer.addVertex(matrix, x1 + nx, y1 + ny, 0).setColor(r, g, b, 255).setLight(15728880);
    }
}