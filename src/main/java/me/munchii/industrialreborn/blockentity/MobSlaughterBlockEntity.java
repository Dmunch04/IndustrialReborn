package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import me.munchii.industrialreborn.utils.IndustrialTags;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;

import java.util.List;
import java.util.Objects;

public class MobSlaughterBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, IRangedBlockEntity {
    public int slaughterTime = 0;
    public int totalSlaughterTime = IndustrialRebornConfig.mobSlaughterTicksPerSlaughter;

    public final int slaughterRadius = IndustrialRebornConfig.mobSlaughterRadius;
    public int extraRadius = 0;

    public final Tank experienceTank;

    private BlockPos centerPos;
    private Box slaughterArea;

    public MobSlaughterBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.MOB_SLAUGHTER, pos, state, "MobSlaughter", IndustrialRebornConfig.mobSlaughterMaxInput, IndustrialRebornConfig.mobSlaughterMaxEnergy, IRContent.Machine.MOB_SLAUGHTER.block, 6);
        this.inventory = new RebornInventory<>(7, "MobSlaughterBlockEntity", 64, this);

        this.experienceTank = new Tank("MobSlaughterBlockEntity", FluidValue.BUCKET.multiply(16), this);
        experienceTank.setFluid(IRFluids.LIQUID_EXPERIENCE.getFluid());
    }

    @Override
    @NotNull
    public Tank getTank() {
        return experienceTank;
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

        if (slaughterArea == null) {
            slaughterArea = new Box(
                    centerPos.getX() - getRadius(),
                    centerPos.getY(),
                    centerPos.getZ() - getRadius(),
                    centerPos.getX() + getRadius(),
                    centerPos.getY() + 3,
                    centerPos.getZ() + getRadius()
            );
        }

        updateState();

        if (getStored() > IndustrialRebornConfig.mobSlaughterEnergyPerSlaughter && !experienceTank.isFull()) {
            if (slaughterTime >= totalSlaughterTime) {
                boolean didKill = killEntity(world);
                if (didKill) {
                    useEnergy(IndustrialRebornConfig.mobSlaughterEnergyPerSlaughter);
                }
                slaughterTime = 0;
            } else {
                slaughterTime += (int) Math.round(getSpeedMultiplier() / TechRebornConfig.overclockerSpeed) + 1;
            }
        }
    }

    private boolean killEntity(World world) {
        ServerWorld serverWorld = (ServerWorld) world;
        List<MobEntity> nearbyEntities = serverWorld.getEntitiesByClass(MobEntity.class, slaughterArea, entity -> entity.isAlive()
                && !entity.isInvulnerable()
                && !entity.isBaby()
                && !(entity instanceof WitherEntity && ((WitherEntity) entity).getInvulnerableTimer() > 0));

        if (!nearbyEntities.isEmpty()) {
            MobEntity entity = nearbyEntities.get(0);
            FakePlayer player = FakePlayer.get(serverWorld);
            if (TagUtil.isIn(IndustrialTags.EntityTypes.MOB_SLAUGHTER_INSTANT_KILL_BLACKLIST, entity.getType())) {
                damage(entity, player);
            } else {
                instantKill(world, entity, player);
            }

            return true;
        }

        return false;
    }

    private void damage(MobEntity entity, FakePlayer player) {
        entity.damage(entity.getDamageSources().playerAttack(player), IndustrialRebornConfig.mobSlaughterAttackDamage);
    }

    private void instantKill(World world, MobEntity entity, FakePlayer player) {
        final int experience = entity.getXpToDrop();
        final DamageSource damageSource = entity.getDamageSources().playerAttack(player);

        LootTable table = Objects.requireNonNull(world.getServer()).getLootManager().getLootTable(entity.getLootTable());
        LootContextParameterSet context = new LootContextParameterSet.Builder((ServerWorld) world)
                .add(LootContextParameters.THIS_ENTITY, entity)
                .add(LootContextParameters.ORIGIN, entity.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                .add(LootContextParameters.KILLER_ENTITY, player)
                .add(LootContextParameters.LAST_DAMAGE_PLAYER, player)
                .build(LootContextTypes.ENTITY);

        insertIntoInv(table.generateLoot(context));
        insertFluid(FluidValue.fromMillibuckets(experience * IndustrialRebornConfig.mobSlaughterExperienceMultiplier));

        entity.setHealth(0);
        entity.remove(Entity.RemovalReason.KILLED);
    }

    public int getRadius() {
        return slaughterRadius + extraRadius;
    }

    @Override
    public void addRange(int range) {
        extraRadius += range;
        centerPos = null;
        slaughterArea = null;
    }

    @Override
    public void addRangeMultiplier(float multiplier) {
        extraRadius += Math.round(getRadius() * multiplier);
        centerPos = null;
        slaughterArea = null;
    }

    private void updateState() {
        assert world != null;

        final BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof final BlockMachineBase blockMachineBase) {
            boolean active = !experienceTank.isFull() && getStored() > IndustrialRebornConfig.mobSlaughterEnergyPerSlaughter;
            if (blockState.get(BlockMachineBase.ACTIVE) != active) {
                blockMachineBase.setActive(active, world, pos);
            }
        }
    }

    private void insertFluid(FluidValue amount) {
        if (experienceTank.getFluidAmount().add(amount).equalOrMoreThan(FluidValue.BUCKET.multiply(16))) {
            setExperienceAmount(FluidValue.BUCKET.multiply(16));
            return;
        }
        setExperienceAmount(experienceTank.getFluidAmount().add(amount));
    }

    private boolean insertIntoInv(List<ItemStack> stacks) {
        boolean result = false;
        for (ItemStack stack : stacks) {
            for (int i = 0; i < 6; i++) {
                if (insertIntoInv(i, stack)) result = true;
                if (stack.isEmpty()) break;
            }
        }

        return result;
    }

    private boolean insertIntoInv(int slot, ItemStack stack) {
        ItemStack targetStack = inventory.getStack(slot);
        if (targetStack.isEmpty()) {
            inventory.setStack(slot, stack.copy());
            stack.decrement(stack.getCount());
            return true;
        } else {
            if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
                int freeStackSpace = targetStack.getMaxCount() - targetStack.getCount();
                if (freeStackSpace > 0) {
                    int transferAmount = Math.min(freeStackSpace, stack.getCount());
                    targetStack.increment(transferAmount);
                    stack.decrement(transferAmount);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack getToolDrop(PlayerEntity p0) {
        return IRContent.Machine.MOB_SLAUGHTER.getStack();
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("mob_slaughter").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .energySlot(6, 8, 72)
                .outputSlot(0, 70, 22).outputSlot(1, 88, 22)
                .outputSlot(2, 70, 40).outputSlot(3, 88, 40)
                .outputSlot(4, 70, 58).outputSlot(5, 88, 58)
                .syncEnergyValue()
                .sync(this::getExperienceAmount, this::setExperienceAmount)
                .sync(this::getSlaughterTime, this::setSlaughterTime)
                .sync(this::getTotalSlaughterTime, this::setTotalSlaughterTime)
                .sync(experienceTank)
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
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        getTank().read(tag);
        centerPos = null;
        slaughterArea = null;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        getTank().write(tag);
    }

    @Override
    public void resetUpgrades() {
        super.resetUpgrades();
        extraRadius = 0;
    }

    public FluidValue getExperienceAmount() {
        return experienceTank.getFluidAmount();
    }

    public void setExperienceAmount(FluidValue amount) {
        experienceTank.setFluidAmount(amount);
    }

    public int getSlaughterTime() {
        return slaughterTime;
    }

    public void setSlaughterTime(int slaughterTime) {
        this.slaughterTime = slaughterTime;
    }

    public int getTotalSlaughterTime() {
        return totalSlaughterTime;
    }

    public void setTotalSlaughterTime(final int totalSlaughterTime) {
        this.totalSlaughterTime = totalSlaughterTime;
    }
}
