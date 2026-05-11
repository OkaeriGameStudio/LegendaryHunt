package moe.okaeri.legendaryhunt.hunt;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import moe.okaeri.legendaryhunt.LegendaryHuntState;
import moe.okaeri.legendaryhunt.config.LegendaryHuntConfig;
import moe.okaeri.legendaryhunt.item.RestoredMemoryItem;
import moe.okaeri.legendaryhunt.memory.MemoryType;
import moe.okaeri.legendaryhunt.spawning.LegendarySpawner;
import moe.okaeri.legendaryhunt.effect.ModStatusEffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public final class LegendaryHuntManager {
	private static final int HUNT_DURATION_TICKS = LegendaryHuntConfig.get().huntDurationTicks;
	private static final int CHECK_INTERVAL_TICKS = LegendaryHuntConfig.get().checkIntervalTick;
	private static final int SPAWN_IN_ONE_CHANCE = LegendaryHuntConfig.get().spawnOneInChance;

	private static final Object2ObjectOpenHashMap<UUID, ActiveHunt> ACTIVE_HUNTS = new Object2ObjectOpenHashMap<>();

	private static int serverTick;
	private static int nextGlobalWakeTick = Integer.MAX_VALUE;

	private LegendaryHuntManager() { }

	public static void activate(ServerPlayerEntity player, MemoryType memory) {
		UUID playerUuid = player.getUuid();
		ActiveHunt activeHunt = ACTIVE_HUNTS.get(playerUuid);

		if (activeHunt != null && activeHunt.memory == memory) {
			ACTIVE_HUNTS.remove(playerUuid);
			removeHuntEffects(player);
			rebuildWakeSchedule();

			player.sendMessage(Text.translatable("message.legendaryhunt.hunt_cancelled", memory.displayNameText()).formatted(Formatting.GRAY), true);
			return;
		}

		if (!isInValidBiome(player, memory)) {
			player.sendMessage(Text.translatable("message.legendaryhunt.wrong_biome", memory.displayNameText()).formatted(Formatting.RED), true);
			return;
		}

		int now = serverTick;
		boolean creative = player.getAbilities().creativeMode;

		int cachedSlot = creative ? -1 : findMemoryItemSlot(player, memory, -1);

		if (!creative && cachedSlot == -1) {
			player.sendMessage(Text.translatable("message.legendaryhunt.memory_missing").formatted(Formatting.RED), true);
			return;
		}

		ActiveHunt hunt = new ActiveHunt(memory, now + HUNT_DURATION_TICKS, now + CHECK_INTERVAL_TICKS, SPAWN_IN_ONE_CHANCE, cachedSlot);

		ACTIVE_HUNTS.put(playerUuid, hunt);

		if (activeHunt != null) {
			rebuildWakeSchedule();
		} else {
			rememberWake(hunt);
		}

		applyHuntEffect(player, hunt.expireTick - now);
		applyBiomeEffect(player, hunt.expireTick - now);

		player.sendMessage(Text.translatable(activeHunt == null ? "message.legendaryhunt.hunt_started" : "message.legendaryhunt.hunt_changed", memory.displayNameText()).formatted(Formatting.GOLD), true);
	}

	public static boolean noActiveHunts() {
		return ACTIVE_HUNTS.isEmpty();
	}

	public static void tick(MinecraftServer server) {
		if (ACTIVE_HUNTS.isEmpty()) return;

		int now = ++serverTick;

		if (!isDue(now, nextGlobalWakeTick)) return;

		nextGlobalWakeTick = Integer.MAX_VALUE;

		ObjectIterator<Object2ObjectMap.Entry<UUID, ActiveHunt>> iterator = ACTIVE_HUNTS.object2ObjectEntrySet().fastIterator();

		while (iterator.hasNext()) {
			Object2ObjectMap.Entry<UUID, ActiveHunt> entry = iterator.next();
			ActiveHunt hunt = entry.getValue();

			if (isDue(now, hunt.expireTick)) {
				ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());

				if (player != null) {
					removeHuntEffects(player);
					player.sendMessage(Text.translatable("message.legendaryhunt.hunt_expired", hunt.memory.displayNameText()).formatted(Formatting.RED), true);
				}

				iterator.remove();
				continue;
			}

			if (!isDue(now, hunt.nextCheckTick)) {
				rememberWake(hunt);
				continue;
			}

			hunt.nextCheckTick = now + CHECK_INTERVAL_TICKS;

			ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());

			if (player == null) {
				iterator.remove();
				continue;
			}

			ensureHuntEffect(player, hunt, now);

			boolean validBiome = isInValidBiomeCached(player, hunt);

			syncBiomeEffect(player, hunt, now, validBiome);

			if (!validBiome) {
				rememberWake(hunt);
				continue;
			}

			if (!LegendaryHuntState.rng().oneIn(hunt.windowOneInChance)) {
				rememberWake(hunt);
				continue;
			}

			boolean creative = player.getAbilities().creativeMode;

			int memorySlot = creative ? -1 : findMemoryItemSlot(player, hunt.memory, hunt.cachedMemorySlot);

			if (!creative && memorySlot == -1) {
				removeHuntEffects(player);

				player.sendMessage(Text.translatable("message.legendaryhunt.memory_missing").formatted(Formatting.RED), true);

				iterator.remove();
				continue;
			}

			hunt.cachedMemorySlot = memorySlot;

			PokemonEntity entity = LegendarySpawner.spawnLegendary(player, hunt.memory);

			if (entity == null) {
				rememberWake(hunt);
				continue;
			}

			if (!creative) player.getInventory().getStack(memorySlot).decrement(1);

			removeHuntEffects(player);

			player.sendMessage(Text.translatable("message.legendaryhunt.legendary_answered", hunt.memory.displayNameText()).formatted(Formatting.GOLD), true);
			player.playSoundToPlayer(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0F, 1.0F);

			iterator.remove();
		}
	}

	private static boolean isInValidBiome(ServerPlayerEntity player, MemoryType memory) {
		ServerWorld world = player.getServerWorld();
		BlockPos blockPos = player.getBlockPos();

		return world.getBiome(blockPos).isIn(memory.biomeTag());
	}

	private static boolean isInValidBiomeCached(ServerPlayerEntity player, ActiveHunt hunt) {
		ServerWorld world = player.getServerWorld();
		BlockPos blockPos = player.getBlockPos();
		long packedPos = blockPos.asLong();

		if (hunt.cachedBiomeWorld == world && hunt.cachedBiomeBlockPos == packedPos) return hunt.cachedBiomeValid;

		boolean valid = world.getBiome(blockPos).isIn(hunt.memory.biomeTag());

		hunt.cachedBiomeWorld = world;
		hunt.cachedBiomeBlockPos = packedPos;
		hunt.cachedBiomeValid = valid;

		return valid;
	}

	private static int findMemoryItemSlot(ServerPlayerEntity player, MemoryType memory, int preferredSlot) {
		PlayerInventory inventory = player.getInventory();

		if (preferredSlot >= 0 && preferredSlot < inventory.size()) {
			ItemStack stack = inventory.getStack(preferredSlot);

			if (isMatchingMemory(stack, memory)) return preferredSlot;
		}

		for (int slot = 0; slot < inventory.size(); slot++) {
			if (slot == preferredSlot) continue;

			ItemStack stack = inventory.getStack(slot);

			if (isMatchingMemory(stack, memory)) return slot;
		}

		return -1;
	}

	private static boolean isMatchingMemory(ItemStack stack, MemoryType memory) {
		if (stack.isEmpty()) return false;
		if (!(stack.getItem() instanceof RestoredMemoryItem restoredMemoryItem)) return false;

		return restoredMemoryItem.getMemory() == memory;
	}

	private static void rememberWake(ActiveHunt hunt) {
		int wakeTick = isBefore(hunt.nextCheckTick, hunt.expireTick) ? hunt.nextCheckTick : hunt.expireTick;

		if (isBefore(wakeTick, nextGlobalWakeTick)) nextGlobalWakeTick = wakeTick;
	}

	private static boolean isDue(int now, int tick) {
		return now - tick >= 0;
	}

	private static boolean isBefore(int a, int b) {
		return a - b < 0;
	}

	public static void disconnect(ServerPlayerEntity player) {
		if (ACTIVE_HUNTS.remove(player.getUuid()) == null) return;
		removeHuntEffects(player);
	}

	private static void applyHuntEffect(ServerPlayerEntity player, int durationTicks) {
		player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.LEGENDARY_HUNT, Math.max(1, durationTicks), 0, false, false, true));
	}

	private static void ensureHuntEffect(ServerPlayerEntity player, ActiveHunt hunt, int now) {
		if (player.hasStatusEffect(ModStatusEffects.LEGENDARY_HUNT)) return;
		applyHuntEffect(player, hunt.expireTick - now);
	}

	private static void removeHuntEffect(ServerPlayerEntity player) {
		player.removeStatusEffect(ModStatusEffects.LEGENDARY_HUNT);
	}

	private static void applyBiomeEffect(ServerPlayerEntity player, int durationTicks) {
		player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.LEGENDARY_HUNT_RESONANCE, Math.max(1, durationTicks), 0, false, false, true));
	}

	private static void ensureBiomeEffect(ServerPlayerEntity player, ActiveHunt hunt, int now) {
		if (player.hasStatusEffect(ModStatusEffects.LEGENDARY_HUNT_RESONANCE)) return;
		applyBiomeEffect(player, hunt.expireTick - now);
	}

	private static void removeBiomeEffect(ServerPlayerEntity player) {
		player.removeStatusEffect(ModStatusEffects.LEGENDARY_HUNT_RESONANCE);
	}

	private static void syncBiomeEffect(ServerPlayerEntity player, ActiveHunt hunt, int now, boolean validBiome) {
		if (validBiome) {
			ensureBiomeEffect(player, hunt, now);
		} else {
			removeBiomeEffect(player);
		}

		if (hunt.wasInValidBiome == validBiome) return;
		hunt.wasInValidBiome = validBiome;

		String messageKey = validBiome ? "message.legendaryhunt.memory_stronger" : "message.legendaryhunt.memory_weaker";
		Formatting formatting = validBiome ? Formatting.GOLD : Formatting.RED;
		player.sendMessage(Text.translatable(messageKey, hunt.memory.displayNameText()).formatted(formatting), true);
	}

	private static void removeHuntEffects(ServerPlayerEntity player) {
		removeHuntEffect(player);
		removeBiomeEffect(player);
	}

	private static void rebuildWakeSchedule() {
		nextGlobalWakeTick = Integer.MAX_VALUE;

		for (ActiveHunt hunt : ACTIVE_HUNTS.values()) {
			rememberWake(hunt);
		}
	}

	private static final class ActiveHunt {
		private final MemoryType memory;
		private final int expireTick;
		private final int windowOneInChance;

		private int nextCheckTick;
		private int cachedMemorySlot;

		private ServerWorld cachedBiomeWorld;
		private long cachedBiomeBlockPos = Long.MIN_VALUE;
		private boolean cachedBiomeValid;

		private boolean wasInValidBiome = true;

		private ActiveHunt(MemoryType memory, int expireTick, int nextCheckTick, int windowOneInChance, int cachedMemorySlot) {
			this.memory = memory;
			this.expireTick = expireTick;
			this.nextCheckTick = nextCheckTick;
			this.windowOneInChance = windowOneInChance;
			this.cachedMemorySlot = cachedMemorySlot;
		}
	}
}