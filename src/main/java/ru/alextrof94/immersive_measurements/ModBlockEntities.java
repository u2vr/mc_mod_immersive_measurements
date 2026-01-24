package ru.alextrof94.immersive_measurements;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.alextrof94.immersive_measurements.blockentities.DepthMeterBlockEntity;
import ru.alextrof94.immersive_measurements.blockentities.DigitalClockBlockEntity;
import ru.alextrof94.immersive_measurements.blockentities.TriangulatorBlockEntity;

public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(Registries.BLOCK_ENTITY_TYPE, ImmersiveMeasurements.MODID);

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DepthMeterBlockEntity>> DEPTH_METER = BLOCK_ENTITIES
                        .register("depth_meter",
                                        () -> new BlockEntityType<>(DepthMeterBlockEntity::new,
                                                        ModBlocks.DEPTH_METER.get()));

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DigitalClockBlockEntity>> DIGITAL_CLOCK = BLOCK_ENTITIES
                        .register("digital_clock",
                                        () -> new BlockEntityType<>(DigitalClockBlockEntity::new,
                                                        ModBlocks.DIGITAL_CLOCK.get()));

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TriangulatorBlockEntity>> TRIANGULATOR = BLOCK_ENTITIES
                        .register("triangulator",
                                        () -> new BlockEntityType<>(TriangulatorBlockEntity::new,
                                                        ModBlocks.TRIANGULATOR.get()));
}
