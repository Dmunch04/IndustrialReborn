package me.munchii.industrialreborn;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.core.store.StoreItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.common.config.Configuration;

public class IndustrialReborn implements ModInitializer {
    // TODO: make new animation for powered spawner because the current looks cursed

    public static final String MOD_ID = "industrialreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        new Configuration(IndustrialRebornConfig.class, MOD_ID);

        RegistryManager.register();

        LOGGER.info("IndustrialReborn initialized");

        RegistryEntryAddedCallback.event(Registries.ITEM).register((rawId, id, object) -> {
            // this doesnt work
            if (object instanceof StoreItem storeItem) {
                LOGGER.warn("YEEEEEEET " + storeItem.getClass().getName());
                storeItem.initStores(storeItem.provider);
            }
        });
    }
}
