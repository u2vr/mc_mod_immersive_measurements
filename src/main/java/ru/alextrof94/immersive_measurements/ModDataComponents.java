package ru.alextrof94.immersive_measurements;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import com.mojang.serialization.Codec;

import java.util.List;

import static ru.alextrof94.immersive_measurements.ImmersiveMeasurements.LOGGER;
import static ru.alextrof94.immersive_measurements.ImmersiveMeasurements.MODID;

public class ModDataComponents {
    // Создаем регистратор для компонентов
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SAVED_Y =
            COMPONENTS.register("saved_y", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<GlobalPos>>> LODESTONE_POSITIONS =
            COMPONENTS.register("lodestone_positions", () -> DataComponentType.<List<GlobalPos>>builder()
                    .persistent(GlobalPos.CODEC.listOf()) // Кодек для списка GlobalPos
                    .networkSynchronized(GlobalPos.STREAM_CODEC.apply(ByteBufCodecs.list())) // Сетевая синхронизация списка
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> TRIANGULATED_POS =
            COMPONENTS.register("triangulated_pos", () -> DataComponentType.<GlobalPos>builder()
                    .persistent(GlobalPos.CODEC)
                    .networkSynchronized(GlobalPos.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LEDS_COUNT =
            COMPONENTS.register("leds_count", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT) // Сохраняется на диск
                    .networkSynchronized(ByteBufCodecs.VAR_INT) // Синхронизируется с клиентом (ВАЖНО!)
                    .build());

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
