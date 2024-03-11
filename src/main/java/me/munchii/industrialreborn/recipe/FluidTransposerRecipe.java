package me.munchii.industrialreborn.recipe;

import me.munchii.industrialreborn.blockentity.FluidTransposerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;

import java.util.List;

public class FluidTransposerRecipe extends RebornFluidRecipe {
    public FluidTransposerRecipe(RebornRecipeType<FluidTransposerRecipe> type, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, FluidInstance fluid) {
        super(type, ingredients, outputs, power, time, fluid);
    }

    @Override
    public Tank getTank(BlockEntity blockEntity) {
        return ((FluidTransposerBlockEntity) blockEntity).getTank();
    }
}
