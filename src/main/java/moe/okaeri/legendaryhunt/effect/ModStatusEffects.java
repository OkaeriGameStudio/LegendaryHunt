package moe.okaeri.legendaryhunt.effect;

import moe.okaeri.legendaryhunt.LegendaryHunt;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class ModStatusEffects {
	public static final RegistryEntry<StatusEffect> LEGENDARY_HUNT = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(LegendaryHunt.MOD_ID, "legendary_hunt"), new LegendaryHuntStatusEffect());
	public static final RegistryEntry<StatusEffect> LEGENDARY_HUNT_RESONANCE = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(LegendaryHunt.MOD_ID, "legendary_hunt_resonance"), new LegendaryHuntResonanceStatusEffect());

	private ModStatusEffects() { }

	public static void register() { }
}