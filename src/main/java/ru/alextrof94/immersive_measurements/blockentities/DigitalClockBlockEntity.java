package ru.alextrof94.immersive_measurements.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.alextrof94.immersive_measurements.ModBlockEntities;

public class DigitalClockBlockEntity extends BlockEntity {
    public DigitalClockBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DIGITAL_CLOCK.get(), pos, blockState);
    }
}
