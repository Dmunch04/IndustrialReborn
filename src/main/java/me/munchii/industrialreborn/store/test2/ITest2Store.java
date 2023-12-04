package me.munchii.industrialreborn.store.test2;

import net.minecraft.item.ItemStack;

public interface ITest2Store {
    default boolean hasStoredString(ItemStack stack) {
        return !getStoredString(stack).isEmpty();
    }

    String getStoredString(ItemStack stack);

    void setStoredString(ItemStack stack, String s);
}
