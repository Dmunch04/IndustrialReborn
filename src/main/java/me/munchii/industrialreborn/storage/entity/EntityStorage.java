package me.munchii.industrialreborn.storage.entity;

import me.munchii.industrialreborn.IRNBTKeys;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class EntityStorage {
    public static boolean hasStoredEntity(ItemStack stack) {
        if (!stack.hasNbt()) return false;

        NbtCompound nbt = stack.getNbt();
        if (nbt == null || (!nbt.contains(IRNBTKeys.ENTITY_STORAGE))) return false;

        NbtCompound entityNbt = nbt.getCompound(IRNBTKeys.ENTITY_STORAGE);
        return entityNbt != null && !entityNbt.isEmpty();
    }

    public static void saveEntity(ItemStack stack, LivingEntity entity) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtCompound entityNbt = new NbtCompound();
        if (nbt.contains(IRNBTKeys.ENTITY_STORAGE)) {
            entityNbt = nbt.getCompound(IRNBTKeys.ENTITY_STORAGE);
        }

        entity.saveSelfNbt(entityNbt);
        nbt.put(IRNBTKeys.ENTITY_STORAGE, entityNbt);
        stack.setNbt(stack.writeNbt(nbt));
    }

    public static Optional<NbtCompound> getStoredEntity(ItemStack stack) {
        if (!hasStoredEntity(stack)) {
            return Optional.empty();
        }

        NbtCompound entityNbt = stack.getNbt();

        if (entityNbt == null) {
            return Optional.empty();
        }

        return Optional.of(entityNbt.getCompound(IRNBTKeys.ENTITY_STORAGE));
    }
}
