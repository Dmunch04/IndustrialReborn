package me.munchii.industrialreborn.config;

import reborncore.common.config.Config;

public class IndustrialRebornConfig {
    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerInput", comment = "Powered Spawner Max Input (Energy per tick)")
    public static int poweredSpawnerMaxInput = 256;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerMaxEnergy", comment = "Powered Spawner Max Energy")
    public static int poweredSpawnerMaxEnergy = 1_000_000;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerEnergyPerSpawn", comment = "Powered Spawner Energy Per Spawn")
    public static int poweredSpawnerEnergyPerSpawn = 512;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerTicksPerSpawn", comment = "Powered Spawner Ticks Per Spawn")
    public static int poweredSpawnerTicksPerSpawn = 200;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerSpawnRange", comment = "Powered Spawner Spawn Range")
    public static int poweredSpawnerSpawnRange = 8;

    @Config(config = "machines", category = "powered_spawner", key = "PoweredSpawnerExactCopy", comment = "Should Powered Spawner Spawn Exact Copies Of Provided Entity")
    public static boolean poweredSpawnerExactCopy = false;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterInput", comment = "Mob Slaughter Max Input (Energy per tick)")
    public static int mobSlaughterMaxInput = 128;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterMaxEnergy", comment = "Mob Slaughter Max Energy")
    public static int mobSlaughterMaxEnergy = 10_000;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterEnergyPerSlaughter", comment = "Mob Slaughter Energy Per Slaughter")
    public static int mobSlaughterEnergyPerSlaughter = 256;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterTicksPerSlaughter", comment = "Mob Slaughter Ticks Per Slaughter")
    public static int mobSlaughterTicksPerSlaughter = 200;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterRadius", comment = "Mob Slaughter Radius")
    public static int mobSlaughterRadius = 4;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterAttackDamage", comment = "Mob Slaughter Attack Damage")
    public static float mobSlaughterAttackDamage = 75;

    @Config(config = "machines", category = "mob_slaughter", key = "MobSlaughterExperienceMultiplier", comment = "Mob Slaughter Experience Multiplier")
    public static long mobSlaughterExperienceMultiplier = 20L;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorInput", comment = "Soul Extractor Max Input (Energy per tick)")
    public static int soulExtractorMaxInput = 128;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorMaxEnergy", comment = "Soul Extractor Max Energy")
    public static int soulExtractorMaxEnergy = 10_000;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorEnergyPerSlaughter", comment = "Soul Extractor Energy Per Slaughter")
    public static int soulExtractorEnergyPerExtraction = 200;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorTicksPerSlaughter", comment = "Soul Extractor Ticks Per Extraction")
    public static int soulExtractorTicksPerExtraction = 80;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorRadius", comment = "Soul Extractor Radius")
    public static int soulExtractorRadius = 4;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorExperienceMultiplier", comment = "Soul Extractor Essence Multiplier")
    public static long soulExtractorEssenceMultiplier = 20L;

    @Config(config = "machines", category = "soul_extractor", key = "SoulExtractorAttackDamage", comment = "Soul Extractor Attack Damage")
    public static float soulExtractorAttackDamage = 5;

    @Config(config = "machines", category = "fluid_infuser", key = "FluidInfuserMaxInput", comment = "Fluid Infuser Max Input")
    public static int fluidInfuserMaxInput = 64;

    @Config(config = "machines", category = "fluid_infuser", key = "FluidInfuserMaxEnergy", comment = "Fluid Infuser Max Energy")
    public static int fluidInfuserMaxEnergy = 2_000;

    @Config(config = "machines", category = "animal_feeder", key = "AnimalFeederInput", comment = "Animal Feeder Max Input (Energy per tick)")
    public static int animalFeederMaxInput = 64;

    @Config(config = "machines", category = "animal_feeder", key = "AnimalFeederMaxEnergy", comment = "Animal Feeder Max Energy")
    public static int animalFeederMaxEnergy = 1_000;

    @Config(config = "machines", category = "animal_feeder", key = "AnimalFeederEnergyPerFeeding", comment = "Animal Feeder Energy Per Slaughter")
    public static int animalFeederEnergyPerFeeding = 100;

    @Config(config = "machines", category = "animal_feeder", key = "AnimalFeederTicksPerFeeding", comment = "Animal Feeder Ticks Per Feeding")
    public static int animalFeederTicksPerFeeding = 100;

    @Config(config = "machines", category = "animal_feeder", key = "AnimalFeederRadius", comment = "Animal Feeder Radius")
    public static int animalFeederRadius = 4;

    @Config(config = "machines", category = "animal_feeder", key = "AnimalFeederMaxAnimalsInArea", comment = "Animal Feeder Max Animals In Area")
    public static int animalFeederMaxAnimalsInArea = 50;

    @Config(config = "machines", category = "animal_baby_separator", key = "AnimalBabySeparatorInput", comment = "Animal Baby Separator Max Input (Energy per tick)")
    public static int animalBabySeparatorMaxInput = 64;

    @Config(config = "machines", category = "animal_baby_separator", key = "AnimalBabySeparatorMaxEnergy", comment = "Animal Baby Separator Max Energy")
    public static int animalBabySeparatorMaxEnergy = 1_000;

    @Config(config = "machines", category = "animal_baby_separator", key = "AnimalBabySeparatorEnergyPerFeeding", comment = "Animal Baby Separator Energy Per Slaughter")
    public static int animalBabySeparatorEnergyPerSeparation = 100;

    @Config(config = "machines", category = "animal_baby_separator", key = "AnimalBabySeparatorTicksPerFeeding", comment = "Animal Baby Separator Ticks Per Feeding")
    public static int animalBabySeparatorTicksPerSeparation = 100;

    @Config(config = "machines", category = "animal_baby_separator", key = "AnimalBabySeparatorRadius", comment = "Animal Baby Separator Radius")
    public static int animalBabySeparatorRadius = 4;

    @Config(config = "machines", category = "animal_baby_separator", key = "AnimalBabySeparatorMaxAnimalsInArea", comment = "Animal Baby Separator Max Animals In Area")
    public static int animalBabySeparatorMaxAnimalsInArea = 50;

    @Config(config = "machines", category = "auto_enchanter", key = "AutoEnchanterInput", comment = "Auto Enchanter Max Input (Energy per tick)")
    public static int autoEnchanterMaxInput = 64;

    @Config(config = "machines", category = "auto_enchanter", key = "AutoEnchanterMaxEnergy", comment = "Auto Enchanter Max Energy")
    public static int autoEnchanterMaxEnergy = 2_000;

    @Config(config = "machines", category = "auto_enchanter", key = "AutoEnchanterEnergyPerEnchant", comment = "Auto Enchanter Energy Per Slaughter")
    public static int autoEnchanterEnergyPerEnchant = 100;

    @Config(config = "machines", category = "auto_enchanter", key = "AutoEnchanterExperiencePerEnchant", comment = "Auto Enchanter Experience Per Enchant (Experience in milli buckets)")
    public static int autoEnchanterExperiencePerEnchant = 30_000;

    @Config(config = "machines", category = "auto_enchanter", key = "AutoEnchanterTicksPerEnchant", comment = "Auto Enchanter Ticks Per Feeding")
    public static int autoEnchanterTicksPerEnchant = 100;

    @Config(config = "machines", category = "auto_enchanter", key = "AutoEnchanterAllowTreasureEnchantments", comment = "Auto Enchanter Allow Treasure Enchantments (Unobtainable trough enchmant table)")
    public static boolean autoEnchanterAllowTreasureEnchantments = false;

}
