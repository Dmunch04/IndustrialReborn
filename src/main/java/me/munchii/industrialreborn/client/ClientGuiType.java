package me.munchii.industrialreborn.client;

import me.munchii.industrialreborn.IndustrialReborn;
import me.munchii.industrialreborn.blockentity.GuiType;
import me.munchii.industrialreborn.blockentity.PoweredSpawnerBlockEntity;
import me.munchii.industrialreborn.client.gui.GuiPoweredSpawner;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.util.Identifier;
import techreborn.client.GuiFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record ClientGuiType<T extends BlockEntity>(GuiType<T> guiType, GuiFactory<T> guiFactory) {
    public static final Map<Identifier, ClientGuiType<?>> TYPES = new HashMap<>();

    public static final ClientGuiType<PoweredSpawnerBlockEntity> POWERED_SPAWNER = register(GuiType.POWERED_SPAWNER, GuiPoweredSpawner::new);

    public static <T extends BlockEntity> ClientGuiType<T> register(GuiType<T> type, GuiFactory<T> factory) {
        return new ClientGuiType<>(type, factory);
    }

    public static void validate() {
        // Ensure all gui types also have a client version.
        for (Identifier identifier : GuiType.TYPES.keySet()) {
            if (!identifier.getNamespace().equals(IndustrialReborn.MOD_ID)) continue;
            Objects.requireNonNull(TYPES.get(identifier), "No ClientGuiType for " + identifier);
        }
    }

    public ClientGuiType(GuiType<T> guiType, GuiFactory<T> guiFactory) {
        this.guiType = Objects.requireNonNull(guiType);
        this.guiFactory = Objects.requireNonNull(guiFactory);

        HandledScreens.register(guiType.getScreenHandlerType(), guiFactory());
        TYPES.put(guiType.getIdentifier(), this);
    }
}
