package moe.okaeri.legendaryhunt;

import moe.okaeri.legendaryhunt.util.FastRng;

public final class LegendaryHuntState {
	private static FastRng rng;

	private LegendaryHuntState() { }

	public static void init(long seed) {
		rng = new FastRng(seed);
	}

	public static void clear() {
		rng = null;
	}

	public static FastRng rng() {
		if (rng == null) rng = new FastRng(System.nanoTime());
		return rng;
	}
}