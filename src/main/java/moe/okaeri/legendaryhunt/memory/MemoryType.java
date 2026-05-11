package moe.okaeri.legendaryhunt.memory;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Set;

public enum MemoryType {
	ARCEUS("arceus", "arceus", 80, cobblemonBiome("is_peak")),
	ARTICUNO("articuno", "articuno", 50, cobblemonBiome("is_glacial")),
	ARTICUNO_GALARIAN("articuno_galarian", "articuno", "galarian", Set.of("galarian"), 70, cobblemonBiome("is_glacial")),
	AZELF("azelf", "azelf", 50, cobblemonBiome("is_freshwater")),
	CALYREX("calyrex", "calyrex", 80, cobblemonBiome("is_snowy_forest")),
	CELEBI("celebi", "celebi", 30, cobblemonBiome("is_magical")),
	CHIENPAO("chienpao", "chienpao", 60, cobblemonBiome("is_glacial")),
	CHIYU("chiyu", "chiyu", 60, cobblemonBiome("is_badlands")),
	COBALION("cobalion", "cobalion", 45, cobblemonBiome("is_highlands")),
	COSMOG("cosmog", "cosmog", 5, cobblemonBiome("is_end")),
	CRESSELIA("cresselia", "cresselia", 50, cobblemonBiome("is_magical")),
	DARKRAI("darkrai", "darkrai", 40, cobblemonBiome("is_spooky")),
	DEOXYS("deoxys", "deoxys", 30, cobblemonBiome("is_end")),
	DIALGA("dialga", "dialga", 50, cobblemonBiome("is_peak")),
	DIANCIE("diancie", "diancie", 50, cobblemonBiome("is_cave")),
	ENAMORUS("enamorus", "enamorus", 70, cobblemonBiome("is_floral")),
	ENTEI("entei", "entei", 40, cobblemonBiome("is_badlands")),
	ETERNATUS("eternatus", "eternatus", 60, cobblemonBiome("is_end")),
	FEZANDIPITI("fezandipiti", "fezandipiti", 70, cobblemonBiome("is_forest")),
	GIRATINA("giratina", "giratina", 70, cobblemonBiome("is_end")),
	GLASTRIER("glastrier", "glastrier", 75, cobblemonBiome("is_glacial")),
	GROUDON("groudon", "groudon", 45, cobblemonBiome("is_badlands")),
	HEATRAN("heatran", "heatran", 70, cobblemonBiome("is_badlands")),
	HOOH("hooh", "hooh", 70, cobblemonBiome("is_peak")),
	HOOPA("hoopa", "hoopa", 50, cobblemonBiome("is_desert")),
	JIRACHI("jirachi", "jirachi", 5, cobblemonBiome("is_peak")),
	KELDEO("keldeo", "keldeo", Set.of("ordinary-form"), 15, cobblemonBiome("is_floral")),
	KORAIDON("koraidon", "koraidon", 70, cobblemonBiome("is_badlands")),
	KUBFU("kubfu", "kubfu", 10, cobblemonBiome("is_mountain")),
	KYOGRE("kyogre", "kyogre", 45, cobblemonBiome("is_deep_ocean")),
	KYUREM("kyurem", "kyurem", 75, cobblemonBiome("is_glacial")),
	LANDORUS("landorus", "landorus", 70, cobblemonBiome("is_savanna")),
	LATIAS("latias", "latias", 40, cobblemonBiome("is_plains")),
	LATIOS("latios", "latios", 40, cobblemonBiome("is_plains")),
	LUGIA("lugia", "lugia", 70, cobblemonBiome("is_deep_ocean")),
	MAGEARNA("magearna", "magearna", 50, cobblemonBiome("is_floral")),
	MAGEARNA_ORIGINAL("magearna_original", "magearna", "original-color", Set.of("original-color"), 50, cobblemonBiome("is_floral")),
	MANAPHY("manaphy", "manaphy", 50, cobblemonBiome("is_warm_ocean")),
	MARSHADOW("marshadow", "marshadow", 50, cobblemonBiome("is_spooky")),
	MELOETTA("meloetta", "meloetta", 50, cobblemonBiome("is_magical")),
	MELTAN("meltan", "meltan", 5, cobblemonBiome("is_mountain")),
	MESPRIT("mesprit", "mesprit", 50, cobblemonBiome("is_magical")),
	MEW("mew", "mew", 30, cobblemonBiome("is_jungle")),
	MEWTWO("mewtwo", "mewtwo", 70, cobblemonBiome("is_cave")),
	MIRAIDON("miraidon", "miraidon", 70, cobblemonBiome("is_mountain")),
	MOLTRES("moltres", "moltres", 50, cobblemonBiome("is_badlands")),
	MOLTRES_GALARIAN("moltres_galarian", "moltres", "galarian", Set.of("galarian"), 70, cobblemonBiome("is_badlands")),
	MUNKIDORI("munkidori", "munkidori", 70, cobblemonBiome("is_forest")),
	NECROZMA("necrozma", "necrozma", 75, cobblemonBiome("is_end")),
	OGERPON("ogerpon", "ogerpon", 70, cobblemonBiome("is_forest")),
	OKIDOGI("okidogi", "okidogi", 70, cobblemonBiome("is_mountain")),
	PALKIA("palkia", "palkia", 50, cobblemonBiome("is_end")),
	PECHARUNT("pecharunt", "pecharunt", 75, cobblemonBiome("is_spooky")),
	PHIONE("phione", "phione", 1, cobblemonBiome("is_warm_ocean")),
	RAIKOU("raikou", "raikou", 40, cobblemonBiome("is_mountain")),
	RAYQUAZA("rayquaza", "rayquaza", 70, cobblemonBiome("is_peak")),
	REGICE("regice", "regice", 40, cobblemonBiome("is_glacial")),
	REGIDRAGO("regidrago", "regidrago", 70, cobblemonBiome("is_cave")),
	REGIELEKI("regieleki", "regieleki", 70, cobblemonBiome("is_cave")),
	REGIGIGAS("regigigas", "regigigas", 70, cobblemonBiome("is_mountain")),
	REGIROCK("regirock", "regirock", 40, cobblemonBiome("is_mountain")),
	REGISTEEL("registeel", "registeel", 40, cobblemonBiome("is_cave")),
	RESHIRAM("reshiram", "reshiram", 50, cobblemonBiome("is_peak")),
	SHAYMIN("shaymin", "shaymin", 30, cobblemonBiome("is_floral")),
	SPECTRIER("spectrier", "spectrier", 75, cobblemonBiome("is_spooky")),
	SUICUNE("suicune", "suicune", 40, cobblemonBiome("is_freshwater")),
	TAPUBULU("tapubulu", "tapubulu", 60, cobblemonBiome("is_grassland")),
	TAPUFINI("tapufini", "tapufini", 60, cobblemonBiome("is_coast")),
	TAPUKOKO("tapukoko", "tapukoko", 60, cobblemonBiome("is_peak")),
	TAPULELE("tapulele", "tapulele", 60, cobblemonBiome("is_beach")),
	TERRAKION("terrakion", "terrakion", 42, cobblemonBiome("is_mountain")),
	THUNDURUS("thundurus", "thundurus", 40, cobblemonBiome("is_peak")),
	TORNADUS("tornadus", "tornadus", 40, cobblemonBiome("is_peak")),
	UXIE("uxie", "uxie", 50, cobblemonBiome("is_freshwater")),
	VICTINI("victini", "victini", 15, cobblemonBiome("is_beach")),
	VOLCANION("volcanion", "volcanion", 70, cobblemonBiome("is_badlands")),
	WOCHIEN("wochien", "wochien", 60, cobblemonBiome("is_forest")),
	XERNEAS("xerneas", "xerneas", 50, cobblemonBiome("is_magical")),
	YVELTAL("yveltal", "yveltal", 50, cobblemonBiome("is_spooky")),
	ZACIAN("zacian", "zacian", 70, cobblemonBiome("is_forest")),
	ZAMAZENTA("zamazenta", "zamazenta", 70, cobblemonBiome("is_forest")),
	ZAPDOS("zapdos", "zapdos", 50, cobblemonBiome("is_peak")),
	ZAPDOS_GALARIAN("zapdos_galarian", "zapdos", "galarian", Set.of("galarian"), 70, cobblemonBiome("is_savanna")),
	ZARUDE("zarude", "zarude", 60, cobblemonBiome("is_jungle")),
	ZEKROM("zekrom", "zekrom", 50, cobblemonBiome("is_peak")),
	ZERAORA("zeraora", "zeraora", 50, cobblemonBiome("is_mountain")),
	ZYGARDE("zygarde", "zygarde", Set.of("50-percent"), 70, cobblemonBiome("is_cave"));

