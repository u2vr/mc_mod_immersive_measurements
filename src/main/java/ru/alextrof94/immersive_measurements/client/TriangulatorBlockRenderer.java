package ru.alextrof94.immersive_measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import org.joml.Quaternionf;
import ru.alextrof94.immersive_measurements.blockentities.TriangulatorBlockEntity;
import ru.alextrof94.immersive_measurements.blocks.TriangulatorBlock;

public class TriangulatorBlockRenderer implements BlockEntityRenderer<TriangulatorBlockEntity> {
    // Configurable text rendering parameters
    // Display is max width 9.175. Offset slightly to 9.1
    private static final float TEXT_OFFSET_X = 9.1f / 16f;
    private static final float TEXT_OFFSET_Y = 5.025f / 16f;
    private static final float TEXT_OFFSET_Z = 0.5f;

    // Rotations in degrees
    private static final float ROTATION_X = 0f;
    private static final float ROTATION_Y = -90f;
    private static final float ROTATION_Z = 0f;

    // Scale factor
    private static final float TEXT_SCALE = 0.012f; // Slightly smaller for 2 lines

    private final Font font;

    public TriangulatorBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
    }

    @Override
    public void render(TriangulatorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay,
            net.minecraft.world.phys.Vec3 cameraPos) {

        Direction facing = Direction.WEST;
        if (blockEntity.getBlockState().hasProperty(TriangulatorBlock.FACING)) {
            facing = blockEntity.getBlockState().getValue(TriangulatorBlock.FACING);
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

        GlobalPos pos = blockEntity.getTriangulatedPos();
        String line1 = "x: N/A";
        String line2 = "z: N/A";

        if (pos != null) {
            line1 = "x: " + pos.pos().getX();
            line2 = "z: " + pos.pos().getZ();
        }

        float h = this.font.lineHeight;

        // Draw 2 lines centered around Y=0
        // Line 1 above center
        this.font.drawInBatch(line1, -this.font.width(line1) / 2f, -h, 0xFFFFFF, false, poseStack.last().pose(),
                bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
        // Line 2 at/below center
        this.font.drawInBatch(line2, -this.font.width(line2) / 2f, 0, 0xFFFFFF, false, poseStack.last().pose(),
                bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }
}
