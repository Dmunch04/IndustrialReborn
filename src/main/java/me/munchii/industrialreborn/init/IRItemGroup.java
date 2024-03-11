package me.munchii.industrialreborn.init;

import me.munchii.industrialreborn.utils.Resources;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.Arrays;

public class IRItemGroup {
    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Resources.id("item_group"));

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.industrialreborn.item_group"))
                .icon(() -> new ItemStack(IRContent.FILLED_SOUL_VIAL))
                .build());

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(IRItemGroup::entries);
    }

    private static void entries(FabricItemGroupEntries entries) {
        entries.add(IRContent.EMPTY_SOUL_VIAL);
        entries.add(IRContent.BROKEN_SPAWNER);

        addContent(IRContent.Machine.values(), entries);
        addContent(IRContent.Upgrade.values(), entries);
        Arrays.stream(IRFluids.values()).forEach(value -> entries.add(value.getBucket()));
    }

    private static void addContent(ItemConvertible[] items, FabricItemGroupEntries entries) {
        for (ItemConvertible item : items) {
            entries.add(item);
        }
    }
}
