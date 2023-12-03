package me.munchii.industrialreborn.core.store;

import java.util.IdentityHashMap;

public enum StoreManager {
    INSTANCE;

    private final IdentityHashMap<String, Store<?>> providers = new IdentityHashMap<>();

    public static <T> Store<T> getStore(StoreToken<T> type) {
        return INSTANCE.getStore(type.TOKEN);
    }

    <T> Store<T> getStore(String token) {
        Store<T> store;

        synchronized (providers) {
            token = token.intern();
            store = (Store<T>) providers.computeIfAbsent(token, Store::new);
        }

        return store;
    }
}