	private final String id;
	private final String species;
	private final String form;
	private final Set<String> aspects;
	private final int level;
	private final TagKey<Biome> biomeTag;

	MemoryType(String id, String species, int level, TagKey<Biome> biomeTag) {
		this(id, species, "", Set.of(), level, biomeTag);
	}

	MemoryType(String id, String species, Set<String> aspects, int level, TagKey<Biome> biomeTag) {
		this(id, species, "", aspects, level, biomeTag);
	}

	MemoryType(String id, String species, String form, Set<String> aspects, int level, TagKey<Biome> biomeTag) {
		this.id = id;
		this.species = species;
		this.form = form;
		this.aspects = aspects;
		this.level = level;
		this.biomeTag = biomeTag;
	}

	private static TagKey<Biome> cobblemonBiome(String path) {
		return TagKey.of(RegistryKeys.BIOME, Identifier.of("cobblemon", path));
	}

	public String id() {
		return id;
	}

	public String species() {
		return species;
	}

	public String form() {
		return form;
	}

	public Set<String> aspects() {
		return aspects;
	}

	public Text displayNameText() {
		Species cobblemonSpecies = PokemonSpecies.getByName(species);
		Text speciesName = cobblemonSpecies == null ? Text.literal(species) : cobblemonSpecies.getTranslatedName();

		if (form == null || form.isEmpty()) return speciesName;

		Text formName = Text.translatable("ui.legendaryhunt.form." + form);

		return Text.translatable("ui.legendaryhunt.formatted_species", formName, speciesName);
	}

	public int level() {
		return level;
	}

	public TagKey<Biome> biomeTag() {
		return biomeTag;
	}

	public Text biomeDisplayNameText() {
		Identifier id = biomeTag.id();
		return Text.translatable("biome_tag." + id.getNamespace() + "." + id.getPath().replace('/', '.'));
	}
}