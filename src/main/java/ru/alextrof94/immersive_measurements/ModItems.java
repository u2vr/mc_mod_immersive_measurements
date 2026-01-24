package ru.alextrof94.immersive_measurements;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.alextrof94.immersive_measurements.items.DepthMeterItem;
import ru.alextrof94.immersive_measurements.items.DigitalClockItem;
import ru.alextrof94.immersive_measurements.items.TriangulatorItem;

import static ru.alextrof94.immersive_measurements.ImmersiveMeasurements.LOGGER;
import static ru.alextrof94.immersive_measurements.ImmersiveMeasurements.MODID;

public class ModItems {

        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

        public static final DeferredItem<Item> DEPTH_METER = ITEMS.registerItem(
                        "depth_meter",
                        properties -> new DepthMeterItem(ModBlocks.DEPTH_METER.get(), properties),
                        new Item.Properties());
        public static final DeferredItem<Item> DIGITAL_CLOCK = ITEMS.registerItem(
                        "digital_clock",
                        properties -> new DigitalClockItem(ModBlocks.DIGITAL_CLOCK.get(), properties),
                        new Item.Properties());
        public static final DeferredItem<Item> TRIANGULATOR = ITEMS.registerItem(
                        "triangulator",
                        properties -> new TriangulatorItem(ModBlocks.TRIANGULATOR.get(), properties),
                        new Item.Properties());

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }

        public static void updateItemModelFromLeds(ItemStack stack, String baseName) {
                int n = stack.getOrDefault(ModDataComponents.LEDS_COUNT.get(), 0);

                // clamp 0..3
                if (n < 0)
                        n = 0;
                if (n > 3)
                        n = 3;

                ResourceLocation model = ResourceLocation.fromNamespaceAndPath(
                                MODID,
                                baseName + "_" + n);

                // ВАЖНО: это id client item json из assets/<ns>/items/<id>.json
                stack.set(DataComponents.ITEM_MODEL, model);
        }
        // ГОТОВО Depthmeter - отображает глубину (типа барометр)
        // ГОТОВО DigitalClock - цифровые часы
        // ГОТОВО Triangulator крафтится из 3-х компасов, магнитного камня и прочего,
        // работает за счёт хранения в себе 3-х позиций магнитных камней (которые должны
        // быть на расстоянии 100м друг от друга, если сломать - показания ломаются
        // также), отображает x-z координаты

        // GPS собирается из триангулятора, глубиномера и цифровых часов, отображает все
        // 3 координаты и время (требования как у триангулятора)
        // SCANNER сканирует блок, выдавая о нём информацию.
}
