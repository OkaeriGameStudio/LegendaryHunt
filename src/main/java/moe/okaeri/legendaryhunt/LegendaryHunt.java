package moe.okaeri.legendaryhunt;

import moe.okaeri.legendaryhunt.config.LegendaryHuntConfig;
import moe.okaeri.legendaryhunt.effect.ModStatusEffects;
import moe.okaeri.legendaryhunt.hunt.LegendaryHuntManager;
import moe.okaeri.legendaryhunt.item.ModItems;
import moe.okaeri.legendaryhunt.loot.ModLootTableModifiers;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LegendaryHunt implements ModInitializer {
	public static final String MOD_ID = "legendaryhunt";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LegendaryHuntConfig.load();

		ModItems.register();
		ModStatusEffects.register();
		ModLootTableModifiers.register();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LegendaryHuntState.init(server.getOverworld().getSeed() ^ System.nanoTime());
		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			LegendaryHuntState.clear();
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			if (LegendaryHuntManager.noActiveHunts()) return;
			LegendaryHuntManager.disconnect(handler.getPlayer());
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (LegendaryHuntManager.noActiveHunts()) return;
			LegendaryHuntManager.tick(server);
		});

		LOGGER.info("LegendaryHunt >> Up and running!");
	}
}
