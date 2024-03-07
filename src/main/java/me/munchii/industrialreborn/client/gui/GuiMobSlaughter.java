package me.munchii.industrialreborn.client.gui;

import me.munchii.industrialreborn.blockentity.MobSlaughterBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.gui.GuiBase;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiMobSlaughter extends GuiBase<BuiltScreenHandler> {
    final MobSlaughterBlockEntity blockEntity;

    public GuiMobSlaughter(int syncID, final PlayerEntity player, final MobSlaughterBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncID, player));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void drawBackground(DrawContext drawContext, final float f, final int mouseX, final int mouseY) {
        super.drawBackground(drawContext, f, mouseX, mouseY);
        final Layer layer = Layer.BACKGROUND;
    }

    @Override
    protected void drawForeground(DrawContext drawContext, final int mouseX, final int mouseY) {
        super.drawForeground(drawContext, mouseX, mouseY);
        final Layer layer = Layer.FOREGROUND;
        //builder.drawFluid(drawContext, this, new FluidInstance(IRFluids.LIQUID_EXPERIENCE.getFluid()));

        builder.drawMultiEnergyBar(drawContext, this, 9, 19, (int) blockEntity.getEnergy(), (int) blockEntity.getMaxStoredPower(), mouseX, mouseY, 0, layer);
        builder.drawTank(drawContext, this, 44, 25, mouseX, mouseY, blockEntity.experienceTank.getFluidInstance(), blockEntity.experienceTank.getFluidValueCapacity(), blockEntity.experienceTank.isEmpty(), layer);
    }
}
