package dev.bluesheep.jaopcaextras;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;

//? if < 1.21
//import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
//? if >= 1.21
import net.neoforged.fml.ModContainer;
//~ if >= 1.21 'eventbus' -> 'bus'
import net.neoforged.bus.api.IEventBus;

@Mod(JAOPCAExtras.MODID)
public class JAOPCAExtras {
    public static final String MODID = "jaopcaextras";

    //? if >= 1.21 {
    public JAOPCAExtras(IEventBus modEventBus, ModContainer container) {}
    //?} else {
    /*public JAOPCAExtras() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }
    *///?}

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
