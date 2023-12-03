package me.munchii.industrialreborn.store.test;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class TestStoreItemStack implements ITestStore {
    private final ItemStack stack;

    public TestStoreItemStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public String getStoredString() {
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
    public void setStoredString(String s) {
        NbtCompound tag = stack.getOrCreateNbt();
        NbtCompound stringTag = new NbtCompound();
        stringTag.putString("TEST", s);
        tag.put(BlockItem.BLOCK_ENTITY_TAG_KEY, stringTag);
    }
}
