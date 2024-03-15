package me.munchii.industrialreborn.client.gui;

import me.munchii.industrialreborn.blockentity.PoweredSpawnerBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiPoweredSpawner extends GuiBase<BuiltScreenHandler> {
    final PoweredSpawnerBlockEntity blockEntity;

    public GuiPoweredSpawner(int syncID, final PlayerEntity player, final PoweredSpawnerBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
        super.drawBackground(drawContext, f, mouseX, mouseY);
        final Layer layer = Layer.BACKGROUND;

        drawSlot(drawContext, 8, 72, layer);
        drawSlot(drawContext, 80, 54, layer);
    }

    @Override
    protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        final Layer layer = Layer.FOREGROUND;

        addHologramButton(29, 19, 212, layer).clickHandler(this::onClick);
        builder.drawHologramButton(drawContext, this, 29, 19, mouseX, mouseY, layer);

        // TODO: maybe change layout (specifically position of the progress bar)
        builder.drawText(drawContext, this, Text.of(blockEntity.getEntityType()), (backgroundWidth / 2 - getTextRenderer().getWidth(blockEntity.getEntityType()) / 2), 20, 0);
        builder.drawProgressBar(drawContext, this, blockEntity.getScaledSpawnTime(100), 100, 80, 40, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }

    public void onClick(GuiButtonExtended button, Double x, Double y) {
        blockEntity.renderMultiblock ^= !hideGuiElements();
    }
}
