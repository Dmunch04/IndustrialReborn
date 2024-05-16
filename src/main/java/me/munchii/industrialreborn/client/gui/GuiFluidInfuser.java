package me.munchii.industrialreborn.client.gui;

import me.munchii.industrialreborn.blockentity.FluidInfuserBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiFluidInfuser extends GuiBase<BuiltScreenHandler> {
    private final FluidInfuserBlockEntity blockEntity;

    public GuiFluidInfuser(int syncID, final PlayerEntity player, final FluidInfuserBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
        super.drawBackground(drawContext, f, mouseX, mouseY);
        final Layer layer = Layer.BACKGROUND;

        // Battery slot
        drawSlot(drawContext, 8, 72, layer);
        // Liquid input slot
        drawSlot(drawContext, 34, 35, layer);
        // Liquid output slot
        drawSlot(drawContext, 34, 55, layer);
        // Solid material input slot
        drawSlot(drawContext, 84, 43, layer);
        // Output slot
        drawSlot(drawContext, 126, 43, layer);
    }

    @Override
    protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        final Layer layer = Layer.FOREGROUND;

        builder.drawProgressBar(drawContext, this, blockEntity.getProgressScaled(100), 100, 105, 47, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawTank(drawContext, this, 53, 25, mouseX, mouseY, blockEntity.tank.getFluidInstance(), blockEntity.tank.getFluidValueCapacity(), blockEntity.tank.isEmpty(), layer);
        builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
