package dev.bluesheep.jaopcaextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.IEventBus;

//? if < 1.21
//import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
//? if >= 1.21
import net.neoforged.fml.ModContainer;

@Mod(JAOPCAExtras.MODID)
public class JAOPCAExtras {
    public static final String MODID = "jaopcaextras";

    //? if >= 1.21 {
    public JAOPCAExtras(IEventBus modEventBus, ModContainer container) {
    //?} else {
    /*public JAOPCAExtras() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    *///?}
        JAOPCAExtrasItems.REGISTRY.register(modEventBus);
        modEventBus.addListener(this::addItemsToCreativeTab);
    }

    private void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ResourceKey.create(Registries.CREATIVE_MODE_TAB, IdentifierWrapper.fromNamespaceAndPath("jaopca", "tab"))) {
            event.accept(JAOPCAExtrasItems.EXTRA_PRESS.get());
        }
    }

    public static ResourceLocation rl(String path) {
        return IdentifierWrapper.fromNamespaceAndPath(MODID, path);
    }
}
