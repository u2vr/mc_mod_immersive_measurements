package ru.alextrof94.immersive_measurements.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import ru.alextrof94.immersive_measurements.ModDataComponents;

public class DepthMeterItem extends BlockItem {
    public DepthMeterItem(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide) {
            int currentY = player.blockPosition().getY();
            player.getItemInHand(hand).set(ModDataComponents.SAVED_Y.get(), currentY);
        }
        return InteractionResult.SUCCESS;
    }
}
