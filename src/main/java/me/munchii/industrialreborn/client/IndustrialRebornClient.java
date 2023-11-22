package me.munchii.industrialreborn.client;

import net.fabricmc.api.ClientModInitializer;

public class IndustrialRebornClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientGuiType.validate();
    }
}
