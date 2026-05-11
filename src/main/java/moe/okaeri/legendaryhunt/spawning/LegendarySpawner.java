package moe.okaeri.legendaryhunt.spawning;

import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;

import moe.okaeri.legendaryhunt.config.LegendaryHuntConfig;
import moe.okaeri.legendaryhunt.memory.MemoryType;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

import java.util.Set;

import static moe.okaeri.legendaryhunt.LegendaryHuntState.rng;
import static moe.okaeri.legendaryhunt.LegendaryHunt.LOGGER;

public final class LegendarySpawner {
	private static final int SPAWN_RADIUS = LegendaryHuntConfig.get().spawnRangeBlocks;
	private static final int MIN_SPAWN_DISTANCE = LegendaryHuntConfig.get().minSpawnDistance;
	private static final int SPAWN_ATTEMPTS = LegendaryHuntConfig.get().spawnAttempts;

	private static final int MIN_SPAWN_DISTANCE_SQUARED = MIN_SPAWN_DISTANCE * MIN_SPAWN_DISTANCE;

	private static final Stat[] IV_STATS = {
			Stats.HP,
			Stats.ATTACK,
			Stats.DEFENCE,
			Stats.SPECIAL_ATTACK,
			Stats.SPECIAL_DEFENCE,
			Stats.SPEED
	};

	private LegendarySpawner() { }

	public static PokemonEntity spawnLegendary(ServerPlayerEntity player, MemoryType memory) {
		ServerWorld world = player.getServerWorld();

		if (world.isClient()) return null;

		Species species = PokemonSpecies.getByName(memory.species());
		Set<String> aspects = memory.aspects();

		if (species == null) {
			LOGGER.error("LegendaryHunt >> Unknown Cobblemon species while trying to spawn: {}", memory.species());
			return null;
		}

		Pokemon pokemon = new Pokemon();

		pokemon.setSpecies(species);

		if (aspects != null && !aspects.isEmpty()) pokemon.setForcedAspects(aspects);

		pokemon.setLevel(memory.level());
		pokemon.setShiny(rng().oneIn(LegendaryHuntConfig.get().legendaryShinyOneInChance));

		pokemon.initialize();

		applyLegendaryIVs(pokemon);

		PokemonEntity entity = new PokemonEntity(world, pokemon, CobblemonEntities.POKEMON);

		Vec3d spawnPos = findValidSpawnPos(world, player.getBlockPos(), memory, entity);

		if (spawnPos == null) return null;

		entity.refreshPositionAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, rng().nextFloat() * 360.0F, 0.0F);

		entity.setCountsTowardsSpawnCap(false);

		if (!world.spawnEntity(entity)) return null;

		return entity;
	}

	private static Vec3d findValidSpawnPos(ServerWorld world, BlockPos center, MemoryType memory, PokemonEntity entity) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int attempt = 0; attempt < SPAWN_ATTEMPTS; attempt++) {
			int dx = rng().nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
			int dz = rng().nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;

			if (dx * dx + dz * dz < MIN_SPAWN_DISTANCE_SQUARED) continue;

			int x = center.getX() + dx;
			int z = center.getZ() + dz;
			int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);

			mutable.set(x, y - 1, z);

			if (world.getFluidState(mutable).isIn(FluidTags.WATER)) y--;

			mutable.set(x, y, z);

			if (!world.getWorldBorder().contains(mutable)) continue;
			if (!world.getBiome(mutable).isIn(memory.biomeTag())) continue;
			if (!isValidEntitySpawnSpot(world, entity, x + 0.5D, y, z + 0.5D)) continue;

			return new Vec3d(x + 0.5D, y, z + 0.5D);
		}

		return null;
	}

	private static boolean isValidEntitySpawnSpot(ServerWorld world, PokemonEntity entity, double x, double y, double z) {
		if (y <= world.getBottomY() || y >= world.getTopY()) return false;

		BlockPos feetPos = BlockPos.ofFloored(x, y, z);
		BlockPos groundPos = feetPos.down();

		BlockState groundState = world.getBlockState(groundPos);

		boolean hasSolidFloor = groundState.isSideSolidFullSquare(world, groundPos, Direction.UP);
		boolean feetInWater = world.getFluidState(feetPos).isIn(FluidTags.WATER);
		boolean feetInLava = world.getFluidState(feetPos).isIn(FluidTags.LAVA);

		if (feetInLava) return false;
		if (!hasSolidFloor && !feetInWater) return false;

		float yaw = rng().nextFloat() * 360.0F;

		entity.refreshPositionAndAngles(x, y, z, yaw, 0.0F);

		return world.isSpaceEmpty(entity);
	}

	private static void applyLegendaryIVs(Pokemon pokemon) {
		int perfectMask = 0;

		for (int count = 0; count < 3;) {
			int index = rng().nextInt(IV_STATS.length);
			int bit = 1 << index;

			if ((perfectMask & bit) != 0) continue;

			perfectMask |= bit;
			count++;
		}

		for (int i = 0; i < IV_STATS.length; i++) {
			int value = ((perfectMask & (1 << i)) != 0) ? 31 : rng().nextInt(32);
			pokemon.setIV(IV_STATS[i], value);
		}
	}
}