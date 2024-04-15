package me.munchii.industrialreborn.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class EntityUtil {
    public static Optional<Entity> createFromNbt(ServerWorld world, NbtCompound tag) {
        if (!tag.contains("id")) return Optional.empty();
        EntityType<?> entityType = Registries.ENTITY_TYPE.get(Identifier.tryParse(tag.getString("id")));
        Entity entity = entityType.create(world, tag, t -> {}, BlockPos.ORIGIN, SpawnReason.SPAWNER, true, false);
        if (entity == null) return Optional.empty();

        return Optional.of(entity);
    }
}
