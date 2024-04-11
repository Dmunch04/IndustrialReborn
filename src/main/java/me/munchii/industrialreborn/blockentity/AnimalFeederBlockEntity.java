package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.IndustrialReborn;
import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
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

import java.util.Comparator;
import java.util.List;

public class AnimalFeederBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, IRangedBlockEntity {
    public int feedingTime;
    public final int totalFeedingTime = IndustrialRebornConfig.animalFeederTicksPerFeeding;

    public final int feedingRadius = IndustrialRebornConfig.animalFeederRadius;
    public int extraRadius;

    private BlockPos centerPos;
    private Box feedingArea;

    public AnimalFeederBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.ANIMAL_FEEDER, pos, state, "AnimalFeeder", IndustrialRebornConfig.animalFeederMaxInput, IndustrialRebornConfig.animalFeederMaxEnergy, IRContent.Machine.ANIMAL_FEEDER.block, 6);
        this.inventory = new RebornInventory<>(7, "AnimalFeederBlockEntity", 64, this);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (!(world instanceof ServerWorld) || !isActive(RedstoneConfiguration.POWER_IO)) {
            return;
        }

        if (centerPos == null) {
            centerPos = pos.offset(getFacing().getOpposite(), getRadius() + 1);
        }

        if (feedingArea == null) {
            feedingArea = new Box(
                    centerPos.getX() - getRadius(),
                    centerPos.getY(),
                    centerPos.getZ() - getRadius(),
                    centerPos.getX() + getRadius(),
                    centerPos.getY() + 3,
                    centerPos.getZ() + getRadius()
            );
        }

        updateState();

        if (getStored() > IndustrialRebornConfig.animalFeederEnergyPerFeeding) {
            if (feedingTime >= totalFeedingTime) {
                feedEntity();
                useEnergy(IndustrialRebornConfig.animalFeederEnergyPerFeeding);
                feedingTime = 0;
            } else {
                feedingTime++;
            }
        }
    }

    public void feedEntity() {
        ServerWorld serverWorld = (ServerWorld) world;
        assert serverWorld != null;
        List<AnimalEntity> nearbyEntities = serverWorld.getEntitiesByClass(AnimalEntity.class, feedingArea.expand(1), LivingEntity::isAlive);
        nearbyEntities.removeIf(entity -> entity.age < entity.getBreedingAge() || getFeedingItem(entity).getLeft().isEmpty());
        nearbyEntities.sort(Comparator.comparingInt(a -> a.age));

        if (!nearbyEntities.isEmpty() && nearbyEntities.size() <= IndustrialRebornConfig.animalFeederMaxAnimalsInArea) {
            for (AnimalEntity firstParent : nearbyEntities) {
                for (AnimalEntity secondParent : nearbyEntities) {
                    if (firstParent.equals(secondParent) || !firstParent.getClass().equals(secondParent.getClass())) {
                        continue;
                    }

                    Pair<ItemStack, Integer> stack = getFeedingItem(firstParent);
                    ItemStack foodStack = stack.getLeft();
                    ItemStack original = foodStack.copy();
                    foodStack.decrement(1);

                    foodStack = getFeedingItem(secondParent).getLeft();
                    if (foodStack.isEmpty()) {
                        original.setCount(1);
                        if (stack.getRight() != -1) inventory.setStack(stack.getRight(), original);
                        continue;
                    }

                    foodStack.decrement(1);
                    firstParent.lovePlayer(null);
                    secondParent.lovePlayer(null);
                    return;
                }
            }
        }
    }

    private Pair<ItemStack, Integer> getFeedingItem(AnimalEntity animal) {
        for (int i = 0; i < 6; i++) {
            if (animal.isBreedingItem(inventory.getStack(i))) {
                return new Pair<>(inventory.getStack(i), i);
            }
        }

        return new Pair<>(ItemStack.EMPTY, -1);
    }

    private void updateState() {
        assert world != null;

        final BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof final BlockMachineBase blockMachineBase) {
            boolean active = getStored() > IndustrialRebornConfig.animalFeederEnergyPerFeeding;
            if (blockState.get(BlockMachineBase.ACTIVE) != active) {
                blockMachineBase.setActive(active, world, pos);
            }
        }
    }

    public int getRadius() {
        return feedingRadius + extraRadius;
    }

    @Override
    public void addRange(int range) {
        extraRadius += range;
        centerPos = null;
        feedingArea = null;
    }

    @Override
    public void addRangeMultiplier(float multiplier) {
        extraRadius += Math.round(getRadius() * multiplier);
        centerPos = null;
        feedingArea = null;
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("animal_feeder").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .energySlot(6, 8, 72)
                .slot(0, 70, 22).slot(1, 88, 22)
                .slot(2, 70, 40).slot(3, 88, 40)
                .slot(4, 70, 58).slot(5, 88, 58)
                .sync(this::getFeedingTime, this::setFeedingTime)
                .sync(this::getTotalFeedingTime, this::setTotalFeedingTime)
                .syncEnergyValue()
                .addInventory().create(this, syncID);
    }

    @Override
    public void writeMultiblock(MultiblockWriter writer) {
        final BlockState glass = Blocks.RED_STAINED_GLASS.getDefaultState();

        final int radius = getRadius();
        final int diameter = radius * 2 + 1;
        for (int i = 1; i <= diameter; i++) {
            for (int j = -radius; j <= radius; j++) {
                writer.add(i, 0, j, (world, pos) -> true, glass);
                writer.add(i, 1, j, (world, pos) -> true, glass);
                writer.add(i, 2, j, (world, pos) -> true, glass);
            }
        }
    }

    @Override
    public ItemStack getToolDrop(PlayerEntity p0) {
        return IRContent.Machine.ANIMAL_FEEDER.getStack();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        centerPos = null;
        feedingArea = null;
    }

    @Override
    public void resetUpgrades() {
        super.resetUpgrades();
        extraRadius = 0;
    }

    public int getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(final int feedingTime) {
        this.feedingTime = feedingTime;
    }

    public int getTotalFeedingTime() {
        return totalFeedingTime;
    }

    public void setTotalFeedingTime(final int totalFeedingTime) {

    }
}
