package me.munchii.industrialreborn.client.gui;

import me.munchii.industrialreborn.blockentity.AnimalFeederBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiAnimalFeeder extends GuiBase<BuiltScreenHandler> {
    private final AnimalFeederBlockEntity blockEntity;

    public GuiAnimalFeeder(int syncID, final PlayerEntity player, final AnimalFeederBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
        super.drawBackground(drawContext, f, mouseX, mouseY);
        final Layer layer = Layer.BACKGROUND;

        drawSlot(drawContext, 8, 72, layer);

        final int gridYPos = 22;
        drawSlot(drawContext, 70, gridYPos, layer);
        drawSlot(drawContext, 88, gridYPos, layer);
        drawSlot(drawContext, 70, gridYPos + 18, layer);
        drawSlot(drawContext, 88, gridYPos + 18, layer);
        drawSlot(drawContext, 70, gridYPos + 36, layer);
        drawSlot(drawContext, 88, gridYPos + 36, layer);
    }

    @Override
    protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        final Layer layer = Layer.FOREGROUND;

        addHologramButton(120, 22, 212, layer).clickHandler(this::onClick);
        builder.drawHologramButton(drawContext, this, 120, 22, mouseX, mouseY, layer);

        builder.drawProgressBar(drawContext, this, blockEntity.getFeedingTime(), blockEntity.getTotalFeedingTime(), 121, 42, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }

    public void onClick(GuiButtonExtended button, Double x, Double y) {
        blockEntity.renderMultiblock ^= !hideGuiElements();
    }
}
