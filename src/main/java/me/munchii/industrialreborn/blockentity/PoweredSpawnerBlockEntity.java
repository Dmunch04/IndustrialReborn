package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.storage.entity.EntityStorage;
import me.munchii.industrialreborn.utils.EntityUtil;
import me.munchii.industrialreborn.utils.IndustrialTags;
import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.MobEntity;
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
import net.minecraft.util.math.Vec3i;
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
import techreborn.config.TechRebornConfig;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PoweredSpawnerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, IRangedBlockEntity {
    public int spawnTime = 0;
    public int totalSpawnTime = IndustrialRebornConfig.poweredSpawnerTicksPerSpawn;

    public final int vialSlot = 0;

    public final int spawnRange = IndustrialRebornConfig.poweredSpawnerSpawnRange;
    public int extraRange = 0;

    public final EntitySoulStore entityStore = new EntitySoulStore();

    private boolean exactCopy;

    public PoweredSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.POWERED_SPAWNER, pos, state, "PoweredSpawner", IndustrialRebornConfig.poweredSpawnerMaxInput, IndustrialRebornConfig.poweredSpawnerMaxEnergy, IRContent.Machine.POWERED_SPAWNER.block, 1);

        this.inventory = new RebornInventory<>(2, "PoweredSpawnerBlockEntity", 1, this);

        this.exactCopy = IndustrialRebornConfig.poweredSpawnerExactCopy;
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
                if (spawnTime >= totalSpawnTime) {
                    boolean didSpawn = spawnEntity(world, pos, entityStore.entityTag, getRange(), exactCopy);
                    if (didSpawn) {
                        useEnergy(IndustrialRebornConfig.poweredSpawnerEnergyPerSpawn);
                        spawnTime = 0;
                    }
                } else {
                    spawnTime += (int) Math.round(getSpeedMultiplier() / TechRebornConfig.overclockerSpeed) + 1;
                }
            } else if (spawnTime > 0) {
                spawnTime = 0;
            }
        }
    }

    private static boolean spawnEntity(World world, BlockPos pos, NbtCompound entityTag, int range, boolean exactCopy) {
        Random random = world.getRandom();
        double spawnX = pos.getX() + (random.nextDouble() - random.nextDouble()) * (double) range + 0.5D;
        double spawnY = pos.getY() + 0.2;
        double spawnZ = pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double) range + 0.5D;

        // what if the surface area around the mob spawner isn't flat? they will spawn in the air. is that fine?

        Optional<Entity> optionalEntity = EntityUtil.createFromNbt((ServerWorld) world, entityTag, SpawnReason.SPAWNER);
        if (optionalEntity.isEmpty()) return false;
        Entity entity = optionalEntity.get();

        if (TagUtil.isIn(IndustrialTags.EntityTypes.POWERED_SPAWNER_BLACKLIST, entity.getType())) return false;

        if (canEntitySpawn(world, entity)) return false;

        if (entity instanceof WardenEntity warden) {
            warden.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, null, 1200L);
        }

        if (exactCopy) {
            entity.readNbt(entityTag);
        }
        entity.setUuid(UUID.randomUUID());

        entity.setPosition(spawnX, spawnY, spawnZ);
        entity.applyRotation(BlockRotation.random(random));
        return world.spawnEntity(entity);
    }

    private static boolean canEntitySpawn(World world, Entity entity) {
        return !world.getEntityCollisions(entity, entity.getBoundingBox()).isEmpty() && !world.containsFluid(entity.getBoundingBox());
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
            if (!EntityStorage.hasStoredEntity(stack)) return false;
            Optional<NbtCompound> tag = EntityStorage.getStoredEntity(stack);
            if (tag.isEmpty()) return false;
            return !TagUtil.isIn(IndustrialTags.EntityTypes.POWERED_SPAWNER_BLACKLIST, Registries.ENTITY_TYPE.get(Identifier.tryParse(tag.get().getString("id"))));
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
            Optional<NbtCompound> tag = EntityStorage.getStoredEntity(soulVial);
            tag.ifPresent(nbtCompound -> {
                this.entityTag = nbtCompound;

                Optional<Entity> entity = EntityUtil.createFromNbt((ServerWorld) world, entityTag, SpawnReason.SPAWNER);
                entity.ifPresent(ent -> {
                    this.entityType = EntityUtil.getNameToBeDisplayed(ent);
                });
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
