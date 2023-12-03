package me.munchii.industrialreborn.core.store;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Store<T> {
    private final String name;
    List<Consumer<Store<T>>> listeners = new ArrayList<>();

    Store(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
