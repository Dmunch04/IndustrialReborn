package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

import java.util.List;

public class AnimalBabySeparatorBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, IRangedBlockEntity {
    public int separationTime;
    public final int totalSeparationTime = IndustrialRebornConfig.animalBabySeparatorTicksPerSeparation;

    public final int separationRadius = IndustrialRebornConfig.animalBabySeparatorRadius;
    public int extraRadius;

    private boolean movingAdults = false;

    private BlockPos fromCenterPos;
    private Box fromArea;
    private BlockPos toCenterPos;
    private Box toArea;

    public AnimalBabySeparatorBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.ANIMAL_BABY_SEPARATOR, pos, state, "AnimalBabySeparator", IndustrialRebornConfig.animalBabySeparatorMaxInput, IndustrialRebornConfig.animalBabySeparatorMaxEnergy, IRContent.Machine.ANIMAL_BABY_SEPARATOR.block, 0);
        this.inventory = new RebornInventory<>(1, "AnimalBabySeparatorBlockEntity", 64, this);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (!(world instanceof ServerWorld) || !isActive(RedstoneConfiguration.POWER_IO)) {
            return;
        }

        if (fromCenterPos == null) {
            fromCenterPos = pos.offset(getFacing().getOpposite(), getRadius() + 1);
        }
        if (toCenterPos == null) {
            toCenterPos = pos.offset(getFacing(), getRadius() + 1);
        }

        if (fromArea == null) {
            fromArea = new Box(
                    fromCenterPos.getX() - getRadius(),
                    fromCenterPos.getY(),
                    fromCenterPos.getZ() - getRadius(),
                    fromCenterPos.getX() + getRadius(),
                    fromCenterPos.getY() + 3,
                    fromCenterPos.getZ() + getRadius()
            );
        }
        if (toArea == null) {
            toArea = new Box(
                    toCenterPos.getX() - getRadius(),
                    toCenterPos.getY(),
                    toCenterPos.getZ() - getRadius(),
                    toCenterPos.getX() + getRadius(),
                    toCenterPos.getY() + 3,
                    toCenterPos.getZ() + getRadius()
            );
        }

        updateState();

        if (getStored() > IndustrialRebornConfig.animalBabySeparatorEnergyPerSeparation) {
            if (separationTime >= totalSeparationTime) {
                boolean didSeparate = separateBaby();
                if (didSeparate) useEnergy(IndustrialRebornConfig.animalBabySeparatorEnergyPerSeparation);
                separationTime = 0;
            } else {
                separationTime++;
            }
        }
    }

    public boolean separateBaby() {
        ServerWorld serverWorld = (ServerWorld) world;
        assert serverWorld != null;
        List<AnimalEntity> nearbyEntities = serverWorld.getEntitiesByClass(AnimalEntity.class, fromArea.expand(0.5D), entity -> entity.isAlive() && entity.isBaby() == !movingAdults);

        if (!nearbyEntities.isEmpty() && nearbyEntities.size() <= IndustrialRebornConfig.animalBabySeparatorMaxAnimalsInArea) {
            AnimalEntity entity = nearbyEntities.get(0);
            entity.setPosition(toCenterPos.toCenterPos());
            return true;
        }

        return false;
    }

    private void updateState() {
        assert world != null;

        final BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof final BlockMachineBase blockMachineBase) {
            boolean active = getStored() > IndustrialRebornConfig.animalBabySeparatorEnergyPerSeparation;
            if (blockState.get(BlockMachineBase.ACTIVE) != active) {
                blockMachineBase.setActive(active, world, pos);
            }
        }
    }

    public int getRadius() {
        return separationRadius + extraRadius;
    }

    @Override
    public void addRange(int range) {
        extraRadius += range;
        fromCenterPos = null;
        toCenterPos = null;
        fromArea = null;
        toArea = null;
    }

    @Override
    public void addRangeMultiplier(float multiplier) {
        extraRadius += Math.round(getRadius() * multiplier);
        fromCenterPos = null;
        toCenterPos = null;
        fromArea = null;
        toArea = null;
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("animal_baby_separator").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .energySlot(0, 8, 72)
                .sync(this::getSeparationTime, this::setSeparationTime)
                .sync(this::getTotalSeparationTime, this::setTotalSeparationTime)
                .syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    @Override
    public void writeMultiblock(MultiblockWriter writer) {
        final BlockState glass = Blocks.RED_STAINED_GLASS.getDefaultState();
        final BlockState greenGlass = Blocks.GREEN_STAINED_GLASS.getDefaultState();

        final int radius = getRadius();
        final int diameter = radius * 2 + 1;
        for (int i = 1; i <= diameter; i++) {
            for (int j = -radius; j <= radius; j++) {
                writer.add(i, 0, j, (world, pos) -> true, glass);
                writer.add(i, 1, j, (world, pos) -> true, glass);
                writer.add(i, 2, j, (world, pos) -> true, glass);

                writer.add(-i, 0, j, (world, pos) -> true, greenGlass);
                writer.add(-i, 1, j, (world, pos) -> true, greenGlass);
                writer.add(-i, 2, j, (world, pos) -> true, greenGlass);
            }
        }
    }

    @Override
    public ItemStack getToolDrop(PlayerEntity p0) {
        return IRContent.Machine.ANIMAL_BABY_SEPARATOR.getStack();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        fromCenterPos = null;
        toCenterPos = null;
        fromArea = null;
        toArea = null;
    }

    @Override
    public void resetUpgrades() {
        super.resetUpgrades();
        extraRadius = 0;
        movingAdults = false;
    }

    public int getSeparationTime() {
        return separationTime;
    }

    public void setSeparationTime(final int separationTime) {
        this.separationTime = separationTime;
    }

    public int getTotalSeparationTime() {
        return totalSeparationTime;
    }

    public void setTotalSeparationTime(final int totalSeparationTime) {

    }

    public void setMovingAdults(boolean movingAdults) {
        this.movingAdults = movingAdults;
    }
}
