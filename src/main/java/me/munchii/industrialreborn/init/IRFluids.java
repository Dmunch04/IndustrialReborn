package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.utils.Resources;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import reborncore.common.fluid.*;
import techreborn.init.TRBlockSettings;

import java.util.Locale;

public enum IRFluids implements ItemConvertible {
    LIQUID_EXPERIENCE,
    SOUL_ESSENCE;

    private RebornFluid stillFluid;
    private RebornFluid flowingFluid;

    private RebornFluidBlock block;
    private RebornBucketItem bucket;
    private final Identifier identifier;

    IRFluids() {
        this.identifier = Resources.id(this.toString().toLowerCase(Locale.ROOT));

        FluidSettings fluidSettings = FluidSettings.create();

        Identifier stillTexture = Resources.id("block/fluids/" + this.toString().toLowerCase(Locale.ROOT) + "_still");
        Identifier flowingTexture = Resources.id("block/fluids/" + this.toString().toLowerCase(Locale.ROOT) + "_flowing");

        fluidSettings.setStillTexture(stillTexture);
        fluidSettings.setFlowingTexture(flowingTexture);

        this.stillFluid = new RebornFluid(true, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid) {};
        this.flowingFluid = new RebornFluid(false, fluidSettings, () -> block, () -> bucket, () -> flowingFluid, () -> stillFluid) {};

        this.block = new RebornFluidBlock(stillFluid, TRBlockSettings.fluid());
        this.bucket = new RebornBucketItem(stillFluid, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1));
    }

    public void register() {
        // TODO: for some reason our buckets isn't really registered. at least not in EMI, but they do still work, so idk

        RebornFluidManager.register(stillFluid, identifier);
        RebornFluidManager.register(flowingFluid, Resources.id(identifier.getPath() + "_flowing"));

        Registry.register(Registries.BLOCK, identifier, block);
        Registry.register(Registries.ITEM, Resources.id(identifier.getPath() + "_bucket"), bucket);
    }

    public RebornFluid getFluid() {
        return stillFluid;
    }

    public RebornFluid getFlowingFluid() {
        return flowingFluid;
    }

    public RebornFluidBlock getBlock() {
        return block;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public RebornBucketItem getBucket() {
        return bucket;
    }

    @Override
    public Item asItem() {
        return getBucket();
    }
}
