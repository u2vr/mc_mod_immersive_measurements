package ru.alextrof94.immersive_measurements.items;

import net.minecraft.core.BlockPos;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpeedometerItem extends BaseDisplayItem {
    public SpeedometerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAction(Level level, @NotNull Player player) {
    }

    @Override
    public InteractionResult rightClickAction(Level level, @NotNull Player player, @Nullable BlockPos blockPos) {
        return InteractionResult.SUCCESS;
    }
}
