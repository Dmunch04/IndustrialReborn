package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.recipe.FluidTransposerRecipe;
import me.munchii.industrialreborn.utils.Resources;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.crafting.serde.RebornFluidRecipeSerde;

public class IRRecipes {
    public static final RebornFluidRecipeSerde<FluidTransposerRecipe> FLUID_TRANSPOSER_RECIPE_SERDE = RebornFluidRecipeSerde.create(FluidTransposerRecipe::new);

    public static final RebornRecipeType<FluidTransposerRecipe> FLUID_TRANSPOSER = RecipeManager.newRecipeType(FLUID_TRANSPOSER_RECIPE_SERDE, Resources.id("fluid_transposer"));

    public static RebornRecipeType<?> byName(Identifier identifier) {
        return (RebornRecipeType<?>) Registries.RECIPE_SERIALIZER.get(identifier);
    }
}
