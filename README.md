# Legendary Hunt

A simple Cobblemon side mod that turns legendary encounters into player driven hunts.

Instead of legendary Pokémon simply appearing out of nowhere, players restore legendary memories, track specific legends, and trigger a timed hunt. Each hunt is like a small ritual: prepare the memory, find the correct biome, and hope for them to spawn.

> Built for Fabric + Cobblemon!
> 
> Requires Cobblemon 1.7.3 + something to add missing legendaries (Like AllTheMons)

---

## Features

- **Legendary hunt system**  
  Use restored memories to begin a hunt for a specific legendary or mythical Pokémon.

- **Memory-based progression**  
  Players collect memory fragments and restore them into usable legendary memories.

- **Biome-aware encounters**  
  Each legendary is tied to a thematic Cobblemon biome tag, such as glacial, arid, freshwater, magical, peak, and more.

- **Species and form support**  
  Regional and special forms have their own dedicated memories, such as Galarian legendary birbs.

- **Timed hunts**  
  Hunts last for a configurable duration and periodically check whether the player is in a valid location.

- **Configurable balancing**  
  Spawn chances, hunt duration, loot chances, cooldowns, and shiny odds can be adjusted through the config file.

- **Custom restoration recipes**  
  Memories are restored through thematic crafting recipes using Cobblemon items, evolution stones, memory fragments, ancient debris, and legendary signature materials.

- **Server-friendly design**  
  Hunt logic only runs when hunts are active, keeping idle overhead extremely small.

---

## Gameplay Loop

1. **Find Memory Fragments**  
   Memory fragments can be obtained through configured loot sources such as exploration rewards and fishing.

2. **Reform a memory**  
   Combine a memory fragment with thematic materials to create a restored memory for a specific legendary Pokémon.

3. **Travel to the correct biome**  
   Each memory displays the biome type required for the hunt.

4. **Use the reformed memory**  
   Activating the memory starts a timed hunt for that Pokémon.

5. **Wait for the hunt to succeed**  
   While the hunt is active, the mod periodically checks whether the player is in a valid biome and rolls for the legendary spawn.

6. **Encounter the legendary**  
   If the roll succeeds, the legendary spawns near the player and the hunt completes.

---

## Installation

### Requirements

- Minecraft `1.21.1`
- Fabric Loader
- Fabric API
- Cobblemon `1.7.3`
- Fabric Language Kotlin

### Client and Server

Legendary Hunt is intended to be installed on both the client and the server.

---

## Configuration

The config file is generated at:

```txt
config/legendaryhunt.json
```

Example config values will include stuff like this:

```json
{
  "version": 1,
  "huntDurationTicks": 72000,
  "checkIntervalTicks": 20,
  "reformedMemoryCooldown": 60,
  "memoryFragmentChestChance": 0.5,
  "memoryFragmentFishingChance": 0.01,
  "spawnOneInChance": 1024,
  "legendaryShinyOneInChance": 8192
}
```

### Important values

| Option | Description                                                                          |
| --- |--------------------------------------------------------------------------------------|
| `huntDurationTicks` | How long a hunt lasts, in ticks. `72000` ticks is one Minecraft hour.                |
| `checkIntervalTicks` | How often the hunt checks for valid spawn conditions. `20` ticks is once per second. |
| `reformedMemoryCooldown` | Cooldown before a memory can be used again, in ticks.                                |
| `memoryFragmentChestChance` | Chance for memory fragments to appear in configured chest loot.                      |
| `memoryFragmentFishingChance` | Chance for memory fragments to be obtained from fishing.                             |
| `spawnOneInChance` | Base spawn roll chance during an active hunt.                                        |
| `legendaryShinyOneInChance` | Shiny odds for legendary spawns created by the hunt system.                          |

---

## Spawn Timing

Legendary Hunt uses a periodic roll system.

For example, with:

```txt
checkIntervalTicks = 20
spawnOneInChance = 1024
```

The mod checks once per second, and each check has a `1 / 1024` chance to succeed while the player is in the correct biome.

That means in this case the average time to get a successful roll is about:

```txt
1024 seconds ~ 17 minutes
```

This is only an average tho, a hunt can succeed instantly, or it can take much longer if the player is very unlucky.

---

## Memory Recipes

Memory recipes are designed around a consistent pattern:

```txt
Special Item       Thematic Evolution Stone     Special Item
Thematic Block     Memory Fragment              Thematic Block
Thematic Block     Thematic Gem                 Thematic Block
```

The goal is for every legendary memory to feel unique while keeping the overall difficulty fair across species.

Default recipes avoid relying on drops from default Minecraft mobs and instead focus on base blocks, Cobblemon stuff, evolution stones and so on.

---

## Biome Requirements

Each legendary memory is linked to a single thematic biome tag where possible.

Examples:

| Pokémon | Example biome theme |
| --- | --- |
| Articuno | Glacial |
| Azelf | Freshwater |
| Celebi | Magical |
| Chi-Yu | Badlands |
| Cobalion | Highlands |
| Kyogre | Deep Ocean |
| Groudon | Arid |
| Rayquaza | Peak |

The required biome type is shown on the restored memory tooltip so players know where to go.

---

## For Modpack Makers

Legendary Hunt is intended to be configurable for modpacks and servers.

You can adjust:

- Hunt duration
- Check interval
- Spawn odds
- Shiny odds
- Memory fragment loot chances
- Fishing loot chances
- Recipe difficulty (datapack)
- Required materials (datapack)

For harder progression, reduce memory fragment rates and increase recipe complexity.  
For more casual players, increase fragment drop rates, change recipes and lower `spawnOneInChance`.

---

## Performance Notes

Legendary Hunt keeps server overhead low by only ticking hunt logic when at least one player has an active hunt.

The hunt manager stores active hunts by player UUID and skips its main logic entirely when no hunts are active.

In normal gameplay, this means the mod should have negligible idle performance cost.

It has been tested on a reaaaaally old Xeon E5 1620 V2 without any visible impact.

---

## Compatibility (IMPORTANT)

Legendary Hunt is built around base Cobblemon species, forms, items, biome tags, and Pokémon spawning.

You will require a mod adding specific species, such as AllTheMons. If you do, you may also want to remove that mod's normal legendary mons spawn files so Legendary Hunt can control legendary progression.

It should work best in packs that keep Cobblemon biome tags intact.

---

## FAQ

### Does this uses Cobblemon default buckets spawning?

No. Legendary Hunt adds its own spawning system. It does not interfere with the spawn limits either.

### Can a player have multiple hunts active?

The intended design is one active hunt per player.

### Are regional forms supported?

Yes. Memories can target specific species and form/aspect combinations.

### Can the spawn chance be changed?

Yes. Adjust `spawnOneInChance` in the config file.

### Why did my legendary not spawn immediately?

The hunt uses random rolls. Being in the correct biome allows the hunt to roll, but success is not guaranteed on every check.

### What happens if the hunt expires?

If the hunt duration runs out before a successful spawn, the hunt ends and the player must start another hunt.

---

## Credits

Created by **EasyMochi (Okaeri Game Studio)**.

Special thanks to:

- The Cobblemon team <3
- The Fabric community

---

```txt
MIT, Provided as-is~
```

(Yes, I used AI to make that README more polished than it originally was :D)