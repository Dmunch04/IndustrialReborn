package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.storage.entity.EntityStorage;
import me.munchii.industrialreborn.utils.EntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

import java.util.Objects;
import java.util.Optional;

public class PoweredSpawnerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, IRangedBlockEntity {
    public int spawnTime = 0;
    public int totalSpawnTime = IndustrialRebornConfig.poweredSpawnerTicksPerSpawn;

    public final int vialSlot = 0;

    public final int spawnRange = IndustrialRebornConfig.poweredSpawnerSpawnRange;
    public int extraRange = 0;

    public final EntitySoulStore entityStore = new EntitySoulStore();

    public PoweredSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.POWERED_SPAWNER, pos, state, "PoweredSpawner", IndustrialRebornConfig.poweredSpawnerMaxInput, IndustrialRebornConfig.poweredSpawnerMaxEnergy, IRContent.Machine.POWERED_SPAWNER.block, 1);

        this.inventory = new RebornInventory<>(2, "PoweredSpawnerBlockEntity", 1, this);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (!(world instanceof ServerWorld) || !isActive(RedstoneConfiguration.POWER_IO)) {
            return;
        }

        // NOTE: im unsure about the use of updateState. when i only had it inside the vial check (if-else below), it didn't work

        // if there's a vial, but the spawn type hasn't been set, set it, otherwise remove
        if (!this.inventory.getStack(vialSlot).isEmpty() && !entityStore.hasStoredSoul()) {
            entityStore.storeSoul(this.inventory.getStack(vialSlot));
            updateState();
        } else if (this.inventory.getStack(vialSlot).isEmpty() && entityStore.hasStoredSoul()) {
            entityStore.emptyStore();
            updateState();
        }

        updateState();

        if (getStored() > IndustrialRebornConfig.poweredSpawnerEnergyPerSpawn) {
            if (entityStore.hasStoredSoul()) {
                if (spawnTime == totalSpawnTime) {
                    boolean didSpawn = spawnEntity(world, pos, entityStore.entityTag, getRange());
                    if (didSpawn) {
                        useEnergy(IndustrialRebornConfig.poweredSpawnerEnergyPerSpawn);
                        spawnTime = 0;
                    }
                } else {
                    spawnTime++;
                }
            } else if (spawnTime > 0) {
                spawnTime = 0;
            }
        }
    }

    private static boolean spawnEntity(World world, BlockPos pos, NbtCompound entityTag, int range) {
        Random random = world.getRandom();
        double spawnX = pos.getX() + (random.nextDouble() - random.nextDouble()) * (double) range + 0.5D;
        double spawnY = pos.getY() + 0.2;
        double spawnZ = pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double) range + 0.5D;

        // what if the surface area around the mob spawner isn't flat? they will spawn in the air. is that fine?

        Optional<Entity> optionalEntity = EntityUtil.createFromNbt((ServerWorld) world, entityTag);
        if (optionalEntity.isEmpty()) return false;
        Entity entity = optionalEntity.get();

        if (!world.getEntityCollisions(entity, entity.getBoundingBox()).isEmpty()) return false;

        if (entity instanceof WardenEntity warden) {
            warden.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, null, 1200L);
        }

        entity.setPosition(spawnX, spawnY, spawnZ);
        entity.applyRotation(BlockRotation.random(random));
        return world.spawnEntity(entity);
    }

    public int getRange() {
        return spawnRange + extraRange;
    }

    @Override
    public void addRange(int range) {
        extraRange += range;
    }

    @Override
    public void addRangeMultiplier(float multiplier) {
        extraRange += Math.round(spawnRange * multiplier);
    }

    private void updateState() {
        assert world != null;

        final BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof final BlockMachineBase blockMachineBase) {
            boolean active = entityStore.hasStoredSoul() && getStored() > IndustrialRebornConfig.poweredSpawnerEnergyPerSpawn;
            if (blockState.get(BlockMachineBase.ACTIVE) != active) {
                blockMachineBase.setActive(active, world, pos);
            }
        }
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("powered_spawner").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .filterSlot(0, 80, 54, PoweredSpawnerBlockEntity::filledVialFilter).energySlot(1, 8, 72).syncEnergyValue()
                .sync(this::getSpawnTime, this::setSpawnTime)
                .sync(this::getTotalSpawnTime, this::setTotalSpawnTime)
                .sync(this::getEntityType, this::setEntityType).addInventory().create(this, syncID);
    }

    @Override
    public void writeMultiblock(MultiblockWriter writer) {
        final BlockState glass = Blocks.RED_STAINED_GLASS.getDefaultState();

        final int range = getRange();
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                writer.add(i, 0, j, (world, pos) -> true, glass);
            }
        }
    }

    public static boolean filledVialFilter(ItemStack stack) {
        Item item = stack.getItem();
        if (item == IRContent.FILLED_SOUL_VIAL) {
            return EntityStorage.hasStoredEntity(stack);
        }

        return false;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        entityStore.emptyStore();
        // TODO: read entity store
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        // TODO: write entity store
    }

    @Override
    public void resetUpgrades() {
        super.resetUpgrades();
        extraRange = 0;
    }

    public int getScaledSpawnTime(final int i) {
        return (int) ((float) spawnTime / (float) totalSpawnTime * i);
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    public void setSpawnTime(final int spawnTime) {
        this.spawnTime = spawnTime;
    }

    public int getTotalSpawnTime() {
        return totalSpawnTime;
    }

    public void setTotalSpawnTime(final int totalSpawnTime) {
        this.totalSpawnTime = totalSpawnTime;
    }

    public String getEntityType() {
        return entityStore.entityType;
    }

    public void setEntityType(String entityType) {
        entityStore.entityType = entityType;
    }

    public class EntitySoulStore {
        public String entityType;
        @Nullable
        public NbtCompound entityTag;

        public EntitySoulStore() {
            this.entityType = "";
            this.entityTag = null;
        }

        public void storeSoul(ItemStack soulVial) {
            Optional<NbtCompound> tag = EntityStorage.getEntityDataCompound(soulVial);
            tag.ifPresent(nbtCompound -> {
                this.entityTag = nbtCompound;

                Optional<Entity> entity = EntityType.getEntityFromNbt(this.entityTag, world);
                entity.ifPresent(ent -> this.entityType = Objects.requireNonNull(ent.getDisplayName()).getString());
            });
        }

        public void emptyStore() {
            this.entityType = "";
            this.entityTag = null;
        }

        public boolean hasStoredSoul() {
            return entityTag != null;
        }
    }
}
