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
}
