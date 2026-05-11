package moe.okaeri.legendaryhunt.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class MemoryFragmentItem extends Item {
	public MemoryFragmentItem(Settings settings) {
		super(settings);
	}

	@Override
	public Text getName(ItemStack stack) {
		return Text.translatable(this.getTranslationKey(stack)).formatted(Formatting.GOLD);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add(Text.translatable("item.legendaryhunt.memory_fragment.tooltip.1").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.legendaryhunt.memory_fragment.tooltip.2").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.legendaryhunt.memory_fragment.tooltip.3").formatted(Formatting.GRAY));
	}
}