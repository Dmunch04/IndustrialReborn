package me.munchii.industrialreborn;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.common.config.Configuration;

public class IndustrialReborn implements ModInitializer {
    // TODO: make new animation for powered spawner because the current looks cursed
    // TODO: make textures for mob slaughter, soul extractor, fluid transposer, animal feeder and animal baby separator
    // TODO: balance machines a bit out tier wise (max energy, max input, energy consumption)

    // TODO: AnimalRancher block
    // TODO: Sewer block? (how to use the fertilizer? when greenhouse controller already exists in TR)

    public static final String MOD_ID = "industrialreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        new Configuration(IndustrialRebornConfig.class, MOD_ID);

        RegistryManager.register();

        LOGGER.info("IndustrialReborn initialized");
    }
}
