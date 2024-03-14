package me.munchii.industrialreborn.client.gui;

import me.munchii.industrialreborn.blockentity.AnimalBabySeparatorBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.client.gui.widget.GuiButtonExtended;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiAnimalBabySeparator extends GuiBase<BuiltScreenHandler> {
    private final AnimalBabySeparatorBlockEntity blockEntity;

    public GuiAnimalBabySeparator(int syncID, final PlayerEntity player, final AnimalBabySeparatorBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
        super.drawBackground(drawContext, f, mouseX, mouseY);
        final Layer layer = Layer.BACKGROUND;

        drawSlot(drawContext, 8, 72, layer);
    }

    @Override
    protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        final Layer layer = Layer.FOREGROUND;

        addHologramButton(120, 22, 212, layer).clickHandler(this::onClick);
        builder.drawHologramButton(drawContext, this, 120, 24, mouseX, mouseY, layer);

        builder.drawProgressBar(drawContext, this, blockEntity.getSeparationTime(), blockEntity.getTotalSeparationTime(), 79, 80, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }

    public void onClick(GuiButtonExtended button, Double x, Double y) {
        blockEntity.renderMultiblock ^= !hideGuiElements();
    }
}
