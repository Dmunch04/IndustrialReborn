package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.recipe.FluidTransposerRecipe;
import me.munchii.industrialreborn.recipe.FluidTransposerRecipeFactory;
import me.munchii.industrialreborn.utils.Resources;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.crafting.serde.RebornFluidRecipeSerde;
import reborncore.common.crafting.serde.RebornRecipeSerde;

public class IRRecipes {
    public static final RebornFluidRecipeSerde<FluidTransposerRecipe> FLUID_TRANSPOSER_RECIPE_SERDE = RebornFluidRecipeSerde.create(new FluidTransposerRecipeFactory());
    public static final RebornRecipeType<FluidTransposerRecipe> FLUID_TRANSPOSER = RecipeManager.newRecipeType(FLUID_TRANSPOSER_RECIPE_SERDE, Resources.id("fluid_transposer"));

    public static RebornRecipeType<?> byName(Identifier identifier) {
        return (RebornRecipeType<?>) Registries.RECIPE_SERIALIZER.get(identifier);
    }
}
