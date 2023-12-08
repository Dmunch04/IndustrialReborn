package me.munchii.industrialreborn.utils;

import me.munchii.industrialreborn.IndustrialReborn;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public class Resources {
    public static Identifier id(final String path) {
        return new Identifier(IndustrialReborn.MOD_ID, path);
    }
}
