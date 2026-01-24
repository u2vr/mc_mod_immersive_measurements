package ru.alextrof94.immersive_measurements;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.alextrof94.immersive_measurements.blocks.DepthMeterBlock;
import ru.alextrof94.immersive_measurements.blocks.DigitalClockBlock;
import ru.alextrof94.immersive_measurements.blocks.TriangulatorBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ImmersiveMeasurements.MODID);

    public static final DeferredBlock<DepthMeterBlock> DEPTH_METER = BLOCKS.registerBlock("depth_meter",
            DepthMeterBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F).noOcclusion());

    public static final DeferredBlock<DigitalClockBlock> DIGITAL_CLOCK = BLOCKS.registerBlock("digital_clock",
            DigitalClockBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F).noOcclusion());

    public static final DeferredBlock<TriangulatorBlock> TRIANGULATOR = BLOCKS.registerBlock("triangulator",
            TriangulatorBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F).noOcclusion());
}
