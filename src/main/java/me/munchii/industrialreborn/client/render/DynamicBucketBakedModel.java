package me.munchii.industrialreborn.client.render;

import me.munchii.industrialreborn.utils.Resources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import techreborn.client.render.BaseDynamicFluidBakedModel;

public class DynamicBucketBakedModel extends BaseDynamicFluidBakedModel {
    private static final ModelIdentifier BUCKET_BASE = new ModelIdentifier(Resources.id("bucket_base"), "inventory");
    private static final ModelIdentifier BUCKET_BACKGROUND = new ModelIdentifier(Resources.id("bucket_background"), "inventory");
    private static final ModelIdentifier BUCKET_FLUID = new ModelIdentifier(Resources.id("bucket_fluid"), "inventory");

    @Override
    public Sprite getParticleSprite() {
        return MinecraftClient.getInstance()
                .getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
                .apply(new Identifier("minecraft:item/bucket"));
    }

    @Override
    public ModelIdentifier getBaseModel() {
        return BUCKET_BASE;
    }

    @Override
    public ModelIdentifier getBackgroundModel() {
        return BUCKET_BACKGROUND;
    }

    @Override
    public ModelIdentifier getFluidModel() {
        return BUCKET_FLUID;
    }
}
