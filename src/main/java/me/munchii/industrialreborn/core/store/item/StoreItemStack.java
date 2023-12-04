package me.munchii.industrialreborn.core.store.item;

import me.munchii.industrialreborn.core.store.Store;
import me.munchii.industrialreborn.core.store.StoreProvider;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class StoreItemStack {
    private final ItemStack stackRef;
    private final StoreProvider provider;

    public StoreItemStack(ItemStack stack, StoreProvider provider) {
        this.stackRef = stack;
        this.provider = provider;
    }

    public ItemStack getStack() {
        return stackRef;
    }

    public <T> Optional<T> getStore(Store<T> store) {
        return provider.getStore(store);
    }
}
