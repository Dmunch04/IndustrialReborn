package me.munchii.industrialreborn.core.store;

import net.minecraft.item.Item;

import java.util.Optional;

public abstract class StoreItem extends Item {
    public final StoreProvider provider;

    public StoreItem(Settings settings) {
        super(settings);

        this.provider = new StoreProvider();
        initStores(provider);
    }

    public abstract void initStores(StoreProvider provider);

    public <T> Optional<T> getStore(Store<T> store) {
        return provider.getStore(store);
    }
}
