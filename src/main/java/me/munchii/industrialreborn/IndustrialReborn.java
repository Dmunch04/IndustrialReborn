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
    // TODO: make textures for mob slaughter, soul extractor and fluid transposer
    // TODO: redesign gui for above machines maybe? (probably not fluid transposer)
    // TODO: AnimalFeeder block texture
    // TODO: AnimalBabySeparator block texture + test
    // TODO: AnimalRancher block
    // TODO: adult_filter_upgrade texture and item resource

    public static final String MOD_ID = "industrialreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        new Configuration(IndustrialRebornConfig.class, MOD_ID);

        RegistryManager.register();

        LOGGER.info("IndustrialReborn initialized");
    }
}
