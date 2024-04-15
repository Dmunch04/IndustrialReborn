package me.munchii.industrialreborn.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class IndustrialTags {
    public static class EntityTypes {
        public static final TagKey<EntityType<?>> MOB_SLAUGHTER_INSTANT_KILL_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, Resources.id("mob_crusher_blacklist"));
        public static final TagKey<EntityType<?>> SOUL_VIAL_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, Resources.id("soul_vial_blacklist"));
        public static final TagKey<EntityType<?>> POWERED_SPAWNER_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, Resources.id("powered_spawner_blacklist"));
    }
}
