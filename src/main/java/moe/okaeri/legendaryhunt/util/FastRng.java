package moe.okaeri.legendaryhunt.util;

public final class FastRng {
	private long state;

	public FastRng(long seed) {
		setSeed(seed);
	}

	public void setSeed(long seed) {
		this.state = seed != 0L ? seed : 0x9E3779B97F4A7C15L;
	}

	public long nextLong() {
		long x = state;

		x ^= x >>> 12;
		x ^= x << 25;
		x ^= x >>> 27;

		state = x;

		return x * 0x2545F4914F6CDD1DL;
	}

	public int nextInt() {
		return (int) nextLong();
	}

	public int nextInt(int bound) {
		return (int)((Integer.toUnsignedLong(nextInt()) * bound) >>> 32);
	}

	public float nextFloat() {
		return (nextInt() >>> 8) * 0x1.0p-24f;
	}

	public boolean oneIn(int chance) {
		return nextInt(chance) == 0;
	}

	public boolean chance(float probability) {
		return nextFloat() < probability;
	}
}