package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.recipe.FluidInfuserRecipe;
import me.munchii.industrialreborn.recipe.FluidInfuserRecipeFactory;
import me.munchii.industrialreborn.utils.Resources;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.crafting.serde.RebornFluidRecipeSerde;

public class IRRecipes {
    public static final RebornFluidRecipeSerde<FluidInfuserRecipe> FLUID_INFUSER_RECIPE_SERDE = RebornFluidRecipeSerde.create(new FluidInfuserRecipeFactory());
    public static final RebornRecipeType<FluidInfuserRecipe> FLUID_INFUSER = RecipeManager.newRecipeType(FLUID_INFUSER_RECIPE_SERDE, Resources.id("fluid_infuser"));

    public static RebornRecipeType<?> byName(Identifier identifier) {
        return (RebornRecipeType<?>) Registries.RECIPE_SERIALIZER.get(identifier);
    }
}
