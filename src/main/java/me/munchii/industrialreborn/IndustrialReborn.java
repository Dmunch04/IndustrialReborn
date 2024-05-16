package me.munchii.industrialreborn;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.items.SoulVialItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.ChestBlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.common.config.Configuration;

public class IndustrialReborn implements ModInitializer {
    // TODO: AnimalRancher block
    // TODO: Sewer block? (how to use the fertilizer? when greenhouse controller already exists in TR) (Manure or Fertilizer, which is best?)
    // TODO: AnimalGrowthIncreaser block?
    // TODO: Auto Enchanter, Enchantment Applicator, Enchantment Extractor
    // TODO: Soul Infused Ingot
    // TODO: datagen?

    public static final String MOD_ID = "industrialreborn";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        new Configuration(IndustrialRebornConfig.class, MOD_ID);

        RegistryManager.register();

        SoulVialItem.setup();

        LOGGER.info("IndustrialReborn initialized");
    }
}
