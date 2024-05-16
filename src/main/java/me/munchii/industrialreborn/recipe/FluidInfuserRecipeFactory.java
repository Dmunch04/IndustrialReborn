package me.munchii.industrialreborn.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.crafting.serde.RebornFluidRecipeSerde;
import reborncore.common.fluid.container.FluidInstance;

import java.util.List;

public class FluidInfuserRecipeFactory implements RebornFluidRecipeSerde.SimpleFluidRecipeFactory<FluidInfuserRecipe> {
    // why is this needed when in TR 5.8.7 (their own source code), they use the way I do in 1.20.4?
    @Override
    public FluidInfuserRecipe create(RebornRecipeType<FluidInfuserRecipe> type, Identifier id, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @NotNull FluidInstance fluid) {
        return new FluidInfuserRecipe(type, ingredients, outputs, power, time, fluid);
    }
}
