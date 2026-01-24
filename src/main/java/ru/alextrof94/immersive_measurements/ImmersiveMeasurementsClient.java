package ru.alextrof94.immersive_measurements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import ru.alextrof94.immersive_measurements.client.DepthMeterBlockRenderer;
import ru.alextrof94.immersive_measurements.client.DigitalClockBlockRenderer;
import ru.alextrof94.immersive_measurements.client.TriangulatorBlockRenderer;
import ru.alextrof94.immersive_measurements.items.DepthMeterRenderer;
import net.neoforged.neoforge.client.event.ModelEvent;
import ru.alextrof94.immersive_measurements.items.DigitalClockRenderer;
import ru.alextrof94.immersive_measurements.items.TriangulatorRenderer;
import java.util.List;

import static ru.alextrof94.immersive_measurements.ImmersiveMeasurements.LOGGER;
import static ru.alextrof94.immersive_measurements.ImmersiveMeasurements.MODID;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods
// in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ImmersiveMeasurementsClient {

    public ImmersiveMeasurementsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.DEPTH_METER.get(), DepthMeterBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DIGITAL_CLOCK.get(), DigitalClockBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.TRIANGULATOR.get(), TriangulatorBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerSpecialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(MODID, "depth_meter_text"),
                DepthMeterRenderer.Unbaked.CODEC);
        event.register(
                ResourceLocation.fromNamespaceAndPath(MODID, "digital_clock_text"),
                DigitalClockRenderer.Unbaked.CODEC);
        event.register(
                ResourceLocation.fromNamespaceAndPath(MODID, "triangulator_text"),
                TriangulatorRenderer.Unbaked.CODEC);
    }
}
