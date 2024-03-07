package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidUtils;
import reborncore.common.fluid.FluidValue;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class MobSlaughterBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
    public final Tank experienceTank;
    public final Tank essenceTank;

    public MobSlaughterBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.MOB_SLAUGHTER, pos, state, "MobSlaughter", IndustrialRebornConfig.mobSlaughterMaxInput, IndustrialRebornConfig.mobSlaughterMaxEnergy, IRContent.Machine.MOB_SLAUGHTER.block, 6);

        this.inventory = new RebornInventory<>(7, "MobSlaughterBlockEntity", 64, this);
        this.experienceTank = new Tank("MobSlaughter_LiquidExperience", FluidValue.BUCKET.multiply(10), this);
        this.essenceTank = new Tank("MobSlaughter_SoulEssence", FluidValue.BUCKET.multiply(10), this);
        experienceTank.setFluid(IRFluids.LIQUID_EXPERIENCE.getFluid());
        essenceTank.setFluid(IRFluids.SOUL_ESSENCE.getFluid());
        experienceTank.setFluidAmount(FluidValue.BUCKET.multiply(5));
        //FluidUtils.fillContainers(experienceTank, inventory, 0, 1);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("mob_slaughter").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .slot(0, 25, 35).outputSlot(1, 25, 35)
                .syncEnergyValue()
                .sync(this::getExperienceAmount, this::setExperienceAmount)
                .sync(this::getEssenceAmount, this::setEssenceAmount)
                .sync(experienceTank).sync(essenceTank)
                .addInventory().create(this, syncID);
    }

    public FluidValue getExperienceAmount() {
        return experienceTank.getFluidAmount();
    }

    public void setExperienceAmount(FluidValue amount) {
        experienceTank.setFluidAmount(amount);
    }

    public FluidValue getEssenceAmount() {
        return essenceTank.getFluidAmount();
    }

    public void setEssenceAmount(FluidValue amount) {
        essenceTank.setFluidAmount(amount);
    }
}
