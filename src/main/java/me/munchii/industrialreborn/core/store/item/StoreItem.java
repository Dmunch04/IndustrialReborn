package me.munchii.industrialreborn.core.store.item;

import me.munchii.industrialreborn.core.store.Store;
import me.munchii.industrialreborn.core.store.StoreProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.Optional;

public abstract class StoreItem extends Item {
    public final StoreProvider provider;

    public StoreItem(Settings settings) {
        super(settings);

        this.provider = new StoreProvider();
        initStores(provider);
    }

    public abstract void initStores(StoreProvider provider);

    // ?: maybe make `StoreItemStack` a regular `ItemStack` instead
    // ?: should the `StoreItemStack` then take in a `StoreProvider` in it's constructor
    // ^: or how should we init/register the stores on the `StoreItemStack`
    // ^: since we wanna be able to do something like:
    // ^: public void initStores(StoreProvider provider, StoreItemStack stack) { provider.addStore(IRStores.STORE, Optional.of(new SomeStore(stack))); }
    //public abstract void initStores(StoreProvider provider, StoreItemStack stack);

    public <T> Optional<T> getStore(Store<T> store) {
        return provider.getStore(store);
    }

    public ActionResult useOnBlock(ItemUsageContext context, StoreItemStack storeStack) {
        return super.useOnBlock(context);
    }
    public ActionResult useOnEntity(StoreItemStack storeStack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return super.useOnEntity(storeStack.getStack(), user, entity, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return this.useOnBlock(context, new StoreItemStack(context.getStack(), provider));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return this.useOnEntity(new StoreItemStack(stack, provider), user, entity, hand);
    }
}
