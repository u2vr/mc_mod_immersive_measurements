package ru.alextrof94.immersive_measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;
import ru.alextrof94.immersive_measurements.blockentities.DigitalClockBlockEntity;
import ru.alextrof94.immersive_measurements.blocks.DigitalClockBlock;

public class DigitalClockBlockRenderer implements BlockEntityRenderer<DigitalClockBlockEntity> {
    // Configurable text rendering parameters
    // Display is approx at X=8.2 pixels. We want it slightly in front (West side ->
    // smaller X).
    private static final float TEXT_OFFSET_X = 8.1f / 16f;
    private static final float TEXT_OFFSET_Y = 4.05f / 16f; // Center of display
    private static final float TEXT_OFFSET_Z = 0.5f;

    // Rotations in degrees
    private static final float ROTATION_X = 0f;
    private static final float ROTATION_Y = -90f;
    private static final float ROTATION_Z = 0f;

    // Scale factor
    private static final float TEXT_SCALE = 0.015f;

    private final Font font;

    public DigitalClockBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(DigitalClockBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay,
            net.minecraft.world.phys.Vec3 cameraPos) {

        var level = blockEntity.getLevel();
        if (level == null)
            return;

        Direction facing = Direction.WEST;
        if (blockEntity.getBlockState().hasProperty(DigitalClockBlock.FACING)) {
            facing = blockEntity.getBlockState().getValue(DigitalClockBlock.FACING);
        }

        poseStack.pushPose();

        // Rotate around block center based on facing
        poseStack.translate(0.5, 0.5, 0.5);
        float rot = -facing.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(rot + 90));
        poseStack.translate(-0.5, -0.5, -0.5);

        poseStack.translate(TEXT_OFFSET_X, TEXT_OFFSET_Y, TEXT_OFFSET_Z);

        poseStack.mulPose(new Quaternionf().rotationXYZ(
                (float) Math.toRadians(ROTATION_X),
                (float) Math.toRadians(ROTATION_Y),
                (float) Math.toRadians(ROTATION_Z)));

        poseStack.scale(TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);

        long rawTime = level.getDayTime() % 24000;
        int hours = (int) ((rawTime / 1000 + 6) % 24);
        int minutes = (int) ((rawTime % 1000) * 60 / 1000);
        String text = String.format("%02d:%02d", hours, minutes);

        int color = 0xFFFFFF;
        float width = this.font.width(text);
        float height = this.font.lineHeight;

        float x = -width / 2f;
        float y = -height / 2f;

        this.font.drawInBatch(text, x, y, color, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL,
                0, packedLight);

        poseStack.popPose();
    }
}
