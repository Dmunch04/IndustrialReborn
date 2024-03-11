package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import me.munchii.industrialreborn.init.IRRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidUtils;
import reborncore.common.fluid.FluidValue;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class FluidTransposerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
    public final Tank tank;
    private int ticksSinceLastChange;

    public FluidTransposerBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.FLUID_TRANSPOSER, pos, state, "FluidTransposer", IndustrialRebornConfig.fluidTransposerMaxInput, IndustrialRebornConfig.fluidTransposerMaxEnergy, IRContent.Machine.FLUID_TRANSPOSER.block, 4);

        final int[] inputs = new int[] { 0, 1 };
        final int[] outputs = new int[] { 2 };
        this.inventory = new RebornInventory<MachineBaseBlockEntity>(5, "FluidTransposerBlockEntity", 64, this);
        this.crafter = new RecipeCrafter(IRRecipes.FLUID_TRANSPOSER, this, 1, 1, this.inventory, inputs, outputs);
        this.tank = new Tank("FluidTransposerBlockEntity", FluidValue.BUCKET.multiply(16), this);
        this.ticksSinceLastChange = 0;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (world == null || world.isClient) {
            return;
        }

        ticksSinceLastChange++;
        if (ticksSinceLastChange >= 10) {
            if (!inventory.getStack(1).isEmpty()) {
                FluidUtils.drainContainers(tank, inventory, 1, 3);
                FluidUtils.fillContainers(tank, inventory, 1, 3);
            }
            ticksSinceLastChange = 0;
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        tank.read(tag);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tank.write(tag);
    }

    @Nullable
    @Override
    public Tank getTank() {
        return tank;
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("fluidtransposer").player(player.getInventory()).inventory().hotbar().addInventory()
                .blockEntity(this).fluidSlot(1, 34, 35).slot(0, 84, 43).outputSlot(2, 126, 43)
                .outputSlot(3, 34, 55).energySlot(4, 8, 72)
                .sync(tank).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
    }
}
