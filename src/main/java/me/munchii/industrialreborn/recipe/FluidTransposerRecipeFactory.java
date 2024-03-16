package me.munchii.industrialreborn.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.crafting.serde.RebornFluidRecipeSerde;
import reborncore.common.fluid.container.FluidInstance;

import java.util.List;

public class FluidTransposerRecipeFactory implements RebornFluidRecipeSerde.SimpleFluidRecipeFactory<FluidTransposerRecipe> {
    // why is this needed when in TR 5.8.7 (their own source code), they use the way I do in 1.20.4?
    @Override
    public FluidTransposerRecipe create(RebornRecipeType<FluidTransposerRecipe> type, Identifier id, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @NotNull FluidInstance fluid) {
        return new FluidTransposerRecipe(type, ingredients, outputs, power, time, fluid);
    }
}
