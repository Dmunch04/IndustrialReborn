package me.munchii.industrialreborn.store.test2;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class Test2StoreItemStack implements ITest2Store {
    @Override
    public String getStoredString(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        String s = "";
        if (tag.contains(BlockItem.BLOCK_ENTITY_TAG_KEY)) {
            //NbtCompound stringTag = tag.getCompound(BlockItem.BLOCK_ENTITY_TAG_KEY).getCompound("TEST");
            //s = stringTag.asString();
            s = tag.getCompound(BlockItem.BLOCK_ENTITY_TAG_KEY).getString("TEST");
        }
        return s;
    }

    @Override
    public void setStoredString(ItemStack stack, String s) {
        NbtCompound tag = stack.getOrCreateNbt();
        NbtCompound stringTag = new NbtCompound();
        stringTag.putString("TEST", s);
        tag.put(BlockItem.BLOCK_ENTITY_TAG_KEY, stringTag);
    }
}
