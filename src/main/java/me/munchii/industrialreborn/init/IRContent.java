package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.blockentity.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import reborncore.api.blockentity.IUpgrade;
import techreborn.blocks.GenericMachineBlock;
import techreborn.items.UpgradeItem;
import techreborn.utils.InitUtils;

import java.util.Locale;

public class IRContent {
    public static Block BROKEN_SPAWNER;

    public static Item EMPTY_SOUL_VIAL;

    public static Item FILLED_SOUL_VIAL;

    public enum Machine implements ItemConvertible {
        POWERED_SPAWNER(new GenericMachineBlock(GuiType.POWERED_SPAWNER, PoweredSpawnerBlockEntity::new)),
        MOB_SLAUGHTER(new GenericMachineBlock(GuiType.MOB_SLAUGHTER, MobSlaughterBlockEntity::new)),
        SOUL_EXTRACTOR(new GenericMachineBlock(GuiType.SOUL_EXTRACTOR, SoulExtractorBlockEntity::new)),
        FLUID_TRANSPOSER(new GenericMachineBlock(GuiType.FLUID_TRANSPOSER, FluidTransposerBlockEntity::new)),
        ANIMAL_FEEDER(new GenericMachineBlock(GuiType.ANIMAL_FEEDER, AnimalFeederBlockEntity::new));

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

    public enum Upgrade implements ItemConvertible {
        RANGE((blockEntity, handler, stack) -> {
            if (blockEntity instanceof IRangedBlockEntity rangedBlockEntity) {
                rangedBlockEntity.addRange(2);
            }
        });

        public final String name;
        public final Item item;

        Upgrade(IUpgrade upgrade) {
            name = this.toString().toLowerCase(Locale.ROOT) + "_upgrade";
            item = new UpgradeItem(name, upgrade);
        }

        @Override
        public Item asItem() {
            return item;
        }
    }
}
