package me.munchii.industrialreborn;

import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRItemGroup;
import me.munchii.industrialreborn.items.SoulVialItem;
import me.munchii.industrialreborn.utils.Resources;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import techreborn.init.TRBlockSettings;

import java.util.Arrays;

public class RegistryManager {
    public static void register() {
        registerBlocks();
        registerItems();

        IRBlockEntities.init();
        IRItemGroup.register();
    }

    private static void registerBlocks() {
        Arrays.stream(IRContent.Machine.values()).forEach(value -> registerBlock(value.name, value.block));

        // TODO: i'm still not happy with how the broken spawner looks because the edges are see through and you can't see the backsides through it
        IRContent.BROKEN_SPAWNER = registerBlock("broken_spawner", new Block(TRBlockSettings.machine().nonOpaque()));
    }

    private static void registerItems() {
        IRContent.EMPTY_SOUL_VIAL = registerItem("empty_soul_vial", new SoulVialItem(false));
        IRContent.FILLED_SOUL_VIAL = registerItem("filled_soul_vial", new SoulVialItem(true));
    }

    private static <I extends Item> I registerItem(String name, I item) {
        Registry.register(Registries.ITEM, Resources.id(name), item);
        return item;
    }

    private static <B extends Block> B registerBlock(String name, B block) {
        return registerBlock(name, block, new Item.Settings());
    }

    private static <B extends Block> B registerBlock(String name, B block, Item.Settings itemBlockSettings) {
        Registry.register(Registries.BLOCK, Resources.id(name), block);
        Registry.register(Registries.ITEM, Resources.id(name), new BlockItem(block, itemBlockSettings));
        return block;
    }
}
