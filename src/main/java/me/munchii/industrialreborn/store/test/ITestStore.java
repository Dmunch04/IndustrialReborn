package me.munchii.industrialreborn.store.test;

public interface ITestStore {
    default boolean hasStoredString() {
        return !getStoredString().isEmpty();
    }

    String getStoredString();

    void setStoredString(String s);
}
