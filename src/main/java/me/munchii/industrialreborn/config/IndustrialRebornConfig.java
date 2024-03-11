package me.munchii.industrialreborn.config;

import reborncore.common.config.Config;

public class IndustrialRebornConfig {
    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerInput", comment = "Powered Spawner Max Input (Energy per tick")
    public static int poweredSpawnerMaxInput = 512;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerMaxEnergy", comment = "Powered Spawner Max Energy")
    public static int poweredSpawnerMaxEnergy = 1_000_000;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerEnergyPerSpawn", comment = "Powered Spawner Energy Per Spawn")
    public static int poweredSpawnerEnergyPerSpawn = 512;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerTicksPerSpawn", comment = "Powered Spawner Ticks Per Spawn")
    public static int poweredSpawnerTicksPerSpawn = 200;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerSpawnRange", comment = "Powered Spawner Spawn Range")
    public static int poweredSpawnerSpawnRange = 8;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterInput", comment = "Mob Slaughter Max Input (Energy per tick")
    public static int mobSlaughterMaxInput = 512;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterMaxEnergy", comment = "Mob Slaughter Max Energy")
    public static int mobSlaughterMaxEnergy = 1_000_000;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterEnergyPerSlaughter", comment = "Mob Slaughter Energy Per Slaughter")
    public static int mobSlaughterEnergyPerSlaughter = 512;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterTicksPerSlaughter", comment = "Mob Slaughter Ticks Per Slaughter")
    public static int mobSlaughterTicksPerSlaughter = 200;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterRadius", comment = "Mob Slaughter Radius")
    public static int mobSlaughterRadius = 4;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterAttackDamage", comment = "Mob Slaughter Attack Damage")
    public static float mobSlaughterAttackDamage = 75;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterExperienceMultiplier", comment = "Mob Slaughter Experience Multiplier")
    public static long mobSlaughterExperienceMultiplier = 20L;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorInput", comment = "Soul Extractor Max Input (Energy per tick")
    public static int soulExtractorMaxInput = 512;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorMaxEnergy", comment = "Soul Extractor Max Energy")
    public static int soulExtractorMaxEnergy = 1_000_000;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorEnergyPerSlaughter", comment = "Soul Extractor Energy Per Slaughter")
    public static int soulExtractorEnergyPerExtraction = 256;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorTicksPerSlaughter", comment = "Soul Extractor Ticks Per Extraction")
    public static int soulExtractorTicksPerExtraction = 80;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorRadius", comment = "Soul Extractor Radius")
    public static int soulExtractorRadius = 4;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorExperienceMultiplier", comment = "Soul Extractor Essence Multiplier")
    public static long soulExtractorEssenceMultiplier = 20L;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorAttackDamage", comment = "Soul Extractor Attack Damage")
    public static float soulExtractorAttackDamage = 5;
}
