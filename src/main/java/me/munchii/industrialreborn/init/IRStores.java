package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.core.store.Store;
import me.munchii.industrialreborn.core.store.StoreManager;
import me.munchii.industrialreborn.core.store.StoreToken;
import me.munchii.industrialreborn.store.entity.IEntityStore;
import me.munchii.industrialreborn.store.test.ITestStore;
import me.munchii.industrialreborn.store.test2.ITest2Store;

public class IRStores {
    public static final Store<IEntityStore> ENTITY_STORE = StoreManager.getStore(new StoreToken<>("1"));
    public static final Store<ITestStore> TEST_STORE = StoreManager.getStore(new StoreToken<>("2"));
    public static final Store<ITest2Store> TEST_2_STORE = StoreManager.getStore(new StoreToken<>("3"));
}
