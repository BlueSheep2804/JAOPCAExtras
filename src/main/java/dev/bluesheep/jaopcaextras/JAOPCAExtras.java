package dev.bluesheep.jaopcaextras;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(JAOPCAExtras.MODID)
public class JAOPCAExtras {
    public static final String MODID = "jaopcaextras";

    public JAOPCAExtras(IEventBus modEventBus, ModContainer container) {}

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
