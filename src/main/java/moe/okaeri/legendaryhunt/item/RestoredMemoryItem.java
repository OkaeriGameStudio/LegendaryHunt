package moe.okaeri.legendaryhunt.item;

import moe.okaeri.legendaryhunt.config.LegendaryHuntConfig;
import moe.okaeri.legendaryhunt.hunt.LegendaryHuntManager;
import moe.okaeri.legendaryhunt.memory.MemoryType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class RestoredMemoryItem extends Item {
	private static final String NAME_TRANSLATION_KEY = "item.legendaryhunt.reformed_memory";
	private static final int USE_COOLDOWN_TICKS = LegendaryHuntConfig.get().reformedMemoryCooldown;

	private final MemoryType memory;

	private Text cachedMemoryDisplayName;
	private Text cachedBiomeDisplayName;

	public RestoredMemoryItem(Settings settings, MemoryType memory) {
		super(settings);
		this.memory = memory;
	}

	public MemoryType getMemory() {
		return memory;
	}

	private Text memoryDisplayName() {
		if (cachedMemoryDisplayName == null) cachedMemoryDisplayName = memory.displayNameText();
		return cachedMemoryDisplayName.copy();
	}

	private Text biomeDisplayName() {
		if (cachedBiomeDisplayName == null) cachedBiomeDisplayName = memory.biomeDisplayNameText();
		return cachedBiomeDisplayName.copy();
	}

	@Override
	public Text getName(ItemStack stack) {
		return Text.translatable(NAME_TRANSLATION_KEY, memoryDisplayName()).formatted(Formatting.LIGHT_PURPLE);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (world.isClient()) return TypedActionResult.success(stack, true);
		if (!(user instanceof ServerPlayerEntity serverPlayer)) return TypedActionResult.pass(stack);
		if (serverPlayer.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.fail(stack);

		serverPlayer.getItemCooldownManager().set(this, USE_COOLDOWN_TICKS);
		LegendaryHuntManager.activate(serverPlayer, memory);

		return TypedActionResult.success(stack, false);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("tooltip.legendaryhunt.reformed_memory.tooltip.1").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("tooltip.legendaryhunt.reformed_memory.tooltip.2", biomeDisplayName().copy().formatted(Formatting.GOLD)).formatted(Formatting.GRAY));
	}
}