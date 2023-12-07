package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class MobSlaughterBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
    public MobSlaughterBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.MOB_SLAUGHTER, pos, state, "MobSlaughter", IndustrialRebornConfig.mobSlaughterMaxInput, IndustrialRebornConfig.mobSlaughterMaxEnergy, IRContent.Machine.POWERED_SPAWNER.block, 6);

        this.inventory = new RebornInventory<>(7, "MobSlaughterBlockEntity", 64, this);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return null;
    }
}
