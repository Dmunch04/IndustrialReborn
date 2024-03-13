package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.blockentity.*;
import me.munchii.industrialreborn.utils.Resources;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class IRBlockEntities {
    private static final List<BlockEntityType<?>> TYPES = new ArrayList<>();

    public static final BlockEntityType<PoweredSpawnerBlockEntity> POWERED_SPAWNER = register(PoweredSpawnerBlockEntity::new, "powered_spawner", IRContent.Machine.POWERED_SPAWNER);
    public static final BlockEntityType<MobSlaughterBlockEntity> MOB_SLAUGHTER = register(MobSlaughterBlockEntity::new, "mob_slaughter", IRContent.Machine.MOB_SLAUGHTER);
    public static final BlockEntityType<SoulExtractorBlockEntity> SOUL_EXTRACTOR = register(SoulExtractorBlockEntity::new, "soul_extractor", IRContent.Machine.SOUL_EXTRACTOR);
    public static final BlockEntityType<FluidTransposerBlockEntity> FLUID_TRANSPOSER = register(FluidTransposerBlockEntity::new, "fluid_transposer", IRContent.Machine.FLUID_TRANSPOSER);
    public static final BlockEntityType<AnimalFeederBlockEntity> ANIMAL_FEEDER = register(AnimalFeederBlockEntity::new, "animal_feeder", IRContent.Machine.ANIMAL_FEEDER);

    public static <T extends BlockEntity> BlockEntityType<T> register(BiFunction<BlockPos, BlockState, T> supplier, String name, ItemConvertible... items) {
        return register(supplier, name, Arrays.stream(items).map(itemConvertible -> Block.getBlockFromItem(itemConvertible.asItem())).toArray(Block[]::new));
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(BiFunction<BlockPos, BlockState, T> supplier, String name, Block... blocks) {
        Validate.isTrue(blocks.length > 0, "no blocks for blockEntity entity type!");
        return register(Resources.id(name).toString(), FabricBlockEntityTypeBuilder.create(supplier::apply, blocks));
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder<T> builder) {
        BlockEntityType<T> blockEntityType = builder.build(null);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(id), blockEntityType);
        IRBlockEntities.TYPES.add(blockEntityType);

        return blockEntityType;
    }

    public static void init() {
        TYPES.toString();
    }
}
