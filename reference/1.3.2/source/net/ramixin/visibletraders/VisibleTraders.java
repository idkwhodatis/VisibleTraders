package net.ramixin.visibletraders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.ramixin.visibletraders.threading.SerializableListing;
import net.ramixin.visibletraders.threading.TradeWorker;
import org.slf4j.Logger;

import java.util.function.Function;

public class VisibleTraders implements ModInitializer {

    public static final String MOD_NAME = "Visible Traders";
    public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MOD_NAME);

    public static final TradeWorker TRADE_WORKER = new TradeWorker();

    private static final class_5321<class_2378<MapCodec<? extends SerializableListing>>> LISTINGS_REGISTRY_KEY = class_5321.method_29180(class_2960.method_60654("visibletraders:listings"));
    public static final class_2378<MapCodec<? extends SerializableListing>> LISTINGS_REGISTRY = FabricRegistryBuilder.createSimple(LISTINGS_REGISTRY_KEY).buildAndRegister();

    public static final Codec<SerializableListing> CODEC = LISTINGS_REGISTRY.method_39673().dispatch(SerializableListing::visibleTrades$getCodec, Function.identity());

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing (1/1)");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> startTradeWorker());
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> TRADE_WORKER.stop());

        class_2378.method_10230(LISTINGS_REGISTRY, class_2960.method_60655("minecraft", "treasure_map_for_emeralds"), ListingCodecs.NORMALIZED_TREASURE_MAP_FOR_EMERALDS_CODEC);
    }

    private static void startTradeWorker() {
        TRADE_WORKER.reset();
        Thread thread = new Thread(TRADE_WORKER);
        thread.setName("Visible Traders Trade Thread");
        thread.start();
    }
}
