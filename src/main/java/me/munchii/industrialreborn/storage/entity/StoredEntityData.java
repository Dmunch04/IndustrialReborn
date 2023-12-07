package me.munchii.industrialreborn.storage.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import reborncore.common.util.NBTSerializable;

import java.util.Optional;

public class StoredEntityData implements NBTSerializable {
    private NbtCompound entityTag = new NbtCompound();
    private float maxHealth = 0.0f;

    public static final String KEY_ID = "id";

    public static final String KEY_ENTITY = "Entity";
    private static final String KEY_HEALTH = "Health";
    private static final String KEY_MAX_HEALTH = "MaxHealth";

    public static StoredEntityData of(LivingEntity entity) {
        StoredEntityData data = new StoredEntityData();
        data.entityTag = livingEntitySerializeNBT(entity);
        data.maxHealth = entity.getMaxHealth();
        return data;
    }

    public static StoredEntityData of(Identifier entityType) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(KEY_ID, entityType.toString());

        StoredEntityData data = new StoredEntityData();
        data.entityTag = nbt;
        data.maxHealth = 0.0f;
        return data;
    }

    public static StoredEntityData empty() {
        StoredEntityData data = new StoredEntityData();
        data.maxHealth = 0.0f;
        return data;
    }

    public Optional<Identifier> getEntityType() {
        NbtCompound nbt = entityTag;
        if (nbt.contains(KEY_ID)) {
            return Optional.of(new Identifier(nbt.getString(KEY_ID)));
        }

        return Optional.empty();
    }

    public NbtCompound getEntityTag() {
        return entityTag;
    }

    public Optional<Pair<Float, Float>> getHealthState() {
        if (maxHealth > 0.0f) {
            NbtCompound nbt = entityTag;
            if (nbt.contains(KEY_HEALTH)) {
                return Optional.of(new Pair<>(nbt.getFloat(KEY_HEALTH), maxHealth));
            }
        }

        return Optional.empty();
    }

    @Override
    public @NotNull NbtCompound write() {
        NbtCompound nbt = new NbtCompound();
        nbt.put(KEY_ENTITY, entityTag);
        if (maxHealth > 0.0f) {
            nbt.putFloat(KEY_MAX_HEALTH, maxHealth);
        }

        return nbt;
    }

    @Override
    public void read(@NotNull NbtCompound nbtCompound) {
        entityTag = nbtCompound.getCompound(KEY_ENTITY);
        if (nbtCompound.contains(KEY_MAX_HEALTH)) {
            maxHealth = nbtCompound.getFloat(KEY_MAX_HEALTH);
        }
    }

    public static NbtCompound livingEntitySerializeNBT(LivingEntity entity) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(KEY_ID, Registries.ENTITY_TYPE.getKey(entity.getType()).get().getValue().toString());
        nbt.putFloat(KEY_HEALTH, entity.getHealth());
        return nbt;
    }
}
