package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.blockentity.GuiType;
import me.munchii.industrialreborn.blockentity.MobSlaughterBlockEntity;
import me.munchii.industrialreborn.blockentity.PoweredSpawnerBlockEntity;
import me.munchii.industrialreborn.blockentity.SoulExtractorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import techreborn.blocks.GenericMachineBlock;

import java.util.Locale;

public class IRContent {
    public static Block BROKEN_SPAWNER;

    public static Item EMPTY_SOUL_VIAL;

    public static Item FILLED_SOUL_VIAL;

    public enum Machine implements ItemConvertible {
        POWERED_SPAWNER(new GenericMachineBlock(GuiType.POWERED_SPAWNER, PoweredSpawnerBlockEntity::new)),
        MOB_SLAUGHTER(new GenericMachineBlock(GuiType.MOB_SLAUGHTER, MobSlaughterBlockEntity::new)),
        SOUL_EXTRACTOR(new GenericMachineBlock(GuiType.SOUL_EXTRACTOR, SoulExtractorBlockEntity::new));

        public final String name;
        public final Block block;

        <B extends Block> Machine(B block) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.block = block;
        }

        public ItemStack getStack() {
            return new ItemStack(block);
        }

        @Override
        public Item asItem() {
            return block.asItem();
        }
    }
}
