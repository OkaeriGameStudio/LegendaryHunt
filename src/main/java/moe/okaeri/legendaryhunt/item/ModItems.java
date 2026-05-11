package moe.okaeri.legendaryhunt.item;

import moe.okaeri.legendaryhunt.LegendaryHunt;
import moe.okaeri.legendaryhunt.memory.MemoryType;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.EnumMap;

public final class ModItems {
	private static final EnumMap<MemoryType, Item> MEMORY_ITEMS = new EnumMap<>(MemoryType.class);

	public static final Item MEMORY_FRAGMENT = register("memory_fragment", new MemoryFragmentItem(new Item.Settings().maxCount(16)));
	public static final Item THERMO_REAGENT = register("thermocatalytic_reagent", new ThermoReagentItem(new Item.Settings().maxCount(16)));

	public static final ItemGroup LEGENDARY_HUNT_GROUP = Registry.register(
			Registries.ITEM_GROUP,
			Identifier.of(LegendaryHunt.MOD_ID, "legendary_hunt"),
			FabricItemGroup.builder().icon(() -> new ItemStack(MEMORY_FRAGMENT)).displayName(Text.translatable("itemGroup.legendaryhunt.legendary_hunt")).entries((displayContext, entries) -> {
						entries.add(MEMORY_FRAGMENT);
						entries.add(THERMO_REAGENT);

						for (Item item : MEMORY_ITEMS.values()) {
							entries.add(item);
						}
					}).build()
	);

	private ModItems() { }

	public static void register() {
		registerMemoryItems();
	}

	private static void registerMemoryItems() {
		if (!MEMORY_ITEMS.isEmpty()) return;

		for (MemoryType memory : MemoryType.values()) {
			String itemId = memory.id() + "_memory";

			Item item = register(itemId, new RestoredMemoryItem(new Item.Settings().maxCount(16), memory));

			MEMORY_ITEMS.put(memory, item);
		}
	}

	private static Item register(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(LegendaryHunt.MOD_ID, name), item);
	}
}