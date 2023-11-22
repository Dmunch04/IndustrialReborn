package me.munchii.industrialreborn.utils;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityCaptureUtils {
    private static final Identifier ENDER_DRAGON = new Identifier("minecraft", "ender_dragon");

    @Nullable
    private static List<Identifier> capturableEntities = null;

    public static List<Identifier> getCapturableEntities() {
        if (capturableEntities == null) {
            var livingEntities = ImmutableList.copyOf(
                    Registries.ENTITY_TYPE.stream()
                            .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                            .collect(Collectors.toList()));

            List<Identifier> entities = new ArrayList<>();
            for (EntityType<? extends LivingEntity> type : livingEntities) {
                if (getCapturableStatus(type, null) == CapturableStatus.CAPTURABLE) {
                    Optional<RegistryKey<EntityType<?>>> key = Registries.ENTITY_TYPE.getKey(type);
                    if (key.isPresent()) {
                        if (!key.get().getValue().equals(ENDER_DRAGON)) {
                            entities.add(key.get().getValue());
                        }
                    }
                }
            }

            capturableEntities = ImmutableList.copyOf(entities);
        }

        return capturableEntities;
    }

    public enum CapturableStatus {
        CAPTURABLE(Text.empty()),
        BOSS(Text.empty()),
        BLACKLISTED(Text.empty()),
        INCOMPATIBLE(Text.empty());

        private final Text errorMessage;

        CapturableStatus(Text errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Text getErrorMessage() {
            return errorMessage;
        }
    }

    public static CapturableStatus getCapturableStatus(EntityType<? extends LivingEntity> type, @Nullable Entity entity) {
        // boss check?

        // incompatible check

        // blacklist check

        return CapturableStatus.CAPTURABLE;
    }
}
