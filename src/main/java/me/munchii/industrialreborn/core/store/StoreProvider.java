package me.munchii.industrialreborn.core.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StoreProvider {
    private final Map<Store<?>, Optional<?>> stores;

    public StoreProvider() {
        this.stores = new HashMap<>();
    }

    public <T> void addStore(Store<T> store, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<?> optional) {
        stores.putIfAbsent(store, optional);
    }

    public <T> Optional<T> getStore(Store<T> store) {
        //noinspection unchecked
        return (Optional<T>) stores.getOrDefault(store, Optional.empty());
    }
}
