package moe.okaeri.legendaryhunt.loot;

import moe.okaeri.legendaryhunt.config.LegendaryHuntConfig;
import moe.okaeri.legendaryhunt.item.ModItems;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;

import java.util.Set;

public final class ModLootTableModifiers {
	private static final Set<RegistryKey<LootTable>> MEMORY_FRAGMENT_CHESTS = Set.of(
			LootTables.SIMPLE_DUNGEON_CHEST,
			LootTables.ABANDONED_MINESHAFT_CHEST,
			LootTables.DESERT_PYRAMID_CHEST,
			LootTables.JUNGLE_TEMPLE_CHEST,
			LootTables.WOODLAND_MANSION_CHEST,
			LootTables.ANCIENT_CITY_CHEST,
			LootTables.STRONGHOLD_CORRIDOR_CHEST,
			LootTables.STRONGHOLD_LIBRARY_CHEST,
			LootTables.END_CITY_TREASURE_CHEST,
			LootTables.BURIED_TREASURE_CHEST
	);

	private ModLootTableModifiers() { }

	public static void register() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!source.isBuiltin()) return;

			if (MEMORY_FRAGMENT_CHESTS.contains(key)) {
				tableBuilder.pool(memoryFragmentPool(LegendaryHuntConfig.get().memoryFragmentChestChance));
				return;
			}

			if (key == LootTables.FISHING_GAMEPLAY) tableBuilder.pool(memoryFragmentPool(LegendaryHuntConfig.get().memoryFragmentFishingChance));
		});
	}

	private static LootPool.Builder memoryFragmentPool(float chance) {
		return LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).conditionally(RandomChanceLootCondition.builder(chance)).with(ItemEntry.builder(ModItems.MEMORY_FRAGMENT));
	}
}