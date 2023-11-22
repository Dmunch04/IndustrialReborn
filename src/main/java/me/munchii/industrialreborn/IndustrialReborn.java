package me.munchii.industrialreborn;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.common.config.Configuration;

public class IndustrialReborn implements ModInitializer {
    // TODO: make the broken spawner transparent
    // TODO: make recipes
    // TODO: test the powered spawner on/active animation (at frametime 2 it was way too fast, it looked weird)

    public static final String MOD_ID = "industrialreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        new Configuration(IndustrialRebornConfig.class, MOD_ID);

        RegistryManager.register();
    }
}
