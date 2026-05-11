package moe.okaeri.legendaryhunt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import moe.okaeri.legendaryhunt.LegendaryHunt;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class LegendaryHuntConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("legendaryhunt.json");

	public static final int VERSION = 1;

	private static Values values = new Values();

	private LegendaryHuntConfig() { }

	public static Values get() {
		return values;
	}

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) {
			LegendaryHunt.LOGGER.info("LegendaryHunt >> Config not found, creating default config.");
			values = new Values();
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			Values loaded = GSON.fromJson(reader, Values.class);

			if (loaded == null) {
				LegendaryHunt.LOGGER.warn("LegendaryHunt >> Config was empty or invalid, using defaults.");
				values = new Values();
				save();
				return;
			}

			boolean needsSave = false;

			if (!Objects.equals(loaded.version, VERSION)) {
				LegendaryHunt.LOGGER.warn("LegendaryHunt >> Config version is different, updating...");
				loaded.version = VERSION;

				// Do stuff idk

				needsSave = true;
			}

			values = loaded;

			if (needsSave) save();

			LegendaryHunt.LOGGER.info("LegendaryHunt >> Loaded config.");
		} catch (IOException | JsonParseException exception) {
			LegendaryHunt.LOGGER.error("LegendaryHunt >> Failed to load config, using defaults.", exception);
			values = new Values();
			save();
		}
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());

			values.version = VERSION;

			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
				GSON.toJson(values, writer);
			}

			LegendaryHunt.LOGGER.info("LegendaryHunt >> Saved config.");
		} catch (IOException exception) {
			LegendaryHunt.LOGGER.error("LegendaryHunt >> Failed to save config.", exception);
		}
	}

	public static final class Values {
		public int version = VERSION;

		public int huntDurationTicks = 72000;
		public int checkIntervalTick = 20;

		public int spawnRangeBlocks = 24;
		public int minSpawnDistance = 6;
		public int spawnAttempts = 48;

		public int reformedMemoryCooldown = 60;

		public float memoryFragmentChestChance = 0.5F;
		public float memoryFragmentFishingChance = 0.01F;

		public int spawnOneInChance = 2048;
		public int legendaryShinyOneInChance = 8192;
	}
}