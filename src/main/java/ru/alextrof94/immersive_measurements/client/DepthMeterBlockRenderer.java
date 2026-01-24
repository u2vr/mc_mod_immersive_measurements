package ru.alextrof94.immersive_measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import ru.alextrof94.immersive_measurements.blockentities.DepthMeterBlockEntity;
import ru.alextrof94.immersive_measurements.blocks.DepthMeterBlock;

public class DepthMeterBlockRenderer implements BlockEntityRenderer<DepthMeterBlockEntity> {
    // Configurable text rendering parameters
    // Position offsets (in blocks, 1 block = 16 pixels)
    private static final float TEXT_OFFSET_X = 7.48f / 16f;
    private static final float TEXT_OFFSET_Y = 9f / 16f;
    private static final float TEXT_OFFSET_Z = 0.5f;

    // Rotations in degrees
    private static final float ROTATION_X = 0f;
    private static final float ROTATION_Y = -90f;
    private static final float ROTATION_Z = 0f;

    // Scale factor
    private static final float TEXT_SCALE = 0.015f;

    private final Font font;

    public DepthMeterBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(DepthMeterBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {

        Direction facing = Direction.WEST;
        if (blockEntity.getBlockState().hasProperty(DepthMeterBlock.FACING)) {
            facing = blockEntity.getBlockState().getValue(DepthMeterBlock.FACING);
        }

        poseStack.pushPose();

        // Rotate around block center based on facing
        poseStack.translate(0.5, 0.5, 0.5);
        float rot = -facing.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(rot + 90));
        poseStack.translate(-0.5, -0.5, -0.5);

        // Apply configurable translations
        poseStack.translate(TEXT_OFFSET_X, TEXT_OFFSET_Y, TEXT_OFFSET_Z);

        // Apply configurable rotations
        poseStack.mulPose(new Quaternionf().rotationXYZ(
            (float) Math.toRadians(ROTATION_X),
            (float) Math.toRadians(ROTATION_Y),
            (float) Math.toRadians(ROTATION_Z)
        ));

        // Apply scale (negative Y to flip text upright)
        poseStack.scale(TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);

        int yValue = blockEntity.getYValue();
        String text = String.valueOf(yValue);

        int color = 0xFFFFFF;
        float width = this.font.width(text);
        float height = this.font.lineHeight;

        float x = -width / 2f;
        float y = -height / 2f;

        this.font.drawInBatch(text, x, y, color, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }
}