package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

import java.util.List;

public class SoulExtractorBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, IRangedBlockEntity {
    public int extractionTime = 0;
    public final int totalExtractionTime = IndustrialRebornConfig.soulExtractorTicksPerExtraction;

    public final int extractionRadius = IndustrialRebornConfig.soulExtractorRadius;
    public int extraRadius = 0;

    public final Tank essenceTank;

    private BlockPos centerPos;
    private Box extractArea;

    @Nullable
    private MobEntity currentTarget;

    public SoulExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.SOUL_EXTRACTOR, pos, state, "SoulExtractor", IndustrialRebornConfig.soulExtractorMaxInput, IndustrialRebornConfig.soulExtractorMaxEnergy, IRContent.Machine.SOUL_EXTRACTOR.block, 0);
        this.inventory = new RebornInventory<>(1, "SoulExtractorBlockEntity", 64, this);

        this.essenceTank = new Tank("SoulExtractorBlockEntity", FluidValue.BUCKET.multiply(16), this);
        essenceTank.setFluid(IRFluids.SOUL_ESSENCE.getFluid());
    }

    @Override
    @NotNull
    public Tank getTank() {
        return essenceTank;
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

        if (extractArea == null) {
            final int radius = getRadius();
            extractArea = new Box(
                    centerPos.getX() - radius,
                    centerPos.getY(),
                    centerPos.getZ() - radius,
                    centerPos.getX() + radius,
                    centerPos.getY() + 3,
                    centerPos.getZ() + radius
            );
        }

        updateState();

        if (getStored() > IndustrialRebornConfig.soulExtractorEnergyPerExtraction && !essenceTank.isFull()) {
            if (extractionTime >= totalExtractionTime) {
                boolean res = extractSoul(world);
                if (res) useEnergy(IndustrialRebornConfig.soulExtractorEnergyPerExtraction);
                extractionTime = 0;
            } else {
                extractionTime++;
            }
        }
    }

    private boolean extractSoul(World world) {
        if (currentTarget == null) {
            MobEntity target = getTarget();
            if (target == null) return false;
            currentTarget = target;
        }

        currentTarget.setHealth(currentTarget.getHealth() - IndustrialRebornConfig.soulExtractorAttackDamage);
        if (currentTarget.getHealth() <= 0) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, currentTarget.getX(), currentTarget.getY(), currentTarget.getZ(), 50, 0, 0, 0, 0);
            currentTarget.remove(Entity.RemovalReason.KILLED);
        }
        insertFluid(FluidValue.fromMillibuckets(Math.round(currentTarget.getMaxHealth() * IndustrialRebornConfig.soulExtractorEssenceMultiplier)));

        if (currentTarget.isDead()) {
            currentTarget = null;
        }

        return true;
    }

    @Nullable
    private MobEntity getTarget() {
        ServerWorld serverWorld = (ServerWorld) world;
        assert serverWorld != null;
        List<MobEntity> nearbyEntities = serverWorld.getEntitiesByClass(MobEntity.class, extractArea, entity -> entity.isAlive()
                && !entity.isInvulnerable()
                && !(entity instanceof WitherEntity && ((WitherEntity) entity).getInvulnerableTimer() > 0));

        if (!nearbyEntities.isEmpty()) {
            return nearbyEntities.get(0);
        }

        return null;
    }

    public int getRadius() {
        return extractionRadius + extraRadius;
    }

    @Override
    public void addRange(int range) {
        extraRadius += range;
        centerPos = null;
        extractArea = null;
    }

    @Override
    public void addRangeMultiplier(float multiplier) {
        extraRadius += Math.round(extractionRadius * multiplier);
        centerPos = null;
        extractArea = null;
    }

    private void updateState() {
        assert world != null;

        final BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof final BlockMachineBase blockMachineBase) {
            boolean active = !essenceTank.isFull() && getStored() > IndustrialRebornConfig.poweredSpawnerEnergyPerSpawn;
            if (blockState.get(BlockMachineBase.ACTIVE) != active) {
                blockMachineBase.setActive(active, world, pos);
            }
        }
    }

    private void insertFluid(FluidValue amount) {
        if (essenceTank.getFluidAmount().add(amount).equalOrMoreThan(FluidValue.BUCKET.multiply(16))) {
            setEssenceAmount(FluidValue.BUCKET.multiply(16));
            return;
        }
        setEssenceAmount(essenceTank.getFluidAmount().add(amount));
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("mob_slaughter").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .energySlot(0, 8, 72)
                .syncEnergyValue()
                .sync(this::getEssenceAmount, this::setEssenceAmount)
                .sync(essenceTank)
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
        return IRContent.Machine.SOUL_EXTRACTOR.getStack();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        getTank().read(tag);
        centerPos = null;
        extractArea = null;
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

    public FluidValue getEssenceAmount() {
        return essenceTank.getFluidAmount();
    }

    public void setEssenceAmount(FluidValue amount) {
        essenceTank.setFluidAmount(amount);
    }
}
