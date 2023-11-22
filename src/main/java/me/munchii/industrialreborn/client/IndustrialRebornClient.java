package me.munchii.industrialreborn.client;

import me.munchii.industrialreborn.init.IRContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class IndustrialRebornClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        setupTextures();

        ClientGuiType.validate();
    }

    private void setupTextures() {
        BlockRenderLayerMap.INSTANCE.putBlock(IRContent.BROKEN_SPAWNER, RenderLayer.getTranslucent());
    }
}
