package me.munchii.industrialreborn.client;

import me.munchii.industrialreborn.IndustrialReborn;
import me.munchii.industrialreborn.client.render.DynamicBucketBakedModel;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import me.munchii.industrialreborn.utils.Resources;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import reborncore.client.multiblock.MultiblockRenderer;
import techreborn.client.render.BaseDynamicFluidBakedModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class IndustrialRebornClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBuckets();
        setupTextures();
        registerMultiblocks();

        ClientGuiType.validate();
    }

    private void setupTextures() {
        BlockRenderLayerMap.INSTANCE.putBlock(IRContent.BROKEN_SPAWNER, RenderLayer.getTranslucent());

        Arrays.stream(IRFluids.values()).forEach(fluid -> {
            BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFluid(), RenderLayer.getTranslucent());
            BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFlowingFluid(), RenderLayer.getTranslucent());
        });
    }

    private void registerBuckets() {
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(
                    new ModelIdentifier(Resources.id("bucket_base"), "inventory"),
                    new ModelIdentifier(Resources.id("bucket_fluid"), "inventory"),
                    new ModelIdentifier(Resources.id("bucket_background"), "inventory")
            );

            pluginContext.resolveModel().register(context -> {
                final Identifier id = context.id();

                if (!id.getNamespace().equals(IndustrialReborn.MOD_ID) || !id.getPath().startsWith("item/")) {
                    return null;
                }

                String path = id.getPath().replace("item/", "");

                Fluid fluid = Registries.FLUID.get(Resources.id(path.split("_bucket")[0]));
                if (path.endsWith("_bucket") && fluid != Fluids.EMPTY) {
                    if (!RendererAccess.INSTANCE.hasRenderer()) {
                        return JsonUnbakedModel.deserialize("{\"parent\":\"minecraft:item/generated\",\"textures\":{\"layer0\":\"minecraft:item/bucket\"}}");
                    }

                    return new UnbakedDynamicModel(DynamicBucketBakedModel::new);
                }

                return null;
            });
        });
    }

    private void registerMultiblocks() {
        BlockEntityRendererFactories.register(IRBlockEntities.MOB_SLAUGHTER, MultiblockRenderer::new);
        BlockEntityRendererFactories.register(IRBlockEntities.SOUL_EXTRACTOR, MultiblockRenderer::new);
    }

    private record UnbakedDynamicModel(Supplier<BaseDynamicFluidBakedModel> supplier) implements UnbakedModel {
        @Override
        public Collection<Identifier> getModelDependencies() {
            return Collections.emptyList();
        }

        @Override
        public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

        }

        @Nullable
        @Override
        public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
            return supplier.get();
        }
    }
}
