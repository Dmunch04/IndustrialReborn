package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.utils.Resources;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;

import java.util.HashMap;
import java.util.Map;

public class GuiType<T extends BlockEntity> implements IMachineGuiHandler {
    public static final Map<Identifier, GuiType<?>> TYPES = new HashMap<>();

    public static final GuiType<PoweredSpawnerBlockEntity> POWERED_SPAWNER = register("powered_spawner");
    public static final GuiType<MobSlaughterBlockEntity> MOB_SLAUGHTER = register("mob_slaughter");
    public static final GuiType<SoulExtractorBlockEntity> SOUL_EXTRACTOR = register("soul_extractor");

    private static <T extends BlockEntity> GuiType<T> register(String id) {
        return register(Resources.id(id));
    }

    public static <T extends BlockEntity> GuiType<T> register(Identifier identifier) {
        if (TYPES.containsKey(identifier)) {
            throw new RuntimeException("Duplicate gui type found");
        }

        return new GuiType<>(identifier);
    }

    private final Identifier identifier;
    private final ScreenHandlerType<BuiltScreenHandler> screenHandlerType;

    private GuiType(Identifier identifier) {
        this.identifier = identifier;
        this.screenHandlerType = Registry.register(Registries.SCREEN_HANDLER, identifier, new ExtendedScreenHandlerType<>(getScreenHandlerFactory()));

        TYPES.put(identifier, this);
    }

    private ExtendedScreenHandlerType.ExtendedFactory<BuiltScreenHandler> getScreenHandlerFactory() {
        return (syncId, playerInventory, packetByteBuf) -> {
            final BlockEntity blockEntity = playerInventory.player.getWorld().getBlockEntity(packetByteBuf.readBlockPos());
            BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, playerInventory.player);

            //Set the screen handler type, not ideal but works lol
            screenHandler.setType(screenHandlerType);

            return screenHandler;
        };
    }

    @Override
    public void open(PlayerEntity player, BlockPos pos, World world) {
        if (!world.isClient) {
            //This is awful
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
                    packetByteBuf.writeBlockPos(pos);
                }

                @Override
                public Text getDisplayName() {
                    return Text.literal("What is this for?");
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    final BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
                    BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, player);
                    screenHandler.setType(screenHandlerType);
                    return screenHandler;
                }
            });
        }
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public ScreenHandlerType<BuiltScreenHandler> getScreenHandlerType() {
        return screenHandlerType;
    }
}
