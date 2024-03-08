package me.munchii.industrialreborn.utils;

import net.fabricmc.fabric.api.tag.convention.v1.TagUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class IndustrialTags {
    public static class EntityTypes {
        public static final TagKey<EntityType<?>> MOB_SLAUGHTER_INSTANT_KILL_BLACKLIST = TagKey.of(RegistryKeys.ENTITY_TYPE, Resources.id("mob_crusher_blacklist"));
    }
}
